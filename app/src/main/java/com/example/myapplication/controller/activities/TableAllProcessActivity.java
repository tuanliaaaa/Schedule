package com.example.myapplication.controller.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.apdater.ListAssigmentUserAdapter;
import com.example.myapplication.apdater.ListProcessUserAdapter;
import com.example.myapplication.dto.ErrorResponse;
import com.example.myapplication.dto.response.AssigmentUserResponse;
import com.example.myapplication.dto.response.AssignmentOfUserResponse;
import com.example.myapplication.dto.response.ProcessUserResponse;
import com.example.myapplication.entity.AssigmentUser;
import com.example.myapplication.entity.ProcessUser;
import com.example.myapplication.service.ListAssigmentUserInterFace;
import com.example.myapplication.service.ListProcessUserInterFace;
import com.example.myapplication.service.ServiceImpl.ListAssigmentUserImpl;
import com.example.myapplication.service.ServiceImpl.ListProcessUserImpl;

import java.util.ArrayList;
import java.util.List;

public class TableAllProcessActivity extends Activity {
    private RecyclerView recyclerViewLProcessUser;
    private String token;
    private String domain;
    private ListProcessUserAdapter listProcessUserAdapter;
    private List<ProcessUser> dataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tableallprocess);
        try{
            domain= getResources().getString(R.string.domain);
            dataList=new ArrayList<>();
            checkLogin();
            getAllProcess(15);
            recyclerViewLProcessUser= findViewById(R.id.recyclerViewListProcessUser);
            recyclerViewLProcessUser.setLayoutManager(new LinearLayoutManager(this));
            listProcessUserAdapter = new ListProcessUserAdapter(this, dataList);
            listProcessUserAdapter.setOnItemClickListener(new ListProcessUserAdapter.OnItemListProcessUserClickListener() {
                @Override
                public void onItemClick(ProcessUser processUser, int potition) {
//                    Intent intent = new Intent(TableAllProcessActivity.this, UpdateAssigmentActivity.class);
//                    startActivity(intent);
                }
            });


            ImageView exportReport = findViewById(R.id.edit_tableAllProcess);
            exportReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TableAllProcessActivity.this, AssigmentActivity.class);
                    startActivity(intent); // Bắt đầu Activity mới
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void checkLogin(){
        // Đọc token từ SharedPreferences
        SharedPreferences sharedPref = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        token = sharedPref.getString("Token", null);
        if(token == null){
            finish();
            Intent intent = new Intent(TableAllProcessActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private  void clearToken(){
        SharedPreferences sharedPref = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("Token");
        editor.apply();
        finish();
        Intent intent = new Intent(TableAllProcessActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void getAllProcess(Integer id)
    {
        ListProcessUserImpl listAssigmentUser = new ListProcessUserImpl(new ListProcessUserInterFace() {
            @Override
            public void onSuccess(ProcessUserResponse result) {
                dataList.clear();
                for (AssigmentUserResponse response : result.getUserStatus())
                    dataList.add(new ProcessUser(response));
                listProcessUserAdapter.notifyDataSetChanged();
                recyclerViewLProcessUser.setAdapter(listProcessUserAdapter);


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
        },domain,token,id);
        listAssigmentUser.getAll(TableAllProcessActivity.this);
    }
}
