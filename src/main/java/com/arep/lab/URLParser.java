package com.arep.lab;
import java.net.*;
import java.io.*;
import java.net.URL;
import java.net.MalformedURLException;

public class URLParser {
    public static void main(String[] args) throws MalformedURLException {
        URL myurl = new URL("http://ldbn.is.escuelaing.edu.co:7865/arep/respuestas.txt?val=3&t=4#examenfinal");

        System.out.println("Protocol: " + myurl.getProtocol());
        System.out.println("Host: " + myurl.getHost());
        System.out.println("Port: " + myurl.getPort());
        System.out.println("Authority: " + myurl.getAuthority());
        System.out.println("Path: " + myurl.getPath());
        System.out.println("File: " + myurl.getFile());
        System.out.println("Query: " + myurl.getQuery());
        System.out.println("Ref: " + myurl.getRef());
            
    }
    
}
