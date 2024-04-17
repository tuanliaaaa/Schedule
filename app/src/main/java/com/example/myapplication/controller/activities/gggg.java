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

import com.example.myapplication.R;

import java.net.URI;
import java.net.URISyntaxException;

import dev.gustavoavila.websocketclient.WebSocketClient;

public class gggg extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupmess);
        checkLogin();
        LinearLayout linearLayout = findViewById(R.id.ahihi);
        ImageView i = findViewById(R.id.footerImgIcon_updateAssigment);

        // Button to AddGroup
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(gggg.this, CreateGroupActivity.class);
                startActivity(intent);
            }
        });
        // Thiết lập sự kiện lắng nghe cho LinearLayout
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent để chuyển từ MainActivity sang Activity mới
                Intent intent = new Intent(gggg.this, AccountFeatureActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkLogin(){
        SharedPreferences sharedPref = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        String token = sharedPref.getString("Token", null);
        if(token == null){

            finish();
            Intent intent = new Intent(gggg.this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
