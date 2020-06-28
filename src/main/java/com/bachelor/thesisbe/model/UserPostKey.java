package com.bachelor.thesisbe.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class UserPostKey implements Serializable {
    @Column(name = "user_id")
    Long userId;

    @Column(name = "post_id")
    Long postId;
}
