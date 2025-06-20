package utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class AIPayloadClient {

    private static final String API_KEY = System.getenv("OPENAI_API_KEY");

    public static JSONObject getPayload(String endpoint, String testType) {
        try {
            URL url = new URL("https://api.openai.com/v1/chat/completions");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String prompt = String.format("Generate a %s JSON payload for this API endpoint: %s. Only return the raw JSON object.", testType, endpoint);

            JSONObject message1 = new JSONObject().put("role", "system")
                                                  .put("content", "You are a helpful API testing assistant.");
            JSONObject message2 = new JSONObject().put("role", "user")
                                                  .put("content", prompt);

            JSONArray messages = new JSONArray().put(message1).put(message2);

            JSONObject requestBody = new JSONObject()
                .put("model", "gpt-4")
                .put("messages", messages)
                .put("temperature", 0.3);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(requestBody.toString().getBytes());
                os.flush();
            }

            Scanner scanner = new Scanner(conn.getInputStream());
            String response = scanner.useDelimiter("\A").next();
            scanner.close();

            JSONObject responseJson = new JSONObject(response);
            String payloadText = responseJson.getJSONArray("choices")
                                             .getJSONObject(0)
                                             .getJSONObject("message")
                                             .getString("content");

            return new JSONObject(payloadText);

        } catch (Exception e) {
            throw new RuntimeException("Failed to get payload from OpenAI: " + e.getMessage(), e);
        }
    }
}