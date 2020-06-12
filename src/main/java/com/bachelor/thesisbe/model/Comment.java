package com.bachelor.thesisbe.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Comment extends BaseObject {
    @Column
    private String text;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post postId;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parentId;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserEntity owner;
}
