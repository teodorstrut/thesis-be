package com.bachelor.thesisbe.views;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ForumViewModel {
    private Long id;
    private Long userId;
    private String forumName;
    private String description;
}
