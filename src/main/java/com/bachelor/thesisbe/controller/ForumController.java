package com.bachelor.thesisbe.controller;

import com.bachelor.thesisbe.model.Forum;
import com.bachelor.thesisbe.model.UserEntity;
import com.bachelor.thesisbe.service.ForumService;
import com.bachelor.thesisbe.service.UserService;
import com.bachelor.thesisbe.views.ForumViewModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/forums")
public class ForumController {
    private final ForumService forumService;
    private final UserService userService;

    public ForumController(ForumService forumService, UserService userService) {
        this.forumService = forumService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<Long> createForum(@RequestBody ForumViewModel viewModel) {
        UserEntity user = this.userService.getUserById(viewModel.getUserId());
        Long forumId = this.forumService.addForum(user, viewModel.getForumName(), viewModel.getDescription()).getId();
        return ResponseEntity.ok(forumId);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ForumViewModel>> getAllForums() throws Exception {
        UserEntity user = this.getCurrentUserId();
        return ResponseEntity.ok(this.forumService.getAllForums().stream().map(forum ->
                buildForumViewModel(forum, user)).collect(Collectors.toList()));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ForumViewModel> getById(@PathVariable("id") Long id) throws Exception {
        Forum forum = this.forumService.getById(id);
        UserEntity user = this.getCurrentUserId();
        return ResponseEntity.ok(buildForumViewModel(forum, user));
    }

    @GetMapping("/follow/{userId}/{forumId}")
    public ResponseEntity<String> followForum(@PathVariable("userId") Long userId, @PathVariable("forumId") Long forumId) {
        Forum forum = this.forumService.getById(forumId);
        this.userService.followForum(userId, forum, true);
        return ResponseEntity.ok("Forum followed successfully!");
    }

    @GetMapping("/un-follow/{userId}/{forumId}")
    public ResponseEntity<String> unFollowForum(@PathVariable("userId") Long userId, @PathVariable("forumId") Long forumId) {
        Forum forum = this.forumService.getById(forumId);
        this.userService.followForum(userId, forum ,false);
        return ResponseEntity.ok("Forum followed successfully!");
    }

    private UserEntity getCurrentUserId() throws Exception {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            return this.userService.getUserByEmail(email);
        } else {
            throw new Exception("No user currently logged in!");
        }
    }

    private ForumViewModel buildForumViewModel(Forum forum, UserEntity user) {
        return new ForumViewModel(
                forum.getId(),
                forum.getOwner().getId(),
                forum.getName(),
                forum.getDescription(),
                forum.getFollowingUsers().contains(user),
                forum.getFollowingUsers().size());
    }
}
