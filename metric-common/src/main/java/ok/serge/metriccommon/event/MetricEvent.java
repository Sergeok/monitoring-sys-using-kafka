package ok.serge.metriccommon.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ok.serge.metriccommon.metric.Appearance;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricEvent {

    private UUID id;

    private String name;

    private String baseUnit;

    private Double value;

    private Appearance appearance;

    private String source;
}
