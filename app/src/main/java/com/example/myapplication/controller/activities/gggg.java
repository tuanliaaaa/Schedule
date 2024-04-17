package com.example.myapplication.controller.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
    private WebSocketClient client;
    private int notificationId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupmess);
        LinearLayout linearLayout = findViewById(R.id.ahihi);
        ImageView i = findViewById(R.id.footerImgIcon_updateAssigment);
//        createNotificationChannel();
//        connectWebSocket();
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
                startActivity(intent); // Bắt đầu Activity mới
            }
        });
    }

//    private void createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = "channel_1";
//            String description = "channel_description";
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel("CHANNEL_1", name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }
//
//
//    private void connectWebSocket() {
//        URI uri;
//        try {
//            uri = new URI("ws://192.168.1.6:8000/ws/notification/broadcast/");
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//            return;
//        }
//
//        client = new WebSocketClient(uri) {
//            @Override
//            public void onOpen() {
//                Log.d("WebSocket", "Connected to server");
//            }
//
//            @Override
//            public void onTextReceived(String s) {
//                Log.d("d", s);
//                showNotification(s);
//            }
//
//            @Override
//            public void onBinaryReceived(byte[] bytes) {
//                Log.d("btte", "d");
//            }
//
//            @Override
//            public void onPingReceived(byte[] bytes) {
//                Log.d("Ping", "d");
//            }
//
//            @Override
//            public void onPongReceived(byte[] bytes) {
//                Log.d("Pong", "d");
//            }
//
//            @Override
//            public void onException(Exception e) {
//                Log.d("exp", "d");
//            }
//
//            @Override
//            public void onCloseReceived(int i, String s) {
//                Log.d("close", "d");
//            }
//
//
//        };
//
//        client.connect();
//    }
//
//    private void showNotification(String message) {
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(gggg.this,
//                "CHANNEL_1")
//                .setContentTitle("Thong Bao")
//                .setContentText(message)
//                .setSmallIcon(R.drawable.load_icon)
//                .setColor(Color.RED)
//                .setCategory(NotificationCompat.CATEGORY_ALARM)
//                .setDefaults(NotificationCompat.DEFAULT_SOUND);
//
//        // Kiểm tra quyền trước khi hiển thị thông báo
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.USE_FULL_SCREEN_INTENT) == PackageManager.PERMISSION_GRANTED) {
//            // Nếu quyền đã được cấp, hiển thị thông báo
//            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
//            managerCompat.notify(notificationId, builder.build());
//        } else {
//            // Nếu quyền chưa được cấp, yêu cầu quyền từ người dùng
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(getApplicationContext(),"dd",Toast.LENGTH_LONG).show();
//                }
//            });
//        }
//
//
//
//    }


}
