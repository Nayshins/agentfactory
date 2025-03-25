package com.agentfactory.agents;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.agentfactory.memory.AgentMemory;
import com.agentfactory.models.AIModel;
import com.agentfactory.tools.FinalAnswerTool;
import com.agentfactory.tools.Tool;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ToolCallingAgentTest {

    private static final String SYSTEM_PROMPT = "You are a helpful assistant";
    private static final String TASK = "Complete this task";
    private static final String FINAL_ANSWER = "This is the final answer";

    @Mock
    private AIModel mockModel;

    @Mock
    private Tool mockTool;

    @Mock
    private FinalAnswerTool finalAnswerTool;

    private ToolCallingAgent agent;

    @BeforeEach
    public void setUp() {
        // Set up the mock tools
        when(mockTool.getName()).thenReturn("mock_tool");
        when(finalAnswerTool.getName()).thenReturn("final_answer");
        
        // Set up the tool execution behavior
        when(mockTool.execute(any())).thenReturn("Tool execution result");
        when(finalAnswerTool.execute(argThat(args -> 
            args != null && FINAL_ANSWER.equals(args.get("answer"))))).thenReturn(FINAL_ANSWER);

        // Set up the agent
        List<Tool> tools = Arrays.asList(mockTool, finalAnswerTool);
        agent = new ToolCallingAgent(mockModel, SYSTEM_PROMPT, tools);
    }

    @Test
    public void getMemory_ShouldReturnMemory() {
        AgentMemory memory = agent.getMemory();
        assertThat(memory).isNotNull();
        assertThat(memory.getSteps()).isEmpty();
    }
    
    @Test
    public void run_WithFinalAnswerResponse_ShouldReturnFinalAnswer() {
        // Mock the model to return a response with a final_answer action
        String modelResponse = "{\"name\":\"final_answer\",\"arguments\":{\"answer\":\"" + FINAL_ANSWER + "\"}}";
        when(mockModel.generateResponse(any())).thenReturn(modelResponse);

        // Run the agent
        String result = agent.run(TASK);

        // Verify the result
        assertThat(result).isEqualTo(FINAL_ANSWER);
    }

    @Test
    public void run_WithMultipleSteps_ShouldReturnFinalAnswer() {
        // First response uses the mock tool
        String firstResponse = "{\"name\":\"mock_tool\",\"arguments\":{\"param\":\"value\"}}";
        // Second response provides the final answer
        String secondResponse = "{\"name\":\"final_answer\",\"arguments\":{\"answer\":\"" + FINAL_ANSWER + "\"}}";
        
        // Set up the model to return different responses in sequence
        when(mockModel.generateResponse(any()))
            .thenReturn(firstResponse)
            .thenReturn(secondResponse);

        // Run the agent
        String result = agent.run(TASK);

        // Verify the result
        assertThat(result).isEqualTo(FINAL_ANSWER);
        
        // Verify the memory contains both steps
        AgentMemory memory = agent.getMemory();
        assertThat(memory.getSteps()).hasSize(2);
    }

    @Test
    public void run_WithInvalidToolResponse_ShouldThrowException() {
        // Mock the model to return an invalid JSON response
        when(mockModel.generateResponse(any())).thenReturn("Invalid JSON");

        // Run the agent and verify exception
        assertThatThrownBy(() -> agent.run(TASK))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("valid action JSON");
    }

    @Test
    public void run_WithUnknownTool_ShouldThrowException() {
        // Mock the model to return a response with an unknown tool
        String modelResponse = "{\"name\":\"unknown_tool\",\"arguments\":{}}";
        when(mockModel.generateResponse(any())).thenReturn(modelResponse);

        // Run the agent and verify exception
        assertThatThrownBy(() -> agent.run(TASK))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unknown tool");
    }
}