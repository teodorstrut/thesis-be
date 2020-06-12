package com.bachelor.thesisbe.service;

import com.bachelor.thesisbe.model.Comment;
import com.bachelor.thesisbe.model.Forum;
import com.bachelor.thesisbe.model.Post;
import com.bachelor.thesisbe.model.UserEntity;
import com.bachelor.thesisbe.repo.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class PostService {
    @Autowired
    PostRepo repo;

    public Post addPost(String title, String description, UserEntity owner, Forum parentForum, byte[] imageBlob) {
        return repo.save(new Post(
                title,
                description,
                owner,
                parentForum,
                new HashSet<>(),
                imageBlob,
                new HashSet<>())
        );
    }

    public void likePost(Long userId, Long postId) {
        repo.likePostNative(userId, postId);
    }

    public void deletePost(Long postId, UserEntity user) {
        Post p = repo.findById(postId).get();
        if (p.getOwner().getId().equals(user.getId())) {
            repo.delete(p);
        }
    }

    public List<Post> getPostsForForum(Forum forum) {
        ArrayList<Post> posts = new ArrayList<>();
        repo.findAll().forEach(f -> {
            if (f.getForum().getId().equals(forum.getId())) {
                posts.add(f);
            }
        });
        return posts;
    }

    public Post getPostById(long postId) {
        return repo.findById(postId).get();
    }

    public void savePost(Post post) {
        repo.save(post);
    }

}
