package com.example.easychatgpt;

import okhttp3.*;
import org.json.*;

import java.io.IOException;

public class OpenAIHelper {

    private static final String API_URL = "https://api.openai.com/v1/completions";

    public static String callOpenAPI(String prompt, String apiKey) throws IOException, JSONException {

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"prompt\": \"" + prompt + "\",\"max_tokens\": 50}");
        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            JSONObject jsonObject = new JSONObject(response.body().string());
            return jsonObject.getJSONArray("choices").getJSONObject(0).getString("text");
        } else {
            int statusCode = response.code();
            return ""+statusCode+"  \n"+response.body().toString();
        }
    }
}
