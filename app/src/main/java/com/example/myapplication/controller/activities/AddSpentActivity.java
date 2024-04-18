package com.example.myapplication.controller.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.R;

public class AddSpentActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addspent);
        ImageView saved = findViewById(R.id.save_addSpent);
        Spinner dropdownMenu = findViewById(R.id.dropdown_menu);
        ImageView loadIcon_addSpend =findViewById(R.id.loadIcon_addSpend);
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        // Set animation properties
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(Animation.INFINITE); // Infinite rotation
        rotateAnimation.setDuration(2000); // 2 seconds for each rotation

        // Start the animation
        loadIcon_addSpend.startAnimation(rotateAnimation);

        new Thread(new Runnable() {
            @Override
            public void run() {
                // Thực hiện công việc nền ở đây
                try {
                    // Giả định thực hiện công việc nền trong 3 giây
                    Thread.sleep(3000);
//
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                           LinearLayout l = findViewById(R.id.loading_AddSpend);
                           l.setVisibility(View.INVISIBLE);
                           ScrollView s =findViewById(R.id.scrollviewcontent_addSpent);
                           s.setVisibility(View.VISIBLE);
                        }
                    });

                } catch (InterruptedException e) {
                    System.out.println("ng");
                }

                // Sau khi công việc nền hoàn thành, cập nhật giao diện người dùng

            }
        }).start();
// Tạo một mảng dữ liệu cho các lựa chọn
        String[] options = {"Option 1", "Option 2", "Option 3"};

// Tạo một ArrayAdapter từ mảng dữ liệu
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownMenu.setAdapter(adapter);

// Lắng nghe sự kiện chọn của Dropdown Menu
        dropdownMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Xử lý sự kiện khi một lựa chọn được chọn
                String selectedOption = options[position];
                Toast.makeText(getApplicationContext(), "Selected: " + selectedOption, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Xử lý khi không có lựa chọn nào được chọn
            }
        });
        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddSpentActivity.this, TableAllSpentActivity.class);
                startActivity(intent); // Bắt đầu Activity mới
            }
        });
    }
}
