package com.agentfactory.tools;

import java.util.Map;

public class SearchTool implements Tool {
    @Override
    public String getName() {
        return "search";
    }

    @Override
    public Object execute(Map<String, Object> args) {
        String query = (String) args.get("query");
        if (query == null) {
            throw new IllegalArgumentException("Search tool requires a 'query' argument.");
        }
        // Simulate a search (replace with real API call if desired)
        return "Search results for '" + query + "': [simulated results]";
    }
}
