package nl.fontys.tweetletimelineservice.persistence.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "timelines")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Timeline {
    @Id
    private String id;

    @Indexed(unique = true)
    private Long userId;
    private List<TweetRef> tweets;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TweetRef {
        private String tweetId;
        private Long authorId;
        private long createdAt;
    }
}
