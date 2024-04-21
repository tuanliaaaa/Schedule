package com.example.myapplication.controller.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
    private ImageView loadIcon_allAssigmentUser;
    private LinearLayout loading_allAssigmentUser;
    private ScrollView scrollviewcontent_allAssigmentUser;
    

    private List<AssigmentUser> dataList;
    private ListAssigmentUserAdapter listAssigmentUserAdapter;
    private RotateAnimation rotateAnimation;
    private Integer idTeam;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allassigmentuser);
        try{
            Intent t =getIntent();
            idTeam=t.getIntExtra("idTeam",0);
            checkLogin();
            loading_allAssigmentUser = findViewById(R.id.loading_allAssigmentUser);
            scrollviewcontent_allAssigmentUser =findViewById(R.id.scrollviewcontent_allAssigmentUser);
            loadIcon_allAssigmentUser =findViewById(R.id.loadIcon_allAssigmentUser);
             rotateAnimation = new RotateAnimation(0, 360,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);

            // Set animation properties
            rotateAnimation.setInterpolator(new LinearInterpolator());
            rotateAnimation.setRepeatCount(Animation.INFINITE); // Infinite rotation
            rotateAnimation.setDuration(2000); // 2 seconds for each rotation

            // Start the animation


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
                    intent.putExtra("idAssigment", assigmentUser.getIdAssigment());
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
        loadIcon_allAssigmentUser.startAnimation(rotateAnimation);
        ListAssigmentUserImpl listAssigmentUser = new ListAssigmentUserImpl(new ListAssigmentUserInterFace() {
            @Override
            public void onSuccess(List<AssignmentOfUserResponse> result) {
                dataList.clear();
                for (AssignmentOfUserResponse response : result)
                    dataList.add(new AssigmentUser(response));
                listAssigmentUserAdapter.notifyDataSetChanged();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        scrollviewcontent_allAssigmentUser.setVisibility(View.VISIBLE);
                        loading_allAssigmentUser.setVisibility(View.INVISIBLE);
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
                        loadIcon_allAssigmentUser.clearAnimation();

                        loadIcon_allAssigmentUser.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadIcon_allAssigmentUser.setOnClickListener(null);
                                getAllAssigment();

                            }
                        });
                    }
                });

            }
        },domain,token,idTeam);
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
