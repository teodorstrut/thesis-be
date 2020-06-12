package com.bachelor.thesisbe.controller;

import com.bachelor.thesisbe.model.Forum;
import com.bachelor.thesisbe.model.UserEntity;
import com.bachelor.thesisbe.service.ForumService;
import com.bachelor.thesisbe.service.UserService;
import com.bachelor.thesisbe.views.ForumViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ForumController {
    @Autowired
    ForumService forumService;
    @Autowired
    UserService userService;

    @PostMapping("/forums/create")
    public ResponseEntity<?> createForum(@RequestBody ForumViewModel viewModel) {
        UserEntity user = this.userService.getUserById(viewModel.getUserId());
        Long forumId = this.forumService.addForum(user, viewModel.getForumName(), viewModel.getDescription()).getId();
        var response = new Object() {
            public Long response = forumId;
        };
        return ResponseEntity.ok(response);
    }

    @GetMapping("/forums/all")
    public ResponseEntity<List<ForumViewModel>> getAllForums() {
        return ResponseEntity.ok(this.forumService.getAllForums().stream().map(forum ->
                new ForumViewModel(forum.getId(), forum.getOwner().getId(), forum.getName(), forum.getDescription())
        ).collect(Collectors.toList()));
    }

    @GetMapping("forums/get/{id}")
    public ResponseEntity<ForumViewModel> getById(@PathVariable("id") Long id) {
        Forum f = this.forumService.getById(id);
        return ResponseEntity.ok(new ForumViewModel(f.getId(), f.getOwner().getId(), f.getName(), f.getDescription()));
    }
}
