package com.bachelor.thesisbe.service;

import com.bachelor.thesisbe.model.Comment;
import com.bachelor.thesisbe.model.Post;
import com.bachelor.thesisbe.model.UserEntity;
import com.bachelor.thesisbe.repo.CommentRepo;
import com.bachelor.thesisbe.views.CommentViewModel;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepo repo;

    public CommentService(CommentRepo repo) {
        this.repo = repo;
    }

    public Long createComment(String text, UserEntity user, Post post, Comment parent) {
        return this.repo.save(new Comment(text, post, parent, user)).getId();
    }

    public Comment getCommentById(Long commentId) {
        if (commentId != null) {
            var comment = this.repo.findById(commentId);
            return comment.orElse(null);
        } else {
            return null;
        }
    }

    public void updateComment(Long commentId, String newComment) {
        if (commentId != null) {
            Optional<Comment> commentOptional = this.repo.findById(commentId);
            if (commentOptional.isPresent()) {
                var comment = commentOptional.get();
                comment.setText(newComment);
                repo.save(comment);
            }
        }
    }

    public ArrayList<CommentViewModel> getCommentsByPost(Long postId) {
        ArrayList<CommentViewModel> parentComments = new ArrayList<>();
        var comms = this.repo.findAllByPostId_IdAndParentIdIsNull(postId);
        comms.forEach(
                c -> parentComments.add(new CommentViewModel(
                        c.getId(),
                        c.getOwner().getId(),
                        c.getText(),
                        new ArrayList<>(),
                        c.getPostId().getId(),
                        null,
                        c.getOwner().getFirstName() + " " + c.getOwner().getLastName(),
                        c.getUpdatedDate()
                ))
        );
        for (CommentViewModel c : parentComments) {
            this.getChildComments(c);
        }
        return parentComments;
    }

    private void getChildComments(CommentViewModel comment) {
        this.repo.findAllByPostId_IdAndParentId_Id(comment.getPostId(), comment.getId()).forEach(
                c -> comment.getReplies().add(new CommentViewModel(
                        c.getId(),
                        c.getOwner().getId(),
                        c.getText(),
                        new ArrayList<>(),
                        c.getPostId().getId(),
                        c.getParentId().getId(),
                        c.getOwner().getFirstName() + " " + c.getOwner().getLastName(),
                        c.getUpdatedDate()
                ))
        );
        for (CommentViewModel c : comment.getReplies()) {
            this.getChildComments(c);
        }
    }

    @Transactional
    public void deleteComment(Long commentId) {
        ArrayList<Long> replies = new ArrayList<>();
        this.getAllReplyIds(commentId, replies);
        replies.add(commentId);
        repo.deleteByIdIn(replies);
    }

    private void getAllReplyIds(Long parentId, ArrayList<Long> commentReplyIds) {
        this.repo.findAllByParentId_Id(parentId).forEach(comment -> {
            if (!commentReplyIds.contains(comment.getId())) {
                commentReplyIds.add(comment.getId());
            }
            this.getAllReplyIds(comment.getId(), commentReplyIds);
        });
    }
}
