package com.dev.consumer;

import com.dev.model.Message;
import com.dev.model.MessageQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

public class Consumer implements Runnable {

    private static final Logger logger = LogManager.getLogger(Consumer.class);
    private final MessageQueue messageQueue;
    private final AtomicInteger successCount;
    private final AtomicInteger errorCount;

    public Consumer(MessageQueue messageQueue, AtomicInteger successCount, AtomicInteger errorCount) {
        this.messageQueue = messageQueue;
        this.successCount = successCount;
        this.errorCount = errorCount;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Message message = messageQueue.consume();
                if ("TERMINATE".equals(message.getStatusCode())) {
                    logger.info("Consumer {} received TERMINATE signal. Stopping...", Thread.currentThread().getName());
                    break;
                }
                processMessage(message);
                successCount.incrementAndGet();
            } catch (Exception e) {
                errorCount.incrementAndGet();
                logger.error("Error processing message", e);
            }
        }
        logger.info("Consumer {} finished processing messages.", Thread.currentThread().getName());
    }

    private void processMessage(Message message) {
        int statusCode = Integer.parseInt(message.getStatusCode());

        if (statusCode >= 400 && statusCode <= 599) {
            throw new RuntimeException("Failed to process message: " + message);
        }

        logger.info("Message processed successfully: {}", message);
    }

}
