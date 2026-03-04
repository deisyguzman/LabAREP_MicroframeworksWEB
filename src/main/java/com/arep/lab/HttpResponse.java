package com.arep.lab;

/**
 * Represents an HTTP response object
 */
public class HttpResponse {
    private StringBuilder body;
    private int statusCode;
    private String contentType;

    public HttpResponse() {
        this.body = new StringBuilder();
        this.statusCode = 200;
        this.contentType = "text/html";
    }

    /**
     * Set the response body
     * @param content The content to set as response body
     */
    public void setBody(String content) {
        this.body = new StringBuilder(content);
    }

    /**
     * Get the response body
     * @return The response body as string
     */
    public String getBody() {
        return body.toString();
    }

    /**
     * Set the HTTP status code
     * @param code The status code (e.g., 200, 404, 500)
     */
    public void setStatusCode(int code) {
        this.statusCode = code;
    }

    /**
     * Get the HTTP status code
     * @return The status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Set the content type header
     * @param type The content type (e.g., "text/html", "application/json")
     */
    public void setContentType(String type) {
        this.contentType = type;
    }

    /**
     * Get the content type
     * @return The content type
     */
    public String getContentType() {
        return contentType;
    }
}
