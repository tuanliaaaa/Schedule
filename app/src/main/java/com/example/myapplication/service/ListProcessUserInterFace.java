package com.example.myapplication.service;

import com.example.myapplication.dto.ErrorResponse;
import com.example.myapplication.dto.response.ProcessUserResponse;

public interface ListProcessUserInterFace {
    void onSuccess(ProcessUserResponse result);
    void onError(String error);
    void onErrorResponse(ErrorResponse<?> errorResponse);
}
