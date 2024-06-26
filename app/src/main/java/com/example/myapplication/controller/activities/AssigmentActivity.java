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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.myapplication.apdater.RecommendUserAdapter;
import com.example.myapplication.dto.ApiResponse;
import com.example.myapplication.dto.ErrorResponse;
import com.example.myapplication.dto.request.AssignmentCreateRequest;
import com.example.myapplication.dto.request.Team;
import com.example.myapplication.dto.response.AssignmentResponse;
import com.example.myapplication.dto.response.RecommendUserResponse;
import com.example.myapplication.dto.response.TeamCreateResponse;
import com.example.myapplication.entity.UserRcm;
import com.example.myapplication.service.ListUserInTeamInterFace;
import com.example.myapplication.service.RecommendUserInterFace;
import com.example.myapplication.service.ServiceImpl.ListUserInTeamImpl;
import com.example.myapplication.service.ServiceImpl.RecommendUserImpl;
import com.example.myapplication.utils.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssigmentActivity extends Activity {
    private EditText inputAssignment_assignment;
    private String token;
    private EditText inputForPeople_assignment;
    private TextView inputStartDate_assignment;
    private TextView inputStartTime_assignment;
    private TextView inputEndDate_assignment;
    private TextView inputEndTime_assignment;
    private EditText inputDescription_assignment;
    private RecyclerView recyclerViewRcmuser;
    private RecommendUserAdapter recommendUserAdapter;

    private ImageView loadIcon_assigment;
    private LinearLayout loading_assigment;
    private ScrollView scrollviewcontent_assigment;
    private RotateAnimation rotateAnimation;

    private String domain;
    private boolean isClickedStartDay = false,isClickedEndDay=false,isClickedStartTime=false,isClickedEndTime=false;

    private RequestQueue mRequestQueue;
    private List<UserRcm> dataList;
    private List<Integer> userList=new ArrayList<>();
    private Integer idTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigment);
        try{
            idTeam=getIntent().getIntExtra("idTeam",0);
            checkLogin();
            loading_assigment = findViewById(R.id.loading_assigment);
            scrollviewcontent_assigment =findViewById(R.id.scrollviewcontent_assigment);
            loadIcon_assigment =findViewById(R.id.loadIcon_assigment);
            rotateAnimation = new RotateAnimation(0, 360,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);

            // Set animation properties
            rotateAnimation.setInterpolator(new LinearInterpolator());
            rotateAnimation.setRepeatCount(Animation.INFINITE); // Infinite rotation
            rotateAnimation.setDuration(2000); // 2 seconds for each rotation
            loadIcon_assigment.startAnimation(rotateAnimation);
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
            domain= getResources().getString(R.string.domain);
            ImageView linearLayout = findViewById(R.id.save_assigment);
            inputForPeople_assignment = findViewById(R.id.inputForPeople_assigment);
            inputAssignment_assignment = findViewById(R.id.inputAssignment_assigment);
            inputDescription_assignment = findViewById(R.id.inputDescription_assigment);
            inputStartDate_assignment = findViewById(R.id.inputStartDate_assigment);
            inputStartTime_assignment = findViewById(R.id.inputStartTime_assigment);
            inputEndDate_assignment = findViewById(R.id.inputEndDate_assigment);
            inputEndTime_assignment = findViewById(R.id.inputEndTime_assigment);
//            inputForPeople_assignment = findViewById(R.id.inputAddPeople_createGroup);
            recyclerViewRcmuser= findViewById(R.id.recyclerViewUserList);
            recyclerViewRcmuser.setLayoutManager(new LinearLayoutManager(this));
            dataList = new ArrayList<>();

            recommendUserAdapter = new RecommendUserAdapter(this, dataList);
            recommendUserAdapter.setOnItemClickListener(new RecommendUserAdapter.OnItemRcmUserClickListener() {
                @Override
                public void onItemClick(UserRcm userRcm,int potition) {
                    if(!inputForPeople_assignment.getText().toString().trim().equals(""))
                        inputForPeople_assignment.setText(inputForPeople_assignment.getText()+", "+userRcm.getUsername());
                    else inputForPeople_assignment.setText(userRcm.getUsername());
                    dataList.get(potition).setStatus("1");
                    userList.add(dataList.get(potition).getIdUser());
                    recommendUserAdapter.notifyDataSetChanged();
                }
            });

            recyclerViewRcmuser.setAdapter(recommendUserAdapter);
            getSuggestUser();
            // click startDay
            inputStartDate_assignment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isClickedStartDay) {
                        isClickedStartDay=true;
                        showStartDatePickerDialog();

                    }
                }
            });
            // click EndDay
            inputEndDate_assignment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isClickedEndDay) {
                        isClickedEndDay=true;
                        showEndDatePickerDialog();

                    }
                }
            });

            //click StartTime
            inputStartTime_assignment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isClickedStartTime){
                        isClickedStartTime=true;
                        showStartTimePickerDialog();
                    }
                }
            });

            //click EndTime
            inputEndTime_assignment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isClickedEndTime){
                        isClickedEndTime=true;
                        showEndTimePickerDialog();
                    }
                }
            });

            // Click to button save
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postAssignment();
                }
            });
        }catch (Exception e){
            Log.e("Errors"," Lỗi Tổng ở AssigmentActivity");
        }
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
                    inputStartDate_assignment.setText(String.format("%02d/%02d/%04d", dayOfMonth,month+1, year));
                    isClickedStartDay=false;
                }
            }, year, month, dayOfMonth);

            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
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
                    inputEndDate_assignment.setText(String.format("%02d/%02d/%04d", dayOfMonth,month+1, year));
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
                    inputStartTime_assignment.setText( String.format("%02d:%02d", hourOfDay, minute));
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
                    inputEndTime_assignment.setText(String.format("%02d:%02d", hourOfDay, minute));
                    isClickedEndTime=false;
                }
            }, hourOfDay, minute, false); // Tham số cuối cùng là kiểu 24 giờ (true) hoặc 12 giờ (false)

            timePickerDialog.show();
        }catch (Exception e){
            Log.e("Error","Lỗi ở onclick inputEndTime");
            Toast.makeText(getApplicationContext(),"không thể mở hộp thoại Time",Toast.LENGTH_LONG).show();
        }
    }



    public  void postAssignment(){
        loading_assigment.setVisibility(View.VISIBLE);
        try{
            JSONObject jsonBody = new JSONObject();


            String assignment = String.valueOf(inputAssignment_assignment.getText());
//            Toast.makeText(getApplicationContext(),assignment,Toast.LENGTH_LONG).show();
            // Lấy mô tả từ input

            String description = String.valueOf(inputDescription_assignment.getText());

            // Lấy thời gian bắt đầu và kết thúc từ input

            LocalDate startDate = LocalDate.parse(inputStartDate_assignment.getText().toString(),DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalTime startTime = LocalTime.parse(inputStartTime_assignment.getText().toString());
            LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
            Toast.makeText(getApplicationContext(),userList.toString(),Toast.LENGTH_LONG).show();


            LocalDate endDate = LocalDate.parse(inputEndDate_assignment.getText().toString(),DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalTime endTime = LocalTime.parse(inputEndTime_assignment.getText().toString());
            LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);
            try {
                // Đưa dữ liệu vào JSONObject
                jsonBody.put("startAt", startDateTime);
                jsonBody.put("endAt", endDateTime);
                jsonBody.put("description", description);
                jsonBody.put("usersId", new JSONArray(userList));
                jsonBody.put("nameAssignment",assignment);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, domain+"/Assignment/Team/"+String.valueOf(idTeam),
                        jsonBody,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("success", "in onResponse");
                                Log.d("Data",response.toString());
                                GsonBuilder gsonBuilder = new GsonBuilder();
                                gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
                                Gson gson = gsonBuilder.create();
                                try{
                                    Type responseType = new TypeToken<ApiResponse<AssignmentResponse>>(){}.getType();
                                    ApiResponse<AssignmentResponse> apiResponse = gson.fromJson(response.toString(), responseType);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "Thêm Assigment Thành công", Toast.LENGTH_LONG).show();
                                            finish();
                                            Intent intent = new Intent(AssigmentActivity.this, TableAllProcessActivity.class);
                                            intent.putExtra("idTeam",idTeam);
                                            startActivity(intent);
                                        }
                                    });
                                }catch (Exception e){
                                    Log.e("Error","Giải mã sai định dạng trả về");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
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
                                loading_assigment.setVisibility(View.INVISIBLE);

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
                Log.e("Error","không đúng định dạng Json");
            }
        }catch (Exception e){
            Log.e("errors",e.toString());
            Toast.makeText(getApplicationContext(),"Lỗi nhập thông tin",Toast.LENGTH_LONG).show();
            loading_assigment.setVisibility(View.INVISIBLE);
        }
    }


    private void checkLogin(){
        SharedPreferences sharedPref = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        token = sharedPref.getString("Token", null);
        if(token == null){
            finish();
            Intent intent = new Intent(AssigmentActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
    private  void clearToken(){
        SharedPreferences sharedPref = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("Token");
        editor.apply();
        finish();
        Intent intent = new Intent(AssigmentActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void getSuggestUser(){
        ListUserInTeamImpl recommendUser = new ListUserInTeamImpl(new ListUserInTeamInterFace() {
            @Override
            public void onSuccess(List<RecommendUserResponse> result) {
                dataList.clear();
                for(RecommendUserResponse response:result)
                    dataList.add(new UserRcm(response));
                recommendUserAdapter.notifyDataSetChanged();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        loading_createGroup.setVisibility(View.INVISIBLE);
                    }
                });
            }

            @Override
            public void onErrorResponse(VolleyError errorResponse) {
                clearToken();
            }

            @Override
            public void onError(String error) {
                clearToken();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Lỗi",Toast.LENGTH_LONG).show();
                    }
                });

            }
        },domain,token,idTeam);
        recommendUser.getAllUser(AssigmentActivity.this);

    }


}
