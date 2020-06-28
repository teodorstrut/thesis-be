package com.bachelor.thesisbe.views;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostViewModel {
    public Long id;
    public String title;
    public Long userId;
    public String description;
    public Long forumId;
    public String image;
    public ArrayList<Long> likes, dislikes;
}
