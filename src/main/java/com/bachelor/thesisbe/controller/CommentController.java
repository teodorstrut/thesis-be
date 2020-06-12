package com.bachelor.thesisbe.controller;

import com.bachelor.thesisbe.model.Post;
import com.bachelor.thesisbe.model.UserEntity;
import com.bachelor.thesisbe.service.CommentService;
import com.bachelor.thesisbe.service.PostService;
import com.bachelor.thesisbe.service.UserService;
import com.bachelor.thesisbe.views.CommentViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;

    @PostMapping("/comments/create")
    public ResponseEntity<?> createComment(@RequestBody CommentViewModel commentViewModel) {
        var user = userService.getUserById(commentViewModel.getOwnerId());
        var post = postService.getPostById(commentViewModel.getPostId());
        var comment = commentService.getCommentById(commentViewModel.getParentId());
        Long returnedId = commentService.createComment(commentViewModel.getText(), user, post, comment);
        return ResponseEntity.ok(new Object() {
            public Long id = returnedId;
        });
    }

    @GetMapping("/comments/get/{postId}")
    public ResponseEntity<?> getCommentsForPostId(@PathVariable("postId") Long postId) {
        ArrayList<CommentViewModel> comments = commentService.getCommentsByPost(postId);
        return ResponseEntity.ok(comments);
    }
}
