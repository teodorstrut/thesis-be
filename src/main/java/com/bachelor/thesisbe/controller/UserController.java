package com.bachelor.thesisbe.controller;

import com.bachelor.thesisbe.model.UserEntity;
import com.bachelor.thesisbe.service.NotificationService;
import com.bachelor.thesisbe.service.UserService;
import com.bachelor.thesisbe.views.NotificationViewModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    private NotificationService notificationService;

    public UserController(UserService userService, NotificationService notificationService) {
        this.userService = userService;
        this.notificationService = notificationService;
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

    @GetMapping("/get-notifications")
    public ResponseEntity<List<NotificationViewModel>> getUserNotifications() throws Exception {
        UserEntity user;
        List<NotificationViewModel> notificationList = new ArrayList<>();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            user = userService.getUserByEmail(email);
            notificationService.getNotificationsForUser(user).forEach(notification -> notificationList.add(
                    new NotificationViewModel(notification.getNotificationType(), notification.getTarget(), notification.getNavigationLink(), notification.isSeen(), notification.getId())
            ));
            return ResponseEntity.ok(notificationList);
        } else {
            throw new Exception("No user currently logged in!");
        }
    }

    @GetMapping("/mark-as-seen/{notificationId}")
    public ResponseEntity<String> markNotificationAsSeen(@PathVariable("notificationId") Long notificationId) {
        this.notificationService.markNotificationAsSeen(notificationId);
        return ResponseEntity.ok("notification saved successfully!");
    }
}
