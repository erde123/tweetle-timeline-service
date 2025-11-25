package nl.fontys.tweetletimelineservice.business.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonAlias;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TweetCreatedEvent {
    private String id;

    @JsonAlias({"authorId"})
    private Long userId;

    private String content;
    private long createdAt;

    // Compatibility helpers so existing code can still call getAuthorId/setAuthorId
    public Long getAuthorId() {
        return userId;
    }

    public void setAuthorId(Long authorId) {
        this.userId = authorId;
    }
}
