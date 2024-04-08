package com.example.myapplication.controller.activities;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.dto.ApiResponse;
import com.example.myapplication.dto.request.Team;
import com.example.myapplication.dto.response.CostResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateGroupActivity extends Activity {
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private EditText inputAddPeople_createGroup;
    private EditText inputGroupName_createGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creategroup);
        ImageView linearLayout = findViewById(R.id.save_createGroup);
        getSuggestUser();
        // Thiết lập sự kiện lắng nghe cho LinearLayout
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRequestQueue = Volley.newRequestQueue(getApplicationContext());
                postTeam();
                // Tạo Intent để chuyển từ MainActivity sang Activity mới
                Intent intent = new Intent(CreateGroupActivity.this, gggg.class);
                startActivity(intent); // Bắt đầu Activity mới
            }
        });
    }

    public void getSuggestUser(){

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
                List<Integer> users = new ArrayList<>();
                inputAddPeople_createGroup = findViewById(R.id.inputAddPeople_createGroup);
                String peopleID = String.valueOf(inputAddPeople_createGroup.getText());
                String[] peopleIDArray = peopleID.split(",");
                for (String id : peopleIDArray){
                    users.add(Integer.parseInt(id.trim()));
                }

                inputGroupName_createGroup = findViewById(R.id.inputGroupName_createGroup);
                String nameTeam = String.valueOf(inputGroupName_createGroup.getText());


                Team team = new Team(nameTeam, users);

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
                headers.put("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuaGkiLCJyb2xlIjpbXSwiaWF0IjoxNzEyNTcyMDQ2LCJleHAiOjE3MTI1NzU2NDZ9.RSaQDA89C-83jNCdfKA0rGkc2H6BSCA-GOl42zFWMYo");
                return headers;
            }
        };

        // Add the request to the RequestQueue
        mRequestQueue.add(stringRequest);


    }

}
