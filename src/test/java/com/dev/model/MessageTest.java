package com.dev.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class MessageTest {

    @Test
    void testMessageCreation() {
        Message message = new Message("200", "Test Message");

        assertEquals("200", message.getStatusCode());
        assertEquals("Test Message", message.getContent());
    }

    @Test
    void testToStringMethod() {
        Message message = new Message("404", "Not Found");
        assertEquals("Message{statusCode='404', content='Not Found'}", message.toString());
    }

}