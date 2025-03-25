package com.agentfactory.memory;

import java.util.ArrayList;
import java.util.List;

public class AgentMemory {
    private List<MemoryStep> steps;

    public AgentMemory() {
        this.steps = new ArrayList<>();
    }

    public void addStep(MemoryStep step) {
        steps.add(step);
    }

    public List<MemoryStep> getSteps() {
        return new ArrayList<>(steps); // Return a copy to prevent external modification
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (MemoryStep step : steps) {
            sb.append(step.toString()).append("\n");
        }
        return sb.toString();
    }
}
