package com.example.myapplication.controller.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.myapplication.dto.response.AssigmentResponse;
import com.example.myapplication.dto.response.AssignmentResponse;
import com.example.myapplication.dto.response.TeamCreateResponse;
import com.example.myapplication.utils.LocalDateTimeAdapter;
import com.example.myapplication.utils.LocalDateTimeUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateAssigmentActivity extends Activity {
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private Context context;
    private String token;
    private ImageView loadIcon_UpdateAssigment;
    private LinearLayout loading_UpdateAssigment;
    private ScrollView scrollviewcontent_UpdateAssigment;
    private String domain;
    private TextView inputDescription_updateAssigment;
    private TextView inputStartDate_updateAssigment,inputEndDate_updateAssigment,inputStartTime_updateAssigment,inputEndTime_updateAssigment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateassigment);
        try{
            checkLogin();
            loading_UpdateAssigment = findViewById(R.id.loading_UpdateAssigment);
            scrollviewcontent_UpdateAssigment =findViewById(R.id.scrollviewcontent_UpdateAssigment);
            loadIcon_UpdateAssigment =findViewById(R.id.loadIcon_UpdateAssigment);
            RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);

            // Set animation properties
            rotateAnimation.setInterpolator(new LinearInterpolator());
            rotateAnimation.setRepeatCount(Animation.INFINITE); // Infinite rotation
            rotateAnimation.setDuration(2000); // 2 seconds for each rotation

            // Start the animation
            loadIcon_UpdateAssigment.startAnimation(rotateAnimation);
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
            domain= getResources().getString(R.string.domain);
            getAssigment();
            // Click to button save
            ImageView linearLayout1 = findViewById(R.id.save_updateAssigment);
            linearLayout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateAssigment();
                    Intent intent = new Intent(UpdateAssigmentActivity.this, gggg.class);
                    startActivity(intent); // Bắt đầu Activity mới
                }
            });
        }catch (Exception e){
            Log.e("Errors"," Lỗi Tổng ở UpdateAssigmentActivity");
        }

    }

    public void updateAssigment(){
        try{
            JSONObject jsonBody = new JSONObject();
            loading_UpdateAssigment.setVisibility(View.VISIBLE);

            try {
                // Đưa dữ liệu vào JSONObject
                jsonBody.put("process", "2");

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, domain+"/Assignment/AssigmentUser/18",
                        jsonBody,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("success", "in onResponse");
                                GsonBuilder gsonBuilder = new GsonBuilder();
                                gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
                                Gson gson = gsonBuilder.create();
                                try {
                                    Type responseType = new TypeToken<ApiResponse<AssigmentResponse>>(){}.getType();
                                    ApiResponse<AssigmentResponse> apiResponse = gson.fromJson(response.toString(), responseType);
                                    AssigmentResponse assigmentResponse =(AssigmentResponse) apiResponse.getData();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();

                                            try{
                                                inputDescription_updateAssigment=findViewById(R.id.inputDescription_updateAssigment);
                                                inputDescription_updateAssigment.setText(assigmentResponse.getDescription());
                                                loading_UpdateAssigment.setVisibility(View.INVISIBLE);

                                            }catch (Exception e){
                                                Log.e("Error","lỗi giao diện");
                                            }
                                        }
                                    });
                                } catch (Exception e) {
                                    Log.e("Error", "Giải mã sai định dạng trả về");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            loading_UpdateAssigment.setVisibility(View.INVISIBLE);

                                            Toast.makeText(getApplicationContext(), "Giải mã sai định dạng trả về", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
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
                               runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {
                                       loading_UpdateAssigment.setVisibility(View.INVISIBLE);
                                   }
                               });
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        // Thêm token vào header
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Authorization", "Bearer "+token);
                        return headers;
                    }
                };
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                mRequestQueue.add(jsonObjectRequest);
            } catch (JSONException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading_UpdateAssigment.setVisibility(View.INVISIBLE);
                    }
                });
                Log.e("Error","không đúng định dạng Json");
            }
        }catch (Exception e){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loading_UpdateAssigment.setVisibility(View.INVISIBLE);
                }
            });
            Log.e("errors",e.toString());
        }
    }

    public void getAssigment() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, domain + "/Assignment/AssigmentUser/18", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Xử lý phản hồi thành công
                        Log.i("success", "in onResponse");
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
                        Gson gson = gsonBuilder.create();
                        try {
                            Type responseType = new TypeToken<ApiResponse<AssigmentResponse>>(){}.getType();
                            ApiResponse<AssigmentResponse> apiResponse = gson.fromJson(response.toString(), responseType);
                            AssigmentResponse assigmentResponse =(AssigmentResponse) apiResponse.getData();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();

                                    try{
                                        LocalDateTimeUtils start = new LocalDateTimeUtils(assigmentResponse.getStartAt());
                                        inputStartDate_updateAssigment=findViewById(R.id.inputStartDate_updateAssigment);
                                        inputStartDate_updateAssigment.setText(start.getDate());
                                        inputStartTime_updateAssigment =findViewById(R.id.inputStartTime_updateAssigment);
                                        inputStartTime_updateAssigment.setText(start.getTime());
                                        LocalDateTimeUtils end = new LocalDateTimeUtils(assigmentResponse.getEndAt());
                                        inputEndDate_updateAssigment = findViewById(R.id.inputEndDate_updateAssigment);
                                        inputEndDate_updateAssigment.setText(end.getDate());
                                        inputEndTime_updateAssigment = findViewById(R.id.inputEndTime_updateAssigment);
                                        inputEndTime_updateAssigment.setText(end.getTime());
                                        inputDescription_updateAssigment=findViewById(R.id.inputDescription_updateAssigment);
                                        inputDescription_updateAssigment.setText(assigmentResponse.getDescription());
                                        loading_UpdateAssigment.setVisibility(View.INVISIBLE);
                                        scrollviewcontent_UpdateAssigment.setVisibility(View.VISIBLE);
                                    }catch (Exception e){
                                        Log.e("Error","lỗi giao diện");
                                    }
                                }
                            });
                        } catch (Exception e) {
                            Log.e("Error", "Giải mã sai định dạng trả về");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Giải mã sai định dạng trả về", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loading_UpdateAssigment.setVisibility(View.INVISIBLE);
                            }
                        });
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loading_UpdateAssigment.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // Thêm token vào header
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer "+token);
                return headers;
            }
        };

// Đặt retry policy
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

// Thêm yêu cầu vào hàng đợi
        mRequestQueue.add(jsonObjectRequest);

    }


    private void checkLogin(){
        SharedPreferences sharedPref = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        token = sharedPref.getString("Token", null);
        if(token == null){
            finish();
            Intent intent = new Intent(UpdateAssigmentActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
    private  void clearToken(){
        SharedPreferences sharedPref = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("Token");
        editor.apply();
        finish();
        Intent intent = new Intent(UpdateAssigmentActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
