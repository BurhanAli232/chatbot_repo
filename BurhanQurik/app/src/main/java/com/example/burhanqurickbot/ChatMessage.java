package com.example.burhanqurickbot;

public class ChatMessage {
    private String sender;
    private String message;

    // Constructor
    public ChatMessage(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    // Getters
    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String s) {

    }
}
