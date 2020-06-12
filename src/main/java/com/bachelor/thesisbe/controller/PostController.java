package com.bachelor.thesisbe.controller;

import com.bachelor.thesisbe.model.BaseObject;
import com.bachelor.thesisbe.model.Forum;
import com.bachelor.thesisbe.model.Post;
import com.bachelor.thesisbe.model.UserEntity;
import com.bachelor.thesisbe.service.ForumService;
import com.bachelor.thesisbe.service.PostService;
import com.bachelor.thesisbe.service.UserService;
import com.bachelor.thesisbe.views.PostViewModel;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PostController {
    @Autowired
    UserService userService;
    @Autowired
    PostService postService;
    @Autowired
    ForumService forumService;

    @PostMapping("/posts/create")
    public ResponseEntity<?> createPost(@RequestBody PostViewModel viewModel) {
        byte[] imageByte = parseBase64Binary(viewModel.getImage());
        UserEntity user = this.userService.getUserById(viewModel.getUserId());
        Forum forum = this.forumService.getById(viewModel.getForumId());
        this.postService.addPost(viewModel.getTitle(), viewModel.getDescription(), user, forum, imageByte);
        var response = new Object() {
            public String response = "Post added successfully";
        };
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/posts/delete/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(new Object() {
            public String response = "Object removed successfully!";
        });
    }

    @GetMapping("/posts/forum/{forumId}")
    public ResponseEntity<?> getAllPostsForForumId(@PathVariable("forumId") Long forumId) {
        ArrayList<PostViewModel> posts = new ArrayList<>();
        this.postService.getPostsForForum(this.forumService.getById(forumId)).forEach(
                p -> posts.add(new PostViewModel(
                        p.getId(),
                        p.getTitle(),
                        p.getOwner().getId(),
                        p.getDescription(),
                        p.getForum().getId(),
                        printBase64Binary(p.getImage()),
                        new ArrayList<>(p.getUserLikes().stream().mapToLong(BaseObject::getId).boxed().collect(Collectors.toList()))
                )));
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable("postId") Long postId) {
        Post returnedPost = this.postService.getPostById(postId);
        return ResponseEntity.ok(
                new PostViewModel(
                        returnedPost.getId(),
                        returnedPost.getTitle(),
                        returnedPost.getOwner().getId(),
                        returnedPost.getDescription(),
                        returnedPost.getForum().getId(),
                        printBase64Binary(returnedPost.getImage()),
                        new ArrayList<>(returnedPost.getUserLikes().stream().mapToLong(BaseObject::getId).boxed().collect(Collectors.toList()))
                ));
    }

    @GetMapping("/posts/like/{userId}/{postId}")
    public ResponseEntity<?> likePost(@PathVariable("userId") Long userId, @PathVariable("postId") Long postId) {
        this.postService.likePost(userId, postId);
        return ResponseEntity.ok(new Object() {
            public String message = "like successful";
        });
    }
}
