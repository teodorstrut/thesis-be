package com.bachelor.thesisbe.service;

import com.bachelor.thesisbe.model.*;
import com.bachelor.thesisbe.repo.PostRepo;
import com.bachelor.thesisbe.repo.UserPostRatingRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepo postRepo;
    private final UserPostRatingRepo ratingsRepo;

    public PostService(PostRepo postRepo, UserPostRatingRepo ratingsRepo) {
        this.postRepo = postRepo;
        this.ratingsRepo = ratingsRepo;
    }

    public Long addPost(String title, String description, UserEntity owner, Forum parentForum, byte[] imageBlob) {
        Post newPost = postRepo.save(new Post(
                title,
                description,
                owner,
                parentForum,
                new HashSet<>(),
                imageBlob,
                new HashSet<>())
        );
        return newPost.getId();
    }

    public void likePost(UserEntity user, Post post, boolean isliked) {
        Optional<UserPostRating> rating = ratingsRepo.findById(new UserPostKey(user.getId(), post.getId()));
        if (rating.isPresent()) {
            UserPostRating ratingEntity = rating.get();
            if (ratingEntity.isLiked() != isliked) {
                ratingEntity.setLiked(isliked);
                ratingsRepo.save(ratingEntity);
            }
        } else {
            ratingsRepo.save(new UserPostRating(user, post, isliked));
        }
    }

    public void deletePost(Long postId, UserEntity user) {
        Optional<Post> p = postRepo.findById(postId);
        if (p.isPresent() && p.get().getOwner().getId().equals(user.getId())) {
            postRepo.delete(p.get());
        }
    }

    public List<Post> getPostsForForum(Forum forum) {
        ArrayList<Post> posts = new ArrayList<>();
        postRepo.findAll().forEach(f -> {
            if (f.getForum().getId().equals(forum.getId())) {
                posts.add(f);
            }
        });
        return posts;
    }

    public Post getPostById(long postId) {
        return postRepo.findById(postId).orElse(null);
    }

    public void savePost(Post post) {
        postRepo.save(post);
    }

}
