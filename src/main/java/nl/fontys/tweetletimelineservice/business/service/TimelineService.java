package nl.fontys.tweetletimelineservice.business.service;

import nl.fontys.tweetletimelineservice.persistence.document.Timeline;
import nl.fontys.tweetletimelineservice.persistence.document.Timeline.TweetRef;
import nl.fontys.tweetletimelineservice.persistence.repository.TimelineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;

@Service
public class TimelineService {

    @Autowired
    private TimelineRepository timelineRepository;

    public Timeline getTimeline(Long userId) {
        return timelineRepository.findByUserId(userId);
    }

    public Timeline addTweetToTimeline(Long userId, String tweetId, Long authorId) {
        Timeline timeline = timelineRepository.findByUserId(userId);

        if (timeline == null) {
            timeline = Timeline.builder()
                    .userId(userId)
                    .tweets(new ArrayList<>())
                    .build();
        }

        TweetRef tweetRef = TweetRef.builder()
                .tweetId(tweetId)
                .authorId(authorId)
                .createdAt(Instant.now().toEpochMilli())
                .build();

        timeline.getTweets().add(0, tweetRef);

        if (timeline.getTweets().size() > 100) {
            timeline.setTweets(timeline.getTweets().subList(0, 100));
        }

        return timelineRepository.save(timeline);
    }

    public void clearTimeline(Long userId) {
        Timeline timeline = timelineRepository.findByUserId(userId);
        if (timeline != null) {
            timeline.setTweets(new ArrayList<>());
            timelineRepository.save(timeline);
        }
    }
}

