package com.agentfactory.memory;

public abstract class MemoryStep {
    protected int stepNumber;

    public MemoryStep(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public abstract String toString();
}
