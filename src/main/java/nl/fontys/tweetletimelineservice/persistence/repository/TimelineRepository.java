package nl.fontys.tweetletimelineservice.persistence.repository;

import nl.fontys.tweetletimelineservice.persistence.document.Timeline;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimelineRepository extends MongoRepository<Timeline, String> {
    Timeline findByUserId(Long userId);
}
