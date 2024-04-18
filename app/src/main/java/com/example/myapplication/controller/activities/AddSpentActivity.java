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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
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
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.dto.ApiResponse;
import com.example.myapplication.dto.ErrorResponse;
import com.example.myapplication.dto.response.TeamCreateResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class AddSpentActivity extends Activity {
    private TextView inputSpentName_addSpent;
    private Spinner dropdown_menu;
    private TextView inputMoney_addSpent;
    private String domain;
    private TextView inputDescription_addSpent;
    private String token;
    private RequestQueue mRequestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addspent);
        ImageView saved = findViewById(R.id.save_addSpent);
        Spinner dropdownMenu = findViewById(R.id.dropdown_menu);
        ImageView loadIcon_addSpend =findViewById(R.id.loadIcon_addSpend);
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        try {
            checkLogin();
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
            // Set animation properties
            rotateAnimation.setInterpolator(new LinearInterpolator());
            rotateAnimation.setRepeatCount(Animation.INFINITE); // Infinite rotation
            rotateAnimation.setDuration(2000); // 2 seconds for each rotation

            // Start the animation
            loadIcon_addSpend.startAnimation(rotateAnimation);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    // Thực hiện công việc nền ở đây
                    try {
                        // Giả định thực hiện công việc nền trong 3 giây
                        Thread.sleep(3000);
//
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                LinearLayout l = findViewById(R.id.loading_AddSpend);
                                l.setVisibility(View.INVISIBLE);
                                ScrollView s =findViewById(R.id.scrollviewcontent_addSpent);
                                s.setVisibility(View.VISIBLE);
                            }
                        });

                    } catch (InterruptedException e) {
                        System.out.println("ng");
                    }

                    // Sau khi công việc nền hoàn thành, cập nhật giao diện người dùng

                }
            }).start();
// Tạo một mảng dữ liệu cho các lựa chọn
            String[] options = {"Option 1", "Option 2", "Option 3"};

// Tạo một ArrayAdapter từ mảng dữ liệu
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dropdownMenu.setAdapter(adapter);

// Lắng nghe sự kiện chọn của Dropdown Menu
            dropdownMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // Xử lý sự kiện khi một lựa chọn được chọn
                    String selectedOption = options[position];
                    Toast.makeText(getApplicationContext(), "Selected: " + selectedOption, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // Xử lý khi không có lựa chọn nào được chọn
                }
            });
            saved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    domain= getResources().getString(R.string.domain);
                    postSpent();

                }
            });
        }catch (Exception e){
            Log.e("Errors"," Lỗi Tổng ở AddSpentActivity");
        }

    }

    public void postSpent() {
        try{

            inputSpentName_addSpent =findViewById(R.id.inputSpentName_addSpent);
            inputDescription_addSpent = findViewById(R.id.inputDescription_addSpent);
            inputMoney_addSpent = findViewById(R.id.inputMoney_addSpent);
            dropdown_menu = findViewById(R.id.dropdown_menu);
            String nameSpent = String.valueOf(inputSpentName_addSpent.getText());
            int costExpected= Integer.parseInt(String.valueOf(inputMoney_addSpent.getText()));
            int idAssignment = 1;

            JSONObject jsonBody = new JSONObject();

            try {
                jsonBody.put("costName", nameSpent);
                jsonBody.put("price", costExpected);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, domain+"/Cost/Assigment/" + idAssignment, jsonBody,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Xử lý phản hồi thành công
                                Log.i("success", "in onResponse");
                                Gson gson = new Gson();
                                try{
                                    Log.d("Data",response.toString());
                                    Type responseType = new TypeToken<ApiResponse<TeamCreateResponse>>(){}.getType();
                                    ApiResponse<TeamCreateResponse> apiResponse = gson.fromJson(response.toString(), responseType);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(),"Thêm chi phí thành công thành công", Toast.LENGTH_LONG).show();
                                            finish();
                                            Intent intent = new Intent(AddSpentActivity.this, TableAllSpentActivity.class);
                                            startActivity(intent); // Bắt đầu Activity mới
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
                        headers.put("Authorization", "Bearer "+token);
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

    private void checkLogin(){
        SharedPreferences sharedPref = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        token = sharedPref.getString("Token", null);
        if(token == null){
            finish();
            Intent intent = new Intent(AddSpentActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
    private  void clearToken(){
        SharedPreferences sharedPref = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("Token");
        editor.apply();
        finish();
        Intent intent = new Intent(AddSpentActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
