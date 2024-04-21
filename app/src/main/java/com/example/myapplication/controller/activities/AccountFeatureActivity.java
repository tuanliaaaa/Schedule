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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.example.myapplication.dto.response.InforResponse;
import com.example.myapplication.dto.response.RoleResponse;
import com.example.myapplication.utils.ExcelUltil;
import com.example.myapplication.utils.LocalDateTimeAdapter;
import com.example.myapplication.utils.LocalDateTimeUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.ConnectException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountFeatureActivity extends Activity {
    private RequestQueue mRequestQueue;
    private String token;
    private String domain;
    private LinearLayout toAssigment,toManageSpent,toManageProcessWork,toViewAssigment;
    private ImageView loadIcon_AccountFeature;
    private LinearLayout loading_AccountFeature;
    private ScrollView scrollviewcontent_AccountFeature;
    private Integer idTeam;
    private RotateAnimation rotateAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_feature);
        try{
            Intent t =getIntent();
            idTeam=t.getIntExtra("idTeam",0);
            checkLogin();
            loading_AccountFeature = findViewById(R.id.loading_AccountFeature);
            loadIcon_AccountFeature =findViewById(R.id.loadIcon_AccountFeature);
            scrollviewcontent_AccountFeature= findViewById(R.id.scrollviewcontent_AccountFeature);
             rotateAnimation = new RotateAnimation(0, 360,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);

            // Set animation properties
            rotateAnimation.setInterpolator(new LinearInterpolator());
            rotateAnimation.setRepeatCount(Animation.INFINITE); // Infinite rotation
            rotateAnimation.setDuration(2000); // 2 seconds for each rotation

            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
            domain= getResources().getString(R.string.domain);
            getRoles();
            toViewAssigment =findViewById(R.id.viewAssigment_accountFeature);
            toAssigment = findViewById(R.id.addAssigment_accountFeature);
            toManageSpent = findViewById(R.id.manageSpent_accountFeature);
            toManageProcessWork = findViewById(R.id.managePocessWork_accountFeature);
        }catch (Exception e){
            Log.e("Error",e.toString());
        }
    }
    public void getRoles(){
        loadIcon_AccountFeature.startAnimation(rotateAnimation);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, domain + "/api/auth/infor", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Xử lý phản hồi thành công
                        Log.i("success", "in onResponse");
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
                        Gson gson = gsonBuilder.create();
                        try {
                            Type responseType = new TypeToken<ApiResponse<InforResponse>>(){}.getType();
                            ApiResponse<InforResponse> apiResponse = gson.fromJson(response.toString(), responseType);
                            InforResponse assigmentResponse =(InforResponse) apiResponse.getData();
                            List<String> roleNames = new ArrayList<>();

                            // Duyệt qua mảng roles và thêm giá trị roleName vào danh sách
                            for (RoleResponse role : assigmentResponse.getRoles()) {
                                String roleName = role.getRoleName();
                                roleNames.add(roleName);
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                                    try{
                                        if(roleNames.contains("admin"))setAdmin();
                                        else setUser();
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
                                loading_AccountFeature.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Success", "in onError");
                        Log.e("Error", error.toString());
                        if(error.networkResponse!=null){
                            Log.i("Success", "in onErrorResponse");
                            if ( error.networkResponse.statusCode == 400) {
                                Gson gson = new Gson();
                                String errorResponse = new String(error.networkResponse.data);
                                Type responseType = new TypeToken<ErrorResponse<?>>(){}.getType();
                                ErrorResponse<?> apiResponse = gson.fromJson(errorResponse, responseType);
                                Toast.makeText(getApplicationContext(), apiResponse.getError().toString(), Toast.LENGTH_LONG).show();
                                Log.e("Error", "Bad request: " + apiResponse.getError());
                            } else {
                                clearToken();
                                Gson gson = new Gson();
                                String errorResponse = new String(error.networkResponse.data);
                                Type responseType = new TypeToken<ErrorResponse<?>>(){}.getType();
                                ErrorResponse<?> apiResponse = gson.fromJson(errorResponse, responseType);
                                Log.e("Error", "Server: " + apiResponse.getError());
                                Toast.makeText(getApplicationContext(), apiResponse.getError().toString(), Toast.LENGTH_LONG).show();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loading_AccountFeature.setVisibility(View.INVISIBLE);
                                }
                            });
                        }else{
                            Log.i("Success", "in onError");
                            if (error instanceof TimeoutError) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Request Time Out", Toast.LENGTH_LONG).show();
                                        loadIcon_AccountFeature.clearAnimation();
                                        loadIcon_AccountFeature.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                loadIcon_AccountFeature.setOnClickListener(null);
                                                getRoles();
                                            }
                                        });
                                    }
                                });
                                Log.e("Error", "Request Time Out");
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Lỗi Mạng", Toast.LENGTH_LONG).show();
                                        loadIcon_AccountFeature.clearAnimation();
                                        loadIcon_AccountFeature.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                loadIcon_AccountFeature.setOnClickListener(null);
                                                getRoles();
                                            }
                                        });
                                    }
                                });
                                Log.e("Error","Lỗi Mạng");
                            }
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // Thêm token vào header
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " +token);
                return headers;
            }
        };

// Đặt retry policy
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

// Thêm yêu cầu vào hàng đợi
        mRequestQueue.add(jsonObjectRequest);
    }
    public void setAdmin(){

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                toViewAssigment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AccountFeatureActivity.this, AllAssigmentUserActivity.class);
                        intent.putExtra("idTeam",idTeam);
                        startActivity(intent);
                    }
                });


                loading_AccountFeature.setVisibility(View.INVISIBLE);
                scrollviewcontent_AccountFeature.setVisibility(View.VISIBLE);
                toManageSpent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AccountFeatureActivity.this, ChartMoneyManageActivity.class);
                        intent.putExtra("idTeam",idTeam);
                        startActivity(intent);
                    }
                });
                toManageSpent.setVisibility(View.VISIBLE);
                toAssigment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Tạo Intent để chuyển từ MainActivity sang Activity mới
                        Intent intent = new Intent(AccountFeatureActivity.this, AssigmentActivity.class);
                        intent.putExtra("idTeam",idTeam);
                        startActivity(intent); // Bắt đầu Activity mới
                    }
                });
                toAssigment.setVisibility(View.VISIBLE);
                toManageProcessWork.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AccountFeatureActivity.this, TableAllProcessActivity.class);
                        intent.putExtra("idTeam",idTeam);
                        startActivity(intent);
                    }
                });
                toManageProcessWork.setVisibility(View.VISIBLE);
            }
        });

    }

    public void setUser(){
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                toViewAssigment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AccountFeatureActivity.this, AllAssigmentUserActivity.class);
                        intent.putExtra("idTeam",idTeam);
                        startActivity(intent);
                    }
                });
                loading_AccountFeature.setVisibility(View.INVISIBLE);
                scrollviewcontent_AccountFeature.setVisibility(View.VISIBLE);
            }
        });
    }

    public void checkLogin(){
        // Đọc token từ SharedPreferences
        SharedPreferences sharedPref = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        token = sharedPref.getString("Token", null);
        if(token == null){
            finish();
            Intent intent = new Intent(AccountFeatureActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private  void clearToken(){
        SharedPreferences sharedPref = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("Token");
        editor.apply();
        finish();
        Intent intent = new Intent(AccountFeatureActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
