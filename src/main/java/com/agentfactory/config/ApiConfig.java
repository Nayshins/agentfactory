package com.agentfactory.config;

/**
 * Configuration class for API keys and other settings.
 * Using a configuration object is preferred over hardcoded values or environment variables.
 */
public class ApiConfig {
    private final String openAiApiKey;
    private final String serperApiKey;

    /**
     * Creates a new API configuration with the specified keys.
     *
     * @param openAiApiKey the OpenAI API key
     * @param serperApiKey the Serper API key for web search
     */
    public ApiConfig(String openAiApiKey, String serperApiKey) {
        this.openAiApiKey = openAiApiKey;
        this.serperApiKey = serperApiKey;
    }

    /**
     * Gets the OpenAI API key.
     *
     * @return the OpenAI API key
     */
    public String getOpenAiApiKey() {
        return openAiApiKey;
    }

    /**
     * Gets the Serper API key.
     *
     * @return the Serper API key
     */
    public String getSerperApiKey() {
        return serperApiKey;
    }

    /**
     * Creates a new configuration from environment variables.
     * This method is provided as a convenience for simple applications.
     * For production use, consider using a proper configuration management system.
     *
     * @return a new ApiConfig instance with keys from environment variables
     * @throws IllegalStateException if required environment variables are not set
     */
    public static ApiConfig fromEnvironment() {
        String openAiKey = System.getenv("OPENAI_API_KEY");
        String serperKey = System.getenv("SERPER_API_KEY");
        
        if (openAiKey == null || openAiKey.isEmpty()) {
            throw new IllegalStateException("OPENAI_API_KEY environment variable is not set");
        }
        
        if (serperKey == null || serperKey.isEmpty()) {
            throw new IllegalStateException("SERPER_API_KEY environment variable is not set");
        }
        
        return new ApiConfig(openAiKey, serperKey);
    }
}