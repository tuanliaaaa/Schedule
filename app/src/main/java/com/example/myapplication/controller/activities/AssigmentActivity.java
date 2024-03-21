package com.example.myapplication.controller.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.myapplication.R;

public class AssigmentActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigment);
        ImageView linearLayout = findViewById(R.id.save_assigment);

        // Thiết lập sự kiện lắng nghe cho LinearLayout
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent để chuyển từ MainActivity sang Activity mới
                Intent intent = new Intent(AssigmentActivity.this, TableAllProcessActivity.class);
                startActivity(intent); // Bắt đầu Activity mới
            }
        });
    }
}
