package com.arep.lab;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an HTTP request with query parameters
 */
public class HttpRequest {
    private String path;
    private Map<String, String> queryParams;
    private String method;

    public HttpRequest(String path, String query, String method) {
        this.path = path;
        this.method = method;
        this.queryParams = new HashMap<>();
        parseQueryParams(query);
    }

    /**
     * Parse query string and extract parameters
     * @param query The query string (e.g., "name=Pedro&age=25")
     */
    private void parseQueryParams(String query) {
        if (query != null && !query.isEmpty()) {
            String[] params = query.split("&");
            for (String param : params) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2) {
                    queryParams.put(keyValue[0], keyValue[1]);
                } else if (keyValue.length == 1) {
                    queryParams.put(keyValue[0], "");
                }
            }
        }
    }

    /**
     * Get a query parameter value by name
     * @param varName The parameter name
     * @return The parameter value or empty string if not found
     */
    public String getValues(String varName) {
        return queryParams.getOrDefault(varName, "");
    }

    /**
     * Get the request path
     * @return The path
     */
    public String getPath() {
        return path;
    }

    /**
     * Get the HTTP method
     * @return The method (GET, POST, etc.)
     */
    public String getMethod() {
        return method;
    }

    /**
     * Get all query parameters
     * @return Map of query parameters
     */
    public Map<String, String> getQueryParams() {
        return queryParams;
    }
}
