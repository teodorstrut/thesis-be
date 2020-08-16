package com.bachelor.thesisbe.views;

import com.bachelor.thesisbe.enums.NotificationType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NotificationViewModel {
    private NotificationType notificationType;
    private String target;
    private String navigationLink;
    private boolean seen;
    private Long id;
}
