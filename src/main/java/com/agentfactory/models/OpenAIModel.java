package com.agentfactory.models;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import okhttp3.*;

public class OpenAIModel implements AIModel {
    private final String apiKey;
    private final String endpoint = "https://api.openai.com/v1/chat/completions";
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    private final String model;

    public OpenAIModel(String apiKey, String model) {
        this.apiKey = apiKey;
        this.model = model;
    }

    public OpenAIModel(String apiKey) {
        this(apiKey, "gpt-4");
    }

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

    private static class RequestBody {
        private final List<Message> messages;
        private final String model;

        RequestBody(List<Message> messages, String model) {
            this.messages = messages;
            this.model = model;
        }
    }

    private static class ResponseBody {
        private List<Choice> choices;

        static class Choice {
            private Message message;
        }
    }
}
