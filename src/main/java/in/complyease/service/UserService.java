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
		if (request.getRole() == UserRole.ROLE_USER) {
	        user.setRole(UserRole.ROLE_USER);
	    } 
	    else if (request.getRole() == UserRole.ROLE_CA) {
	        user.setRole(UserRole.ROLE_CA);
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
    
    public List<CADTO> getAllCAUsers() {

        List<User> caUsers =
                userRepository.findByRole(UserRole.ROLE_CA);

        return caUsers.stream()
                .map(user -> new CADTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail()
                ))
                .toList();
    }

}
