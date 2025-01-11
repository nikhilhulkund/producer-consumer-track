package com.dev.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageQueueTest {

    private MessageQueue messageQueue;

    @BeforeEach
    void setUp() {
        messageQueue = new MessageQueue(3);
    }

    @Test
    void testProduceAndConsumeMessage() {
        Message message = new Message("200", "Test Message");

        messageQueue.produce(message);
        Message consumedMessage = messageQueue.consume();

        assertNotNull(consumedMessage, "Consumed message should not be null.");
        assertEquals("200", consumedMessage.getStatusCode(), "Status code should match.");
        assertEquals("Test Message", consumedMessage.getContent(), "Content should match.");
    }

    @Test
    void testConsumeFromEmptyQueueBlocks() {
        Thread consumerThread = new Thread(() -> {
            Message result = messageQueue.consume();
            assertNull(result, "Expected null when consuming from an empty queue.");
        });

        consumerThread.start();
        try {
            Thread.sleep(1000);  // Let it attempt to consume
            consumerThread.interrupt(); // Interrupt the thread since it will block forever
            consumerThread.join();
        } catch (InterruptedException e) {
            fail("Test interrupted unexpectedly.");
        }
    }

    @Test
    void testMultipleMessagesProducedAndConsumedCorrectly() {
        Message message1 = new Message("201", "Message 1");
        Message message2 = new Message("202", "Message 2");
        Message message3 = new Message("203", "Message 3");

        messageQueue.produce(message1);
        messageQueue.produce(message2);
        messageQueue.produce(message3);

        assertEquals(message1, messageQueue.consume(), "First consumed message should be message1.");
        assertEquals(message2, messageQueue.consume(), "Second consumed message should be message2.");
        assertEquals(message3, messageQueue.consume(), "Third consumed message should be message3.");
    }

    @Test
    void testQueueCapacityRespected() {
        Message message1 = new Message("200", "Message A");
        Message message2 = new Message("201", "Message B");
        Message message3 = new Message("202", "Message C");

        messageQueue.produce(message1);
        messageQueue.produce(message2);
        messageQueue.produce(message3);

        assertEquals(message1, messageQueue.consume(), "First consumed message should be Message A.");
        assertEquals(message2, messageQueue.consume(), "Second consumed message should be Message B.");
        assertEquals(message3, messageQueue.consume(), "Third consumed message should be Message C.");
    }


}