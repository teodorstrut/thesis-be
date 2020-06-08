package com.bachelor.thesisbe.views;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class LoginViewModel {
    private String email;
    private String password;
}
