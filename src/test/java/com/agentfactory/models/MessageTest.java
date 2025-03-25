package com.agentfactory.models;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class MessageTest {

    @Test
    public void constructor_ShouldSetRoleAndContent() {
        String role = "user";
        String content = "Hello, world!";

        Message message = new Message(role, content);

        assertThat(message.getRole()).isEqualTo(role);
        assertThat(message.getContent()).isEqualTo(content);
    }

    @Test
    public void getRole_ShouldReturnRole() {
        Message message = new Message("assistant", "I'm an assistant");
        
        assertThat(message.getRole()).isEqualTo("assistant");
    }

    @Test
    public void getContent_ShouldReturnContent() {
        String content = "This is test content";
        Message message = new Message("system", content);
        
        assertThat(message.getContent()).isEqualTo(content);
    }
}