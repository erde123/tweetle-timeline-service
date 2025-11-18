package nl.fontys.tweetletimelineservice.persistence.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user_metadata")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMetadata {

    @Id
    private Long userId;
    private String username;
    private String avatarUrl;
    private Long updatedAt;
}
