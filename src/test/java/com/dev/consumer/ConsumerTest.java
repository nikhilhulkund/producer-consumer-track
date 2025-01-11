package com.dev.consumer;

import static org.junit.jupiter.api.Assertions.*;

import com.dev.model.Message;
import com.dev.model.MessageQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Mockito.*;

class ConsumerTest {

    private MessageQueue messageQueue;
    private AtomicInteger successCount;
    private AtomicInteger errorCount;
    private Consumer consumer;

    @BeforeEach
    void setUp() {
        messageQueue = mock(MessageQueue.class);
        successCount = new AtomicInteger(0);
        errorCount = new AtomicInteger(0);
        consumer = new Consumer(messageQueue, successCount, errorCount);
    }

    @Test
    void testConsumeTerminateMessage() {
        Message terminateMessage = new Message("TERMINATE", "Stop Message");

        when(messageQueue.consume()).thenReturn(terminateMessage);

        consumer.run();

        verify(messageQueue, times(1)).consume();
    }

    @Test
    void testConsumeValidMessageSuccessfully() {
        Message validMessage = new Message("200", "Valid Message");

        when(messageQueue.consume()).thenReturn(validMessage).thenReturn(new Message("TERMINATE", "Stop"));

        consumer.run();

        verify(messageQueue, times(2)).consume();
        assertEquals(1, successCount.get());
        assertEquals(0, errorCount.get());
    }

    @Test
    void testConsumeErrorMessage() {
        Message errorMessage = new Message("500", "Server Error");

        when(messageQueue.consume()).thenReturn(errorMessage).thenReturn(new Message("TERMINATE", "Stop"));

        consumer.run();

        verify(messageQueue, times(2)).consume();
        assertEquals(0, successCount.get());
        assertEquals(1, errorCount.get());
    }

    @Test
    void testConsumeMultipleMessagesWithDifferentStatusCodes() {
        when(messageQueue.consume())
                .thenReturn(new Message("200", "Success"))
                .thenReturn(new Message("400", "Client Error"))
                .thenReturn(new Message("500", "Server Error"))
                .thenReturn(new Message("TERMINATE", "Stop"));

        consumer.run();

        verify(messageQueue, times(4)).consume();
        assertEquals(1, successCount.get());
        assertEquals(2, errorCount.get());
    }

    @Test
    void testConsumerHandlesExceptionDuringProcessing() {
        Message message = new Message("INVALID", "Bad Data");

        when(messageQueue.consume()).thenReturn(message).thenReturn(new Message("TERMINATE", "Stop"));

        consumer.run();

        verify(messageQueue, times(2)).consume();
        assertEquals(0, successCount.get());
        assertEquals(1, errorCount.get());
    }

}