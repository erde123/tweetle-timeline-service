package nl.fontys.tweetletimelineservice.business.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TweetCreatedEvent {
    private String id;

    private Long authorId;

    private String content;
    private long createdAt;
}
