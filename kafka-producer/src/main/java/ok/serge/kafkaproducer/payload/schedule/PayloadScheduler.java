package ok.serge.kafkaproducer.payload.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ok.serge.kafkaproducer.payload.Store;
import ok.serge.kafkaproducer.payload.utils.LifecycleService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PayloadScheduler {

    private final Store store;

    @Scheduled(fixedRateString = "${order.rate.in.milliseconds}")
    public void onReady() {
        List<String> order = LifecycleService.generateNameList(Store.PRODUCTS_LABEL, Store.PRODUCTS_MAX_COUNT);
        log.debug("Заказанные товары: " + order);
        log.debug("Полученные товары: " + store.doShopping(order));
    }
}
