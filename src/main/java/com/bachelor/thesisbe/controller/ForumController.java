package com.bachelor.thesisbe.controller;

import com.bachelor.thesisbe.model.Forum;
import com.bachelor.thesisbe.model.UserEntity;
import com.bachelor.thesisbe.service.ForumService;
import com.bachelor.thesisbe.service.UserService;
import com.bachelor.thesisbe.views.ForumViewModel;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<ForumViewModel>> getAllForums() {
        return ResponseEntity.ok(this.forumService.getAllForums().stream().map(forum ->
                new ForumViewModel(forum.getId(), forum.getOwner().getId(), forum.getName(), forum.getDescription())
        ).collect(Collectors.toList()));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ForumViewModel> getById(@PathVariable("id") Long id) {
        Forum f = this.forumService.getById(id);
        return ResponseEntity.ok(new ForumViewModel(f.getId(), f.getOwner().getId(), f.getName(), f.getDescription()));
    }
}
