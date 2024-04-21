package com.example.myapplication.service;

import com.android.volley.VolleyError;
import com.example.myapplication.dto.response.RecommendUserResponse;

import java.util.List;

public interface ListUserInTeamInterFace {
    void onSuccess(List<RecommendUserResponse> recommendUserResponseList);
    void onError(String error);
    void onErrorResponse(VolleyError errorResponse);
}
