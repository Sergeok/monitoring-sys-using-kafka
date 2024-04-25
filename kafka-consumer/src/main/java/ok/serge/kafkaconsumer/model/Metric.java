package ok.serge.kafkaconsumer.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ok.serge.metriccommon.metric.Appearance;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Metric {

    @Id
    private UUID id;

    @Column
    private String name;

    @Column
    private String baseUnit;

    @Column
    private Double value;

    @Column
    private Appearance appearance;

    @Column
    private String source;

    @Column
    private LocalDateTime receiptTime;
}
