package nl.fontys.tweetletimelineservice.domain;

public final class RabbitMQConstants {

    private RabbitMQConstants() {
    }

    public static final String USER_AUTH_EXCHANGE = "user-auth-exchange";
    public static final String USER_GENERAL_EXCHANGE = "user-general-exchange";

    public static final String USER_AUTH_REGISTERED_KEY = "user.auth.registered";
    public static final String USER_UPDATED_KEY = "user.updated";
    public static final String USER_DELETED_KEY = "user.deleted";
    public static final String TWEET_CREATED_KEY = "tweet.created";
    public static final String TWEET_DELETED_KEY = "tweet.deleted";
    public static final String FOLLOW_CREATED_KEY = "follow.created";
    public static final String FOLLOW_DELETED_KEY = "follow.deleted";

    public static final String TIMELINE_TWEET_CREATED_QUEUE = "timeline.tweet.created.queue";
    public static final String TIMELINE_TWEET_DELETED_QUEUE = "timeline.tweet.deleted.queue";
    public static final String TIMELINE_FOLLOW_CREATED_QUEUE = "timeline.follow.created.queue";
    public static final String TIMELINE_FOLLOW_DELETED_QUEUE = "timeline.follow.deleted.queue";
    public static final String TIMELINE_USER_REGISTER_QUEUE = "timeline.user.register.queue";
    public static final String TIMELINE_USER_UPDATE_QUEUE = "timeline.user.update.queue";
    public static final String TIMELINE_USER_DELETE_QUEUE = "timeline.user.deleted.queue";
}
