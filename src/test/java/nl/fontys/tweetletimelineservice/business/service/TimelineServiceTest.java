package nl.fontys.tweetletimelineservice.business.service;

import nl.fontys.tweetletimelineservice.persistence.document.Timeline;
import nl.fontys.tweetletimelineservice.persistence.document.Timeline.TweetRef;
import nl.fontys.tweetletimelineservice.persistence.repository.FollowRepository;
import nl.fontys.tweetletimelineservice.persistence.repository.TimelineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimelineServiceTest {

    @Mock
    private TimelineRepository timelineRepository;

    @Mock
    private FollowRepository followRepository;

    @Mock
    private TweetMetadataService tweetMetadataService;

    @Mock
    private UserMetadataService userMetadataService;

    @InjectMocks
    private TimelineService timelineService;

    private Timeline existingTimeline;

    @BeforeEach
    void setUp() {
        existingTimeline = Timeline.builder()
                .id("1")
                .userId(100L)
                .tweets(new ArrayList<>())
                .build();
    }

    @Test
    void getTimeline_WhenExists_ShouldReturnTimeline() {
        when(timelineRepository.findAllByUserId(100L)).thenReturn(List.of(existingTimeline));
        when(timelineRepository.save(any(Timeline.class))).thenAnswer(i -> i.getArgument(0));

        Timeline result = timelineService.getTimeline(100L);

        assertNotNull(result);
        assertEquals(100L, result.getUserId());
        verify(timelineRepository, times(1)).findAllByUserId(100L);
        verify(timelineRepository, times(1)).save(existingTimeline);
    }

    @Test
    void addTweetToTimeline_WhenTimelineExists_ShouldAddTweet() {
        when(timelineRepository.findAllByUserId(100L)).thenReturn(List.of(existingTimeline));
        when(timelineRepository.save(any(Timeline.class))).thenAnswer(i -> i.getArgument(0));

        Timeline result = timelineService.addTweetToTimeline(100L, "tweet123", 200L);

        assertEquals(1, result.getTweets().size());
        assertEquals("tweet123", result.getTweets().get(0).getTweetId());
        assertEquals(200L, result.getTweets().get(0).getAuthorId());
        verify(timelineRepository, times(2)).save(any(Timeline.class));
    }

    @Test
    void addTweetToTimeline_WhenTimelineDoesNotExist_ShouldCreateNewTimeline() {
        when(timelineRepository.findAllByUserId(300L)).thenReturn(new ArrayList<>());
        when(timelineRepository.save(any(Timeline.class))).thenAnswer(i -> i.getArgument(0));

        Timeline result = timelineService.addTweetToTimeline(300L, "tweet999", 500L);

        assertNotNull(result);
        assertEquals(1, result.getTweets().size());
        assertEquals("tweet999", result.getTweets().get(0).getTweetId());
        verify(timelineRepository, times(2)).save(any(Timeline.class));
    }

    @Test
    void addTweetToTimeline_WhenMoreThan100Tweets_ShouldTrimTimeline() {
        existingTimeline.setTweets(new ArrayList<>());
        for (int i = 0; i < 101; i++) {
            existingTimeline.getTweets().add(TweetRef.builder()
                    .tweetId("t" + i)
                    .authorId(200L)
                    .createdAt(System.currentTimeMillis())
                    .build());
        }

        when(timelineRepository.findAllByUserId(100L)).thenReturn(List.of(existingTimeline));
        when(timelineRepository.save(any(Timeline.class))).thenAnswer(i -> i.getArgument(0));

        Timeline result = timelineService.addTweetToTimeline(100L, "newTweet", 300L);

        assertEquals(100, result.getTweets().size());
        verify(timelineRepository, times(2)).save(any(Timeline.class));
    }

    @Test
    void clearTimeline_WhenExists_ShouldEmptyTweets() {
        existingTimeline.getTweets().add(TweetRef.builder().tweetId("t1").authorId(1L).createdAt(1L).build());
        when(timelineRepository.findByUserId(100L)).thenReturn(existingTimeline);

        timelineService.clearTimeline(100L);

        assertTrue(existingTimeline.getTweets().isEmpty());
        verify(timelineRepository, times(1)).save(existingTimeline);
    }

    @Test
    void clearTimeline_WhenNotExists_ShouldDoNothing() {
        when(timelineRepository.findByUserId(404L)).thenReturn(null);

        timelineService.clearTimeline(404L);

        verify(timelineRepository, never()).save(any());
    }
}
