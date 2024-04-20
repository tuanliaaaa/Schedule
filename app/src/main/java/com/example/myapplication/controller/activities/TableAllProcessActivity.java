package com.example.myapplication.controller.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.apdater.ListProcessUserAdapter;
import com.example.myapplication.dto.ErrorResponse;
import com.example.myapplication.dto.response.AssigmentUserResponse;
import com.example.myapplication.dto.response.ProcessUserResponse;
import com.example.myapplication.entity.ProcessUser;
import com.example.myapplication.service.ListProcessUserInterFace;
import com.example.myapplication.service.ServiceImpl.ListProcessUserImpl;
import com.example.myapplication.utils.ExcelUltil;
import com.example.myapplication.utils.LocalDateTimeUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TableAllProcessActivity extends Activity {
    private RecyclerView recyclerViewLProcessUser;
    private String token;
    private String domain;
    private ListProcessUserAdapter listProcessUserAdapter;
    private List<ProcessUser> dataList;
    private  ImageView edit;
    private ImageView export_tableAllProcess;
    private TextView inputtableAllProcess_tableAllProcess,inputStartDate_tableAllProcess,inputStartTime_tableAllProcess,inputEndDate_tableAllProcess,inputEndTime_tableAllProcess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tableallprocess);
        try{
            domain= getResources().getString(R.string.domain);
            inputtableAllProcess_tableAllProcess=findViewById(R.id.inputtableAllProcess_tableAllProcess);
            inputStartDate_tableAllProcess=findViewById(R.id.inputStartDate_tableAllProcess);
            inputStartTime_tableAllProcess =findViewById(R.id.inputStartTime_tableAllProcess);
            inputEndDate_tableAllProcess =findViewById(R.id.inputEndDate_tableAllProcess);
            inputEndTime_tableAllProcess =findViewById(R.id.inputEndTime_tableAllProcess);
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


             edit = findViewById(R.id.edit_tableAllProcess);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TableAllProcessActivity.this, UpdateAssigmentManageActivity.class);
                    startActivity(intent); // Bắt đầu Activity mới
                }
            });
            export_tableAllProcess=findViewById(R.id.export_tableAllProcess);
            export_tableAllProcess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ExcelUltil().createExcelAndDownload(TableAllProcessActivity.this,"hello");
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
                LocalDateTime startTime = result.getStartAt();
                LocalDateTime endTime = result.getEndAt();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        inputEndTime_tableAllProcess.setText(new LocalDateTimeUtils(endTime).getTime());
                        inputEndDate_tableAllProcess.setText(new LocalDateTimeUtils(endTime).getDate());
                        inputStartDate_tableAllProcess.setText(new LocalDateTimeUtils(startTime).getDate());
                        inputStartTime_tableAllProcess.setText(new LocalDateTimeUtils(startTime).getTime());
                        inputtableAllProcess_tableAllProcess.setText(result.getNameAssignment());
                    }
                });

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
