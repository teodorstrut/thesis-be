package com.bachelor.thesisbe.repo;

import com.bachelor.thesisbe.model.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepo extends CrudRepository<Post, Long> {
    List<Post> findByForum_IdOrderByCreatedDateDesc(Long forumId);
}
