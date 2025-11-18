package nl.fontys.tweetletimelineservice.business.service;

import nl.fontys.tweetletimelineservice.business.dto.TweetCreatedEvent;
import nl.fontys.tweetletimelineservice.business.dto.TweetDeletedEvent;
import nl.fontys.tweetletimelineservice.domain.RabbitMQConstants;
import nl.fontys.tweetletimelineservice.domain.UserEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ConsumerService {

    private final TimelineService timelineService;
    private final TweetMetadataService tweetMetadataService;
    private final UserMetadataService userMetadataService;

    public ConsumerService(TimelineService timelineService,
                           TweetMetadataService tweetMetadataService,
                           UserMetadataService userMetadataService) {
        this.timelineService = timelineService;
        this.tweetMetadataService = tweetMetadataService;
        this.userMetadataService = userMetadataService;
    }

    @RabbitListener(queues = RabbitMQConstants.TIMELINE_TWEET_CREATED_QUEUE)
    public void handleTweetCreated(TweetCreatedEvent event) {
        tweetMetadataService.saveFromEvent(event);
        timelineService.insertTweetIntoFollowersTimelines(event);
    }

    @RabbitListener(queues = RabbitMQConstants.TIMELINE_TWEET_DELETED_QUEUE)
    public void handleTweetDeleted(TweetDeletedEvent event) {
        tweetMetadataService.deleteById(event.getId());
        timelineService.removeTweetFromAllTimelines(event.getId());
    }

    @RabbitListener(queues = RabbitMQConstants.TIMELINE_USER_DELETE_QUEUE)
    public void handleUserDeleted(UserEvent event) {
        userMetadataService.deleteUser(event.getUserId());
        timelineService.deleteTimeline(event.getUserId());
    }
}
