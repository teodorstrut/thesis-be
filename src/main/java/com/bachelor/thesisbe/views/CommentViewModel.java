package com.bachelor.thesisbe.views;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentViewModel {
    private Long id;
    private Long ownerId;
    private String text;
    private ArrayList<CommentViewModel> replies;
    private Long postId;
    private Long parentId;
    private String ownerName;
    private Date dateAdded;
}
