package com.shah.bankingapplicationcrud.archive;


import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Restt {

    private final HttpClient httpClient = RestUtils.buildHttpClient(
            System.getProperty("isga.certificate.file"),
            System.getProperty("isga.certificate.password"),
            System.getProperty("isga.certificate.type")
    );

    private final String isgaEndpoint = System.getProperty("isga.endpoint");
    private final String isgaScope = System.getProperty("isga.scope");
    private final String ngaEndpoint = System.getProperty("nga.endpoint") + System.getProperty("npa.api.path")
            .replace("(version)", System.getProperty("nga.version"))
            .replace("(tenant)", System.getProperty("nga.tenant"));

    private String isgaToken = "";
    private LocalDateTime isgaTokenExpiration = LocalDateTime.now().minusYears(1);
    private long isgaTokenValidity = Long.parseLong(System.getProperty("isga.token.validity"));

    private String getToken() throws RestException, IOException, JSONException {
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(isgaTokenExpiration)) {
            log.debug("Refreshing ISGA token at: {}", LocalDateTime.now());

            HttpPost post = new HttpPost(isgaEndpoint);
            post.addHeader("Accept", "application/json");
            post.addHeader("Content-Type", "application/x-www-form-urlencoded");

            List<BasicNameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("grant_type", "client_credentials"));
            params.add(new BasicNameValuePair("scope", isgaScope));

            post.setEntity(new UrlEncodedFormEntity(params));

            // Execute HTTP request
            RestUtils.Result result = RestUtils.execute(httpClient, post);

            if (result.getStatusCode() != HttpStatus.OK.value()) {
                log.error(result.getBody());
                throw new RestException(String.format("Error in ISGA token renewal: %d", result.getStatusCode()));
            }

            JSONObject body = new JSONObject(result.getBody());
            isgaToken = body.getString("access_token");
            isgaTokenValidity = body.getInt("expires_in");
            isgaTokenExpiration = now.plusSeconds(isgaTokenValidity);
        }
        return isgaToken;
    }

    public boolean sendNgaDocument(Path file, Path meta) throws IOException {
        String token;
        try {
            token = getToken();
        } catch (Exception e) {
            log.error("Failed to renew ISGA token", e);
            return true;
        }

        HttpPost post = new HttpPost(ngaEndpoint);
        String boundary = "---WebKitFormBoundary" + RestUtils.randomString(15);

        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create()
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                .setBoundary(boundary)
                .addBinaryBody("meta", Files.readAllBytes(meta), ContentType.TEXT_XML, "metadata.xml")
                .addBinaryBody("content", Files.readAllBytes(file), ContentType.create("application/pdf"), file.getFileName().toString());

        post.addHeader("X-NGA-Size", String.valueOf(Files.size(file)));
        post.addHeader("Authorization", "Bearer " + token);
        post.addHeader("Content-Type", "multipart/form-data; boundary=" + boundary);
        post.setEntity(entityBuilder.build());

        // Execute HTTP request
        RestUtils.Result result = RestUtils.execute(httpClient, post);

        log.debug("{} - {}", result.getStatusCode(), result.getHeaders());
        if (result.getStatusCode() != HttpStatus.CREATED.value()) {
            log.error(result.getBody());
            return false;
        }

        log.debug("Location: {}", result.getHeaders().get("X-NGA-Location"));
        return true;
    }
}
