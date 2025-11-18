package nl.fontys.tweetletimelineservice.business.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TweetDeletedEvent {
    private String id;
    private Long userId;
}
