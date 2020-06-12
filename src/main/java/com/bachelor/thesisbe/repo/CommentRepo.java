package com.bachelor.thesisbe.repo;

import com.bachelor.thesisbe.model.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface CommentRepo extends CrudRepository<Comment, Long> {

    ArrayList<Comment> findAllByPostId_IdAndParentIdIsNull(Long postId);

    ArrayList<Comment> findAllByPostId_IdAndParentId_Id(Long postId, Long parentId);
}
