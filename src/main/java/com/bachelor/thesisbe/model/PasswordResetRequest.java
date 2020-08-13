package com.bachelor.thesisbe.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PasswordResetRequest extends BaseObject {
    @Temporal(TemporalType.TIMESTAMP)
    Date passwordResetExpirationTimestamp;

    @Column
    String newPassword;

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private UserEntity requester;
}
