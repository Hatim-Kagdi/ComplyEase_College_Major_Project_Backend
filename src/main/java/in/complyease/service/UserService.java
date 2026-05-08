package in.complyease.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import in.complyease.enums.UserRole;
import in.complyease.repository.UserRepository;
import in.complyease.security.JWTUtil;

@Service
public class UserService {
	@Autowired private BCryptPasswordEncoder passwordEncoder;
	@Autowired private UserRepository userRepository;
    @Autowired private JWTUtil jwtUtil;

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

		    user.setApproved(false);
		} 
		else if (request.getRole() == UserRole.ROLE_USER) {

		    user.setRole(UserRole.ROLE_USER);

		    user.setApproved(true);
		}
	    else {
	        // This is likely what was being triggered before
	        throw new RuntimeException("Invalid role selected");
	    }
		
		return userRepository.save(user);
	}
	
    public AuthResponseDTO login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!user.isActive()) {
            throw new RuntimeException(
                    "Your account is deactivated"
            );
        }
        
        if (
        	    user.getRole() == UserRole.ROLE_CA
        	    &&
        	    !user.isApproved()
        	) {

        	    throw new RuntimeException(
        	            "CA account pending approval"
        	    );
        	}

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

        return AuthResponseMapper.mapToAuthDTO(
                token,
                user.getRole(),
                user.getName(),
                user.getEmail()
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
                        user.isApproved()
                ))
                .toList();
    }
    
    @Transactional
    public UserManagementDTO toggleUserStatus(
            Long userId
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setActive(!user.isActive());

        User updated = userRepository.save(user);

        return new UserManagementDTO(
                updated.getId(),
                updated.getName(),
                updated.getEmail(),
                updated.getRole(),
                updated.isActive(),
                updated.isApproved()
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
