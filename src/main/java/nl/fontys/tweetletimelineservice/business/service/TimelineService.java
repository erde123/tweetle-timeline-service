package nl.fontys.tweetletimelineservice.business.service;

import nl.fontys.tweetletimelineservice.business.dto.TimelineResponse;
import nl.fontys.tweetletimelineservice.business.dto.TimelineTweetDto;
import nl.fontys.tweetletimelineservice.business.dto.TweetCreatedEvent;
import nl.fontys.tweetletimelineservice.persistence.document.Follow;
import nl.fontys.tweetletimelineservice.persistence.document.Timeline;
import nl.fontys.tweetletimelineservice.persistence.document.TweetMetadata;
import nl.fontys.tweetletimelineservice.persistence.document.UserMetadata;
import nl.fontys.tweetletimelineservice.persistence.repository.FollowRepository;
import nl.fontys.tweetletimelineservice.persistence.repository.TimelineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class TimelineService {

    private static final int MAX_TIMELINE_TWEETS = 100;

    @Autowired
    private TimelineRepository timelineRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private TweetMetadataService tweetMetadataService;

    @Autowired
    private UserMetadataService userMetadataService;

    public Timeline getTimeline(Long userId) {
        return loadOrCreateTimeline(userId);
    }

    public TimelineResponse getTimelineResponse(Long userId) {
        Timeline timeline = getTimeline(userId);

        List<TimelineTweetDto> tweets = timeline.getTweets().stream()
                .map(tweetRef -> tweetMetadataService.findById(tweetRef.getTweetId())
                        .map(metadata -> buildTimelineTweetDto(metadata, tweetRef.getAuthorId()))
                        .orElse(null))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingLong(TimelineTweetDto::getCreatedAt).reversed())
                .toList();

        return new TimelineResponse(userId, tweets);
    }

    public void clearTimeline(Long userId) {
        Timeline timeline = timelineRepository.findByUserId(userId);
        if (timeline != null) {
            timeline.setTweets(new ArrayList<>());
            timelineRepository.save(timeline);
        }
    }

    public void insertTweetIntoFollowersTimelines(TweetCreatedEvent event) {
        List<Follow> followers = followRepository.findByFollowedId(event.getAuthorId());

        for (Follow follow : followers) {
            addTweetToTimelineInternal(
                    follow.getFollowerId(),
                    event.getId(),
                    event.getAuthorId(),
                    event.getCreatedAt()
            );
        }

        addTweetToTimelineInternal(
                event.getAuthorId(),
                event.getId(),
                event.getAuthorId(),
                event.getCreatedAt()
        );
    }

    public void deleteTimeline(Long userId) {
        Timeline timeline = timelineRepository.findByUserId(userId);
        if (timeline != null) {
            timelineRepository.delete(timeline);
        }

        followRepository.deleteByFollowerId(userId);
        followRepository.deleteByFollowedId(userId);
    }

    public void seedTimelineWithExistingTweets(Long followerId, Long followedId, int limit) {
        List<TweetMetadata> recentTweets = tweetMetadataService.findRecentByAuthor(followedId, limit);
        for (int i = recentTweets.size() - 1; i >= 0; i--) {
            TweetMetadata tweet = recentTweets.get(i);
            addTweetToTimelineInternal(
                    followerId,
                    tweet.getTweetId(),
                    tweet.getAuthorId(),
                    tweet.getCreatedAt()
            );
        }
    }

    public void removeTweetFromAllTimelines(String tweetId) {
        List<Timeline> timelines = timelineRepository.findByTweetsTweetId(tweetId);
        for (Timeline timeline : timelines) {
            timeline.getTweets().removeIf(t -> t.getTweetId().equals(tweetId));
            timelineRepository.save(timeline);
        }
    }

    private void addTweetToTimelineInternal(Long userId, String tweetId, Long authorId, long createdAt) {

        Timeline timeline = loadOrCreateTimeline(userId);

        Timeline.TweetRef tweetRef = Timeline.TweetRef.builder()
                .tweetId(tweetId)
                .authorId(authorId)
                .createdAt(createdAt)
                .build();

        timeline.getTweets().add(0, tweetRef);

        if (timeline.getTweets().size() > MAX_TIMELINE_TWEETS) {
            timeline.setTweets(timeline.getTweets().subList(0, MAX_TIMELINE_TWEETS));
        }

        timelineRepository.save(timeline);
    }

    public Timeline addTweetToTimeline(Long userId, String tweetId, Long authorId) {
        Timeline timeline = loadOrCreateTimeline(userId);

        Timeline.TweetRef tweetRef = Timeline.TweetRef.builder()
                .tweetId(tweetId)
                .authorId(authorId)
                .createdAt(Instant.now().toEpochMilli())
                .build();

        timeline.getTweets().add(0, tweetRef);

        if (timeline.getTweets().size() > MAX_TIMELINE_TWEETS) {
            timeline.setTweets(timeline.getTweets().subList(0, MAX_TIMELINE_TWEETS));
        }

        return timelineRepository.save(timeline);
    }

    private TimelineTweetDto buildTimelineTweetDto(TweetMetadata metadata, Long authorId) {
        UserMetadata authorMetadata = userMetadataService.findById(authorId).orElse(null);
        return new TimelineTweetDto(
                metadata.getTweetId(),
                authorId,
                authorMetadata != null ? authorMetadata.getUsername() : null,
                authorMetadata != null ? authorMetadata.getAvatarUrl() : null,
                metadata.getContent(),
                metadata.getCreatedAt()
        );
    }

    private Timeline loadOrCreateTimeline(Long userId) {
        Objects.requireNonNull(userId, "userId must not be null when loading timeline");
        List<Timeline> timelines = timelineRepository.findAllByUserId(userId);

        if (timelines.isEmpty()) {
            return timelineRepository.save(
                    Timeline.builder()
                            .userId(userId)
                            .tweets(new ArrayList<>())
                            .build()
            );
        }

        Timeline primary = timelines.get(0);
        if (primary.getTweets() == null) {
            primary.setTweets(new ArrayList<>());
        }

        // Merge tweets from duplicate timelines (if any) and deduplicate by tweetId
        List<Timeline.TweetRef> mergedTweets = new ArrayList<>(primary.getTweets());
        Set<String> seenTweetIds = new HashSet<>();
        mergedTweets.removeIf(ref -> !seenTweetIds.add(ref.getTweetId()));

        for (int i = 1; i < timelines.size(); i++) {
            Timeline extra = timelines.get(i);
            if (extra.getTweets() != null) {
                for (Timeline.TweetRef ref : extra.getTweets()) {
                    if (seenTweetIds.add(ref.getTweetId())) {
                        mergedTweets.add(ref);
                    }
                }
            }
            timelineRepository.delete(extra);
        }

        mergedTweets.sort(Comparator.comparingLong(Timeline.TweetRef::getCreatedAt).reversed());
        if (mergedTweets.size() > MAX_TIMELINE_TWEETS) {
            mergedTweets = new ArrayList<>(mergedTweets.subList(0, MAX_TIMELINE_TWEETS));
        }

        primary.setTweets(mergedTweets);
        return timelineRepository.save(primary);
    }
}
