package ok.serge.metriccommon.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ok.serge.metriccommon.metric.Appearance;
import ok.serge.metriccommon.view.MetricView;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetricDto {

    @JsonView({MetricView.Full.class, MetricView.NoReceiptTime.class, MetricView.NewWithId.class})
    private String id;

    @JsonView({MetricView.Full.class, MetricView.NoReceiptTime.class, MetricView.New.class, MetricView.NewWithId.class})
    private String name;

    @JsonView({MetricView.Full.class, MetricView.NoReceiptTime.class, MetricView.New.class, MetricView.NewWithId.class})
    private String baseUnit;

    @JsonView({MetricView.Full.class, MetricView.NoReceiptTime.class, MetricView.New.class, MetricView.NewWithId.class})
    private Double value;

    @JsonView({MetricView.Full.class, MetricView.NoReceiptTime.class})
    private Appearance appearance;

    @JsonView({MetricView.Full.class, MetricView.NoReceiptTime.class, MetricView.New.class, MetricView.NewWithId.class})
    private String source;

    @JsonView(MetricView.Full.class)
    private LocalDateTime receiptTime;
}
