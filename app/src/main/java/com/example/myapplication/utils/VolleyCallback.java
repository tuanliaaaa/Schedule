package com.example.myapplication.utils;

import com.example.myapplication.dto.request.LoginRequest;
import com.example.myapplication.dto.response.LoginResponse;

public interface VolleyCallback {
    void onSuccess(LoginResponse result);
    void onError(String error);
}

