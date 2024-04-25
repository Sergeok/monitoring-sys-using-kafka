package ok.serge.kafkaproducer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ok.serge.metriccommon.event.MetricEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaMessageService {

    @Value("${kafka.metrics.topic.name}")
    private String sendClientTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMetrics(MetricEvent metricEvent) {
        try {
            kafkaTemplate.send(sendClientTopic, metricEvent);
            log.info("В топик {} было отправлено сообщение: {}", sendClientTopic, metricEvent);
        } catch (Exception e) {
            log.error("Не удалось отправить в топик {} сообщение: {}", sendClientTopic, metricEvent, e);
        }
    }
}
