package com.arep.lab;

import static com.arep.lab.HttpServer.get;
import static com.arep.lab.HttpServer.staticfiles;
import java.io.IOException;

/**
 * Example application demonstrating the web framework usage
 * This application serves REST services and static files
 */
public class MathService {

    public static void main(String[] args) throws IOException {
        // Define the location of static files
        staticfiles("/webroot/public");

        // Register REST endpoints using lambda functions

        // Simple hello endpoint with query parameter
        get("/App/hello", (req, resp) -> "Hello " + req.getValues("name"));

        // PI constant endpoint
        get("/App/pi", (req, resp) -> {
            return String.valueOf(Math.PI);
        });

        // Euler's number endpoint
        get("/App/euler", (req, resp) -> "Euler's number: " + Math.E);

        // Square calculation endpoint
        get("/App/square", (req, resp) -> {
            String numStr = req.getValues("num");
            if (numStr.isEmpty()) {
                return "Please provide a 'num' parameter";
            }
            try {
                double num = Double.parseDouble(numStr);
                double square = num * num;
                return "The square of " + num + " is " + square;
            } catch (NumberFormatException e) {
                return "Invalid number format";
            }
        });

        // Sine calculation endpoint
        get("/App/sin", (req, resp) -> {
            String angleStr = req.getValues("angle");
            if (angleStr.isEmpty()) {
                return "Please provide an 'angle' parameter";
            }
            try {
                double angle = Double.parseDouble(angleStr);
                double result = Math.sin(angle);
                return "sin(" + angle + ") = " + result;
            } catch (NumberFormatException e) {
                return "Invalid number format";
            }
        });

        // Cosine calculation endpoint
        get("/App/cos", (req, resp) -> {
            String angleStr = req.getValues("angle");
            if (angleStr.isEmpty()) {
                return "Please provide an 'angle' parameter";
            }
            try {
                double angle = Double.parseDouble(angleStr);
                double result = Math.cos(angle);
                return "cos(" + angle + ") = " + result;
            } catch (NumberFormatException e) {
                return "Invalid number format";
            }
        });

        // Info endpoint
        get("/App/info", (req, resp) -> {
            return "Math Service API - Available endpoints: /App/hello, /App/pi, /App/euler, /App/square, /App/sin, /App/cos";
        });

        System.out.println("Math Service Application Started");
        System.out.println("Try accessing:");
        System.out.println("  http://localhost:8080/App/hello?name=Pedro");
        System.out.println("  http://localhost:8080/App/pi");
        System.out.println("  http://localhost:8080/App/square?num=5");
        System.out.println("  http://localhost:8080/index.html");
        
        // Start the server
        HttpServer.startServer();
    }
}
