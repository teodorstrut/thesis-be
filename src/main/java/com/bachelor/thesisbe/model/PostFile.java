package com.bachelor.thesisbe.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostFile extends BaseObject {

    @OneToOne(mappedBy = "file")
    private Post post;

    @Column
    private String fileName;

    @Column
    private String fileType;

    @Column
    private String filePath;
}
