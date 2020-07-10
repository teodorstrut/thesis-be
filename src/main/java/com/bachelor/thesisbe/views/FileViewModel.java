package com.bachelor.thesisbe.views;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileViewModel {
    private String type;
    private byte[] data;
    private String name;
}
