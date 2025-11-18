package nl.fontys.tweetletimelineservice.persistence.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tweet_metadata")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TweetMetadata {
    @Id
    private String tweetId;
    private Long authorId;
    private String content;
    private long createdAt;
}
