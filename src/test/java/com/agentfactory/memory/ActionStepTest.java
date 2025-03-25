package com.agentfactory.memory;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class ActionStepTest {

    @Test
    public void constructor_ShouldSetProperties() {
        int stepNumber = 42;
        String action = "test action";
        String observation = "test observation";

        ActionStep step = new ActionStep(stepNumber, action, observation);

        assertThat(step.getStepNumber()).isEqualTo(stepNumber);
        assertThat(step.getAction()).isEqualTo(action);
        assertThat(step.getObservation()).isEqualTo(observation);
    }

    @Test
    public void getAction_ShouldReturnAction() {
        String action = "complex action";
        ActionStep step = new ActionStep(1, action, "observation");

        assertThat(step.getAction()).isEqualTo(action);
    }

    @Test
    public void getObservation_ShouldReturnObservation() {
        String observation = "detailed observation";
        ActionStep step = new ActionStep(1, "action", observation);

        assertThat(step.getObservation()).isEqualTo(observation);
    }

    @Test
    public void toString_ShouldIncludeAllProperties() {
        int stepNumber = 7;
        String action = "search action";
        String observation = "search results";

        ActionStep step = new ActionStep(stepNumber, action, observation);
        String result = step.toString();

        assertThat(result).contains(String.valueOf(stepNumber));
        assertThat(result).contains(action);
        assertThat(result).contains(observation);
    }
}