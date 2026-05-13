package in.complyease.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.complyease.dto.compliance.*;
import in.complyease.dto.document.DocumentResponse;
import in.complyease.entity.*;
import in.complyease.enums.ComplianceStatus;
import in.complyease.repository.*;

@Service
public class ComplianceService {

	@Autowired private ComplianceRepository complianceRepository;
	@Autowired private BusinessRepository businessRepository;
	@Autowired private UserRepository userRepository;
	@Autowired private DocumentRepository documentRepository;
	@Autowired private NotificationService notificationService;
	@Autowired private EmailService emailService;

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

		String message = request.getComplianceType() + " compliance created for " + business.getBusinessName()
				+ ". Due date: " + request.getDueDate();

		if (business.getAssignedCA() != null) {
			emailService.sendEmail(business.getAssignedCA().getEmail(), "New Compliance Added",
					"A new " + saved.getComplianceType() + " compliance has been added for business "
							+ business.getBusinessName());
		}

		notificationService.createNotification(business, message);

		emailService.sendEmail(user.getEmail(), "Compliance Created", message);

		// 6. Return response
		return mapToResponse(saved);
	}

	public List<ComplianceResponse> getUserCompliances(String email) {

		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		// Get all businesses of user
		List<Business> businesses = user.getBusinesses();

		// Get compliances of those businesses
		List<Compliance> compliances = complianceRepository.findByBusinessIn(businesses);

		// Map to response
		return compliances.stream().map(this::mapToResponse) // Use the new helper
				.collect(Collectors.toList());
	}

	// Get By Id
	public ComplianceResponse getComplianceById(int complianceId, String email) {

		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		Compliance compliance = complianceRepository.findById(complianceId)
				.orElseThrow(() -> new RuntimeException("Compliance not found"));

		// Ownership check
		if (!compliance.getBusiness().getUser().getId().equals(user.getId())) {
			throw new RuntimeException("Unauthorized access");
		}

		return mapToResponse(compliance);
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

		// Update fields
		if (request.getComplianceType() != null) {
			compliance.setComplianceType(request.getComplianceType());
		}

		if (request.getDueDate() != null) {
			compliance.setComplianceDueDate(request.getDueDate());
		}

		Compliance updated = complianceRepository.save(compliance);

		return mapToResponse(updated);
	}

	@Transactional
	public void deleteCompliance(int complianceId, String email) {

		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		Compliance compliance = complianceRepository.findById(complianceId)
				.orElseThrow(() -> new RuntimeException("Compliance not found"));

		// Ownership check
		if (!compliance.getBusiness().getUser().getId().equals(user.getId())) {
			throw new RuntimeException("Unauthorized access");
		}

		complianceRepository.delete(compliance);
	}

	public List<ComplianceResponse> getAssignedBusinessCompliances(String email) {

		User ca = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("CA not found"));

		// Assigned businesses
		List<Business> businesses = businessRepository.findByAssignedCA(ca);

		// Their compliances
		List<Compliance> compliances = complianceRepository.findByBusinessIn(businesses);

		return compliances.stream()
	            .map(this::mapToResponse) // This ensures CA sees the "evidence" files
	            .toList();
	}

	@Transactional
	public ComplianceResponse updateComplianceStatusByCA(int complianceId, String email, String status) {
		User ca = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("CA not found"));

		Compliance compliance = complianceRepository.findById(complianceId)
				.orElseThrow(() -> new RuntimeException("Compliance not found"));

		// CHECK CA IS ASSIGNED TO BUSINESS
		if (compliance.getBusiness().getAssignedCA() == null
				|| !compliance.getBusiness().getAssignedCA().getId().equals(ca.getId())) {
			throw new RuntimeException("You are not assigned to this business");
		}

		compliance.setComplianceStatus(in.complyease.enums.ComplianceStatus.valueOf(status));

		Compliance updated = complianceRepository.save(compliance);

		String message = updated.getComplianceType() + " compliance marked " + updated.getComplianceStatus()
				+ " by your CA for business " + updated.getBusiness().getBusinessName();

		notificationService.createNotification(updated.getBusiness(), message);

		emailService.sendEmail(updated.getBusiness().getUser().getEmail(), "Compliance Status Updated", message);

		return mapToResponse(updated);
	}
	
	public List<ComplianceResponse> getComplianceByBusinessId(Long businessId) {
		List<Compliance> compliances = complianceRepository.findByBusinessBusinessId(businessId);
	    
	    // 2. Map Entities to your ComplianceResponse DTO
	    return compliances.stream()
	        .map(comp -> new ComplianceResponse(
	            comp.getComplianceId(),
	            comp.getBusiness().getBusinessId(),
	            comp.getBusiness().getBusinessName(),
	            comp.getComplianceType(),
	            comp.getComplianceDueDate(),
	            comp.getComplianceStatus(),
	            null // or map documents if needed
	        ))
	        .collect(Collectors.toList());
	}

	private ComplianceResponse mapToResponse(Compliance c) {
		List<DocumentResponse> docs = List.of(); // Default to empty list

		// Only fetch if the compliance record already exists in DB
		if (c.getComplianceId() != 0) {
			docs = documentRepository.findByCompliance_ComplianceId(c.getComplianceId()).stream()
					.map(doc -> new DocumentResponse(doc.getDocumentId(), doc.getBusiness().getBusinessId(),
							doc.getCompliance() != null ? doc.getCompliance().getComplianceId() : null,
							doc.getBusiness().getBusinessName(), doc.getDocumentFileName(), doc.getDocumentFileUrl(),
							doc.getDocumentType()))
					.collect(Collectors.toList());
		}

		return new ComplianceResponse(c.getComplianceId(), c.getBusiness().getBusinessId(),
				c.getBusiness().getBusinessName(), c.getComplianceType(), c.getComplianceDueDate(),
				c.getComplianceStatus(), docs);
	}
}