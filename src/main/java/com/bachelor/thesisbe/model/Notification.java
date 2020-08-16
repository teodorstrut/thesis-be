package com.bachelor.thesisbe.model;

import com.bachelor.thesisbe.enums.NotificationType;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Notification extends BaseObject {
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity notifiedUser;
    @Column
    private NotificationType notificationType;
    @Column
    private String navigationLink;
    @Column
    private String target;
    @Column
    private boolean seen;
}
