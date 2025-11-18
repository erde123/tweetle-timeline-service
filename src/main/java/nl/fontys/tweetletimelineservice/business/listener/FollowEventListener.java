package nl.fontys.tweetletimelineservice.business.listener;

import nl.fontys.tweetletimelineservice.business.dto.FollowEvent;
import nl.fontys.tweetletimelineservice.business.service.FollowEventService;
import nl.fontys.tweetletimelineservice.domain.RabbitMQConstants;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FollowEventListener {

    private final FollowEventService followEventService;

    public FollowEventListener(FollowEventService followEventService) {
        this.followEventService = followEventService;
    }

    @RabbitListener(queues = RabbitMQConstants.TIMELINE_FOLLOW_CREATED_QUEUE)
    public void handleFollowCreated(FollowEvent event) {
        System.out.println("📩 Timeline service received follow.created: follower=" + event.getFollowerId() +
                " -> followed=" + event.getFollowedId());
        followEventService.handleFollowCreated(event);
    }

    @RabbitListener(queues = RabbitMQConstants.TIMELINE_FOLLOW_DELETED_QUEUE)
    public void handleFollowDeleted(FollowEvent event) {
        System.out.println("📩 Timeline service received follow.deleted: follower=" + event.getFollowerId() +
                " -> followed=" + event.getFollowedId());
        followEventService.handleFollowDeleted(event);
    }
}
