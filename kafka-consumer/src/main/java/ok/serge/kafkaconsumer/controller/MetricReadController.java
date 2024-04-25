package ok.serge.kafkaconsumer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ok.serge.kafkaconsumer.service.MetricService;
import ok.serge.metriccommon.dto.MetricDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "metrics", produces = "application/json")
@Tag(name = "Consumer API", description = "Получение и анализ метрик")
@RequiredArgsConstructor
public class MetricReadController {

    private final MetricService metricService;

    @GetMapping
    @Operation(summary = "Получение списка всех метрик")
    @ApiResponse(responseCode = "200", description = "Список всех метрик")
    public List<MetricDto> findAll() {
        return metricService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение конкретной метрики по ее идентификатору")
    @ApiResponse(responseCode = "200", description = "Запрошенная метрика (при наличии)")
    public MetricDto findById(
            @Parameter(description = "Идентификатор метрики (UUID)", required = true)
            @PathVariable String id
    ) {
        return metricService.findById(id);
    }

    @GetMapping("/source/{sourceName}")
    @Operation(summary = "Получение актуальных метрик узла (продюсера) по его наименованию в системе")
    @ApiResponse(responseCode = "200", description = "Список всех метрик")
    public List<MetricDto> findActualForSource(
            @Parameter(description = "Наименование узла в системе", required = true)
            @PathVariable String sourceName
    ) {
        return metricService.findAllLastMetricsForSource(sourceName);
    }
}
