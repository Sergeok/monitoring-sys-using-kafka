package ok.serge.metriccommon.mapper;

import ok.serge.metriccommon.dto.MetricDto;
import ok.serge.metriccommon.event.MetricEvent;

public class MetricMapper {

    public static MetricDto eventToDto(MetricEvent event) {
        return new MetricDto(
                event.getId().toString(),
                event.getName(),
                event.getBaseUnit(),
                event.getValue(),
                event.getAppearance(),
                event.getSource(),
                null
        );
    }
}
