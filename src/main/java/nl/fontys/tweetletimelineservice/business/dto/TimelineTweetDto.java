package nl.fontys.tweetletimelineservice.business.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimelineTweetDto {
    private String tweetId;
    private Long authorId;
    private String authorUsername;
    private String authorAvatarUrl;
    private String content;
    private long createdAt;
}
