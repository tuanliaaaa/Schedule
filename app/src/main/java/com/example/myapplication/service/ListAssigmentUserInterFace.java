package com.example.myapplication.service;

import com.example.myapplication.dto.ErrorResponse;
import com.example.myapplication.dto.response.AssignmentOfUserResponse;
import com.example.myapplication.dto.response.LoginResponse;

import java.util.List;

public interface ListAssigmentUserInterFace {
    void onSuccess(List<AssignmentOfUserResponse> result);
    void onError(String error);
    void onErrorResponse(ErrorResponse<?> errorResponse);
}
