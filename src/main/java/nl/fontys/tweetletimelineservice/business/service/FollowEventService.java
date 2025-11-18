package nl.fontys.tweetletimelineservice.business.service;

import nl.fontys.tweetletimelineservice.business.dto.FollowEvent;
import nl.fontys.tweetletimelineservice.persistence.document.Follow;
import nl.fontys.tweetletimelineservice.persistence.document.Timeline;
import nl.fontys.tweetletimelineservice.persistence.repository.FollowRepository;
import nl.fontys.tweetletimelineservice.persistence.repository.TimelineRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FollowEventService {

    private final FollowRepository followRepository;
    private final TimelineRepository timelineRepository;
    private final TimelineService timelineService;
    private static final int FOLLOW_SEED_LIMIT = 20;

    public FollowEventService(FollowRepository followRepository,
                              TimelineRepository timelineRepository,
                              TimelineService timelineService) {
        this.followRepository = followRepository;
        this.timelineRepository = timelineRepository;
        this.timelineService = timelineService;
    }

    public void handleFollowCreated(FollowEvent event) {
        Follow follow = Follow.builder()
                .followerId(event.getFollowerId())
                .followedId(event.getFollowedId())
                .build();

        followRepository.save(follow);
        timelineService.seedTimelineWithExistingTweets(event.getFollowerId(), event.getFollowedId(), FOLLOW_SEED_LIMIT);
    }

    public void handleFollowDeleted(FollowEvent event) {
        followRepository.deleteByFollowerIdAndFollowedId(
                event.getFollowerId(),
                event.getFollowedId()
        );

        Optional.ofNullable(timelineRepository.findByUserId(event.getFollowerId()))
                .ifPresent(timeline -> {
                    timeline.getTweets().removeIf(t -> t.getAuthorId().equals(event.getFollowedId()));
                    timelineRepository.save(timeline);
                });
    }
}
