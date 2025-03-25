package com.agentfactory.models;

/**
 * Represents a message in a conversation with an AI model.
 * Each message has a role (e.g., "system", "user", "assistant") and content.
 */
public class Message {
    private String role;
    private String content;

    /**
     * Creates a new message with the specified role and content.
     *
     * @param role the role of the message sender (e.g., "system", "user", "assistant")
     * @param content the content of the message
     */
    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }

    /**
     * Gets the role of the message sender.
     *
     * @return the role (e.g., "system", "user", "assistant")
     */
    public String getRole() {
        return role;
    }

    /**
     * Gets the content of the message.
     *
     * @return the message content
     */
    public String getContent() {
        return content;
    }
}
