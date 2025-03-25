package com.agentfactory.models;

public class Message {
    private String role; // e.g., "system", "user", "assistant"
    private String content;

    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public String getRole() {
        return role;
    }

    public String getContent() {
        return content;
    }
}
