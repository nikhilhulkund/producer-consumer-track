package com.dev.producer;

import static org.junit.jupiter.api.Assertions.*;

import com.dev.model.Message;
import com.dev.model.MessageQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class ProducerTest {

    private MessageQueue messageQueue;
    private Producer producer;

    @BeforeEach
    void setUp() {
        messageQueue = mock(MessageQueue.class);
        List<Message> messages = Arrays.asList(
                new Message("200", "Success"),
                new Message("400", "Client Error"),
                new Message("500","Internal server error")
        );
        producer = new Producer(messageQueue, messages);
    }

    @Test
    void testProduceMessages() {
        producer.run();

        verify(messageQueue, times(3)).produce(any(Message.class));
    }

}