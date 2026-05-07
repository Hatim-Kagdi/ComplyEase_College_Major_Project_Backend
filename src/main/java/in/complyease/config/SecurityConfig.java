package in.complyease.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import in.complyease.security.JWTFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired private JWTFilter jwtfilter;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		System.out.println("Security Config!");
	    return new BCryptPasswordEncoder();
	}
	
	 @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

	        http
	        		.cors(cors -> {})
	            .csrf(csrf -> csrf.disable()) // disable CSRF for APIs
	            .authorizeHttpRequests(auth -> auth
	                .requestMatchers("/auth/**").permitAll()
	                .requestMatchers("/user/**").hasRole("USER")
	                .requestMatchers("/ca/**").hasRole("CA")// allow register/login
	                .anyRequest().authenticated() // secure everything else
	            )
	            .addFilterBefore(jwtfilter, UsernamePasswordAuthenticationFilter.class);

	        return http.build();
	    }

}
