package com.agentfactory.tools;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.agentfactory.config.ApiConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class SearchToolTest {

    private static final String TEST_API_KEY = "test_api_key";
    private static final String QUERY = "java programming";
    private static final String SEARCH_ENDPOINT = "https://google.serper.dev/search";

    @Mock
    private OkHttpClient mockClient;

    @Mock
    private Call mockCall;

    private SearchTool searchTool;

    @BeforeEach
    public void setUp() throws Exception {
        searchTool = createSearchToolWithMockClient(TEST_API_KEY);
    }

    @Test
    public void getName_ShouldReturnCorrectName() {
        assertThat(searchTool.getName()).isEqualTo("search");
    }

    @Test
    public void execute_WithValidQuery_ShouldReturnFormattedResults() throws IOException {
        Response successResponse = createSuccessResponse();
        setupMockClientResponse(successResponse);

        Map<String, Object> args = createQueryArgs();
        Object result = searchTool.execute(args);

        assertSearchResults(result);
    }

    @Test
    public void execute_WithoutQuery_ShouldThrowException() {
        Map<String, Object> emptyArgs = new HashMap<>();
        
        assertThatThrownBy(() -> searchTool.execute(emptyArgs))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("'query' argument");
    }

    @Test
    public void execute_WithFailedRequest_ShouldThrowException() throws IOException {
        Response errorResponse = createErrorResponse();
        setupMockClientResponse(errorResponse);

        Map<String, Object> args = createQueryArgs();

        assertThatThrownBy(() -> searchTool.execute(args))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Search failed");
    }

    @Test
    public void constructor_WithApiConfig_ShouldCreateValidTool() {
        ApiConfig config = new ApiConfig("test-openai-key", TEST_API_KEY);
        SearchTool configTool = new SearchTool(config);
        
        assertThat(configTool.getName()).isEqualTo("search");
    }

    private SearchTool createSearchToolWithMockClient(String apiKey) throws Exception {
        SearchTool tool = new SearchTool(apiKey);
        injectMockClient(tool);
        return tool;
    }

    private void injectMockClient(SearchTool tool) throws Exception {
        java.lang.reflect.Field clientField = SearchTool.class.getDeclaredField("client");
        clientField.setAccessible(true);
        clientField.set(tool, mockClient);
    }

    private Map<String, Object> createQueryArgs() {
        Map<String, Object> args = new HashMap<>();
        args.put("query", QUERY);
        return args;
    }

    private void setupMockClientResponse(Response response) throws IOException {
        when(mockClient.newCall(any())).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(response);
    }

    private Response createSuccessResponse() {
        String jsonResponse = createTestSearchResponse();
        ResponseBody responseBody = ResponseBody.create(jsonResponse, okhttp3.MediaType.parse("application/json"));
        
        return new Response.Builder()
                .request(new Request.Builder().url(SEARCH_ENDPOINT).build())
                .protocol(Protocol.HTTP_2)
                .code(200)
                .message("OK")
                .body(responseBody)
                .build();
    }

    private Response createErrorResponse() {
        return new Response.Builder()
                .request(new Request.Builder().url(SEARCH_ENDPOINT).build())
                .protocol(Protocol.HTTP_2)
                .code(401)
                .message("Unauthorized")
                .body(ResponseBody.create("Error", okhttp3.MediaType.parse("text/plain")))
                .build();
    }

    private void assertSearchResults(Object result) {
        assertThat(result).isInstanceOf(String.class);
        String resultStr = (String) result;
        assertThat(resultStr).contains("Search results:");
        assertThat(resultStr).contains("Title: Java Programming");
        assertThat(resultStr).contains("Link: https://example.com/java");
    }

    private String createTestSearchResponse() {
        JsonObject responseJson = new JsonObject();
        
        JsonArray organicArray = new JsonArray();
        organicArray.add(createOrganicResult("Java Programming", "https://example.com/java", 
                "Java is a popular programming language"));
        organicArray.add(createOrganicResult("Learn Java", "https://example.com/learn-java", 
                "Best resources to learn Java programming"));
        
        JsonObject answerBox = new JsonObject();
        answerBox.addProperty("title", "What is Java?");
        answerBox.addProperty("answer", "Java is a high-level, class-based, object-oriented programming language");
        
        responseJson.add("organic", organicArray);
        responseJson.add("answerBox", answerBox);

        return responseJson.toString();
    }
    
    private JsonObject createOrganicResult(String title, String link, String snippet) {
        JsonObject result = new JsonObject();
        result.addProperty("title", title);
        result.addProperty("link", link);
        result.addProperty("snippet", snippet);
        return result;
    }
}