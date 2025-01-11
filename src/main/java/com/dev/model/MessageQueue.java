package com.dev.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageQueue {

    private static final Logger logger = LogManager.getLogger(MessageQueue.class);
    private final BlockingQueue<Message> queue;

    public MessageQueue(int capacity) {
        this.queue = new LinkedBlockingQueue<>(capacity);
    }

    public void produce(Message message) {
        try {
            queue.put(message);
            logger.info("Produced: {}", message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Error producing message: {}", message, e);
        }
    }

    public Message consume() {
        try {
            Message message = queue.take();
            logger.info("Consumed: {}", message);
            return message;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Error consuming message", e);
            return null;
        }
    }
}
