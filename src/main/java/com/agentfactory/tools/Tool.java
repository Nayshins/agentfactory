package com.agentfactory.tools;

import java.util.Map;

/**
 * Interface representing a tool that can be used by an agent.
 * Tools provide specific functionality that agents can invoke during execution.
 */
public interface Tool {
    /**
     * Returns the name of the tool.
     *
     * @return the tool's identifier name
     */
    String getName();

    /**
     * Executes the tool with the provided arguments.
     *
     * @param args a map of argument names to their values
     * @return the result of the tool execution
     */
    Object execute(Map<String, Object> args);
}
