package com.bachelor.thesisbe.controller;

import com.bachelor.thesisbe.model.*;
import com.bachelor.thesisbe.service.ForumService;
import com.bachelor.thesisbe.service.PostService;
import com.bachelor.thesisbe.service.UserService;
import com.bachelor.thesisbe.views.FileViewModel;
import com.bachelor.thesisbe.views.PostViewModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;

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
    public ResponseEntity<Long> createPost(@RequestBody PostViewModel viewModel) throws Exception {
        UserEntity user = this.userService.getUserById(viewModel.getUserId());
        Forum forum = this.forumService.getById(viewModel.getForumId());
        Long newPostId = this.postService.addPost(viewModel.getTitle(), viewModel.getDescription(), user, forum, viewModel.getFile());
        return ResponseEntity.ok(newPostId);
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok("Object removed successfully!");
    }

    @GetMapping("/forum/{forumId}/{pageIndex}/{pageSize}")
    public ResponseEntity<?> getAllPostsForForumId(@PathVariable("forumId") Long forumId,
                                                   @PathVariable("pageIndex") int pageIndex,
                                                   @PathVariable("pageSize") int pageSize) {
        ArrayList<PostViewModel> posts = new ArrayList<>();
        this.postService.getPostsForForum(forumId, pageSize, pageIndex).forEach(
                p -> {
                    try {
                        posts.add(createPostViewModel(p));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable("postId") Long postId) throws IOException {
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

    @PostMapping("/update-description/{postId}")
    public ResponseEntity<String> updatePostDescription(@PathVariable("postId") Long postId, @RequestBody String newDescription) {
        this.postService.updatePostDescription(postId, newDescription);
        return ResponseEntity.ok("Post description updated successful");
    }

    private PostViewModel createPostViewModel(Post p) throws IOException {
        return new PostViewModel(
                p.getId(),
                p.getTitle(),
                p.getOwner().getId(),
                p.getDescription(),
                p.getForum().getId(),
                p.getFile() != null ? new FileViewModel(p.getFile().getFileType(), getFileData(p.getFile()), p.getFile().getFileName()) : null,
                p.getUserLikes().stream().filter(UserPostRating::isLiked).map(userPostRating -> userPostRating.getUser().getId()).collect(Collectors.toCollection(ArrayList::new)),
                p.getUserLikes().stream().filter(userPostRating -> !userPostRating.isLiked()).map(userPostRating -> userPostRating.getUser().getId()).collect(Collectors.toCollection(ArrayList::new))
        );
    }

    private byte[] getFileData(PostFile file) throws IOException {
        Path filePath = Paths.get(file.getFilePath());
        return Files.readAllBytes(filePath);
    }
}
