package com.bachelor.thesisbe.controller;

import com.bachelor.thesisbe.model.*;
import com.bachelor.thesisbe.service.ForumService;
import com.bachelor.thesisbe.service.PostService;
import com.bachelor.thesisbe.service.UserService;
import com.bachelor.thesisbe.views.PostViewModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/posts")
public class PostController {
    private final UserService userService;
    private final PostService postService;
    private final ForumService forumService;

    public PostController(UserService userService, PostService postService, ForumService forumService) {
        this.userService = userService;
        this.postService = postService;
        this.forumService = forumService;
    }

    @PostMapping("/create")
    public ResponseEntity<Long> createPost(@RequestBody PostViewModel viewModel) {
        byte[] imageByte = null;
        if (viewModel.getImage() != null && !viewModel.getImage().isEmpty()) {
            imageByte = parseBase64Binary(viewModel.getImage());
        }
        UserEntity user = this.userService.getUserById(viewModel.getUserId());
        Forum forum = this.forumService.getById(viewModel.getForumId());
        Long newPostId = this.postService.addPost(viewModel.getTitle(), viewModel.getDescription(), user, forum, imageByte);
        return ResponseEntity.ok(newPostId);
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok("Object removed successfully!");
    }

    @GetMapping("/forum/{forumId}")
    public ResponseEntity<?> getAllPostsForForumId(@PathVariable("forumId") Long forumId) {
        ArrayList<PostViewModel> posts = new ArrayList<>();
        this.postService.getPostsForForum(this.forumService.getById(forumId)).forEach(
                p -> posts.add(createPostViewModel(p)));
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable("postId") Long postId) {
        Post returnedPost = this.postService.getPostById(postId);
        return ResponseEntity.ok(
                createPostViewModel(returnedPost));
    }

    @GetMapping("/like/{userId}/{postId}")
    public ResponseEntity<String> likePost(@PathVariable("userId") Long userId, @PathVariable("postId") Long postId) {
        UserEntity requestingUser = this.userService.getUserById(userId);
        Post likedPost = this.postService.getPostById(postId);
        this.postService.likePost(requestingUser, likedPost, true);
        return ResponseEntity.ok("like successful");
    }

    @GetMapping("/dislike/{userId}/{postId}")
    public ResponseEntity<String> dislikePost(@PathVariable("userId") Long userId, @PathVariable("postId") Long postId) {
        UserEntity requestingUser = this.userService.getUserById(userId);
        Post likedPost = this.postService.getPostById(postId);
        this.postService.likePost(requestingUser, likedPost, false);
        return ResponseEntity.ok("dislike successful");
    }

    private PostViewModel createPostViewModel(Post p) {
        return new PostViewModel(
                p.getId(),
                p.getTitle(),
                p.getOwner().getId(),
                p.getDescription(),
                p.getForum().getId(),
                p.getImage() != null ? printBase64Binary(p.getImage()) : null,
                p.getUserLikes().stream().filter(UserPostRating::isLiked).map(userPostRating -> userPostRating.getUser().getId()).collect(Collectors.toCollection(ArrayList::new)),
                p.getUserLikes().stream().filter(userPostRating -> !userPostRating.isLiked()).map(userPostRating -> userPostRating.getUser().getId()).collect(Collectors.toCollection(ArrayList::new))
        );
    }
}
