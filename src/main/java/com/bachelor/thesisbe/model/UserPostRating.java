package com.bachelor.thesisbe.model;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
public class UserPostRating {
    public UserPostRating(UserEntity user, Post post, boolean isLiked) {
        id = new UserPostKey(user.getId(), post.getId());
        this.user = user;
        this.post = post;
        this.isLiked = isLiked;
    }

    @EmbeddedId
    UserPostKey id;

    @ManyToOne
    @MapsId("user_id")
    @JoinColumn(name = "user_id")
    UserEntity user;

    @ManyToOne
    @MapsId("post_id")
    @JoinColumn(name = "post_id")
    Post post;

    boolean isLiked;
}
