package com.bachelor.thesisbe.controller;

import com.bachelor.thesisbe.config.PasswordEncoderConfig;
import com.bachelor.thesisbe.model.UserEntity;
import com.bachelor.thesisbe.security.JwtUtils;
import com.bachelor.thesisbe.security.UserDetailsService;
import com.bachelor.thesisbe.service.UserService;
import com.bachelor.thesisbe.views.LoginViewModel;
import com.bachelor.thesisbe.views.RegisterViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SecurityController {
    @Autowired
    private UserService service;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoderConfig encoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/all")
    public List<UserEntity> getAllUsers() {
        return service.getAllUsers();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginViewModel loginModel) throws Exception {
        try {
            authenticate(loginModel.getEmail(), loginModel.getPassword());
            final UserDetails userDetails = userDetailsService
                    .loadUserByUsername(loginModel.getEmail());
            UserEntity user = service.getUserByEmail(userDetails.getUsername());
            final String token = jwtUtils.generateToken(user);
            var response = new Object() {
                public String tokenData = token;
            };
            return ResponseEntity.ok(response);
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

    @RequestMapping("/hello")
    public String hello() {
        return "Hello World!";
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
}
