package com.agentfactory.models;

import com.agentfactory.config.ApiConfig;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import okhttp3.*;

/**
 * Implementation of AIModel that uses the OpenAI API to generate responses.
 * Supports chat completion API for models like GPT-4.
 */
public class OpenAIModel implements AIModel {
    private final String apiKey;
    private final String endpoint = "https://api.openai.com/v1/chat/completions";
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    private final String model;

    /**
     * Creates a new OpenAI model client with the specified API key and model name.
     *
     * @param apiKey the OpenAI API key
     * @param model the model name to use (e.g., "gpt-4")
     */
    public OpenAIModel(String apiKey, String model) {
        this.apiKey = apiKey;
        this.model = model;
    }

    /**
     * Creates a new OpenAI model client with the API key from configuration and specified model name.
     *
     * @param config the API configuration containing the OpenAI API key
     * @param model the model name to use (e.g., "gpt-4")
     */
    public OpenAIModel(ApiConfig config, String model) {
        this(config.getOpenAiApiKey(), model);
    }

    /**
     * Creates a new OpenAI model client with the specified API key and default model (gpt-4).
     *
     * @param apiKey the OpenAI API key
     */
    public OpenAIModel(String apiKey) {
        this(apiKey, "gpt-4");
    }

    /**
     * Creates a new OpenAI model client with the API key from configuration and default model (gpt-4).
     *
     * @param config the API configuration containing the OpenAI API key
     */
    public OpenAIModel(ApiConfig config) {
        this(config.getOpenAiApiKey(), "gpt-4");
    }

    /**
     * Creates a new OpenAI model client using API key from environment variables and default model (gpt-4).
     * For production use, consider using the constructor that takes a configuration object.
     *
     * @throws IllegalStateException if the OPENAI_API_KEY environment variable is not set
     */
    public static OpenAIModel fromEnvironment() {
        return new OpenAIModel(ApiConfig.fromEnvironment());
    }

    /**
     * {@inheritDoc}
     *
     * @throws RuntimeException if the API request fails
     */
    @Override
    public String generateResponse(List<Message> messages) {
        RequestBody requestBody = new RequestBody(messages, model);
        String jsonBody = gson.toJson(requestBody);

        Request request = new Request.Builder()
                .url(endpoint)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .post(okhttp3.RequestBody.create(jsonBody, MediaType.parse("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("API request failed: " + response.code() + " " + response.message());
            }

            ResponseBody responseBody = gson.fromJson(response.body().string(), ResponseBody.class);
            return responseBody.choices.get(0).message.getContent();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate response: " + e.getMessage(), e);
        }
    }

    /**
     * Inner class representing the request body for the OpenAI API.
     */
    private static class RequestBody {
        private final List<Message> messages;
        private final String model;

        RequestBody(List<Message> messages, String model) {
            this.messages = messages;
            this.model = model;
        }
    }

    /**
     * Inner class representing the response body from the OpenAI API.
     */
    private static class ResponseBody {
        private List<Choice> choices;

        static class Choice {
            private Message message;
        }
    }
}
