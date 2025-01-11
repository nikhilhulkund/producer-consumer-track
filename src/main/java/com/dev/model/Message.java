package com.dev.model;

public class Message {

    private final String statusCode;
    private final String content;

    public Message(String statusCode, String content) {
        this.statusCode = statusCode;
        this.content = content;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Message{statusCode='" + statusCode + "', content='" + content + "'}";
    }

}
