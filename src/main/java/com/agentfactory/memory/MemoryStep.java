package com.agentfactory.memory;

/**
 * Abstract base class for steps stored in an agent's memory.
 * Each memory step has a step number to track the sequence of actions and thoughts.
 */
public abstract class MemoryStep {
    protected int stepNumber;

    /**
     * Creates a new memory step with the given step number.
     *
     * @param stepNumber the sequential number of this step
     */
    public MemoryStep(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    /**
     * Gets the step number.
     *
     * @return the sequential number of this step
     */
    public int getStepNumber() {
        return stepNumber;
    }

    /**
     * Returns a string representation of this memory step.
     *
     * @return a string representation of the step
     */
    public abstract String toString();
}
