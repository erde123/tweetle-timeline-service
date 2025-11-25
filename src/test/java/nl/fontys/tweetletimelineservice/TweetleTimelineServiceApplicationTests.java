package nl.fontys.tweetletimelineservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class TweetleTimelineServiceApplicationTests {
    @Test
    void contextLoads() {
    }

}
