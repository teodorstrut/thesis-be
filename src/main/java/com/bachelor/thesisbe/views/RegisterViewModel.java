package com.bachelor.thesisbe.views;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RegisterViewModel {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
