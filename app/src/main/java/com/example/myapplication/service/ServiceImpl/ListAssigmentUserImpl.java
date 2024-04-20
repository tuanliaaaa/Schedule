package com.example.myapplication.service.ServiceImpl;

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
import com.android.volley.toolbox.Volley;
import com.example.myapplication.dto.ApiResponse;
import com.example.myapplication.dto.ErrorResponse;
import com.example.myapplication.dto.request.LoginRequest;
import com.example.myapplication.dto.response.AssignmentOfUserResponse;
import com.example.myapplication.dto.response.LoginResponse;
import com.example.myapplication.dto.response.RecommendUserResponse;
import com.example.myapplication.service.ListAssigmentUserInterFace;
import com.example.myapplication.service.LoginInterFace;
import com.example.myapplication.service.RecommendUserInterFace;
import com.example.myapplication.utils.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ListAssigmentUserImpl {
    private ListAssigmentUserInterFace callback;
    private RequestQueue requestQueue;
    private String url;
    private String id;
    private JsonObjectRequest jsonObjectRequest;
    private String token;

    public ListAssigmentUserImpl(ListAssigmentUserInterFace callback, String domain) {
        this.callback = callback;
        this.url = domain;
        this.token=null;
    }
    public ListAssigmentUserImpl(ListAssigmentUserInterFace callback,String domain,String token,Integer id) {
        this.callback = callback;
        this.url = domain;
        this.token=token;
        this.id=String.valueOf(id);
    }
    public void getAll(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        JSONObject jsonBody = new JSONObject();
        try {
            jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    url + "/Assignment/AssigmentLogin/Team/"+id,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Xử lý phản hồi thành công
                            Log.i("success", "in onResponse");
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
                            Gson gson = gsonBuilder.create();
                            try {
                                Log.d("Data",response.toString());
                                Type responseType = new TypeToken<ApiResponse<List<AssignmentOfUserResponse>>>(){}.getType();
                                ApiResponse<List<AssignmentOfUserResponse>> apiResponse = gson.fromJson(response.toString(), responseType);
                                callback.onSuccess(apiResponse.getData());
                            } catch (Exception e) {
                                Log.e("Error","Giải mã sai định dạng trả về");
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("Success", "in onErrorResponse");
                            if (error.networkResponse != null){
                                if (error.networkResponse.statusCode == 400) {
                                    Gson gson = new Gson();
                                    String errorResponse = new String(error.networkResponse.data);
                                    Type responseType = new TypeToken<ErrorResponse<?>>(){}.getType();
                                    ErrorResponse<?> apiResponse = gson.fromJson(errorResponse, responseType);
//                                    Toast.makeText(context.getApplicationContext(), apiResponse.getError().toString(), Toast.LENGTH_LONG).show();
                                    Log.e("Error", "Bad request: " + apiResponse.getError());
                                    callback.onErrorResponse(apiResponse);
                                } else {
                                    Gson gson = new Gson();
                                    String errorResponse = new String(error.networkResponse.data);
                                    Type responseType = new TypeToken<ErrorResponse<?>>(){}.getType();
                                    ErrorResponse<?> apiResponse = gson.fromJson(errorResponse, responseType);
                                    Log.e("Error", "Server: " + apiResponse.getError());
                                    callback.onErrorResponse(apiResponse);

                                }

                            }
                            // Xử lý lỗi
                            else {
                                if (error instanceof TimeoutError) {
                                    callback.onError("Request Time Out");
                                    //                                Toast.makeText(context.getApplicationContext(), "Request Time Out", Toast.LENGTH_LONG).show();
                                    Log.e("Error", "Request Time Out");
                                } else {
                                    Log.e("Error", error.toString());
                                    callback.onError("Lỗi Mạng");

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
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
