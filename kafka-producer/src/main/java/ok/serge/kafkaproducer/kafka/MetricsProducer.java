package ok.serge.kafkaproducer.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ok.serge.kafkaproducer.service.KafkaMessageService;
import ok.serge.metriccommon.dto.MetricDto;
import ok.serge.metriccommon.event.MetricEvent;
import ok.serge.metriccommon.mapper.MetricMapper;
import ok.serge.metriccommon.metric.Appearance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class MetricsProducer {

    @Value("${kafka.metric.source}")
    private String source;

    private final KafkaMessageService messageService;

    private final MetricsEndpoint metricsEndpoint;

    @Scheduled(initialDelayString = "${kafka.start.time.init.delay}")
    public void sendStartTimeMetric() {
        makeMetricEventFromActuatorMetric("process.start.time");
    }

    @Scheduled(fixedRateString = "${kafka.default.metric.time.delay}")
    public void sendUptimeMetric() {
        makeMetricEventFromActuatorMetric("process.uptime");
    }

    @Scheduled(fixedRateString = "${kafka.default.metric.time.delay}")
    public void sendCpuMetric() {
        makeMetricEventFromActuatorMetric("process.cpu.usage");
    }

    @Scheduled(fixedRateString = "${kafka.default.metric.time.delay}")
    public void sendMemoryMetric() {
        makeMetricEventFromActuatorMetric("jvm.memory.used");
    }

    private void makeMetricEventFromActuatorMetric(String requiredMetricName) {
        MetricsEndpoint.MetricDescriptor metricDescriptor = metricsEndpoint.metric(requiredMetricName, null);
        String metricName = metricDescriptor.getName();
        String metricBaseUnit = metricDescriptor.getBaseUnit();
        List<MetricsEndpoint.Sample> sampleList = metricDescriptor.getMeasurements();
        if (sampleList.size() != 1) {
            log.error("Некорректный формат метрики {}. Ожидается единственное значение measurements, а получено {}",
                    requiredMetricName, sampleList.size());
            return;
        }

        MetricEvent event = new MetricEvent(
                UUID.randomUUID(),
                metricName,
                metricBaseUnit,
                sampleList.get(0).getValue(),
                Appearance.INTERNAL,
                source
        );
        messageService.sendMetrics(event);
    }

    public MetricDto sendCustomMetric(MetricDto dto) {
        UUID metricId = UUID.randomUUID();
        MetricEvent event = new MetricEvent(
                metricId,
                dto.getName(),
                dto.getBaseUnit(),
                dto.getValue(),
                Appearance.EXTERNAL,
                source
        );
        messageService.sendMetrics(event);

        return MetricMapper.eventToDto(event);
    }

    public MetricDto sendCustomMetricWithId(MetricDto dto) {
        MetricEvent event = new MetricEvent(
                UUID.fromString(dto.getId()),
                dto.getName(),
                dto.getBaseUnit(),
                dto.getValue(),
                Appearance.EXTERNAL,
                source
        );
        messageService.sendMetrics(event);

        return MetricMapper.eventToDto(event);
    }
}
