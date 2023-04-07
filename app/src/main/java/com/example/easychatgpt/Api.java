package com.example.easychatgpt;

import android.os.AsyncTask;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Api extends MainActivity{

    private static final String OPENAI_API_KEY = "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";

    public void api_call(String userMessage, final ApiCallback callback) {
        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... params) {
                try {
                    OkHttpClient client = new OkHttpClient();

                    MediaType mediaType = MediaType.parse("application/json");
                    RequestBody body = RequestBody.create(
                            "{\"model\": \"text-davinci-003\"," +
                                    "\"prompt\": \"" + params[0] + "\"," +
                                    "\"temperature\": 0," +
                                    "\"max_tokens\": 4000}",
                            mediaType
                    );



                    Request request = new Request.Builder()
                            .url("https://api.openai.com/v1/completions")
                            .post(body)
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Authorization", "Bearer " + OPENAI_API_KEY)
                            .addHeader("Organization","org-bQbjH4hunKzPezsthKpsZE5p")
                            .build();

                    Response response = client.newCall(request).execute();
                    String responseBody = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseBody);
                    String chatBotMessage = jsonObject.getJSONArray("choices")
                            .getJSONObject(0)
                            .getString("text");
                    return chatBotMessage;
                } catch (Exception e) {
                    e.printStackTrace();
                    return "An error occurred while communicating with the OpenAI API.";
                }
            }

            @Override
            protected void onPostExecute(String result) {
                callback.onResponse(result);
            }
        }.execute(userMessage);
    }

    public interface ApiCallback {
        void onResponse(String result);
    }
}
