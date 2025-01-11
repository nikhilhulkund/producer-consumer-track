import static org.junit.jupiter.api.Assertions.*;

import com.dev.consumer.Consumer;
import com.dev.model.Message;
import com.dev.model.MessageQueue;
import com.dev.producer.Producer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Mockito.*;

class MainTest {

    private static final Logger logger = LogManager.getLogger(MainTest.class);
    private MessageQueue messageQueue;
    private List<Message> messages;
    private AtomicInteger successCount;
    private AtomicInteger errorCount;
    private ExecutorService executorService;

    @BeforeEach
    void setUp() {
        messageQueue = new MessageQueue(10);
        messages = List.of(
                new Message("200", "Success Message"),
                new Message("400", "Client Error"),
                new Message("500", "Server Error"),
                new Message("TERMINATE", "End Process")
        );
        successCount = new AtomicInteger(0);
        errorCount = new AtomicInteger(0);
        executorService = Executors.newFixedThreadPool(2);
    }

    @Test
    void testMainExecutionFlow() throws InterruptedException {
        logger.info("Starting MainTest execution.");

        Producer producerMock = mock(Producer.class);
        Consumer consumerMock = mock(Consumer.class);

        // Execute producer and consumer threads
        executorService.execute(producerMock);
        executorService.execute(consumerMock);

        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

        // Verify both producer and consumer were executed
        verify(producerMock, times(1)).run();
        verify(consumerMock, times(1)).run();

        logger.info("MainTest execution completed.");
    }

    @Test
    void testExecutorServiceShutdown() throws InterruptedException {
        ExecutorService executorServiceSpy = spy(Executors.newFixedThreadPool(2));

        Producer producer = new Producer(messageQueue, messages);
        Consumer consumer = new Consumer(messageQueue, successCount, errorCount);

        executorServiceSpy.execute(producer);
        executorServiceSpy.execute(consumer);

        executorServiceSpy.shutdown();
        executorServiceSpy.awaitTermination(5, TimeUnit.SECONDS);

        verify(executorServiceSpy, times(1)).shutdown();
    }

    @Test
    void testMessageProcessingCounts() {
        MessageQueue mockQueue = mock(MessageQueue.class);

        when(mockQueue.consume())
                .thenReturn(new Message("200", "Success Message"))
                .thenReturn(new Message("500", "Server Error"))
                .thenReturn(new Message("TERMINATE", "End Process"));

        Consumer consumer = new Consumer(mockQueue, successCount, errorCount);
        consumer.run();

        assert successCount.get() == 1;
        assert errorCount.get() == 1;
    }

    @Test
    void testApplicationLogs() {
        logger.info("Testing log output.");
        logger.error("Simulated error log.");
        logger.warn("Simulated warning log.");
    }

}