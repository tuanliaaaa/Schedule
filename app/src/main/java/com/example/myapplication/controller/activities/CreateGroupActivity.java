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
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.myapplication.apdater.RecommendUserAdapter;
import com.example.myapplication.dto.ApiResponse;
import com.example.myapplication.dto.ErrorResponse;
import com.example.myapplication.dto.response.RecommendUserResponse;
import com.example.myapplication.dto.response.TeamCreateResponse;
import com.example.myapplication.entity.UserRcm;
import com.example.myapplication.service.RecommendUserInterFace;
import com.example.myapplication.service.ServiceImpl.RecommendUserImpl;
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
    private String token;
    private RequestQueue mRequestQueue;
    private RecommendUserAdapter recommendUserAdapter;
    private ImageView loadIcon_createGroup;
    private LinearLayout loading_createGroup;
    private Context context;
    private String domain;
    private String jsonPostCreateGroup;
    private List<UserRcm> dataList;
    private List<Integer> userList=new ArrayList<>();
    private RecyclerView recyclerViewRcmuser;
    private EditText inputAddPeople_createGroup;
    private EditText inputGroupName_createGroup;
    private EditText inputBudget_createGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creategroup);
        try{
            checkLogin();
            inputAddPeople_createGroup = findViewById(R.id.inputAddPeople_createGroup);
            recyclerViewRcmuser= findViewById(R.id.recyclerViewRcmUser);
            recyclerViewRcmuser.setLayoutManager(new LinearLayoutManager(this));
            dataList = new ArrayList<>();

            recommendUserAdapter = new RecommendUserAdapter(this, dataList);
            recommendUserAdapter.setOnItemClickListener(new RecommendUserAdapter.OnItemRcmUserClickListener() {
                @Override
                public void onItemClick(UserRcm userRcm,int potition) {
                    if(!inputAddPeople_createGroup.getText().toString().trim().equals(""))
                    inputAddPeople_createGroup.setText(inputAddPeople_createGroup.getText()+", "+userRcm.getUsername());
                    else inputAddPeople_createGroup.setText(userRcm.getUsername());
                    dataList.get(potition).setStatus("1");
                    userList.add(dataList.get(potition).getIdUser());
                    recommendUserAdapter.notifyDataSetChanged();
                }
            });

            recyclerViewRcmuser.setAdapter(recommendUserAdapter);
            loading_createGroup = findViewById(R.id.loading_createGroup);
            loadIcon_createGroup =findViewById(R.id.loadIcon_createGroup);
            RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);

            // Set animation properties
            rotateAnimation.setInterpolator(new LinearInterpolator());
            rotateAnimation.setRepeatCount(Animation.INFINITE); // Infinite rotation
            rotateAnimation.setDuration(2000); // 2 seconds for each rotation

            // Start the animation
            loadIcon_createGroup.startAnimation(rotateAnimation);


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
        RecommendUserImpl recommendUser = new RecommendUserImpl(new RecommendUserInterFace() {
            @Override
            public void onSuccess(List<RecommendUserResponse> result) {
                dataList.clear();
                for(RecommendUserResponse response:result)
                    dataList.add(new UserRcm(response));
                recommendUserAdapter.notifyDataSetChanged();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading_createGroup.setVisibility(View.INVISIBLE);
                    }
                });
            }

            @Override
            public void onErrorResponse(VolleyError errorResponse) {
                clearToken();
            }

            @Override
            public void onError(String error) {
                clearToken();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Lỗi",Toast.LENGTH_LONG).show();
                    }
                });

            }
        },domain,token);
        recommendUser.getAllUser(CreateGroupActivity.this);

    }

    public  void postTeam(){
        try{

            inputBudget_createGroup =findViewById(R.id.inputBudget_createGroup);
            inputGroupName_createGroup = findViewById(R.id.inputGroupName_createGroup);
            String nameTeam = String.valueOf(inputGroupName_createGroup.getText());
            int costExpected= Integer.parseInt(String.valueOf(inputBudget_createGroup.getText()));

            JSONObject jsonBody = new JSONObject();

            try {
                jsonBody.put("name", nameTeam);
                JSONArray usersArray = new JSONArray(userList);
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
                                    Log.d("Data",response.toString());
                                    Type responseType = new TypeToken<ApiResponse<TeamCreateResponse>>(){}.getType();
                                    ApiResponse<TeamCreateResponse> apiResponse = gson.fromJson(response.toString(), responseType);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(),"Tạo nhóm thành công", Toast.LENGTH_LONG).show();
                                            finish();
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
            Intent intent = new Intent(CreateGroupActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
    private  void clearToken(){
        SharedPreferences sharedPref = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("Token");
        editor.apply();
        finish();
        Intent intent = new Intent(CreateGroupActivity.this, LoginActivity.class);
        startActivity(intent);
    }

}
