package ok.serge.kafkaproducer.payload.utils;

public class WorkerUtils {

    public static void wasteTime(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
