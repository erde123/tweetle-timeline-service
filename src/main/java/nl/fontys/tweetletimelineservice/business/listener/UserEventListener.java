package nl.fontys.tweetletimelineservice.business.listener;

import nl.fontys.tweetletimelineservice.business.service.UserMetadataService;
import nl.fontys.tweetletimelineservice.domain.RabbitMQConstants;
import nl.fontys.tweetletimelineservice.domain.UserEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventListener {

    private final UserMetadataService userMetadataService;

    public UserEventListener(UserMetadataService userMetadataService) {
        this.userMetadataService = userMetadataService;
    }

    @RabbitListener(queues = RabbitMQConstants.TIMELINE_USER_REGISTER_QUEUE)
    public void handleUserRegister(UserEvent event) {
        System.out.println("📩 Timeline service received user register for: " + event.getUsername());
        userMetadataService.saveOrUpdate(event);
    }

    @RabbitListener(queues = RabbitMQConstants.TIMELINE_USER_UPDATE_QUEUE)
    public void handleUserUpdate(UserEvent event) {
        System.out.println("📩 Timeline service received user update for: " + event.getUsername());
        userMetadataService.saveOrUpdate(event);
    }
}
