package in.complyease.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.complyease.dto.admin.UserManagementDTO;
import in.complyease.dto.business.CADTO;
import in.complyease.entity.User;
import in.complyease.enums.UserRole;
import in.complyease.repository.UserRepository;

@Service
public class CaService {
	@Autowired private UserRepository userRepository;
	@Autowired private EmailService emailService;
	
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

	    return userRepository.findByRoleAndIsApproved(UserRole.ROLE_CA,false)
	            .stream()
	            .map(user -> new UserManagementDTO(
	                    user.getId(),
	                    user.getName(),
	                    user.getEmail(),
	                    user.getRole(),
	                    user.isActive(),
	                    user.isApproved()
	            ))
	            .toList();
	}
	
	@Transactional
	public UserManagementDTO approveCA(Long userId) {
	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new RuntimeException("CA not found"));

	    user.setApproved(true);
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
	            updated.isApproved()
	    );
	}

}
