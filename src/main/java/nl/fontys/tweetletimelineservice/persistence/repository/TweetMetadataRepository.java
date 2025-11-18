package nl.fontys.tweetletimelineservice.persistence.repository;

import nl.fontys.tweetletimelineservice.persistence.document.TweetMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetMetadataRepository extends MongoRepository<TweetMetadata, String> {
    List<TweetMetadata> findByAuthorIdOrderByCreatedAtDesc(Long authorId);
}
