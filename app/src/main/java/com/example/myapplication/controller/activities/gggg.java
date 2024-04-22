package com.example.myapplication.controller.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;


import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.apdater.ListTeamAdapter;
import com.example.myapplication.dto.ApiResponse;
import com.example.myapplication.dto.response.CostResponse;
import com.example.myapplication.dto.response.TeamCostResponse;
import com.example.myapplication.dto.response.TeamResponse;
import com.example.myapplication.entity.Team;
import com.example.myapplication.utils.LocalDateAdapter;
import com.example.myapplication.utils.LocalDateTimeAdapter;
import com.example.myapplication.utils.ObjChartUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.gustavoavila.websocketclient.WebSocketClient;

public class gggg extends Activity {
    private Integer idTeam;
    private RecyclerView recyclerViewTeams;
    private ListTeamAdapter teamAdapter;
    private String domain;
    private String token;
    private RequestQueue mRequestQueue;
    private List<Team> teamList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupmess);
        idTeam=1;
        checkLogin();
        recyclerViewTeams = findViewById(R.id.recyclerViewTeams);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getApplicationContext());
        recyclerViewTeams.setLayoutManager(linearLayout);
        teamList=new ArrayList<>();
        teamAdapter = new ListTeamAdapter(getApplicationContext(),teamList);
        recyclerViewTeams.setAdapter(teamAdapter);

        getListTeam();

        ImageView imageView4 =findViewById(R.id.imageView4);
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(gggg.this, LogOutActivity.class);
                startActivity(intent);
            }
        });

        ImageView i = findViewById(R.id.footerImgIcon_updateAssigment);

        // Button to AddGroup
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(gggg.this, CreateGroupActivity.class);
                intent.putExtra("idTeam",idTeam);
                startActivity(intent);
            }
        });
        // Thiết lập sự kiện lắng nghe cho LinearLayout
    }

    private void getListTeam() {
        mRequestQueue = Volley.newRequestQueue(this);
        domain = getResources().getString(R.string.domain);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, domain + "/team", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("success", "in onResponse");
                        Log.d("Data",response.toString());

                        Gson gson = new Gson();
                        try{
                            Type responseType = new TypeToken<ApiResponse<List<TeamResponse>>>(){}.getType();
                            ApiResponse<List<TeamResponse>> apiResponse = gson.fromJson(response.toString(), responseType);
                            List<TeamResponse> teamResponseList = apiResponse.getData();
                            teamList.clear();
                            for(TeamResponse teamResponse:teamResponseList)teamList.add(new Team(teamResponse));
                            teamAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            Log.e("Error","Lỗi parse json");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", "Error in request: " + error.toString());

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(jsonObjectRequest);
    }

    private void checkLogin(){
        SharedPreferences sharedPref = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        token = sharedPref.getString("Token", null);
        if(token == null){
            finish();
            Intent intent = new Intent(gggg.this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
