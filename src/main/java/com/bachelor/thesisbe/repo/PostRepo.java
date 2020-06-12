package com.bachelor.thesisbe.repo;

import com.bachelor.thesisbe.model.Post;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface PostRepo extends CrudRepository<Post, Long> {
    @Modifying
    @Transactional
    @Query(
            value = "INSERT into user_entity_liked_posts values(:userId, :postId)",
            nativeQuery = true)
    void likePostNative(@Param(value = "userId") Long userId, @Param(value = "postId") Long postId);
}
