package com.agentfactory.tools;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FinalAnswerToolTest {

    private FinalAnswerTool tool;

    @BeforeEach
    public void setUp() {
        tool = new FinalAnswerTool();
    }

    @Test
    public void getName_ShouldReturnCorrectName() {
        assertThat(tool.getName()).isEqualTo("final_answer");
    }

    @Test
    public void execute_WithValidAnswer_ShouldReturnAnswer() {
        String expectedAnswer = "This is the final answer";
        Map<String, Object> args = new HashMap<>();
        args.put("answer", expectedAnswer);

        Object result = tool.execute(args);

        assertThat(result).isEqualTo(expectedAnswer);
    }

    @Test
    public void execute_WithoutAnswer_ShouldThrowException() {
        Map<String, Object> args = new HashMap<>();

        assertThatThrownBy(() -> tool.execute(args))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("answer");
    }
}