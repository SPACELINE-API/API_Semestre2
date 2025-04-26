package org.sputnik.api;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Tradução {

    public static String Prompt(String modelo, String prompt) throws Exception {

        String endpoint = "http://localhost:11434/api/generate";

        String promptEscapado = prompt.replace("\n", "\\n").replace("\"", "\\\"");


        String json = "{"
                + "\"model\": \"" + modelo + "\","
                + "\"prompt\": \"" + promptEscapado + "\","
                + "\"stream\": false"
                + "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();


        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }
}