
import com.dev.consumer.Consumer;
import com.dev.model.Message;
import com.dev.model.MessageQueue;
import com.dev.producer.Producer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Application started.");

        MessageQueue messageQueue = new MessageQueue(10);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger errorCount = new AtomicInteger(0);

        List<Message> messages = List.of(
                new Message("200", "OK"),
                new Message("201", "Created"),
                new Message("400", "Bad Request"),
                new Message("500", "Internal Server Error"),
                new Message("200", "OK"),
                new Message("404", "Not Found"),
                new Message("TERMINATE", "End Process")
        );

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Producer producer = new Producer(messageQueue, messages);
        Consumer consumer = new Consumer(messageQueue, successCount, errorCount);
        executorService.execute(producer);
        executorService.execute(consumer);

        executorService.shutdown();

        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }

        logger.info("Application finished. Success: {}, Errors: {}", successCount.get(), errorCount.get());
        logger.info("Application finished execution.");
    }

}
