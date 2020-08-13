package com.bachelor.thesisbe.controller;

import com.bachelor.thesisbe.config.PasswordEncoderConfig;
import com.bachelor.thesisbe.model.UserEntity;
import com.bachelor.thesisbe.security.JwtUtils;
import com.bachelor.thesisbe.security.UserDetailsService;
import com.bachelor.thesisbe.service.UserService;
import com.bachelor.thesisbe.views.LoginViewModel;
import com.bachelor.thesisbe.views.PasswordResetRequestViewModel;
import com.bachelor.thesisbe.views.RegisterViewModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SecurityController {
    private final UserService service;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoderConfig encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public SecurityController(UserService service, UserDetailsService userDetailsService, PasswordEncoderConfig encoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.service = service;
        this.userDetailsService = userDetailsService;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginViewModel loginModel) {
        try {
            authenticate(loginModel.getEmail(), loginModel.getPassword());
            final UserDetails userDetails = userDetailsService
                    .loadUserByUsername(loginModel.getEmail());
            UserEntity user = service.getUserByEmail(userDetails.getUsername());
            final String token = jwtUtils.generateToken(user);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid email or password", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterViewModel newUser) {
        newUser.setPassword(encoder.passwordEncoder().encode(newUser.getPassword()));
        var response = new Object() {
            public String response;
        };
        try {
            this.service.registerUser(newUser);
            response.response = "Authentication successful";
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.response = "Email already exists";
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void authenticate(String email, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> sendResetPasswordRequest(@RequestBody PasswordResetRequestViewModel passwordResetObject) {
        try {
            authenticate(passwordResetObject.getUserName(), passwordResetObject.getOldPassword());
            passwordResetObject.setNewPassword(encoder.passwordEncoder().encode(passwordResetObject.getNewPassword()));
            service.createNewPasswordResetRequest(passwordResetObject);
            return ResponseEntity.ok("password change request sent successfully. Please check your email for the confirmation link!");
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid email or password", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/confirm-password-reset/{userName}")
    public ResponseEntity<String> sendResetPasswordRequest(@PathVariable("userName") String userName) {
        try {
            service.resetPassword(userName);
            return ResponseEntity.ok("password has been reset successfully!");
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid email or password", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
