package pooh;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

public class QueueServiceTest {

    @Test
    public void whenPostThenGetQueue() {
        Service queueService = new QueueService();
        String paramForPostMethod = "temperature18";
        queueService.process(
                new Req("POST", "queue", "weather", paramForPostMethod)
        );
        Resp result = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        assertThat(result.getText()).isEqualTo("temperature18");
    }

    @Test
    public void whenGetNonExistingSource() {
        Service queueService = new QueueService();
        String paramForPostMethod = "temperature18";
        queueService.process(
                new Req("POST", "queue", "weather", paramForPostMethod)
        );
        Resp result = queueService.process(
                new Req("GET", "queue", "day", null)
        );
        assertThat(result.getStatus()).isEqualTo("204");
    }

    @Test
    public void whenHttpReqTypeIsIncorrectThenThrowException() {
        Service queueService = new QueueService();
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> queueService.process(
                        new Req("MISTAKE_HERE", "queue", "weather", null)
                )).withMessage("Input correct httpType");
    }

}