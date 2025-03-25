package com.agentfactory.tools;

import com.agentfactory.config.ApiConfig;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A tool that performs search operations using the Serper API.
 * Provides real search results from Google Search via the Serper service.
 */
public class SearchTool implements Tool {
    private final OkHttpClient client;
    private final Gson gson;
    private final String apiKey;
    private final String endpoint = "https://google.serper.dev/search";

    /**
     * Creates a new SearchTool with the specified API key.
     *
     * @param apiKey the Serper API key
     */
    public SearchTool(String apiKey) {
        this.apiKey = apiKey;
        this.client = new OkHttpClient();
        this.gson = new Gson();
    }

    /**
     * Creates a new SearchTool using the API key from the provided configuration.
     *
     * @param config the API configuration containing the Serper API key
     */
    public SearchTool(ApiConfig config) {
        this(config.getSerperApiKey());
    }

    /**
     * Creates a new SearchTool using the API key from environment variables.
     * This constructor is provided for backward compatibility and simple applications.
     * For production use, consider using the constructor that takes a configuration object.
     *
     * @throws IllegalStateException if the SERPER_API_KEY environment variable is not set
     */
    public SearchTool() {
        this(ApiConfig.fromEnvironment());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "search";
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if the "query" argument is missing
     * @throws RuntimeException if the search request fails
     */
    @Override
    public Object execute(Map<String, Object> args) {
        String query = (String) args.get("query");
        if (query == null) {
            throw new IllegalArgumentException("Search tool requires a 'query' argument.");
        }

        try {
            String results = performSearch(query);
            return results;
        } catch (Exception e) {
            throw new RuntimeException("Search failed: " + e.getMessage(), e);
        }
    }

    /**
     * Performs a search using the Serper API.
     *
     * @param query the search query
     * @return the search results as a formatted string
     * @throws IOException if the API request fails
     */
    private String performSearch(String query) throws IOException {
        JsonObject requestJson = new JsonObject();
        requestJson.addProperty("q", query);

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(requestJson.toString(), mediaType);

        Request request = new Request.Builder()
                .url(endpoint)
                .post(body)
                .addHeader("X-API-KEY", apiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Serper API request failed: " + response.code() + " " + response.message());
            }

            String responseBody = response.body().string();
            return formatResults(responseBody);
        }
    }

    /**
     * Formats the raw API response into a readable string.
     *
     * @param jsonResponse the raw JSON response from the API
     * @return a formatted string with search results
     */
    private String formatResults(String jsonResponse) {
        // Parse the JSON response and extract relevant information
        JsonObject responseObj = gson.fromJson(jsonResponse, JsonObject.class);

        StringBuilder formattedResults = new StringBuilder();
        formattedResults.append("Search results:\n\n");

        // Add organic results if available
        if (responseObj.has("organic")) {
            responseObj.getAsJsonArray("organic").forEach(result -> {
                JsonObject resultObj = result.getAsJsonObject();
                formattedResults
                        .append("- Title: ")
                        .append(resultObj.get("title").getAsString())
                        .append("\n");
                formattedResults
                        .append("  Link: ")
                        .append(resultObj.get("link").getAsString())
                        .append("\n");
                if (resultObj.has("snippet")) {
                    formattedResults
                            .append("  Snippet: ")
                            .append(resultObj.get("snippet").getAsString())
                            .append("\n");
                }
                formattedResults.append("\n");
            });
        }

        // Add answer box if available
        if (responseObj.has("answerBox")) {
            JsonObject answerBox = responseObj.getAsJsonObject("answerBox");
            formattedResults.append("Answer Box:\n");
            if (answerBox.has("title")) {
                formattedResults
                        .append("Title: ")
                        .append(answerBox.get("title").getAsString())
                        .append("\n");
            }
            if (answerBox.has("answer")) {
                formattedResults
                        .append("Answer: ")
                        .append(answerBox.get("answer").getAsString())
                        .append("\n");
            }
            if (answerBox.has("snippet")) {
                formattedResults
                        .append("Snippet: ")
                        .append(answerBox.get("snippet").getAsString())
                        .append("\n");
            }
            formattedResults.append("\n");
        }

        return formattedResults.toString();
    }
}
