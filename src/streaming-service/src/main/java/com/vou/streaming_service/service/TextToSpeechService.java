package com.vou.streaming_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class TextToSpeechService {

    private static final String API_URL = "https://text-to-speech-for-28-languages.p.rapidapi.com/";
    private static final String API_KEY = "b3a91d53b8mshc887a15175d8094p159d01jsn3123fe0ba580";
    private static final String API_HOST = "text-to-speech-for-28-languages.p.rapidapi.com";
    private static final String CONTENT_TYPE = "multipart/form-data; boundary=---011000010111000001101001";

    public String convertTextToSpeech(String question, String otp1, String otp2, String otp3) {
        String text = question
                + "<break time=\"0.5s\"/> A.<break time=\"0.2s\"/>"
                + otp1
                + "<break time=\"0.2s\"/> B.<break time=\"0.2s\"/>"
                + otp2
                + "<break time=\"0.2s\"/> C.<break time=\"0.2s\"/>"
                + otp3;

        String[] langs = {"Salli", "Ivy", "Joey", "Matthew", "Kimberly", "Kendra", "Justin", "Joanna"};
        Random random = new Random();
        String lang = langs[random.nextInt(langs.length)];

        CompletableFuture<String> futureRadioUrl = new CompletableFuture<>();

        convertTextToSpeechAsync(text, lang).thenAccept(response -> {
            if (response != null) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    System.out.println("Response of HTTP: " + response);
                    JsonNode jsonResponse = new ObjectMapper().readTree(response.getResponseBody());

                    if (jsonResponse.has("Error") && jsonResponse.get("Error").asInt() == 0) {
                        if (jsonResponse.has("URL")) {
                            String radioUrl = jsonResponse.get("URL").asText();
                            System.out.println("Audio URL: " + radioUrl);
                            futureRadioUrl.complete(radioUrl);
                        } else {
                            System.out.println("URL not found in the response");
                            futureRadioUrl.completeExceptionally(new RuntimeException("URL not found"));
                        }
                    } else {
                        System.out.println("Error in response: " + jsonResponse.get("Error").asText());
                        futureRadioUrl.completeExceptionally(new RuntimeException("Error in response"));
                    }
                } catch (Exception e) {
                    System.out.println("Failed to parse the response: " + e.getMessage());
                    futureRadioUrl.completeExceptionally(e);
                }
            } else {
                System.out.println("Failed to get a response");
                futureRadioUrl.completeExceptionally(new RuntimeException("Failed to get a response"));
            }
        }).exceptionally(ex -> {
            System.out.println("Failed to convert text to speech: " + ex.getMessage());
            futureRadioUrl.completeExceptionally(ex);
            return null;
        });

        try {
            return futureRadioUrl.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;  // Xử lý lỗi trong quá trình đợi kết quả
        }
    }

    public static CompletableFuture<Response> convertTextToSpeechAsync(String text, String lang) {
        AsyncHttpClient client = new DefaultAsyncHttpClient();

        String bodyContent = "-----011000010111000001101001\r\n" +
                "Content-Disposition: form-data; name=\"msg\"\r\n\r\n" +
                text + "\r\n" +
                "-----011000010111000001101001\r\n" +
                "Content-Disposition: form-data; name=\"lang\"\r\n\r\n" +
                lang + "\r\n" +
                "-----011000010111000001101001\r\n" +
                "Content-Disposition: form-data; name=\"source\"\r\n\r\n" +
                "ttsmp3\r\n" +
                "-----011000010111000001101001--\r\n\r\n";

        return client.prepare("POST", API_URL)
                .setHeader("x-rapidapi-key", API_KEY)
                .setHeader("x-rapidapi-host", API_HOST)
                .setHeader("Content-Type", CONTENT_TYPE)
                .setBody(bodyContent)
                .execute()
                .toCompletableFuture()
                .whenComplete((response, throwable) -> {
                    if (throwable != null) {
                        System.out.println("Error occurred: " + throwable.getMessage());
                    }
                    try {
                        client.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
