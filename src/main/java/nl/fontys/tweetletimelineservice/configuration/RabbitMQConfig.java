package nl.fontys.tweetletimelineservice.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import static nl.fontys.tweetletimelineservice.domain.RabbitMQConstants.*;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange.name}")
    private String eventExchangeName;

    @Bean
    public TopicExchange eventExchange() {
        return new TopicExchange(eventExchangeName);
    }

    @Bean
    public TopicExchange userAuthExchange() {
        return new TopicExchange(USER_AUTH_EXCHANGE);
    }

    @Bean
    public TopicExchange userGeneralExchange() {
        return new TopicExchange(USER_GENERAL_EXCHANGE);
    }

    @Bean
    public Queue tweetCreatedQueue() {
        return new Queue(TIMELINE_TWEET_CREATED_QUEUE, true);
    }

    @Bean
    public Binding tweetCreatedBinding() {
        return BindingBuilder.bind(tweetCreatedQueue())
                .to(eventExchange())
                .with(TWEET_CREATED_KEY);
    }

    @Bean
    public Queue tweetDeletedQueue() {
        return new Queue(TIMELINE_TWEET_DELETED_QUEUE, true);
    }

    @Bean
    public Binding tweetDeletedBinding() {
        return BindingBuilder.bind(tweetDeletedQueue())
                .to(eventExchange())
                .with(TWEET_DELETED_KEY);
    }

    @Bean
    public Queue followCreatedQueue() {
        return new Queue(TIMELINE_FOLLOW_CREATED_QUEUE, true);
    }

    @Bean
    public Binding followCreatedBinding() {
        return BindingBuilder.bind(followCreatedQueue())
                .to(eventExchange())
                .with(FOLLOW_CREATED_KEY);
    }

    @Bean
    public Queue followDeletedQueue() {
        return new Queue(TIMELINE_FOLLOW_DELETED_QUEUE, true);
    }

    @Bean
    public Binding followDeletedBinding() {
        return BindingBuilder.bind(followDeletedQueue())
                .to(eventExchange())
                .with(FOLLOW_DELETED_KEY);
    }

    @Bean
    public Queue timelineUserDeleteQueue() {
        return new Queue(TIMELINE_USER_DELETE_QUEUE, true);
    }

    @Bean
    public Binding timelineUserDeletedBinding() {
        return BindingBuilder.bind(timelineUserDeleteQueue())
                .to(userGeneralExchange())
                .with(USER_DELETED_KEY);
    }

    @Bean
    public Queue timelineUserRegisterQueue() {
        return new Queue(TIMELINE_USER_REGISTER_QUEUE, true);
    }

    @Bean
    public Binding timelineUserRegisterBinding() {
        return BindingBuilder.bind(timelineUserRegisterQueue())
                .to(userAuthExchange())
                .with(USER_AUTH_REGISTERED_KEY);
    }

    @Bean
    public Queue timelineUserUpdateQueue() {
        return new Queue(TIMELINE_USER_UPDATE_QUEUE, true);
    }

    @Bean
    public Binding timelineUserUpdateBinding() {
        return BindingBuilder.bind(timelineUserUpdateQueue())
                .to(userGeneralExchange())
                .with(USER_UPDATED_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setAlwaysConvertToInferredType(true);
        return converter;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}
