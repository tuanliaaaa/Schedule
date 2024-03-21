package com.example.myapplication.controller.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.R;

public class TableAllSpentActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tableallspent);
        Button linearLayout1 = findViewById(R.id.buttonAddSpent_tableAllSpent);

        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TableAllSpentActivity.this, AddSpentActivity.class);
                startActivity(intent); // Bắt đầu Activity mới
            }
        });
    }

}
