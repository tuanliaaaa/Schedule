package com.example.myapplication.controller.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.dto.ApiResponse;
import com.example.myapplication.dto.ErrorResponse;
import com.example.myapplication.dto.response.AssigmentResponse;
import com.example.myapplication.dto.response.AssignmentManagerResponse;
import com.example.myapplication.dto.response.TeamCreateResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddSpentActivity extends Activity {
    private TextView inputSpentName_addSpent;
    private Spinner dropdownMenu;
    private TextView inputMoney_addSpent;
    private String domain;
    private TextView inputDescription_addSpent;
    private ScrollView scrollviewcontent_addSpent;
    private String token;
    private Integer idTeam;
    private RequestQueue mRequestQueue;
    private Integer idAssignment;

    private boolean isClicked = false;
    private TextView inputRefundDate;
    private LinearLayout loading_AddSpend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idTeam=getIntent().getIntExtra("idTeam",0);
        setContentView(R.layout.activity_addspent);
        ImageView saved = findViewById(R.id.save_addSpent);
        dropdownMenu = findViewById(R.id.dropdown_menu);
        ImageView loadIcon_addSpend =findViewById(R.id.loadIcon_addSpend);
        inputRefundDate = findViewById(R.id.inputRefundDate);

        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        inputRefundDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isClicked) {
                    isClicked=true;
                    showStartDatePickerDialog();
                }
            }
        });
        try {
            checkLogin();
            loading_AddSpend=findViewById(R.id.loading_AddSpend);
            scrollviewcontent_addSpent=findViewById(R.id.scrollviewcontent_addSpent);
            domain= getResources().getString(R.string.domain);
             saved = findViewById(R.id.save_addSpent);
            dropdownMenu = findViewById(R.id.dropdown_menu);
             loadIcon_addSpend =findViewById(R.id.loadIcon_addSpend);
             rotateAnimation = new RotateAnimation(0, 360,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);

            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
            // Set animation properties
            rotateAnimation.setInterpolator(new LinearInterpolator());
            rotateAnimation.setRepeatCount(Animation.INFINITE); // Infinite rotation
            rotateAnimation.setDuration(2000); // 2 seconds for each rotation

            // Start the animation
            loadIcon_addSpend.startAnimation(rotateAnimation);
            getAssignmentManager();
            saved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postSpent();

                }
            });
        }catch (Exception e){
            Log.e("Errors"," Lỗi Tổng ở AddSpentActivity");
        }

    }

    public void postSpent() {
        try{
            loading_AddSpend.setVisibility(View.VISIBLE);
            scrollviewcontent_addSpent.setVisibility(View.INVISIBLE);
            inputSpentName_addSpent =findViewById(R.id.inputSpentName_addSpent);
            inputDescription_addSpent = findViewById(R.id.inputDescription_addSpent);
            inputMoney_addSpent = findViewById(R.id.inputMoney_addSpent);
            dropdownMenu = findViewById(R.id.dropdown_menu);
            String nameSpent = String.valueOf(inputSpentName_addSpent.getText());
            int costExpected= Integer.parseInt(String.valueOf(inputMoney_addSpent.getText()));


            // Định dạng của chuỗi
            inputRefundDate = findViewById(R.id.inputRefundDate);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate refundDate = LocalDate.parse(inputRefundDate.getText(), formatter);


            JSONObject jsonBody = new JSONObject();

            try {
                jsonBody.put("costName", nameSpent);
                jsonBody.put("price", costExpected);
                jsonBody.put("refundDate", refundDate);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, domain+"/Cost/Assigment/" + idAssignment, jsonBody,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Xử lý phản hồi thành công
                                Log.i("success", "in onResponse");
                                Gson gson = new Gson();
                                try{
                                    Log.d("Data",response.toString());
                                    Type responseType = new TypeToken<ApiResponse<TeamCreateResponse>>(){}.getType();
                                    ApiResponse<TeamCreateResponse> apiResponse = gson.fromJson(response.toString(), responseType);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(),"Thêm chi phí thành công thành công", Toast.LENGTH_LONG).show();
                                            finish();

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

                // Đặt retry policy
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                mRequestQueue.add(jsonObjectRequest);

            } catch (JSONException e) {
                Log.e("Error","không đúng định dạng Json");
            }
        }catch (Exception e){
            loading_AddSpend.setVisibility(View.INVISIBLE);
            scrollviewcontent_addSpent.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(),"Lỗi giá trị nhập vào",Toast.LENGTH_LONG).show();
            Log.e("errors",e.toString());
        }
    }

    private void checkLogin(){
        SharedPreferences sharedPref = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        token = sharedPref.getString("Token", null);
        if(token == null){
            finish();
            Intent intent = new Intent(AddSpentActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
    private  void clearToken(){
        SharedPreferences sharedPref = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("Token");
        editor.apply();
        finish();
        Intent intent = new Intent(AddSpentActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void getAssignmentManager() {
        // Tạo request để lấy danh sách các nhiệm vụ
        domain= getResources().getString(R.string.domain);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, domain + "/Assignment/Team/" + idTeam, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Parse dữ liệu JSON và tạo danh sách các nhiệm vụ
                            List<AssignmentManagerResponse> assignments = new ArrayList<>();
                            JSONArray data = response.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject assignmentJson = data.getJSONObject(i);
                                AssignmentManagerResponse assignment = new AssignmentManagerResponse();
                                assignment.setIdAssignment(assignmentJson.getInt("idAssignment"));
                                assignment.setNameAssignment(assignmentJson.getString("teamName"));
                                assignments.add(assignment);
                            }

                            // Tạo mảng String chứa tên của các nhiệm vụ
                            String[] options = new String[assignments.size()];
                            for (int i = 0; i < assignments.size(); i++) {
                                options[i] = assignments.get(i).getNameAssignment();
                            }

                            // Tạo ArrayAdapter từ mảng String
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(AddSpentActivity.this, android.R.layout.simple_spinner_item, options);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            dropdownMenu.setAdapter(adapter);
                            loading_AddSpend.setVisibility(View.INVISIBLE);

                            scrollviewcontent_addSpent.setVisibility(View.VISIBLE);
                            // Lắng nghe sự kiện chọn của spinner
                            dropdownMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    // Lấy id của nhiệm vụ khi được chọn
                                    Integer selectedAssignmentId = assignments.get(position).getIdAssignment();
                                    idAssignment = selectedAssignmentId;
                                    Toast.makeText(getApplicationContext(), "Selected assignment id: " + selectedAssignmentId, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                    // Xử lý khi không có nhiệm vụ nào được chọn
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Xử lý lỗi khi gửi request
                        Log.e("Error", "Error in request: " + error.toString());
                        Toast.makeText(getApplicationContext(), "Error in request", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // Thêm token vào header
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        // Đặt retry policy cho request
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Thêm request vào hàng đợi
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
                    inputRefundDate.setText(String.format("%02d/%02d/%04d", dayOfMonth,month+1, year));
                    isClicked=false;
                }
            }, year, month, dayOfMonth);

            // Hiển thị DatePickerDialog
            datePickerDialog.show();
        }catch (Exception e){
            Log.e("Error","Lỗi ở onclick inputStartDate");
            Toast.makeText(getApplicationContext(),"không thể mở hộp thoại date",Toast.LENGTH_LONG).show();
        }
    }
}
