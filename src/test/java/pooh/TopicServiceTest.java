package pooh;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class TopicServiceTest {

    @Test
    public void whenTopic() {
        Service topicService = new TopicService();
        String paramForPublisher = "temperature18";
        String paramForSubscriber1 = "client407";
        String paramForSubscriber2 = "client6565";
        topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher)
        );
        Resp result1 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        Resp result2 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );
        assertThat(result1.getText()).isEqualTo("temperature18");
        assertThat(result2.getText()).isEqualTo("");
    }

    @Test
    public void whenTopicHasFewSubs() {
        Service topicService = new TopicService();
        String paramForPublisher = "temperature18";
        String paramForSubscriber1 = "client407";
        String paramForSubscriber2 = "client6565";
        topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );
        topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher)
        );
        Resp result1 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        Resp result2 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );
        assertThat(result1).isEqualTo(result2);
    }

    @Test
    public void whenHttpReqTypeIsIncorrectThenThrowException() {
        Service topicService = new TopicService();
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> topicService.process(
                        new Req("MISTAKE_HERE", "queue", "weather", null)
                )).withMessage("Input correct httpType");
    }

}