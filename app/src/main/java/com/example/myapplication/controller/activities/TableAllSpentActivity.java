package com.example.myapplication.controller.activities;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.myapplication.dto.response.CostResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class TableAllSpentActivity extends Activity {
    private RequestQueue mRequestQueue;
    private EditText inputAssignment_TableAllSpent;
    private StringRequest mStringRequest;
    private String url = "http://192.168.114.61:8080/Cost/Team/1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tableallspent);

        getAllSpent();
        Button linearLayout1 = findViewById(R.id.buttonAddSpent_tableAllSpent);

        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TableAllSpentActivity.this, AddSpentActivity.class);
                startActivity(intent); // Bắt đầu Activity mới
            }
        });
    }
    public  void getAllSpent(){


        mRequestQueue = Volley.newRequestQueue(this);

        // String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Type responseType = new TypeToken<ApiResponse<CostResponse>>(){}.getType();
                ApiResponse<CostResponse> apiResponse = gson.fromJson(response, responseType);
                inputAssignment_TableAllSpent = findViewById(R.id.inputAssignment_TableAllSpent);
                inputAssignment_TableAllSpent.setText(apiResponse.getMessage());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.statusCode == 400) {
                    // Handle 400 error here
                    Toast.makeText(getApplicationContext(), "Access Denied: Token hết hạn", Toast.LENGTH_LONG).show();
                }

                Log.i(TAG, "Error :" + error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoaCIsInJvbGUiOlsiYWRtaW4iXSwiaWF0IjoxNzEyNDkwOTUwLCJleHAiOjE3MTI0OTQ1NTB9.Pn6XiKBPfV8bdbFL-SHRR6Mle-W0SKuRDMIOPMbH5t4");
                return headers;
            }
        };

        mRequestQueue.add(mStringRequest);
    }
}
