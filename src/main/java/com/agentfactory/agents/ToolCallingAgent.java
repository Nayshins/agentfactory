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

public class ToolCallingAgent {
    private final AIModel model;
    private final AgentMemory memory;
    private final Map<String, Tool> tools;
    private final String systemPrompt;
    private final Gson gson = new Gson();
    private int stepCounter = 0;
    private static final int MAX_STEPS = 10; // Prevent infinite loops

    public ToolCallingAgent(AIModel model, List<Tool> tools) {
        this.model = model;
        this.memory = new AgentMemory();
        this.tools = new HashMap<>();
        for (Tool tool : tools) {
            this.tools.put(tool.getName(), tool);
        }
        this.systemPrompt = PromptLoader.loadPrompt("toolcalling_agent.yaml");
    }

    public ToolCallingAgent(AIModel model, String systemPrompt, List<Tool> tools) {
        this.model = model;
        this.memory = new AgentMemory();
        this.tools = new HashMap<>();
        for (Tool tool : tools) {
            this.tools.put(tool.getName(), tool);
        }
        this.systemPrompt = systemPrompt;
    }

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
                return observation; // Task completed
            } else {
                observation = (String) tools.get(toolName).execute(args);
                memory.addStep(new ActionStep(stepCounter++, actionJson, observation));
                messages.add(new Message("assistant", "Action:\n" + actionJson));
                messages.add(new Message("user", "Observation: " + observation));
            }
        }

        throw new RuntimeException("Max steps reached without final answer.");
    }

    private String extractAction(String response) {
        // Look for an action block in the response
        int actionStart = response.indexOf("{");
        int actionEnd = findMatchingBrace(response, actionStart);

        if (actionStart == -1 || actionEnd == -1) {
            throw new RuntimeException("No valid action JSON found in response: " + response);
        }

        return response.substring(actionStart, actionEnd + 1);
    }

    private int findMatchingBrace(String text, int openBraceIndex) {
        if (openBraceIndex == -1) return -1;

        int count = 1;
        for (int i = openBraceIndex + 1; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '{') count++;
            else if (c == '}') count--;

            if (count == 0) return i;
        }

        return -1; // No matching closing brace
    }

    public AgentMemory getMemory() {
        return memory;
    }
}
