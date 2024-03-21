package com.example.myapplication.controller.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.myapplication.R;

public class TableAllProcessActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tableallprocess);
        ImageView exportReport = findViewById(R.id.edit_tableAllProcess);
        exportReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TableAllProcessActivity.this, AssigmentActivity.class);
                startActivity(intent); // Bắt đầu Activity mới
            }
        });
    }
}
