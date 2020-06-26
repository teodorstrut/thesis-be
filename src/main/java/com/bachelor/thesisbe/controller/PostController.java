package com.bachelor.thesisbe.controller;

import com.bachelor.thesisbe.model.BaseObject;
import com.bachelor.thesisbe.model.Forum;
import com.bachelor.thesisbe.model.Post;
import com.bachelor.thesisbe.model.UserEntity;
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
    public ResponseEntity<String> createPost(@RequestBody PostViewModel viewModel) {
        byte[] imageByte = null;
        if (viewModel.getImage() != null && !viewModel.getImage().isEmpty()) {
            imageByte = parseBase64Binary(viewModel.getImage());
        }
        UserEntity user = this.userService.getUserById(viewModel.getUserId());
        Forum forum = this.forumService.getById(viewModel.getForumId());
        this.postService.addPost(viewModel.getTitle(), viewModel.getDescription(), user, forum, imageByte);
        return ResponseEntity.ok("Post added successfully");
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok("Object removed successfully!");
    }

    @GetMapping("/forum/{forumId}")
    public ResponseEntity<?> getAllPostsForForumId(@PathVariable("forumId") Long forumId) {
        ArrayList<PostViewModel> posts = new ArrayList<>();
        this.postService.getPostsForForum(this.forumService.getById(forumId)).forEach(
                p -> posts.add(new PostViewModel(
                        p.getId(),
                        p.getTitle(),
                        p.getOwner().getId(),
                        p.getDescription(),
                        p.getForum().getId(),
                        p.getImage() != null ? printBase64Binary(p.getImage()) : null,
                        new ArrayList<>(p.getUserLikes().stream().mapToLong(BaseObject::getId).boxed().collect(Collectors.toList()))
                )));
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable("postId") Long postId) {
        Post returnedPost = this.postService.getPostById(postId);
        return ResponseEntity.ok(
                new PostViewModel(
                        returnedPost.getId(),
                        returnedPost.getTitle(),
                        returnedPost.getOwner().getId(),
                        returnedPost.getDescription(),
                        returnedPost.getForum().getId(),
                        returnedPost.getImage() != null ? printBase64Binary(returnedPost.getImage()) : null,
                        new ArrayList<>(returnedPost.getUserLikes().stream().mapToLong(BaseObject::getId).boxed().collect(Collectors.toList()))
                ));
    }

    @GetMapping("/like/{userId}/{postId}")
    public ResponseEntity<String> likePost(@PathVariable("userId") Long userId, @PathVariable("postId") Long postId) {
        this.postService.likePost(userId, postId);
        return ResponseEntity.ok("like successful");
    }
}
