package ok.serge.kafkaconsumer.kafka;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ok.serge.kafkaconsumer.model.Metric;
import ok.serge.kafkaconsumer.repository.MetricRepository;
import ok.serge.metriccommon.event.MetricEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class MetricsConsumer {

    @Value("${kafka.unknown.topic.name}")
    private String unknownTopicName;

    private final KafkaTemplate<Object, Object> template;

    private final MetricRepository metricRepository;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(id = "${kafka.metrics.group.id}", topics = "${kafka.metrics.topic.name}")
    @RetryableTopic(
            attempts = "4",
            backoff = @Backoff(delay = 2000, maxDelay = 10000)
    )
    public void listen(String event) throws JsonProcessingException {
        try {
            log.debug("Metrics-listener получил сообщение: {}", event);
            MetricEvent metricEvent = objectMapper.readValue(event, MetricEvent.class);
            if (metricRepository.findById(metricEvent.getId()).isEmpty()) {
                metricRepository.save(toEntity(metricEvent));
                log.info("Метрика сохранена в БД: {}", metricEvent);
            } else {
                throw new RuntimeException("В БД уже присутствует запись метрики с заданным ID. Метрика не может быть добавлена.");
            }
        } catch (JsonParseException e) {
            template.send(unknownTopicName, event);
        }
    }

    @DltHandler
    public void listenDlt(String event, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic, @Header(KafkaHeaders.OFFSET) long offset) {
        log.info("DLT Handler получил сообщение: {}, topic: {}, offset: {}", event, topic, offset);
    }

    @KafkaListener(id = "${kafka.unknown.group.id}", topics = "${kafka.unknown.topic.name}")
    public void unknown(String event) {
        log.info("Unknown-listener получил сообщение: {}", event);
    }

    private static Metric toEntity(MetricEvent event) {
        return new Metric(
                event.getId(),
                event.getName(),
                event.getBaseUnit(),
                event.getValue(),
                event.getAppearance(),
                event.getSource(),
                LocalDateTime.now()
        );
    }
}
