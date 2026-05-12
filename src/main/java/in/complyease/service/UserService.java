package in.complyease.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.complyease.Mapper.AuthResponseMapper;
import in.complyease.dto.AuthResponseDTO;
import in.complyease.dto.LoginRequest;
import in.complyease.dto.RegisterRequest;
import in.complyease.dto.admin.UserManagementDTO;
import in.complyease.dto.business.CADTO;
import in.complyease.entity.User;
import in.complyease.enums.CAApprovalStatus;
import in.complyease.enums.UserRole;
import in.complyease.repository.UserRepository;
import in.complyease.security.JWTUtil;

@Service
public class UserService {
	@Autowired private BCryptPasswordEncoder passwordEncoder;
	@Autowired private UserRepository userRepository;
    @Autowired private JWTUtil jwtUtil;
    
    @Autowired private EmailService emailService;
    
    @Value("${app.admin.email}") private String adminEmail;

    @Transactional
    public User register(RegisterRequest request) {
        System.out.println("Service Register method!");
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(true);

        if (request.getRole() == UserRole.ROLE_CA) {
            user.setRole(UserRole.ROLE_CA);
            user.setApprovalStatus(CAApprovalStatus.PENDING);
        }
        else if (request.getRole() == UserRole.ROLE_USER) {
            user.setRole(UserRole.ROLE_USER);
            user.setApprovalStatus(CAApprovalStatus.APPROVED);
        }
        else {
            throw new RuntimeException("Invalid role selected");
        }

        User savedUser = userRepository.save(user);
        
        // EMAIL NOTIFICATIONS     
        if (savedUser.getRole() == UserRole.ROLE_CA) {
            // EMAIL TO CA
            emailService.sendEmail(
                    savedUser.getEmail(),
                    "CA Registration Request Received",
                    "Your CA account registration request has been received. "
                    + "Please wait for admin approval."
            );

            // EMAIL TO ADMIN
            emailService.sendEmail(
            			adminEmail,
                    "New CA Approval Request",
                    "A new CA has registered.\n\n"
                    + "Name: " + savedUser.getName()
                    + "\nEmail: " + savedUser.getEmail()
            );
        }
        return savedUser;
    }
	
    public AuthResponseDTO login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!user.isActive()) {
            throw new RuntimeException("Your account is deactivated");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

        return AuthResponseMapper.mapToAuthDTO(
                token,
                user.getRole(),
                user.getName(),
                user.getEmail(),
                user.getApprovalStatus()
        );
    }
    
    
    public List<UserManagementDTO> getAllUsers() {

        return userRepository.findAll()
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
    public UserManagementDTO toggleUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setActive(!user.isActive());
        User updated = userRepository.save(user);
        
        String subject;
        String message;

        if (updated.isActive()) {
            subject = "Account Activated";
            message ="Your ComplyEase account has been activated.";
        }else {
            subject = "Account Deactivated";
            message ="Your ComplyEase account has been temporarily deactivated.";
        }

        emailService.sendEmail(
                updated.getEmail(),
                subject,
                message
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
    public void deleteUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        userRepository.delete(user);
    }
    
}
