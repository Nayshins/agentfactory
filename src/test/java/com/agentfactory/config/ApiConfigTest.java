package com.agentfactory.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class ApiConfigTest {

    private static final String TEST_OPENAI_KEY = "test-openai-key";
    private static final String TEST_SERPER_KEY = "test-serper-key";

    @Test
    public void constructor_ShouldSetApiKeys() {
        ApiConfig config = new ApiConfig(TEST_OPENAI_KEY, TEST_SERPER_KEY);
        
        assertThat(config.getOpenAiApiKey()).isEqualTo(TEST_OPENAI_KEY);
        assertThat(config.getSerperApiKey()).isEqualTo(TEST_SERPER_KEY);
    }
}