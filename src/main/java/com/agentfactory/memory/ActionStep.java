package com.agentfactory.memory;

/**
 * Represents an action taken by an agent and the resulting observation.
 * Actions typically correspond to tool calls made by the agent.
 */
public class ActionStep extends MemoryStep {
    private String action;
    private String observation;

    /**
     * Creates a new action step with the given step number, action, and observation.
     *
     * @param stepNumber the sequential number of this step
     * @param action the action taken by the agent (typically a JSON string for tool calls)
     * @param observation the result of the action
     */
    public ActionStep(int stepNumber, String action, String observation) {
        super(stepNumber);
        this.action = action;
        this.observation = observation;
    }

    /**
     * Gets the action performed by the agent.
     *
     * @return the action as a string (typically JSON for tool calls)
     */
    public String getAction() {
        return action;
    }

    /**
     * Gets the observation resulting from the action.
     *
     * @return the observation as a string
     */
    public String getObservation() {
        return observation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("Step %d:\nAction: %s\nObservation: %s", stepNumber, action, observation);
    }
}
