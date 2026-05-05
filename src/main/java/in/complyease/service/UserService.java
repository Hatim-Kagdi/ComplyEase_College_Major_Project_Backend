package in.complyease.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import in.complyease.dto.LoginRequest;
import in.complyease.dto.RegisterRequest;
import in.complyease.entity.User;
import in.complyease.enums.UserRole;
import in.complyease.repository.UserRepository;
import in.complyease.security.JWTUtil;

@Service
public class UserService {
	@Autowired private BCryptPasswordEncoder passwordEncoder;
	@Autowired private UserRepository userRepository;
    @Autowired private JWTUtil jwtUtil;

	public User register(RegisterRequest request) {
		System.out.println("Service Register method!");
		User user = new User();

		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(UserRole.ROLE_USER);
		return userRepository.save(user);
	}
	
    public String login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return jwtUtil.generateToken(user.getEmail());
    }

}
