package com.agentfactory.memory;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AgentMemoryTest {

    private AgentMemory memory;

    @BeforeEach
    public void setUp() {
        memory = new AgentMemory();
    }

    @Test
    public void newMemory_ShouldBeEmpty() {
        List<MemoryStep> steps = memory.getSteps();
        assertThat(steps).isEmpty();
    }

    @Test
    public void addStep_ShouldStoreStep() {
        ActionStep step = new ActionStep(1, "test action", "test observation");
        memory.addStep(step);

        List<MemoryStep> steps = memory.getSteps();
        assertThat(steps).hasSize(1);
        assertThat(steps.get(0)).isSameAs(step);
    }

    @Test
    public void addMultipleSteps_ShouldStoreAllSteps() {
        ActionStep step1 = new ActionStep(1, "action 1", "observation 1");
        ActionStep step2 = new ActionStep(2, "action 2", "observation 2");
        ActionStep step3 = new ActionStep(3, "action 3", "observation 3");

        memory.addStep(step1);
        memory.addStep(step2);
        memory.addStep(step3);

        List<MemoryStep> steps = memory.getSteps();
        assertThat(steps).hasSize(3);
        assertThat(steps).containsExactly(step1, step2, step3);
    }

    @Test
    public void getSteps_ShouldReturnDefensiveCopy() {
        ActionStep step1 = new ActionStep(1, "action 1", "observation 1");
        ActionStep step2 = new ActionStep(2, "action 2", "observation 2");

        memory.addStep(step1);
        memory.addStep(step2);

        List<MemoryStep> steps = memory.getSteps();
        assertThat(steps).hasSize(2);

        // Try to modify the returned list
        steps.clear();

        // Verify the actual memory is unchanged
        List<MemoryStep> stepsAfterModification = memory.getSteps();
        assertThat(stepsAfterModification).hasSize(2);
    }

    @Test
    public void toString_ShouldIncludeAllSteps() {
        ActionStep step1 = new ActionStep(1, "action 1", "observation 1");
        ActionStep step2 = new ActionStep(2, "action 2", "observation 2");

        memory.addStep(step1);
        memory.addStep(step2);

        String result = memory.toString();
        
        assertThat(result).contains("Step 1");
        assertThat(result).contains("action 1");
        assertThat(result).contains("observation 1");
        assertThat(result).contains("Step 2");
        assertThat(result).contains("action 2");
        assertThat(result).contains("observation 2");
    }
}