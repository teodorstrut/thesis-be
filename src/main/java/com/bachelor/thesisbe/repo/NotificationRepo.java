package com.bachelor.thesisbe.repo;

import com.bachelor.thesisbe.model.Notification;
import com.bachelor.thesisbe.model.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepo extends CrudRepository<Notification, Long> {
    List<Notification> findAllByNotifiedUser(UserEntity user);
}
