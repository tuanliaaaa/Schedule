package com.example.myapplication.controller.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.myapplication.R;

public class AccountFeatureActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_feature);
        LinearLayout toAssigment = findViewById(R.id.addAssigment_accountFeature);
        LinearLayout toManageSpent = findViewById(R.id.manageSpent_accountFeature);
        LinearLayout toManageProcessWork = findViewById(R.id.managePocessWork_accountFeature);

        toManageSpent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountFeatureActivity.this, ChartMoneyManageActivity.class);
                startActivity(intent);
            }
        });
        toAssigment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent để chuyển từ MainActivity sang Activity mới
                Intent intent = new Intent(AccountFeatureActivity.this, AssigmentActivity.class);
                startActivity(intent); // Bắt đầu Activity mới
            }
        });
        toManageProcessWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountFeatureActivity.this, TableAllProcessActivity.class);
                startActivity(intent);
            }
        });
    }
}
