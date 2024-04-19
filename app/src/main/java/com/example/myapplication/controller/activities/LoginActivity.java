package com.example.myapplication.controller.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.VolleyError;
import com.example.myapplication.R;
import com.example.myapplication.dto.ErrorResponse;
import com.example.myapplication.dto.request.LoginRequest;
import com.example.myapplication.dto.response.LoginResponse;
import com.example.myapplication.service.ServiceImpl.LoginImpl;
import com.example.myapplication.service.LoginInterFace;

public class LoginActivity extends Activity {
    private Button loginButton;
    private String domain;
    private EditText username;
    private EditText password;
    private ImageView loadIcon_login;
    private LinearLayout loading_login;
    private TextView loginError;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        try {
            loginError =findViewById(R.id.loginError);
            loading_login = findViewById(R.id.loading_login);
            loading_login.setVisibility(View.INVISIBLE);
            loadIcon_login = findViewById(R.id.loadIcon_login);
            RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);

            // Set animation properties
            rotateAnimation.setInterpolator(new LinearInterpolator());
            rotateAnimation.setRepeatCount(Animation.INFINITE); // Infinite rotation
            rotateAnimation.setDuration(2000); // 2 seconds for each rotation

            // Start the animation
            loadIcon_login.startAnimation(rotateAnimation);

            domain = getResources().getString(R.string.domain);

            loginButton = findViewById(R.id.loginButton);
            username = findViewById(R.id.username);
            password = findViewById(R.id.password);
            username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    loginError.setVisibility(View.INVISIBLE);
                }
            });
            password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    loginError.setVisibility(View.INVISIBLE);
                }
            });
            password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        login();
                    }
                    return false; // Cho phép xử lý mặc định nếu không phải nút "tiếp"
                }
            });
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login();
                }
            });
        } catch (Exception e) {
            Log.i("Error", "Lỗi  Login Activity");
        }

    }

    private void login() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loading_login.setVisibility(View.VISIBLE);
            }
        });
        LoginImpl volleyManager = new LoginImpl(new LoginInterFace() {
            @Override
            public void onSuccess(LoginResponse result) {
                Log.d("Data",result.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Login success", Toast.LENGTH_SHORT).show();
                    }
                });
                SharedPreferences sharedPref = LoginActivity.this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                sharedPref.edit().putString("Token", result.getToken()).apply();
                finish();
                Intent intent = new Intent(LoginActivity.this, gggg.class);
                startActivity(intent); // Bắt đầu Activity mới
            }

            @Override
            public void onError(String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading_login.setVisibility(View.INVISIBLE);
                        loginError.setText(error);
                        loginError.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "Lỗi mạng", Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void onErrorResponse(ErrorResponse<?> error){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading_login.setVisibility(View.INVISIBLE);
                        loginError.setText("thông tin đăng nhập không chính xác");
                        loginError.setVisibility(View.VISIBLE);
                    }
                });
            }
        }, domain);
        volleyManager.postDataToUrl(LoginActivity.this, new LoginRequest(username.getText().toString(), password.getText().toString()));
    }
}
