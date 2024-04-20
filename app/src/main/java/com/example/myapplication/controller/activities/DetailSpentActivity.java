package com.example.myapplication.controller.activities;

import android.app.Activity;
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
import com.example.myapplication.dto.response.AssignmentManagerResponse;
import com.example.myapplication.dto.response.CostResponse;
import com.example.myapplication.dto.response.TeamCreateResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailSpentActivity extends Activity {
    private TextView inputSpentName_addSpent;
    private Spinner dropdownMenu;
    private TextView inputMoney_addSpent;
    private String domain;
    private TextView inputDescription_addSpent;
    private String token;
    private RequestQueue mRequestQueue;
    private List<AssignmentManagerResponse> assignments;
    private int idAssignment;
    private LinearLayout  loading_AddSpend;
    private  ScrollView scrollviewcontent_addSpent;

    private int costId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addspent);
        ImageView saved = findViewById(R.id.save_addSpent);
        loading_AddSpend=findViewById(R.id.loading_AddSpend);
        scrollviewcontent_addSpent=findViewById(R.id.scrollviewcontent_addSpent);
        dropdownMenu = findViewById(R.id.dropdown_menu);
        ImageView loadIcon_addSpend =findViewById(R.id.loadIcon_addSpend);
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        costId = getIntent().getIntExtra("costId", -1);
        try {
            checkLogin();
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
                    domain= getResources().getString(R.string.domain);

                }
            });
        }catch (Exception e){
            Log.e("Errors"," Lỗi Tổng ở AddSpentActivity");
        }

    }

    private void getCostById(Integer idCost) {
        // Tạo request để lấy danh sách các nhiệm vụ
        domain= getResources().getString(R.string.domain);
        String idTeam = "1";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, domain + "/Cost/" + idCost, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Parse dữ liệu JSON và tạo danh sách các nhiệm vụ
                            JSONObject dataCost = response.getJSONObject("data");
                            int idCost = dataCost.getInt("idCost");
                            String codeName = dataCost.getString("costName");
                            int price = dataCost.getInt("price");
                            int idAssignment = dataCost.getInt("idassigment");

                            inputSpentName_addSpent = findViewById(R.id.inputSpentName_addSpent);
                            inputSpentName_addSpent.setText(codeName);

                            inputMoney_addSpent = findViewById(R.id.inputMoney_addSpent);
                            inputMoney_addSpent.setText(String.valueOf(price));


                            //set spinner có idAssignment

                            // Tìm vị trí của idAssignment trong danh sách
                            int positionToSelect = -1;
                            for (int i = 0; i < assignments.size(); i++) {
                                if (assignments.get(i).getIdAssignment() == idAssignment) {
                                    positionToSelect = i;
                                    break;
                                }
                            }
                            loading_AddSpend.setVisibility(View.INVISIBLE);
                            scrollviewcontent_addSpent.setVisibility(View.VISIBLE);
                            // Thiết lập Spinner để chọn mục ở vị trí có idAssignment tương ứng
                            if (positionToSelect != -1) {
                                dropdownMenu.setSelection(positionToSelect);
                            }
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

    private void checkLogin(){
        SharedPreferences sharedPref = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        token = sharedPref.getString("Token", null);
        if(token == null){
            finish();
            Intent intent = new Intent(DetailSpentActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
    private  void clearToken(){
        SharedPreferences sharedPref = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("Token");
        editor.apply();
        finish();
        Intent intent = new Intent(DetailSpentActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void getAssignmentManager() {
        // Tạo request để lấy danh sách các nhiệm vụ
        domain= getResources().getString(R.string.domain);
        String idTeam = "1";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, domain + "/Assignment/Team/" + idTeam, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Parse dữ liệu JSON và tạo danh sách các nhiệm vụ
                            assignments = new ArrayList<>();
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
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(DetailSpentActivity.this, android.R.layout.simple_spinner_item, options);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dropdownMenu.setAdapter(adapter);

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

                            getCostById(costId);

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
}
