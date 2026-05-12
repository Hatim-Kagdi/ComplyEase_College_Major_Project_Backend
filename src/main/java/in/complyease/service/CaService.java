package in.complyease.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.complyease.dto.admin.UserManagementDTO;
import in.complyease.dto.business.CADTO;
import in.complyease.entity.User;
import in.complyease.enums.CAApprovalStatus;
import in.complyease.enums.UserRole;
import in.complyease.repository.UserRepository;

@Service
public class CaService {
	@Autowired private UserRepository userRepository;
	@Autowired private EmailService emailService;
	@Value("${app.admin.email}") private String adminEmail;
	
	public List<CADTO> getAllCAUsers() {
        List<User> caUsers = userRepository.findByRole(UserRole.ROLE_CA);

        return caUsers.stream()
                .map(user -> new CADTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail()
                ))
                .toList();
    }
	
	public List<UserManagementDTO> getPendingCAs() {

	    return userRepository.findByRoleAndApprovalStatus(UserRole.ROLE_CA,CAApprovalStatus.PENDING)
	            .stream()
	            .map(user -> new UserManagementDTO(
	                    user.getId(),
	                    user.getName(),
	                    user.getEmail(),
	                    user.getRole(),
	                    user.isActive(),
	                    user.getApprovalStatus()
	            ))
	            .toList();
	}
	
	@Transactional
	public UserManagementDTO approveCA(Long userId) {
	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new RuntimeException("CA not found"));

	    user.setApprovalStatus(CAApprovalStatus.APPROVED);
	    User updated = userRepository.save(user);
	    
	    emailService.sendEmail(
	            updated.getEmail(),
	            "CA Account Approved - ComplyEase",
	            "Hello " + updated.getName() + ",\n\n"
	            + "Your CA account has been approved successfully.\n"
	            + "You can now log in to ComplyEase.\n\n"
	            + "Regards,\n"
	            + "ComplyEase Team"
	    );

	    return new UserManagementDTO(
	            updated.getId(),
	            updated.getName(),
	            updated.getEmail(),
	            updated.getRole(),
	            updated.isActive(),
	            updated.getApprovalStatus()
	    );
	}
	
	@Transactional
	public UserManagementDTO rejectCA(Long userId) {
		System.out.println("Ca reject service!");
	    User user = userRepository.findById(userId)
	            .orElseThrow(() ->
	                    new RuntimeException("CA not found"));

	    user.setApprovalStatus(CAApprovalStatus.REJECTED);

	    User updated = userRepository.save(user);
	    
	    emailService.sendEmail(
	            updated.getEmail(),
	            "CA Registration Rejected - ComplyEase",
	            "Hello " + updated.getName() + ",\n\n"
	            + "We regret to inform you that your CA registration request "
	            + "has been rejected by the admin.\n\n"
	            + "You may update your information and reapply again.\n\n"
	            + "Regards,\n"
	            + "ComplyEase Team"
	    );

	    return new UserManagementDTO(
	            updated.getId(),
	            updated.getName(),
	            updated.getEmail(),
	            updated.getRole(),
	            updated.isActive(),
	            updated.getApprovalStatus()
	    );
	}
	
	@Transactional
	public UserManagementDTO reapplyCA(String email) {
		System.out.println("Reapplied service!");
	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("CA not found"));

	    // Set status back to PENDING
	    user.setApprovalStatus(CAApprovalStatus.PENDING);
	    User updated = userRepository.save(user);
	    
	    emailService.sendEmail(
	            adminEmail,
	            "CA Reapplication Request",
	            "A CA has reapplied for account approval.\n\n"
	            + "Name: " + updated.getName()
	            + "\nEmail: " + updated.getEmail()
	    );

	    return new UserManagementDTO(
	            updated.getId(),
	            updated.getName(),
	            updated.getEmail(),
	            updated.getRole(),
	            updated.isActive(),
	            updated.getApprovalStatus()
	    );
	}

}
