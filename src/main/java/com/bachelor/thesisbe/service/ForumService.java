package com.bachelor.thesisbe.service;

import com.bachelor.thesisbe.enums.ForumFilterType;
import com.bachelor.thesisbe.exception.ForumDuplicateNameException;
import com.bachelor.thesisbe.model.BaseObject;
import com.bachelor.thesisbe.model.Forum;
import com.bachelor.thesisbe.model.UserEntity;
import com.bachelor.thesisbe.repo.ForumRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.*;

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

    public void updateForumDescription(Long forumId, String newDescription) {
        Optional<Forum> forumOptional = repo.findById(forumId);
        if (forumOptional.isPresent()) {
            Forum forum = forumOptional.get();
            forum.setDescription(newDescription);
            repo.save(forum);
        }
    }

    public List<Forum> getAllForums(ForumFilterType filterType, UserEntity user, Integer pageIndex, Integer pageSize) {
        List<Forum> forums = repo.findAll();
        switch (filterType) {
            case Newest:
                forums.sort(Comparator.comparing(BaseObject::getCreatedDate));
                getForumPageByPageIndexAndPageSize(pageIndex, pageSize, forums);
            case Subscribed:
                forums.sort(Comparator.comparing(forum -> forum.getFollowingUsers().contains(user)));
                Collections.reverse(forums);
                getForumPageByPageIndexAndPageSize(pageIndex, pageSize, forums);
            case MostPopular:
                forums.sort(Comparator.comparingInt(forum -> forum.getFollowingUsers().size()));
                Collections.reverse(forums);
                return getForumPageByPageIndexAndPageSize(pageIndex, pageSize, forums);
            default:
                return getForumPageByPageIndexAndPageSize(pageIndex, pageSize, forums);
        }
    }

    public Forum getById(Long id) {
        return repo.findById(id).orElse(null);
    }

    private List<Forum> getForumPageByPageIndexAndPageSize(int pageIndex, int pageSize, List<Forum> forums) {
        int start = pageIndex * pageSize;
        int end = pageIndex * pageSize + pageSize;
        if (start > forums.size()) {
            start = forums.size();
        }
        if (end > forums.size()) {
            end = forums.size();
        }
        return forums.subList(start, end);
    }
}
