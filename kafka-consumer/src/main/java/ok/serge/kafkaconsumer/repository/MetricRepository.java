package ok.serge.kafkaconsumer.repository;

import ok.serge.kafkaconsumer.model.Metric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface MetricRepository extends JpaRepository<Metric, UUID> {

    @Query("from Metric where source = :name and receiptTime in (select max(m.receiptTime) from Metric m group by m.name)")
    List<Metric> findBySource(String name);
}
