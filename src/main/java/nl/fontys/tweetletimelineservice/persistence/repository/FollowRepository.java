package nl.fontys.tweetletimelineservice.persistence.repository;

import nl.fontys.tweetletimelineservice.persistence.document.Follow;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends MongoRepository<Follow, String> {
    List<Follow> findByFollowedId(Long followedId);
    void deleteByFollowerIdAndFollowedId(Long followerId, Long followedId);
    void deleteByFollowerId(Long userId);
    void deleteByFollowedId(Long userId);
}

