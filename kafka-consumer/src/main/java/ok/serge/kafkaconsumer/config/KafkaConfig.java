package ok.serge.kafkaconsumer.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic metrics(@Value(value = "${kafka.metrics.topic.name}") String metricsTopicName) {
        return new NewTopic(metricsTopicName, 1, (short)1);
    }

    @Bean
    public NewTopic dlt(@Value(value = "${kafka.dlt.metrics.topic.name}") String dltMetricsTopicName) {
        return new NewTopic(dltMetricsTopicName, 1, (short)1);
    }
}
