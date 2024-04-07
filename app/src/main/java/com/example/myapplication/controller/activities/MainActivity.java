package com.example.myapplication.controller.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.dto.request.Team;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creategroup);
        LinearLayout linearLayout = findViewById(R.id.footerMainIcon_accountFeature);
        requestQueue = Volley.newRequestQueue(this);
        postTeam();


        // Thiết lập sự kiện lắng nghe cho LinearLayout
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent để chuyển từ MainActivity sang Activity mới
                Intent intent = new Intent(MainActivity.this, ChartMoneyManageActivity.class);
                startActivity(intent); // Bắt đầu Activity mới
            }
        });

    }
    public  void postTeam(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://awesome-debt-production.up.railway.app/team",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public byte[] getBody() {
                // Create team object
                List<Integer> users = new ArrayList<>();
                users.add(1);
                users.add(2);
                Team team = new Team("ngu", users);

                // Serialize team object to JSON
                Gson gson = new Gson();
                String json = gson.toJson(team);

                // Send JSON data in POST request body
                return json.getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Add authorization token to headers
                headers.put("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoaCIsInJvbGUiOlsiYWRtaW4iXSwiaWF0IjoxNzEyNTA2Nzc5LCJleHAiOjE3MTI1MTAzNzl9.wNx8a3pBq8roZCs0p2AneVDEYBsvoL4aHjIhRHTkUQs");
                return headers;
            }
        };

// Add the request to the RequestQueue
        requestQueue.add(stringRequest);


    }

}