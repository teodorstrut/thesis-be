package com.bachelor.thesisbe.controller;

import com.bachelor.thesisbe.enums.NotificationType;
import com.bachelor.thesisbe.model.Comment;
import com.bachelor.thesisbe.model.Notification;
import com.bachelor.thesisbe.model.Post;
import com.bachelor.thesisbe.service.CommentService;
import com.bachelor.thesisbe.service.NotificationService;
import com.bachelor.thesisbe.service.PostService;
import com.bachelor.thesisbe.service.UserService;
import com.bachelor.thesisbe.views.CommentViewModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;
    private final UserService userService;
    private final NotificationService notificationService;

    public CommentController(CommentService commentService, PostService postService, UserService userService, NotificationService notificationService) {
        this.commentService = commentService;
        this.postService = postService;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @PostMapping("/create")
    public ResponseEntity<Long> createComment(@RequestBody CommentViewModel commentViewModel) {
        var user = userService.getUserById(commentViewModel.getOwnerId());
        var post = postService.getPostById(commentViewModel.getPostId());
        var comment = commentService.getCommentById(commentViewModel.getParentId());
        //add notification to parent comment owner
        if (comment != null) {
            this.createReplyNotification(comment);
        } else {
            this.createCommentOnPostNotification(post);
        }
        Long returnedId = commentService.createComment(commentViewModel.getText(), user, post, comment);
        return ResponseEntity.ok(returnedId);
    }

    @GetMapping("/get/{postId}")
    public ResponseEntity<?> getCommentsForPostId(@PathVariable("postId") Long postId) {
        ArrayList<CommentViewModel> comments = commentService.getCommentsByPost(postId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/update/{commentId}")
    public ResponseEntity<String> updateComment(@PathVariable("commentId") Long commentId, @RequestBody String newComment) {
        commentService.updateComment(commentId, newComment);
        return ResponseEntity.ok("Comment successfully updated!");
    }

    @DeleteMapping("/remove/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable("commentId") Long commentId) {

        commentService.deleteComment(commentId);

        return ResponseEntity.ok("Comment deleted successfully!"
        );
    }

    private void createReplyNotification(Comment comment) {
        Notification newNotification = new Notification(
                comment.getOwner(),
                NotificationType.NewCommentOnOwnedComment,
                "/post/" + comment.getPostId().getId(),
                comment.getOwner().getFirstName() + " " + comment.getOwner().getLastName(),
                false);
        this.notificationService.addNotification(newNotification);
    }

    private void createCommentOnPostNotification(Post post) {
        Notification newNotification = new Notification(
                post.getOwner(),
                NotificationType.NewCommentOnOwnedPost,
                "/post/" + post.getId(),
                post.getTitle(),
                false
        );
        this.notificationService.addNotification(newNotification);
    }

}
