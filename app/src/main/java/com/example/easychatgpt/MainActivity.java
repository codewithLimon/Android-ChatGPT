package com.example.easychatgpt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity{
    RecyclerView recyclerView;
    TextView welcomeTextView,limitation,limitationPoints,example,examplePoints;
    EditText messageEditText;
    TextView owner;
    ImageButton sendButton;
    RelativeLayout bottomLayout;
//    ImageButton stopButton;
    List<Message> messageList;
    MessageAdapter messageAdapter;
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();
    String apiKey="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";
    private Api api;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove the title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove the action bar
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        messageList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        welcomeTextView = findViewById(R.id.welcome_text);
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_btn);
        limitation=findViewById(R.id.limitaion);
        limitationPoints=findViewById(R.id.limitaionPoints);
        example=findViewById(R.id.example);
        examplePoints=findViewById(R.id.examplePoints);
        bottomLayout=findViewById(R.id.bottom_layout);

        //setup recycler view
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);
        api=new Api();

        sendButton.setOnClickListener((v)->{
            String question = messageEditText.getText().toString().trim();
            if(question.trim().length()>=1){
                addToChat(question,Message.SENT_BY_ME);
                addToChat("Typing....",Message.SENT_BY_BOT);
                messageEditText.setText("");
                welcomeTextView.setVisibility(View.GONE);
                example.setVisibility(View.GONE);
                examplePoints.setVisibility(View.GONE);
                limitation.setVisibility(View.GONE);
                limitationPoints.setVisibility(View.GONE);
//            owner.setVisibility(View.GONE);
                api.api_call(question, new Api.ApiCallback() {
                    @Override
                    public void onResponse(String result) {
                        if(result.trim().startsWith(",")){
                            result.substring(0);
                        }

                        addResponse(result.trim());
                    }
                });
            }else {
                Toast.makeText(this,"Please write a prompt",Toast.LENGTH_LONG).show();
            }
        });




    }




    void addToChat(String message,String sentBy){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new Message(message,sentBy));
                messageAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
            }
        });
    }

    void addResponse(String response){
        messageList.remove(messageList.size()-1);
        addToChat(response,Message.SENT_BY_BOT);
    }


}




















