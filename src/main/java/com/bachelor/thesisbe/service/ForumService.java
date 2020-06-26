package com.bachelor.thesisbe.service;

import com.bachelor.thesisbe.model.Forum;
import com.bachelor.thesisbe.model.UserEntity;
import com.bachelor.thesisbe.repo.ForumRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.HashSet;
import java.util.List;

@Service
@CrossOrigin
public class ForumService {
    private final ForumRepo repo;

    public ForumService(ForumRepo repo) {
        this.repo = repo;
    }

    public Forum addForum(UserEntity owner, String name, String description) {
        return repo.save(new Forum(owner, name, description, new HashSet<>()));
    }

    public void deleteForum(Long forumId) {
        repo.deleteById(forumId);
    }

    public void updateForumDescription(Long forumId, String newDescription) {
        repo.updateForumDescription(forumId, newDescription);
    }

    public List<Forum> getAllForums() {
        return repo.findAll();
    }

    public Forum getById(Long id) {
        return repo.findById(id).orElse(null);
    }
}
