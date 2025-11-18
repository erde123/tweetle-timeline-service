package nl.fontys.tweetletimelineservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent {
    private Long userId;
    private String username;
    private String avatarUrl;
}
