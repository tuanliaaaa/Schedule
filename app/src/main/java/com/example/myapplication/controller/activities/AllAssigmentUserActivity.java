package com.example.myapplication.controller.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.example.myapplication.R;
import com.example.myapplication.apdater.ListAssigmentUserAdapter;
import com.example.myapplication.apdater.RecommendUserAdapter;
import com.example.myapplication.dto.ErrorResponse;
import com.example.myapplication.dto.response.AssignmentOfUserResponse;
import com.example.myapplication.dto.response.RecommendUserResponse;
import com.example.myapplication.entity.AssigmentUser;
import com.example.myapplication.entity.UserRcm;
import com.example.myapplication.service.ListAssigmentUserInterFace;
import com.example.myapplication.service.RecommendUserInterFace;
import com.example.myapplication.service.ServiceImpl.ListAssigmentUserImpl;
import com.example.myapplication.service.ServiceImpl.RecommendUserImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AllAssigmentUserActivity extends Activity {
    private RecyclerView recyclerViewLAssigmentUser;
    private String token;
    private String domain;

    private List<AssigmentUser> dataList;
    private ListAssigmentUserAdapter listAssigmentUserAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allassigmentuser);
        try{
            checkLogin();
            domain= getResources().getString(R.string.domain);
            getAllAssigment();
            recyclerViewLAssigmentUser= findViewById(R.id.recyclerViewListAssigmentUser);
            recyclerViewLAssigmentUser.setLayoutManager(new LinearLayoutManager(this));
            dataList = new ArrayList<>();
            listAssigmentUserAdapter = new ListAssigmentUserAdapter(this, dataList);
            listAssigmentUserAdapter.setOnItemClickListener(new ListAssigmentUserAdapter.OnItemListAssigmentUserClickListener() {
                @Override
                public void onItemClick(AssigmentUser assigmentUser, int potition) {
                    Intent intent = new Intent(AllAssigmentUserActivity.this, UpdateAssigmentActivity.class);
                    startActivity(intent);
                }
            });
            listAssigmentUserAdapter.notifyDataSetChanged();
            recyclerViewLAssigmentUser.setAdapter(listAssigmentUserAdapter);
        }catch (Exception e){
            Log.e("Error","Lỗi ở AllAssigment");
        }
    }

    private void getAllAssigment()
    {
        ListAssigmentUserImpl listAssigmentUser = new ListAssigmentUserImpl(new ListAssigmentUserInterFace() {
            @Override
            public void onSuccess(List<AssignmentOfUserResponse> result) {
                dataList.clear();
                for (AssignmentOfUserResponse response : result)
                    dataList.add(new AssigmentUser(response));
                listAssigmentUserAdapter.notifyDataSetChanged();

            }

            @Override
            public void onErrorResponse(ErrorResponse<?> errorResponse) {
               clearToken();
            }

            @Override
            public void onError(String error) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Lỗi Mạng",Toast.LENGTH_LONG).show();
                    }
                });

            }
        },domain,token,1);
        listAssigmentUser.getAll(AllAssigmentUserActivity.this);
    }
    private void checkLogin(){
        SharedPreferences sharedPref = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        token = sharedPref.getString("Token", null);
        if(token == null){
            finish();
            Intent intent = new Intent(AllAssigmentUserActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
    private  void clearToken(){
        SharedPreferences sharedPref = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("Token");
        editor.apply();
        finish();
        Intent intent = new Intent(AllAssigmentUserActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
