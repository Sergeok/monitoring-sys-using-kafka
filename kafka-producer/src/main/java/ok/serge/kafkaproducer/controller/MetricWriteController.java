package ok.serge.kafkaproducer.controller;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ok.serge.kafkaproducer.kafka.MetricsProducer;
import ok.serge.metriccommon.dto.MetricDto;
import ok.serge.metriccommon.view.MetricView;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "metrics", produces = "application/json")
@Tag(name = "Producer API", description = "Ручное добавление метрик")
@RequiredArgsConstructor
public class MetricWriteController {

    private final MetricsProducer metricsProducer;

    @PostMapping
    @Operation(summary = "Ручное добавление метрики")
    @ApiResponse(responseCode = "200", description = "Отправленное в брокер сообщение")
    public @JsonView(MetricView.NoReceiptTime.class) MetricDto addCustomMetric(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные метрики", required = true)
            @RequestBody() @JsonView(MetricView.New.class)
            MetricDto dto
    ) {
        return metricsProducer.sendCustomMetric(dto);
    }

    @PostMapping("/force")
    @Operation(summary = "Ручное добавление метрики с заданным UUID")
    @ApiResponse(responseCode = "200", description = "Отосланное в брокер сообщение")
    public @JsonView(MetricView.NoReceiptTime.class) MetricDto addCustomMetricWithForcedId(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные метрики (включая UUID)", required = true)
            @RequestBody @JsonView(MetricView.NewWithId.class)
            MetricDto dto
    ) {
        return metricsProducer.sendCustomMetricWithId(dto);
    }
}
