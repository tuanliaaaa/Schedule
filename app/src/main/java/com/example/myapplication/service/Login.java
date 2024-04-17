package com.example.myapplication.service;

import android.content.Context;
import android.util.Log;
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
import com.example.myapplication.dto.request.LoginRequest;
import com.example.myapplication.dto.response.LoginResponse;
import com.example.myapplication.dto.response.TeamCreateResponse;
import com.example.myapplication.utils.VolleyCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class Login {
    private VolleyCallback callback;
    private RequestQueue requestQueue;
    private String url;
    private JsonObjectRequest jsonObjectRequest;

    public Login(VolleyCallback callback,String domain) {
        this.callback = callback;
        this.url = domain;
    }

    public void postDataToUrl(Context context, LoginRequest loginRequest) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", loginRequest.getUsername());
            jsonBody.put("password", loginRequest.getPassword());


            jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    url + "/api/auth/login",
                    jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Xử lý phản hồi thành công
                            Log.i("success", "in onResponse");
                            Gson gson = new Gson();
                            try {
                                Type responseType = new TypeToken<ApiResponse<LoginResponse>>(){}.getType();
                                ApiResponse<LoginResponse> apiResponse = gson.fromJson(response.toString(), responseType);

                                callback.onSuccess(apiResponse.getData());

                            } catch (Exception e) {
                                Log.e("Error","Giải mã sai định dạng trả về");

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Xử lý lỗi
                            Log.i("Success", "in onErrorResponse");
                            if (error instanceof TimeoutError) {

//                                Toast.makeText(context.getApplicationContext(), "Request Time Out", Toast.LENGTH_LONG).show();
                                Log.e("Error", "Request Time Out");
                            } else {
                                Log.e("Error", error.toString());
                                if (error.networkResponse != null && error.networkResponse.statusCode == 400) {
                                    Gson gson = new Gson();
                                    String errorResponse = new String(error.networkResponse.data);
                                    Type responseType = new TypeToken<ErrorResponse<?>>(){}.getType();
                                    ErrorResponse<?> apiResponse = gson.fromJson(errorResponse, responseType);
//                                    Toast.makeText(context.getApplicationContext(), apiResponse.getError().toString(), Toast.LENGTH_LONG).show();
                                    Log.e("Error", "Bad request: " + apiResponse.getError());
                                    callback.onError(apiResponse.getError().toString());
                                } else {
                                    Gson gson = new Gson();
                                    String errorResponse = new String(error.networkResponse.data);
                                    Type responseType = new TypeToken<ErrorResponse<?>>(){}.getType();
                                    ErrorResponse<?> apiResponse = gson.fromJson(errorResponse, responseType);
                                    Log.e("Error", "Server: " + apiResponse.getError());
                                    Toast.makeText(context.getApplicationContext(), apiResponse.getError().toString(), Toast.LENGTH_LONG).show();
                                    callback.onError(apiResponse.getError().toString());
                                }
                            }
                        }
                    }) {
            };

            // Đặt retry policy
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
