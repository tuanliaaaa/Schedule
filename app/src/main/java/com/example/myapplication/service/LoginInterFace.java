package com.example.myapplication.service;

import com.android.volley.VolleyError;
import com.example.myapplication.dto.response.LoginResponse;

public interface LoginInterFace {
    void onSuccess(LoginResponse result);
    void onError(String error);
    void onErrorResponse(VolleyError error);
}

