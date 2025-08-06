package com.shubham.backend.service;

import com.shubham.backend.body.LoginRequest;
import com.shubham.backend.entity.User;
import com.shubham.backend.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepo userRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthUserDetailsService userDetailsService;

    @Autowired
    public AuthService(
            UserRepo userRepo,
            AuthenticationManager authenticationManager,
            BCryptPasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthUserDetailsService userDetailsService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    public User login(LoginRequest loginRequest) {
        try {
            // Check if a token is provided in the request
            if (loginRequest.getToken() != null && !loginRequest.getToken().isEmpty()) {
                // Validate the token
                String email = jwtService.extractUsername(loginRequest.getToken());
                if (email != null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                    if (jwtService.validateToken(loginRequest.getToken(), userDetails)) {
                        // Token is valid, set authentication in context
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authToken);

                        // Return the user object
                        return userRepo.findByEmail(email);
                    }
                }
                // If token validation fails, proceed with username/password authentication
            }

            // Create authentication token with credentials from the request
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

            // Authenticate using the authentication manager
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // If we get here, authentication was successful
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT token for the authenticated user
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
            String jwtToken = jwtService.generateToken(userDetails);

            // Get the user object
            User user = userRepo.findByEmail(loginRequest.getEmail());

            // Set the token in the login request (will be used in the controller response)
            loginRequest.setToken(jwtToken);

            return user;
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password");
        } catch (Exception e) {
            throw new BadCredentialsException("Authentication failed: " + e.getMessage());
        }
    }

    public User register(User user) {
        // Check if the user already exists
        if (userRepo.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("User with this email already exists");
        }

        // Encode the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save the user to the repository
        return userRepo.save(user);
    }
}
