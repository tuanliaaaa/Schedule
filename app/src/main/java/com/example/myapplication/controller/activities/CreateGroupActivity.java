package com.example.myapplication.controller.activities;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.dto.ApiResponse;
import com.example.myapplication.dto.ErrorResponse;
import com.example.myapplication.dto.request.Team;
import com.example.myapplication.dto.response.CostResponse;
import com.example.myapplication.dto.response.TeamCreateResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateGroupActivity extends Activity {
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private Context context;
    private String domain;
    private String jsonPostCreateGroup;
    private EditText inputAddPeople_createGroup;
    private EditText inputGroupName_createGroup;
    private EditText inputBudget_createGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creategroup);
        try{
            domain= getResources().getString(R.string.domain);
            ImageView linearLayout = findViewById(R.id.save_createGroup);
            getSuggestUser();
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
            // Click to button save
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postTeam();
                }
            });
        }catch (Exception e){
            Log.e("Errors"," Lỗi Tổng ở CreateGroupActivity");
        }
    }

    public void getSuggestUser(){

    }

    public  void postTeam(){
        try{
            List<Integer> users = new ArrayList<>();
            inputAddPeople_createGroup = findViewById(R.id.inputAddPeople_createGroup);
            String peopleID = String.valueOf(inputAddPeople_createGroup.getText());
            String[] peopleIDArray = peopleID.split(",");
            for (String id : peopleIDArray){
                users.add(Integer.parseInt(id.trim()));
            }
            inputBudget_createGroup =findViewById(R.id.inputBudget_createGroup);
            inputGroupName_createGroup = findViewById(R.id.inputGroupName_createGroup);
            String nameTeam = String.valueOf(inputGroupName_createGroup.getText());
            int costExpected= Integer.parseInt(String.valueOf(inputBudget_createGroup.getText()));

            JSONObject jsonBody = new JSONObject();

            try {
                jsonBody.put("name", nameTeam);
                JSONArray usersArray = new JSONArray(users);
                jsonBody.put("user", usersArray);
                jsonBody.put("costExpected", costExpected);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, domain+"/team", jsonBody,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Xử lý phản hồi thành công
                                Log.i("success", "in onResponse");
                                Gson gson = new Gson();
                                try{
                                    Type responseType = new TypeToken<ApiResponse<TeamCreateResponse>>(){}.getType();
                                    ApiResponse<TeamCreateResponse> apiResponse = gson.fromJson(response.toString(), responseType);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(CreateGroupActivity.this, gggg.class);
                                            startActivity(intent);
                                        }
                                    });
                                }catch (Exception e){
                                    Log.e("Error","Giải mã sai định dạng trả về");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "Giải mã sai định dạng trả về", Toast.LENGTH_LONG).show();

                                        }
                                    });
                                }


                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Xử lý lỗi
                                Log.i("Success", "in onErrorResponse");
                                if (error instanceof TimeoutError) {
                                    Toast.makeText(getApplicationContext(), "Request Time Out", Toast.LENGTH_LONG).show();
                                    Log.e("Error", "Request Time Out");
                                } else {
                                    Log.e("Error", error.toString());
                                    if (error.networkResponse != null && error.networkResponse.statusCode == 400) {
                                        Gson gson = new Gson();
                                        String errorResponse = new String(error.networkResponse.data);
                                        Type responseType = new TypeToken<ErrorResponse<?>>(){}.getType();
                                        ErrorResponse<?> apiResponse = gson.fromJson(errorResponse, responseType);
                                        Toast.makeText(getApplicationContext(), apiResponse.getError().toString(), Toast.LENGTH_LONG).show();
                                        Log.e("Error", "Bad request: " + apiResponse.getError());

                                    } else {
                                        Gson gson = new Gson();
                                        String errorResponse = new String(error.networkResponse.data);
                                        Type responseType = new TypeToken<ErrorResponse<?>>(){}.getType();
                                        ErrorResponse<?> apiResponse = gson.fromJson(errorResponse, responseType);
                                        Log.e("Error", "Server: " + apiResponse.getError());
                                        Toast.makeText(getApplicationContext(), apiResponse.getError().toString(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        // Thêm token vào header
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoaCIsInJvbGUiOlsiYWRtaW4iXSwiaWF0IjoxNzEyNzYzNTc0LCJleHAiOjE3MTI3NjcxNzR9.ErekkimHPtAA7eWkuXZAMcefAUcDrsoG3Pic6TwLiyw");
                        return headers;
                    }
                };

                // Đặt retry policy
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                mRequestQueue.add(jsonObjectRequest);

            } catch (JSONException e) {
                Log.e("Error","không đúng định dạng Json");
            }
        }catch (Exception e){
            Log.e("errors",e.toString());
        }
    }
}
