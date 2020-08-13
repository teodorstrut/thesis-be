package com.bachelor.thesisbe.service;

import com.bachelor.thesisbe.model.Forum;
import com.bachelor.thesisbe.model.PasswordResetRequest;
import com.bachelor.thesisbe.model.UserEntity;
import com.bachelor.thesisbe.repo.PasswordResetRequestRepo;
import com.bachelor.thesisbe.repo.UserEntityRepo;
import com.bachelor.thesisbe.views.PasswordResetRequestViewModel;
import com.bachelor.thesisbe.views.RegisterViewModel;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private final UserEntityRepo userRepo;
    private final PasswordResetRequestRepo passwordResetRepo;
    private final JavaMailSender javaMailSender;

    public UserService(UserEntityRepo userRepo, PasswordResetRequestRepo passwordResetRepo, JavaMailSender javaMailSender) {
        this.userRepo = userRepo;
        this.passwordResetRepo = passwordResetRepo;
        this.javaMailSender = javaMailSender;
    }

    public void registerUser(RegisterViewModel newUser) {
        Random obj = new Random();
        int rand_num = obj.nextInt(0xffffff + 1);
        String colorCode = String.format("#%06x", rand_num);
        System.out.println(colorCode);
        userRepo.save(
                new UserEntity(
                        newUser.getEmail(),
                        newUser.getPassword(),
                        newUser.getFirstName(),
                        newUser.getLastName(),
                        colorCode
                ));
    }

    public UserEntity getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public UserEntity getUserById(Long userId) {
        if (userRepo.findById(userId).isPresent()) {
            return userRepo.findById(userId).get();
        } else {
            return null;
        }
    }

    public void followForum(Long userId, Forum forum, boolean follow) {
        Optional<UserEntity> userOptional = this.userRepo.findById(userId);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            if (follow) {
                user.getFollowedForums().add(forum);
                this.userRepo.save(user);
            } else {
                user.getFollowedForums().remove(forum);
                this.userRepo.save(user);
            }
        }
    }

    public void updatePhoto(Long userId, byte[] imageData) {
        Optional<UserEntity> userEntityOptional = this.userRepo.findById(userId);
        if (userEntityOptional.isPresent()) {
            UserEntity user = userEntityOptional.get();
            user.setProfileImage(imageData);
            this.userRepo.save(user);
        }
    }

    public byte[] getUserProfileImage(Long userId) {
        Optional<UserEntity> userEntityOptional = this.userRepo.findById(userId);
        if (userEntityOptional.isPresent()) {
            UserEntity user = userEntityOptional.get();
            if (user.getProfileImage().length == 0) {
                return null;
            } else {
                return user.getProfileImage();
            }
        } else {
            return null;
        }
    }

    public void createNewPasswordResetRequest(PasswordResetRequestViewModel passwordResetObject) {
        UserEntity user = userRepo.findByEmail(passwordResetObject.getUserName());

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        PasswordResetRequest oldPasswordResetRequest = passwordResetRepo.findByRequester(user);
        if (oldPasswordResetRequest != null) {
            oldPasswordResetRequest.setNewPassword(passwordResetObject.getNewPassword());
            oldPasswordResetRequest.setPasswordResetExpirationTimestamp(calendar.getTime());
            passwordResetRepo.save(oldPasswordResetRequest);
        } else {
            PasswordResetRequest newRequest = new PasswordResetRequest(calendar.getTime(), passwordResetObject.getNewPassword(), user);
            passwordResetRepo.save(newRequest);
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("teodorstrut@gmail.com");
        message.setText("please reset your password here: http://localhost:4200/resetPassword/" + passwordResetObject.getUserName() + " .");
        message.setSubject("Application reset password request");
        this.javaMailSender.send(message);
    }

    public void resetPassword(String userName) {
        UserEntity user = userRepo.findByEmail(userName);
        PasswordResetRequest passwordResetRequest = passwordResetRepo.findByRequester(user);
        if (passwordResetRequest.getPasswordResetExpirationTimestamp().after(new Date())) {
            user.setPassword(passwordResetRequest.getNewPassword());
            passwordResetRepo.delete(passwordResetRequest);
        }
    }
}
