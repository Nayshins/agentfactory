package com.agentfactory.memory;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores and manages the memory of an agent, consisting of a sequence of steps
 * including actions, observations, and thoughts.
 */
public class AgentMemory {
    private List<MemoryStep> steps;

    /**
     * Creates a new empty agent memory.
     */
    public AgentMemory() {
        this.steps = new ArrayList<>();
    }

    /**
     * Adds a step to the agent's memory.
     *
     * @param step the memory step to add
     */
    public void addStep(MemoryStep step) {
        steps.add(step);
    }

    /**
     * Gets all steps in the agent's memory.
     *
     * @return a defensive copy of the list of memory steps
     */
    public List<MemoryStep> getSteps() {
        return new ArrayList<>(steps);
    }

    /**
     * Returns a string representation of the agent's memory.
     *
     * @return a string containing all memory steps
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (MemoryStep step : steps) {
            sb.append(step.toString()).append("\n");
        }
        return sb.toString();
    }
}
