package ok.serge.kafkaconsumer.service;

import lombok.extern.slf4j.Slf4j;
import ok.serge.kafkaconsumer.model.Metric;
import ok.serge.kafkaconsumer.repository.MetricRepository;
import ok.serge.metriccommon.dto.MetricDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MetricService {

    private final MetricRepository metricRepository;

    public MetricService(MetricRepository metricRepository) {
        this.metricRepository = metricRepository;
    }

    public List<MetricDto> findAll() {
        return metricRepository.findAll().stream()
                .map(MetricService::toDto)
                .collect(Collectors.toList());
    }

    public MetricDto findById(String id) {
        try {
            Metric metric = metricRepository.findById(UUID.fromString(id))
                    .orElseGet(() -> {
                        log.error("Не удалось найти метрику с ID: {}", id);
                        return null;
                    });
            return metric == null ? null : toDto(metric);
        } catch (Exception e) {
            return null;
        }
    }

    public List<MetricDto> findAllLastMetricsForSource(String source) {
        return metricRepository
                .findBySource(source)
                .stream()
                .map(MetricService::toDto)
                .collect(Collectors.toList());
    }

    private static MetricDto toDto(Metric metric) {
        return new MetricDto(
                metric.getId().toString(),
                metric.getName(),
                metric.getBaseUnit(),
                metric.getValue(),
                metric.getAppearance(),
                metric.getSource(),
                metric.getReceiptTime()
        );
    }
}
