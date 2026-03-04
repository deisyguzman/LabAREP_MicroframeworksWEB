package com.arep.lab;

import java.net.*;
import java.io.*;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple web framework for REST services and static file management
 */
public class HttpServer {

    private static Map<String, WebMethod> endPoints = new HashMap<>();
    private static String staticFilesLocation = "/webroot/public";
    private static final int PORT = 8080;

    /**
     * Register a GET REST service with a lambda function
     * @param path The URL path for the service
     * @param wm The WebMethod (lambda function) to handle the request
     */
    public static void get(String path, WebMethod wm) {
        endPoints.put(path, wm);
    }

    /**
     * Define the folder where static files are located
     * @param location The folder path (e.g., "/webroot")
     */
    public static void staticfiles(String location) {
        staticFilesLocation = location;
    }

    /**
     * Start the HTTP server
     */
    public static void startServer() throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + PORT);
            System.exit(1);
        }

        boolean running = true;

        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Ready to receive requests...");
                clientSocket = serverSocket.accept();
                handleRequest(clientSocket);
            } catch (IOException e) {
                System.err.println("Error handling request: " + e.getMessage());
            } finally {
                if (clientSocket != null && !clientSocket.isClosed()) {
                    clientSocket.close();
                }
            }
        }
        serverSocket.close();
    }

    /**
     * Handle an individual client request
     */
    private static void handleRequest(Socket clientSocket) throws IOException {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));

        String inputLine;
        boolean isFirstLine = true;
        String reqPath = "";
        String reqQuery = "";
        String method = "";

        // Read HTTP request
        while ((inputLine = in.readLine()) != null) {
            System.out.println("Received: " + inputLine);

            if (isFirstLine) {
                String[] tokens = inputLine.split(" ");
                if (tokens.length >= 3) {
                    method = tokens[0];
                    String uriString = tokens[1];

                    try {
                        URI uri = new URI(uriString);
                        reqPath = uri.getPath();
                        reqQuery = uri.getQuery();
                    } catch (URISyntaxException e) {
                        System.err.println("Invalid URI: " + e.getMessage());
                        reqPath = "/";
                    }

                    System.out.println("Method: " + method);
                    System.out.println("Path: " + reqPath);
                    System.out.println("Query: " + reqQuery);
                }
                isFirstLine = false;
            }

            if (!in.ready()) {
                break;
            }
        }

        // Check if it's a REST endpoint
        if (endPoints.containsKey(reqPath)) {
            handleRestEndpoint(out, reqPath, reqQuery, method);
        } else {
            // Try to serve static file
            handleStaticFile(out, reqPath);
        }

        out.close();
        in.close();
    }

    /**
     * Handle REST endpoint request
     */
    private static void handleRestEndpoint(PrintWriter out, String path, String query, String method) {
        try {
            HttpRequest request = new HttpRequest(path, query, method);
            HttpResponse response = new HttpResponse();

            WebMethod endpoint = endPoints.get(path);
            String result = endpoint.execute(request, response);
            response.setBody(result);

            String httpResponse = buildHttpResponse(response);
            out.println(httpResponse);
        } catch (Exception e) {
            System.err.println("Error executing endpoint: " + e.getMessage());
            e.printStackTrace();
            sendErrorResponse(out, 500, "Internal Server Error");
        }
    }

    /**
     * Handle static file request
     */
    private static void handleStaticFile(PrintWriter out, String requestPath) {
        try {
            // If path is just "/", serve index.html
            if (requestPath.equals("/")) {
                requestPath = "/index.html";
            }

            // Get the file from classpath
            String filePath = staticFilesLocation + requestPath;
            InputStream inputStream = HttpServer.class.getResourceAsStream(filePath);

            if (inputStream != null) {
                // Read file content
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                reader.close();

                // Determine content type
                String contentType = getContentType(requestPath);

                // Send response
                String httpResponse = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: " + contentType + "\r\n"
                        + "\r\n"
                        + content.toString();
                out.println(httpResponse);
                System.out.println("Served static file: " + filePath);
            } else {
                sendErrorResponse(out, 404, "File Not Found");
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            sendErrorResponse(out, 500, "Internal Server Error");
        }
    }

    /**
     * Build HTTP response from HttpResponse object
     */
    private static String buildHttpResponse(HttpResponse response) {
        String statusText = getStatusText(response.getStatusCode());
        return "HTTP/1.1 " + response.getStatusCode() + " " + statusText + "\r\n"
                + "Content-Type: " + response.getContentType() + "\r\n"
                + "\r\n"
                + response.getBody();
    }

    /**
     * Send error response
     */
    private static void sendErrorResponse(PrintWriter out, int statusCode, String message) {
        String statusText = getStatusText(statusCode);
        String response = "HTTP/1.1 " + statusCode + " " + statusText + "\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>"
                + "<html>"
                + "<head><title>" + statusCode + " " + statusText + "</title></head>"
                + "<body>"
                + "<h1>" + statusCode + " " + statusText + "</h1>"
                + "<p>" + message + "</p>"
                + "</body>"
                + "</html>";
        out.println(response);
    }

    /**
     * Get HTTP status text from code
     */
    private static String getStatusText(int statusCode) {
        switch (statusCode) {
            case 200: return "OK";
            case 404: return "Not Found";
            case 500: return "Internal Server Error";
            default: return "Unknown";
        }
    }

    /**
     * Determine content type based on file extension
     */
    private static String getContentType(String path) {
        if (path.endsWith(".html")) return "text/html";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".json")) return "application/json";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
        if (path.endsWith(".gif")) return "image/gif";
        return "text/plain";
    }

    public static void main(String[] args) throws IOException {
        startServer();
    }
}