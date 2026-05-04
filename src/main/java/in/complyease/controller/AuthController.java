package in.complyease.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.complyease.dto.LoginRequest;
import in.complyease.dto.RegisterRequest;
import in.complyease.entity.User;
import in.complyease.security.JWTUtil;
import in.complyease.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
    	
    		System.out.println("Controller Register method!");
        User user = userService.register(request);

        return ResponseEntity.ok(user);
    
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        String token = userService.login(request);

        return ResponseEntity.ok(token);
    }
}
