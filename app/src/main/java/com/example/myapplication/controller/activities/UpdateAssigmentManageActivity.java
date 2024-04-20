package com.example.myapplication.controller.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.dto.ApiResponse;
import com.example.myapplication.dto.ErrorResponse;
import com.example.myapplication.dto.response.AssigmentResponse;
import com.example.myapplication.utils.LocalDateTimeAdapter;
import com.example.myapplication.utils.LocalDateTimeUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UpdateAssigmentManageActivity extends Activity {
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private Context context;
    private ImageView loadIcon_updateAssigmentManage;
    private LinearLayout loading_updateAssigmentManage;
    private ScrollView scrollviewcontent_updateAssigmentManage;
    private String domain;
    private String token;
    private boolean isClickedStartDay = false,isClickedEndDay=false,isClickedStartTime=false,isClickedEndTime=false;
    private EditText inputDescription_updateAssigmentManage;
    private TextView inputStartDate_updateAssigmentManage,inputEndDate_updateAssigmentManage,inputStartTime_updateAssigmentManage,inputEndTime_updateAssigmentManage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateassigmentmanage);
        try{
            checkLogin();
            loading_updateAssigmentManage = findViewById(R.id.loading_updateAssigmentManage);
            scrollviewcontent_updateAssigmentManage =findViewById(R.id.scrollviewcontent_updateAssigmentManage);
            loadIcon_updateAssigmentManage =findViewById(R.id.loadIcon_updateAssigmentManage);
            RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);

            // Set animation properties
            rotateAnimation.setInterpolator(new LinearInterpolator());
            rotateAnimation.setRepeatCount(Animation.INFINITE); // Infinite rotation
            rotateAnimation.setDuration(2000); // 2 seconds for each rotation

            // Start the animation
            loadIcon_updateAssigmentManage.startAnimation(rotateAnimation);
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
            domain= getResources().getString(R.string.domain);
            getAssigment();

            inputStartDate_updateAssigmentManage = findViewById(R.id.inputStartDate_updateAssigmentManage);
            inputStartTime_updateAssigmentManage = findViewById(R.id.inputStartTime_updateAssigmentManage);
            inputEndDate_updateAssigmentManage = findViewById(R.id.inputEndDate_updateAssigmentManage);
            inputEndTime_updateAssigmentManage = findViewById(R.id.inputEndTime_updateAssigmentManage);

            // click startDay
            inputStartDate_updateAssigmentManage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isClickedStartDay) {
                        isClickedStartDay=true;
                        showStartDatePickerDialog();
                    }
                }
            });
            // click EndDay
            inputEndDate_updateAssigmentManage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isClickedEndDay) {
                        isClickedEndDay=true;
                        showEndDatePickerDialog();

                    }
                }
            });

            //click StartTime
            inputStartTime_updateAssigmentManage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isClickedStartTime){
                        isClickedStartTime=true;
                        showStartTimePickerDialog();
                    }
                }
            });

            //click EndTime
            inputEndTime_updateAssigmentManage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isClickedEndTime){
                        isClickedEndTime=true;
                        showEndTimePickerDialog();
                    }
                }
            });




            // Click to button save
            ImageView linearLayout1 = findViewById(R.id.save_updateAssigmentManage);
            linearLayout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateAssigmentManage();
                    Intent intent = new Intent(UpdateAssigmentManageActivity.this, gggg.class);
                    startActivity(intent); // Bắt đầu Activity mới
                }
            });
        }catch (Exception e){
            Log.e("Errors"," Lỗi Tổng ở updateAssigmentManageActivity");
        }

    }

    public void updateAssigmentManage(){
        try{
            JSONObject jsonBody = new JSONObject();
            loading_updateAssigmentManage.setVisibility(View.VISIBLE);

            try {
                // Đưa dữ liệu vào JSONObject
                jsonBody.put("process", "2");

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, domain+"/Assignment/AssigmentUser/18",
                        jsonBody,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("success", "in onResponse");
                                Log.d("Data",response.toString());
                                GsonBuilder gsonBuilder = new GsonBuilder();
                                gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
                                Gson gson = gsonBuilder.create();
                                try {
                                    Type responseType = new TypeToken<ApiResponse<AssigmentResponse>>(){}.getType();
                                    ApiResponse<AssigmentResponse> apiResponse = gson.fromJson(response.toString(), responseType);
                                    AssigmentResponse assigmentResponse =(AssigmentResponse) apiResponse.getData();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "Cập nhật thành công", Toast.LENGTH_LONG).show();

                                            try{
                                                inputDescription_updateAssigmentManage=findViewById(R.id.inputDescription_updateAssigmentManage);
                                                inputDescription_updateAssigmentManage.setText(assigmentResponse.getDescription());
                                                loading_updateAssigmentManage.setVisibility(View.INVISIBLE);

                                            }catch (Exception e){
                                                Log.e("Error","lỗi giao diện");
                                            }
                                        }
                                    });
                                } catch (Exception e) {
                                    Log.e("Error", "Giải mã sai định dạng trả về");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            loading_updateAssigmentManage.setVisibility(View.INVISIBLE);

                                            Toast.makeText(getApplicationContext(), "Giải mã sai định dạng trả về", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("Success", "in onErrorResponse");
                                if (error instanceof TimeoutError) {
                                    Toast.makeText(getApplicationContext(), "Request Time Out", Toast.LENGTH_LONG).show();
                                    Log.e("Error", "Request Time Out");
                                } else {
                                    Log.e("Error", error.toString());
                                    if (error.networkResponse != null && error.networkResponse.statusCode == 400) {
                                        Gson gson = new Gson();
                                        String errorResponse = new String(error.networkResponse.data);
                                        Type responseType = new TypeToken<ErrorResponse<?>>(){}.getType();
                                        ErrorResponse<?> apiResponse = gson.fromJson(errorResponse, responseType);
                                        Toast.makeText(getApplicationContext(), apiResponse.getError().toString(), Toast.LENGTH_LONG).show();
                                        Log.e("Error", "Bad request: " + apiResponse.getError());

                                    } else {
                                        Gson gson = new Gson();
                                        String errorResponse = new String(error.networkResponse.data);
                                        Type responseType = new TypeToken<ErrorResponse<?>>(){}.getType();
                                        ErrorResponse<?> apiResponse = gson.fromJson(errorResponse, responseType);
                                        Log.e("Error", "Server: " + apiResponse.getError());
                                        Toast.makeText(getApplicationContext(), apiResponse.getError().toString(), Toast.LENGTH_LONG).show();
                                    }
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        loading_updateAssigmentManage.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        // Thêm token vào header
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Authorization", "Bearer "+token);
                        return headers;
                    }
                };
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                mRequestQueue.add(jsonObjectRequest);
            } catch (JSONException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading_updateAssigmentManage.setVisibility(View.INVISIBLE);
                    }
                });
                Log.e("Error","không đúng định dạng Json");
            }
        }catch (Exception e){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loading_updateAssigmentManage.setVisibility(View.INVISIBLE);
                }
            });
            Log.e("errors",e.toString());
        }
    }

    public void getAssigment() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, domain + "/Assignment/AssigmentUser/18", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Xử lý phản hồi thành công
                        Log.i("success", "in onResponse");
                        Log.d("Data",response.toString());
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
                        Gson gson = gsonBuilder.create();
                        try {
                            Type responseType = new TypeToken<ApiResponse<AssigmentResponse>>(){}.getType();
                            ApiResponse<AssigmentResponse> apiResponse = gson.fromJson(response.toString(), responseType);
                            AssigmentResponse assigmentResponse =(AssigmentResponse) apiResponse.getData();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();

                                    try{
                                        LocalDateTimeUtils start = new LocalDateTimeUtils(assigmentResponse.getStartAt());
                                        inputStartDate_updateAssigmentManage=findViewById(R.id.inputStartDate_updateAssigmentManage);
                                        inputStartDate_updateAssigmentManage.setText(start.getDate());
                                        inputStartTime_updateAssigmentManage =findViewById(R.id.inputStartTime_updateAssigmentManage);
                                        inputStartTime_updateAssigmentManage.setText(start.getTime());
                                        LocalDateTimeUtils end = new LocalDateTimeUtils(assigmentResponse.getEndAt());
                                        inputEndDate_updateAssigmentManage = findViewById(R.id.inputEndDate_updateAssigmentManage);
                                        inputEndDate_updateAssigmentManage.setText(end.getDate());
                                        inputEndTime_updateAssigmentManage = findViewById(R.id.inputEndTime_updateAssigmentManage);
                                        inputEndTime_updateAssigmentManage.setText(end.getTime());
                                        inputDescription_updateAssigmentManage=findViewById(R.id.inputDescription_updateAssigmentManage);
                                        inputDescription_updateAssigmentManage.setText(assigmentResponse.getDescription());
                                        loading_updateAssigmentManage.setVisibility(View.INVISIBLE);
                                        scrollviewcontent_updateAssigmentManage.setVisibility(View.VISIBLE);
                                    }catch (Exception e){
                                        Log.e("Error","lỗi giao diện");
                                    }
                                }
                            });
                        } catch (Exception e) {
                            Log.e("Error", "Giải mã sai định dạng trả về");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Giải mã sai định dạng trả về", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loading_updateAssigmentManage.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Xử lý lỗi
                        Log.i("Success", "in onErrorResponse");
                        if (error instanceof TimeoutError) {
                            Toast.makeText(getApplicationContext(), "Request Time Out", Toast.LENGTH_LONG).show();
                            Log.e("Error", "Request Time Out");
                        } else {
                            Log.e("Error", error.toString());
                            if (error.networkResponse != null && error.networkResponse.statusCode == 400) {
                                Gson gson = new Gson();
                                String errorResponse = new String(error.networkResponse.data);
                                Type responseType = new TypeToken<ErrorResponse<?>>(){}.getType();
                                ErrorResponse<?> apiResponse = gson.fromJson(errorResponse, responseType);
                                Toast.makeText(getApplicationContext(), apiResponse.getError().toString(), Toast.LENGTH_LONG).show();
                                Log.e("Error", "Bad request: " + apiResponse.getError());
                            } else {
                                Gson gson = new Gson();
                                String errorResponse = new String(error.networkResponse.data);
                                Type responseType = new TypeToken<ErrorResponse<?>>(){}.getType();
                                ErrorResponse<?> apiResponse = gson.fromJson(errorResponse, responseType);
                                Log.e("Error", "Server: " + apiResponse.getError());
                                Toast.makeText(getApplicationContext(), apiResponse.getError().toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loading_updateAssigmentManage.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // Thêm token vào header
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer "+token);
                return headers;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(jsonObjectRequest);

    }

    public void showStartDatePickerDialog(){
        try{
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            // Tạo DatePickerDialog và thiết lập ngày ban đầu là ngày hiện tại
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    // Đặt ngày được chọn vào EditText
                    inputStartDate_updateAssigmentManage.setText(String.format("%02d/%02d/%04d", dayOfMonth,month, year));
                    isClickedStartDay=false;
                }
            }, year, month, dayOfMonth);

            // Hiển thị DatePickerDialog
            datePickerDialog.show();
        }catch (Exception e){
            Log.e("Error","Lỗi ở onclick inputStartDate");
            Toast.makeText(getApplicationContext(),"không thể mở hộp thoại date",Toast.LENGTH_LONG).show();
        }
    }

    public void showEndDatePickerDialog(){
        try{
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            // Tạo DatePickerDialog và thiết lập ngày ban đầu là ngày hiện tại
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    // Đặt ngày được chọn vào EditText
                    inputEndDate_updateAssigmentManage.setText(String.format("%02d/%02d/%04d", dayOfMonth,month, year));
                    isClickedEndDay=false;
                }
            }, year, month, dayOfMonth);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            // Hiển thị DatePickerDialog
            datePickerDialog.show();
        }catch (Exception e){
            Log.e("Error","Lỗi ở onclick inputEndDate");
            Toast.makeText(getApplicationContext(),"không thể mở hộp thoại date",Toast.LENGTH_LONG).show();
        }

    }

    public void showStartTimePickerDialog(){
        try{
            Calendar calendar = Calendar.getInstance();
            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    inputStartTime_updateAssigmentManage.setText( String.format("%02d:%02d", hourOfDay, minute));
                    isClickedStartTime=false;
                }
            }, hourOfDay, minute, false); // Tham số cuối cùng là kiểu 24 giờ (true) hoặc 12 giờ (false)

            timePickerDialog.show();
        }catch (Exception e){
            Log.e("Error","Lỗi ở onclick inputStartTime");
            Toast.makeText(getApplicationContext(),"không thể mở hộp thoại Time",Toast.LENGTH_LONG).show();
        }
    }

    public void showEndTimePickerDialog(){
        try{
            Calendar calendar = Calendar.getInstance();
            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    inputEndTime_updateAssigmentManage.setText(String.format("%02d:%02d", hourOfDay, minute));
                    isClickedEndTime=false;
                }
            }, hourOfDay, minute, false); // Tham số cuối cùng là kiểu 24 giờ (true) hoặc 12 giờ (false)

            timePickerDialog.show();
        }catch (Exception e){
            Log.e("Error","Lỗi ở onclick inputEndTime");
            Toast.makeText(getApplicationContext(),"không thể mở hộp thoại Time",Toast.LENGTH_LONG).show();
        }
    }

    private void checkLogin(){
        SharedPreferences sharedPref = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        token = sharedPref.getString("Token", null);
        if(token == null){
            finish();
            Intent intent = new Intent(UpdateAssigmentManageActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
    private  void clearToken(){
        SharedPreferences sharedPref = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("Token");
        editor.apply();
        finish();
        Intent intent = new Intent(UpdateAssigmentManageActivity.this, LoginActivity.class);
        startActivity(intent);
    }

}
