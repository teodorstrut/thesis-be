package com.bachelor.thesisbe.service;

import com.bachelor.thesisbe.model.Notification;
import com.bachelor.thesisbe.model.UserEntity;
import com.bachelor.thesisbe.repo.NotificationRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepo notificationRepo;

    public NotificationService(NotificationRepo notificationRepo) {
        this.notificationRepo = notificationRepo;
    }

    public void addNotification(Notification notification) {
        notificationRepo.save(notification);
    }

    public void addNotifications(List<Notification> newNotifications) {
        notificationRepo.saveAll(newNotifications);
    }

    public List<Notification> getNotificationsForUser(UserEntity user) {
        return this.notificationRepo.findAllByNotifiedUser(user);
    }
}
