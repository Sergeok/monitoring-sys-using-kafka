package ok.serge.kafkaproducer.payload;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class Warehouse {

    public List<Map.Entry<Worker, String>> receiveWorkersWithProducts(List<CompletableFuture<Map.Entry<Worker, String>>> futureQueue) {
        return futureQueue.stream()
                .map(CompletableFuture::join)
                .toList();
    }

    @Async(value = "workerExecutor")
    public CompletableFuture<Map.Entry<Worker, String>> delegateWork(Worker worker, String productName) {
        if (worker == null || productName == null) {
            throw new RuntimeException("Отсутствует назначаемый работник или заказываемый продукт");
        }
        return CompletableFuture.completedFuture(
                worker.bringProduct(productName)
        );
    }
}
