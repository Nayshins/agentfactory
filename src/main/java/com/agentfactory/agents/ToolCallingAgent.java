package com.agentfactory.agents;

import com.agentfactory.memory.ActionStep;
import com.agentfactory.memory.AgentMemory;
import com.agentfactory.models.AIModel;
import com.agentfactory.models.Message;
import com.agentfactory.prompts.PromptLoader;
import com.agentfactory.tools.Tool;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.*;

/**
 * An agent that can call tools to accomplish tasks.
 * The agent processes tasks by generating responses from an AI model and
 * executing tool calls until a final answer is reached.
 */
public class ToolCallingAgent {
    private final AIModel model;
    private final AgentMemory memory;
    private final Map<String, Tool> tools;
    private final String systemPrompt;
    private final Gson gson = new Gson();
    private int stepCounter = 0;
    private static final int MAX_STEPS = 10;

    /**
     * Creates a new tool calling agent with the specified model and tools.
     * Loads the default system prompt from configuration.
     *
     * @param model the AI model to use for generating responses
     * @param tools the list of tools available to the agent
     */
    public ToolCallingAgent(AIModel model, List<Tool> tools) {
        this.model = model;
        this.memory = new AgentMemory();
        this.tools = new HashMap<>();
        for (Tool tool : tools) {
            this.tools.put(tool.getName(), tool);
        }
        this.systemPrompt = PromptLoader.loadPrompt("toolcalling_agent.yaml");
    }

    /**
     * Creates a new tool calling agent with the specified model, system prompt, and tools.
     *
     * @param model the AI model to use for generating responses
     * @param systemPrompt the system prompt to use for the agent
     * @param tools the list of tools available to the agent
     */
    public ToolCallingAgent(AIModel model, String systemPrompt, List<Tool> tools) {
        this.model = model;
        this.memory = new AgentMemory();
        this.tools = new HashMap<>();
        for (Tool tool : tools) {
            this.tools.put(tool.getName(), tool);
        }
        this.systemPrompt = systemPrompt;
    }

    /**
     * Runs the agent on the specified task.
     * The agent will generate responses and execute tool calls until a final answer is reached
     * or the maximum number of steps is exceeded.
     *
     * @param task the task for the agent to perform
     * @return the final answer from the agent
     * @throws IllegalArgumentException if an unknown tool is called
     * @throws RuntimeException if the maximum number of steps is reached without a final answer
     */
    public String run(String task) {
        List<Message> messages = new ArrayList<>();
        messages.add(new Message("system", systemPrompt));
        messages.add(new Message("user", task));

        while (stepCounter < MAX_STEPS) {
            String response = model.generateResponse(messages);
            String actionJson = extractAction(response);
            JsonObject action = JsonParser.parseString(actionJson).getAsJsonObject();
            String toolName = action.get("name").getAsString();

            if (!tools.containsKey(toolName)) {
                throw new IllegalArgumentException("Unknown tool: " + toolName);
            }

            Map<String, Object> args = gson.fromJson(action.get("arguments"), Map.class);
            String observation;

            if (toolName.equals("final_answer")) {
                observation = (String) tools.get(toolName).execute(args);
                memory.addStep(new ActionStep(stepCounter++, actionJson, observation));
                return observation;
            } else {
                observation = (String) tools.get(toolName).execute(args);
                memory.addStep(new ActionStep(stepCounter++, actionJson, observation));
                messages.add(new Message("assistant", "Action:\n" + actionJson));
                messages.add(new Message("user", "Observation: " + observation));
            }
        }

        throw new RuntimeException("Max steps reached without final answer.");
    }

    /**
     * Extracts a JSON action from the model's response.
     *
     * @param response the model's response
     * @return the extracted JSON action as a string
     * @throws RuntimeException if no valid action JSON is found
     */
    private String extractAction(String response) {
        int actionStart = response.indexOf("{");
        int actionEnd = findMatchingBrace(response, actionStart);

        if (actionStart == -1 || actionEnd == -1) {
            throw new RuntimeException("No valid action JSON found in response: " + response);
        }

        return response.substring(actionStart, actionEnd + 1);
    }

    /**
     * Finds the matching closing brace for an opening brace.
     *
     * @param text the text to search in
     * @param openBraceIndex the index of the opening brace
     * @return the index of the matching closing brace, or -1 if not found
     */
    private int findMatchingBrace(String text, int openBraceIndex) {
        if (openBraceIndex == -1) return -1;

        int count = 1;
        for (int i = openBraceIndex + 1; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '{') count++;
            else if (c == '}') count--;

            if (count == 0) return i;
        }

        return -1;
    }

    /**
     * Gets the agent's memory.
     *
     * @return the agent's memory
     */
    public AgentMemory getMemory() {
        return memory;
    }
}
