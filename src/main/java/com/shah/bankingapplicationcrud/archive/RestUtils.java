package com.shah.bankingapplicationcrud.archive;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;

public class RestUtils {


    public record Result(int statusCode, String body, Map<String, String> headers) {
    }

    // Builds the HttpClient with SSL based on provided certificate information
    public static HttpClient buildHttpClient(String certificateFile, String password, String keyStoreType) {
        try {
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            try (FileInputStream fis = new FileInputStream(certificateFile)) {
                keyStore.load(fis, password.toCharArray());
            }

            SSLContext sslContext = SSLContextBuilder
                    .create()
                    .loadKeyMaterial(keyStore, password.toCharArray())
                    .build();

            return HttpClients.custom()
                    .setSSLContext(sslContext)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to build HTTP client with provided certificate", e);
        }
    }

    // Executes an HTTP request and returns the result
    public static Result execute(HttpClient httpClient, HttpUriRequest request) {
        try {
            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            String body = EntityUtils.toString(response.getEntity());
            Map<String, String> headers = new HashMap<>();

            // Populate headers map using a traditional for-loop
            for (org.apache.http.Header header : response.getAllHeaders()) {
                headers.put(header.getName(), header.getValue());
            }

            return new Result(statusCode, body, headers);
        } catch (IOException e) {
            throw new RuntimeException("Failed to execute HTTP request", e);
        }
    }


    // Random string generator (you can add this for your boundary creation, similar to what was used in Kotlin)
    public static String randomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            randomString.append(characters.charAt(index));
        }
        return randomString.toString();
    }
}
