package in.complyease.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.complyease.dto.compliance.ComplianceRequest;
import in.complyease.dto.compliance.ComplianceResponse;
import in.complyease.entity.Business;
import in.complyease.entity.Compliance;
import in.complyease.entity.User;
import in.complyease.enums.ComplianceStatus;
import in.complyease.repository.BusinessRepository;
import in.complyease.repository.ComplianceRepository;
import in.complyease.repository.UserRepository;

@Service
public class ComplianceService {

	@Autowired
	private ComplianceRepository complianceRepository;
	@Autowired
	private BusinessRepository businessRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private EmailService emailService;

	@Transactional
	public ComplianceResponse createCompliance(ComplianceRequest request, String email) {

		// 1. Get logged-in user
		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		// 2. Get business
		Business business = businessRepository.findById(request.getBusinessId())
				.orElseThrow(() -> new RuntimeException("Business not found"));

		// 3. SECURITY CHECK (VERY IMPORTANT)
		if (!business.getUser().getId().equals(user.getId())) {
			throw new RuntimeException("Unauthorized access to this business");
		}

		// 4. Create compliance
		Compliance compliance = new Compliance();
		compliance.setBusiness(business);
		compliance.setComplianceType(request.getComplianceType());
		compliance.setComplianceDueDate(request.getDueDate());
		compliance.setComplianceStatus(ComplianceStatus.PENDING);

		// 5. Save
		Compliance saved = complianceRepository.save(compliance);
		
		String message =
		        request.getComplianceType()
		        +
		        " compliance created for "
		        +
		        business.getBusinessName()
		        +
		        ". Due date: "
		        +
		        request.getDueDate();
		
		if (business.getAssignedCA() != null) {
		    emailService.sendEmail(
		            business.getAssignedCA().getEmail(),
		            "New Compliance Added",
		            "A new "
		            + saved.getComplianceType()
		            + " compliance has been added for business "
		            + business.getBusinessName()
		    );
		}
		
		notificationService.createNotification(business,message);
		
		emailService.sendEmail(user.getEmail(),"Compliance Created",message);

		// 6. Return response
		return new ComplianceResponse(saved.getComplianceId(), business.getBusinessId(), business.getBusinessName(),
				saved.getComplianceType(), saved.getComplianceDueDate(), saved.getComplianceStatus());
	}

	public List<ComplianceResponse> getUserCompliances(String email) {

		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		// Get all businesses of user
		List<Business> businesses = user.getBusinesses();

		// Get compliances of those businesses
		List<Compliance> compliances = complianceRepository.findByBusinessIn(businesses);

		// Map to response
		return compliances.stream()
				.map(c -> new ComplianceResponse(c.getComplianceId(), c.getBusiness().getBusinessId(),
						c.getBusiness().getBusinessName(), c.getComplianceType(), c.getComplianceDueDate(),
						c.getComplianceStatus()))
				.collect(Collectors.toList());
	}

	// Get By Id
	public ComplianceResponse getComplianceById(int complianceId, String email) {

		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		Compliance compliance = complianceRepository.findById(complianceId)
				.orElseThrow(() -> new RuntimeException("Compliance not found"));

		//Ownership check
		if (!compliance.getBusiness().getUser().getId().equals(user.getId())) {
			throw new RuntimeException("Unauthorized access");
		}

		return new ComplianceResponse(compliance.getComplianceId(), compliance.getBusiness().getBusinessId(),
				compliance.getBusiness().getBusinessName(), compliance.getComplianceType(),
				compliance.getComplianceDueDate(), compliance.getComplianceStatus());
	}

	@Transactional
	public ComplianceResponse updateCompliance(int complianceId, ComplianceRequest request, String email) {

		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		Compliance compliance = complianceRepository.findById(complianceId)
				.orElseThrow(() -> new RuntimeException("Compliance not found"));

		// Ownership check
		if (!compliance.getBusiness().getUser().getId().equals(user.getId())) {
			throw new RuntimeException("Unauthorized access");
		}

		//Update fields
		if (request.getComplianceType() != null) {
			compliance.setComplianceType(request.getComplianceType());
		}

		if (request.getDueDate() != null) {
			compliance.setComplianceDueDate(request.getDueDate());
		}

		Compliance updated = complianceRepository.save(compliance);

		return new ComplianceResponse(updated.getComplianceId(), updated.getBusiness().getBusinessId(),
				updated.getBusiness().getBusinessName(), updated.getComplianceType(), updated.getComplianceDueDate(),
				updated.getComplianceStatus());
	}
	
	@Transactional
	public void deleteCompliance(int complianceId, String email) {

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    Compliance compliance = complianceRepository.findById(complianceId)
	            .orElseThrow(() -> new RuntimeException("Compliance not found"));

	    //Ownership check
	    if (!compliance.getBusiness().getUser().getId().equals(user.getId())) {
	        throw new RuntimeException("Unauthorized access");
	    }

	    complianceRepository.delete(compliance);
	}
	
	public List<ComplianceResponse> getAssignedBusinessCompliances(
	        String email
	) {

	    User ca = userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("CA not found"));

	    // Assigned businesses
	    List<Business> businesses =
	            businessRepository.findByAssignedCA(ca);

	    // Their compliances
	    List<Compliance> compliances =
	            complianceRepository.findByBusinessIn(businesses);

	    return compliances.stream()
	            .map(c -> new ComplianceResponse(
	                    c.getComplianceId(),
	                    c.getBusiness().getBusinessId(),
	                    c.getBusiness().getBusinessName(),
	                    c.getComplianceType(),
	                    c.getComplianceDueDate(),
	                    c.getComplianceStatus()
	            ))
	            .toList();
	}
	
	@Transactional
	public ComplianceResponse updateComplianceStatusByCA(int complianceId,String email,String status) {
	    User ca = userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("CA not found"));

	    Compliance compliance = complianceRepository.findById(complianceId)
	            .orElseThrow(() -> new RuntimeException("Compliance not found"));

	    // CHECK CA IS ASSIGNED TO BUSINESS
	    if (compliance.getBusiness().getAssignedCA() == null ||!compliance.getBusiness().getAssignedCA()
	                .getId().equals(ca.getId())) {
	        throw new RuntimeException("You are not assigned to this business");
	    }

	    compliance.setComplianceStatus(in.complyease.enums.ComplianceStatus.valueOf(status));

	    Compliance updated = complianceRepository.save(compliance);
	    
	    String message =
	            updated.getComplianceType()
	            + " compliance marked "
	            + updated.getComplianceStatus()
	            + " by your CA for business "
	            + updated.getBusiness().getBusinessName();
	    
	    notificationService.createNotification(updated.getBusiness(),message);
	    
	    emailService.sendEmail(updated.getBusiness().getUser().getEmail(),
	    		"Compliance Status Updated",message);

	    return new ComplianceResponse(
	            updated.getComplianceId(),
	            updated.getBusiness().getBusinessId(),
	            updated.getBusiness().getBusinessName(),
	            updated.getComplianceType(),
	            updated.getComplianceDueDate(),
	            updated.getComplianceStatus()
	    );
	}
}