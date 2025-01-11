package com.dev.producer;

import com.dev.model.Message;
import com.dev.model.MessageQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Producer implements  Runnable{

    private static final Logger logger = LogManager.getLogger(Producer.class);
    private final MessageQueue messageQueue;
    private final List<Message> messages;

    public Producer(MessageQueue messageQueue, List<Message> messages) {
        this.messageQueue = messageQueue;
        this.messages = messages;
    }

    @Override
    public void run() {
        for (Message message : messages) {
            messageQueue.produce(message);
        }
        logger.info("Producer {} finished producing messages.", Thread.currentThread().getName());
    }

}
