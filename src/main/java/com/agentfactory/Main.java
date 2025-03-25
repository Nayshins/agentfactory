package com.agentfactory;

import com.agentfactory.agents.ToolCallingAgent;
import com.agentfactory.models.AIModel;
import com.agentfactory.models.OpenAIModel;
import com.agentfactory.tools.FinalAnswerTool;
import com.agentfactory.tools.SearchTool;
import com.agentfactory.tools.Tool;
import java.util.Arrays;
import java.util.List;

/**
 * Example class demonstrating the use of AgentFactory.
 *
 * This class is not part of the library's public API and serves as a demo
 * of how to use AgentFactory components.
 */
public class Main {
    /**
     * Example showing how to configure and use a ToolCallingAgent.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Get OpenAI API key from environment
        String apiKey = System.getenv("OPENAI_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("Error: OPENAI_API_KEY environment variable is not set");
            System.exit(1);
        }

        // Example 1: Basic agent with tools
        runBasicAgent(apiKey);
    }

    /**
     * Example of creating and running a basic agent with tools.
     *
     * @param apiKey The OpenAI API key
     */
    private static void runBasicAgent(String apiKey) {
        // Initialize the model
        AIModel model = new OpenAIModel(apiKey, "gpt-4");

        // Create tools
        List<Tool> tools = Arrays.asList(new SearchTool(), new FinalAnswerTool());

        // Create agent
        ToolCallingAgent agent = new ToolCallingAgent(model, tools);

        // Run a task
        try {
            String task = "Search for 'Java programming' and summarize the results.";
            System.out.println("Executing task: " + task);

            String result = agent.run(task);

            System.out.println("\nFinal result: " + result);
            System.out.println("\nAgent memory:");
            System.out.println(agent.getMemory().toString());
        } catch (Exception e) {
            System.err.println("Error executing agent: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
