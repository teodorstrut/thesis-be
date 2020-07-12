package com.bachelor.thesisbe.model;

import lombok.*;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EnableJpaAuditing
public class Forum extends BaseObject {
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;

    @Column(unique = true)
    private String name;
    @Column
    private String description;
    @OneToMany(mappedBy = "forum")
    private Set<Post> posts;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER, mappedBy = "followedForums")
    private Set<UserEntity> followingUsers;
}
