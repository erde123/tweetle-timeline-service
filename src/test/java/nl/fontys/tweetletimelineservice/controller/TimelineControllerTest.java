package nl.fontys.tweetletimelineservice.controller;

import nl.fontys.tweetletimelineservice.business.dto.TimelineResponse;
import nl.fontys.tweetletimelineservice.business.dto.TimelineTweetDto;
import nl.fontys.tweetletimelineservice.business.service.TimelineService;
import nl.fontys.tweetletimelineservice.persistence.document.Timeline;
import nl.fontys.tweetletimelineservice.persistence.document.Timeline.TweetRef;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimelineControllerTest {

    @Mock
    private TimelineService timelineService;

    @InjectMocks
    private TimelineController timelineController;

    private Timeline testTimeline;
    private TimelineResponse timelineResponse;

    @BeforeEach
    void setUp() {
        testTimeline = Timeline.builder()
                .id("1")
                .userId(100L)
                .tweets(List.of(
                        TweetRef.builder().tweetId("tweet123").authorId(200L).createdAt(System.currentTimeMillis()).build()
                ))
                .build();

        timelineResponse = new TimelineResponse(
                100L,
                List.of(new TimelineTweetDto("tweet123", 200L, "tester", "avatar.png", "hello world", System.currentTimeMillis()))
        );
    }

    @Test
    void getTimeline_ShouldReturnResponse() {
        when(timelineService.getTimelineResponse(100L)).thenReturn(timelineResponse);

        ResponseEntity<TimelineResponse> response = timelineController.getTimeline(100L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(timelineResponse, response.getBody());
        verify(timelineService, times(1)).getTimelineResponse(100L);
    }

    @Test
    void addTweetToTimeline_ShouldReturnUpdatedTimeline() {
        when(timelineService.addTweetToTimeline(100L, "tweet123", 200L)).thenReturn(testTimeline);

        ResponseEntity<Timeline> response = timelineController.addTweetToTimeline(100L, "tweet123", 200L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testTimeline, response.getBody());
        verify(timelineService, times(1)).addTweetToTimeline(100L, "tweet123", 200L);
    }

    @Test
    void clearTimeline_ShouldReturnNoContent() {
        doNothing().when(timelineService).clearTimeline(100L);

        ResponseEntity<Void> response = timelineController.clearTimeline(100L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(timelineService, times(1)).clearTimeline(100L);
    }
}
