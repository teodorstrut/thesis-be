package com.bachelor.thesisbe.service;

import com.bachelor.thesisbe.exception.ForumDuplicateNameException;
import com.bachelor.thesisbe.model.Forum;
import com.bachelor.thesisbe.model.UserEntity;
import com.bachelor.thesisbe.repo.ForumRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@CrossOrigin
public class ForumService {
    private final ForumRepo repo;

    public ForumService(ForumRepo repo) {
        this.repo = repo;
    }

    public Forum addForum(UserEntity owner, String name, String description) throws ForumDuplicateNameException {
        try {
            return repo.save(new Forum(owner, name, description, new HashSet<>(), new HashSet<>()));
        } catch (Exception e) {
            throw new ForumDuplicateNameException("The forum's name already exists");
        }
    }

    public void followForum(Long forumId, UserEntity user) {
        Optional<Forum> optionalForum = this.repo.findById(forumId);
        if (optionalForum.isPresent()) {
            Forum forum = optionalForum.get();
            forum.getFollowingUsers().add(user);
            repo.save(forum);
        }
    }

    public void deleteForum(Long forumId) {
        repo.deleteById(forumId);
    }

    public void updateForumDescription(Long forumId, String newDescription) {
        Optional<Forum> forumOptional = repo.findById(forumId);
        if (forumOptional.isPresent()) {
            Forum forum = forumOptional.get();
            forum.setDescription(newDescription);
            repo.save(forum);
        }
    }

    public List<Forum> getAllForums() {
        return repo.findAll();
    }

    public Forum getById(Long id) {
        return repo.findById(id).orElse(null);
    }
}
