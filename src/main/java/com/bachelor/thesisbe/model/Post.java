package com.bachelor.thesisbe.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Post extends BaseObject {
    @Column
    private String title;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;

    @ManyToOne
    @JoinColumn(name = "forum_id", nullable = false)
    private Forum forum;

    @OneToMany(mappedBy = "post", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    Set<UserPostRating> userLikes;

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private PostFile file;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "postId")
    private Set<Comment> comments;
}
