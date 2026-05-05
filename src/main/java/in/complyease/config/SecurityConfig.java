package in.complyease.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		System.out.println("Security Config!");
	    return new BCryptPasswordEncoder();
	}
	
	 @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

	        http
	            .csrf(csrf -> csrf.disable()) // disable CSRF for APIs
	            .authorizeHttpRequests(auth -> auth
	                .requestMatchers("/auth/**").permitAll()
	                .requestMatchers("/user/**").hasRole("USER")// allow register/login
	                .anyRequest().authenticated() // secure everything else
	            );

	        return http.build();
	    }

}
