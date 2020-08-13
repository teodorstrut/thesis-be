package com.bachelor.thesisbe.controller;

import com.bachelor.thesisbe.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/update-profile-photo/{userId}")
    public ResponseEntity<String> updateUserPhoto(@PathVariable("userId") Long userId, @RequestBody byte[] image) {
        this.userService.updatePhoto(userId, image);
        return ResponseEntity.ok("Profile image updated successfully");
    }

    @GetMapping("/get-profile-image/{userId}")
    public ResponseEntity<byte[]> getUserProfileImage(@PathVariable("userId") Long userId) {
        byte[] userImage = this.userService.getUserProfileImage(userId);
        if (userImage == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(this.userService.getUserProfileImage(userId));
    }
}
