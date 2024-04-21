package com.example.myapplication.controller.activities;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import com.android.volley.toolbox.StringRequest;
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

public class TableAllSpentActivity extends Activity {
    private RequestQueue mRequestQueue;
    private Spinner inputAssignment_TableAllSpent;
    private String token;
    private String domain;
    private Integer idTeam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tableallspent);
        idTeam=getIntent().getIntExtra("idTeam",0);

//        getAssignmentManager();
        Button linearLayout1 = findViewById(R.id.buttonAddSpent_tableAllSpent);

        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TableAllSpentActivity.this, AddSpentActivity.class);
                startActivity(intent); // Bắt đầu Activity mới
            }
        });

        try {
            checkLogin();
            getAssignmentManager();
        }catch (Exception e){
            Log.e("Errors"," Lỗi Tổng ở AddSpentActivity");
        }
    }

    private void getAssignmentManager() {
        // Tạo request để lấy danh sách các nhiệm vụ
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        domain= getResources().getString(R.string.domain);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, domain + "/Assignment/Team/" + String.valueOf(idTeam), null,
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
                            inputAssignment_TableAllSpent = findViewById(R.id.inputAssignment_TableAllSpent);
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(TableAllSpentActivity.this, android.R.layout.simple_spinner_item, options);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            inputAssignment_TableAllSpent.setAdapter(adapter);

                            // Lắng nghe sự kiện chọn của spinner
                            inputAssignment_TableAllSpent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    // Lấy id của nhiệm vụ khi được chọn
                                    Integer selectedAssignmentId = assignments.get(position).getIdAssignment();
                                    Integer idAssignment = selectedAssignmentId;
                                    Toast.makeText(getApplicationContext(), "Selected assignment id: " + selectedAssignmentId, Toast.LENGTH_SHORT).show();
                                    getCostByIdAssignment(idAssignment);
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

    private void getCostByIdAssignment(Integer idAssignment) {
        // Tạo request để lấy danh sách các nhiệm vụ
        domain= getResources().getString(R.string.domain);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, domain + "/Cost/assignment/" + idAssignment, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<CostResponse> costResponses = new ArrayList<>();
                            JSONArray data = response.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject costJson = data.getJSONObject(i);
                                CostResponse cost = new CostResponse();
                                cost.setIdCost(costJson.getInt("idCost"));
                                cost.setCostName(costJson.getString("costName"));
                                cost.setPrice(costJson.getInt("price"));
                                costResponses.add(cost);
                            }
                            populateTable(costResponses);
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
            Intent intent = new Intent(TableAllSpentActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
    private  void clearToken(){
        SharedPreferences sharedPref = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("Token");
        editor.apply();
        finish();
        Intent intent = new Intent(TableAllSpentActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void populateTable(List<CostResponse> costResponses) {
        // Xóa các hàng dữ liệu hiện tại trong TableLayout
        TableLayout tableLayout = findViewById(R.id.tableLayout);
        tableLayout.removeAllViews();

        // Thêm hàng tiêu đề
        addHeaderRowToTableLayout();

        // Thêm dữ liệu từ API vào TableLayout
        for (int i = 0; i < costResponses.size(); i++) {
            CostResponse cost = costResponses.get(i);
            addDataRowToTableLayout(i + 1, cost.getCostName(), cost.getPrice(), cost.getIdCost());
        }
    }
    // Thêm hàng tiêu đề vào TableLayout
    private void addHeaderRowToTableLayout() {
        TableLayout tableLayout = findViewById(R.id.tableLayout);

        TableRow headerRow = new TableRow(this);
        TableRow.LayoutParams headerLayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        headerRow.setLayoutParams(headerLayoutParams);

        TextView textViewNoHeader = createTextView("No.");
        TextView textViewNameHeader = createTextView("Name");
        TextView textViewPriceHeader = createTextView("Price");

        headerRow.addView(textViewNoHeader);
        headerRow.addView(textViewNameHeader);
        headerRow.addView(textViewPriceHeader);

        tableLayout.addView(headerRow);
    }

    // Thêm dữ liệu hàng vào TableLayout
    // Thêm dữ liệu hàng vào TableLayout
    private void addDataRowToTableLayout(int index, String name, int price, final int costId) {
        TableLayout tableLayout = findViewById(R.id.tableLayout);

        TableRow row = new TableRow(this);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(layoutParams);

        TextView textViewNo = createTextView(String.valueOf(index));
        TextView textViewName = createTextView(name);
        TextView textViewPrice = createTextView(String.valueOf(price));

        row.addView(textViewNo);
        row.addView(textViewName);
        row.addView(textViewPrice);

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent và truyền dữ liệu cần thiết tới activity mới
                Intent intent = new Intent(TableAllSpentActivity.this, DetailSpentActivity.class);
                intent.putExtra("costId", costId);
                startActivity(intent);
            }
        });

        tableLayout.addView(row);
    }


    // Tạo TextView mới
    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 113);
        params.setMargins(0, 0, -1, 0);
        textView.setLayoutParams(params);
        textView.setBackgroundResource(R.drawable.background_datatable);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(10, 10, 10, 10);
        textView.setText(text);
        textView.setTextColor(getResources().getColor(R.color.black));
        return textView;
    }

}