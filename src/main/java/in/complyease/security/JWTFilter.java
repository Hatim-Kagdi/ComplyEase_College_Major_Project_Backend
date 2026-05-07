package in.complyease.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // 1. Only proceed if there is a Bearer token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            
            // 2. Wrap in try-catch to prevent "MalformedJwtException" from crashing the request
            try {
                // Ensure token isn't just the string "null" or empty
                if (!token.isBlank() && !token.equals("null")) {
                    String email = jwtUtil.extractEmail(token);

                    // 3. Only authenticate if email is found and user isn't already authenticated
                    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        
                        // Note: In the future, you should load real roles from your DB here
                        List<GrantedAuthority> authorities = List.of(
                                new SimpleGrantedAuthority("ROLE_USER")
                        );

                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(
                                        email,
                                        null,
                                        authorities
                                );

                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            } catch (Exception e) {
                // If token is invalid/expired, we just log it and let the request continue.
                // Spring Security will then block it if the endpoint is not "permitAll()".
                logger.error("Could not set user authentication in security context", e);
            }
        }

        // 4. Always call the next filter!
        filterChain.doFilter(request, response);
    }
}