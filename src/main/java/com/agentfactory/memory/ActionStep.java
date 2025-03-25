package com.agentfactory.memory;

public class ActionStep extends MemoryStep {
    private String action; // JSON string for tool calls
    private String observation;

    public ActionStep(int stepNumber, String action, String observation) {
        super(stepNumber);
        this.action = action;
        this.observation = observation;
    }

    public String getAction() {
        return action;
    }

    public String getObservation() {
        return observation;
    }

    @Override
    public String toString() {
        return String.format("Step %d:\nAction: %s\nObservation: %s", stepNumber, action, observation);
    }
}
