package com.agentfactory.models;

import java.util.List;

public interface AIModel {
    String generateResponse(List<Message> messages);
}
