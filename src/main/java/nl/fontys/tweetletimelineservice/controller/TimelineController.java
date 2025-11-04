package nl.fontys.tweetletimelineservice.controller;

import lombok.RequiredArgsConstructor;
import nl.fontys.tweetletimelineservice.business.service.TimelineService;
import nl.fontys.tweetletimelineservice.persistence.document.Timeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/timeline")
public class TimelineController {

    @Autowired
    private TimelineService timelineService;

    @GetMapping("/{userId}")
    public ResponseEntity<Timeline> getTimeline(@PathVariable Long userId) {
        Timeline timeline = timelineService.getTimeline(userId);
        return (timeline == null)
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(timeline);
    }

    @PostMapping("/{userId}/tweet/{tweetId}/author/{authorId}")
    public ResponseEntity<Timeline> addTweetToTimeline(
            @PathVariable Long userId,
            @PathVariable String tweetId,
            @PathVariable Long authorId
    ) {
        Timeline updated = timelineService.addTweetToTimeline(userId, tweetId, authorId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> clearTimeline(@PathVariable Long userId) {
        timelineService.clearTimeline(userId);
        return ResponseEntity.noContent().build();
    }
}

