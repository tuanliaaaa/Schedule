package com.example.myapplication.controller.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.myapplication.R;

public class gggg extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupmess);
        LinearLayout linearLayout = findViewById(R.id.ahihi);
        ImageView i= findViewById(R.id.footerImgIcon_updateAssigment);


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
}
