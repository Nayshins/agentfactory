package com.agentfactory.models;

import java.util.List;

/**
 * Interface for AI models that can generate responses from messages.
 * This abstraction allows for different model implementations (OpenAI, Anthropic, etc.).
 */
public interface AIModel {
    /**
     * Generates a response from the AI model based on a list of messages.
     *
     * @param messages the list of messages in the conversation
     * @return the generated response as a string
     */
    String generateResponse(List<Message> messages);
}
