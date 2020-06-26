package com.bachelor.thesisbe.service;

import com.bachelor.thesisbe.model.UserEntity;
import com.bachelor.thesisbe.repo.UserEntityRepo;
import com.bachelor.thesisbe.views.RegisterViewModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class UserService {
    private final UserEntityRepo userRepo;

    public UserService(UserEntityRepo userRepo) {
        this.userRepo = userRepo;
    }

    public List<UserEntity> getAllUsers() {
        List<UserEntity> userList = new ArrayList<>();
        userRepo.findAll().forEach(userList::add);
        return userList;
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
}
