package com.bachelor.thesisbe.model;

import lombok.*;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EnableJpaAuditing
public class UserEntity extends BaseObject {
    public UserEntity(String email, String password, String firstName, String lastName, String colorCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.colorCode = colorCode;
    }

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] image;
    @Column
    String colorCode;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    Set<Forum> ownedForums;

}
