package com.example.myapplication.controller.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.apdater.ListProcessUserAdapter;
import com.example.myapplication.dto.ApiResponse;
import com.example.myapplication.dto.ErrorResponse;
import com.example.myapplication.dto.response.AssigmentResponse;
import com.example.myapplication.dto.response.AssigmentUserResponse;
import com.example.myapplication.dto.response.AssignmentManagerResponse;
import com.example.myapplication.dto.response.ProcessUserResponse;
import com.example.myapplication.dto.response.StatusAssigmentAndUserManageResponse;
import com.example.myapplication.dto.response.StatusAssigmentOfTeamManageResponse;
import com.example.myapplication.entity.ProcessUser;
import com.example.myapplication.service.ListProcessUserInterFace;
import com.example.myapplication.service.ServiceImpl.ListProcessUserImpl;
import com.example.myapplication.utils.ExcelUltil;
import com.example.myapplication.utils.LocalDateTimeAdapter;
import com.example.myapplication.utils.LocalDateTimeUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableAllProcessActivity extends Activity {
    private RecyclerView recyclerViewLProcessUser;
    private String token;
    private String domain;
    private ListProcessUserAdapter listProcessUserAdapter;
    private List<ProcessUser> dataList;
    private  ImageView edit;
    private ImageView export_tableAllProcess;
    private Spinner dropdownMenu;
    private Integer idAssignment;
    private RequestQueue mRequestQueue;
    private List<String> options;
    private ArrayAdapter<String> adapter;
    private  StatusAssigmentOfTeamManageResponse statusAssigmentOfTeamManageResponse;
    private LinearLayout startAt_tableAllProcess,endAt_tableAllProcess;
    private TextView inputStartDate_tableAllProcess,inputStartTime_tableAllProcess,inputEndDate_tableAllProcess,inputEndTime_tableAllProcess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tableallprocess);
        try{
            checkLogin();
            startAt_tableAllProcess=findViewById(R.id.startAt_tableAllProcess);
            endAt_tableAllProcess=findViewById(R.id.endAt_tableAllProcess);
            options=new ArrayList<>();
            domain= getResources().getString(R.string.domain);
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
            dropdownMenu=findViewById(R.id.dropdown_menu_tableAllProcess);
            options.add("All Assigment");
            adapter = new ArrayAdapter<>(TableAllProcessActivity.this, android.R.layout.simple_spinner_item, options);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dropdownMenu.setAdapter(adapter);
            inputStartDate_tableAllProcess=findViewById(R.id.inputStartDate_tableAllProcess);
            inputStartTime_tableAllProcess =findViewById(R.id.inputStartTime_tableAllProcess);
            inputEndDate_tableAllProcess =findViewById(R.id.inputEndDate_tableAllProcess);
            inputEndTime_tableAllProcess =findViewById(R.id.inputEndTime_tableAllProcess);
            dataList=new ArrayList<>();
            getAssignmentManager();
            recyclerViewLProcessUser= findViewById(R.id.recyclerViewListProcessUser);
            recyclerViewLProcessUser.setLayoutManager(new LinearLayoutManager(this));
            listProcessUserAdapter = new ListProcessUserAdapter(this, dataList);
            listProcessUserAdapter.setOnItemClickListener(new ListProcessUserAdapter.OnItemListProcessUserClickListener() {
                @Override
                public void onItemClick(ProcessUser processUser, int potition) {
//                    Intent intent = new Intent(TableAllProcessActivity.this, UpdateAssigmentActivity.class);
//                    startActivity(intent);
                }
            });
            recyclerViewLProcessUser.setAdapter(listProcessUserAdapter);



             edit = findViewById(R.id.edit_tableAllProcess);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TableAllProcessActivity.this, UpdateAssigmentManageActivity.class);
                    startActivity(intent); // Bắt đầu Activity mới
                }
            });
            export_tableAllProcess=findViewById(R.id.export_tableAllProcess);
            export_tableAllProcess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ExcelUltil().createExcelAndDownload(TableAllProcessActivity.this,statusAssigmentOfTeamManageResponse,"hello.xlsx");
                }
            });
            getAssignmentTeam();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void checkLogin(){
        // Đọc token từ SharedPreferences
        SharedPreferences sharedPref = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        token = sharedPref.getString("Token", null);
        if(token == null){
            finish();
            Intent intent = new Intent(TableAllProcessActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private  void clearToken(){
        SharedPreferences sharedPref = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("Token");
        editor.apply();
        finish();
        Intent intent = new Intent(TableAllProcessActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void getAllProcess(Integer id)
    {
        edit.setVisibility(View.VISIBLE);
        startAt_tableAllProcess.setVisibility(View.VISIBLE);
        endAt_tableAllProcess.setVisibility(View.VISIBLE);

        ListProcessUserImpl listAssigmentUser = new ListProcessUserImpl(new ListProcessUserInterFace() {
            @Override
            public void onSuccess(ProcessUserResponse result) {
                dataList.clear();
                for (AssigmentUserResponse response : result.getUserStatus())
                    dataList.add(new ProcessUser(response));
                listProcessUserAdapter.notifyDataSetChanged();
                LocalDateTime startTime = result.getStartAt();
                LocalDateTime endTime = result.getEndAt();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        inputEndTime_tableAllProcess.setText(new LocalDateTimeUtils(endTime).getTime());
                        inputEndDate_tableAllProcess.setText(new LocalDateTimeUtils(endTime).getDate());
                        inputStartDate_tableAllProcess.setText(new LocalDateTimeUtils(startTime).getDate());
                        inputStartTime_tableAllProcess.setText(new LocalDateTimeUtils(startTime).getTime());
//                        inputtableAllProcess_tableAllProcess.setText(result.getNameAssignment());
                    }
                });

            }

            @Override
            public void onErrorResponse(ErrorResponse<?> errorResponse) {
                clearToken();
            }

            @Override
            public void onError(String error) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Lỗi Mạng",Toast.LENGTH_LONG).show();
                    }
                });

            }
        },domain,token,id);
        listAssigmentUser.getAll(TableAllProcessActivity.this);
    }

    private void getAssignmentManager() {

        String idTeam = "1";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, domain + "/Assignment/Team/" + idTeam, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("Data",response.toString());
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
                            options.clear();
                            options.add("All Assigment");
                            for (int i = 0; i < assignments.size(); i++) {
                                options.add( assignments.get(i).getNameAssignment());
                            }

                            // Tạo ArrayAdapter từ mảng String
                            adapter.notifyDataSetChanged();

                            // Lắng nghe sự kiện chọn của spinner
                            dropdownMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    // Lấy id của nhiệm vụ khi được chọn
                                    if(position!=0){
                                        Integer selectedAssignmentId = assignments.get(position-1).getIdAssignment();
                                        idAssignment = selectedAssignmentId;
                                        Toast.makeText(getApplicationContext(), "Selected assignment id: " + selectedAssignmentId, Toast.LENGTH_SHORT).show();
                                        getAllProcess(idAssignment);
                                    }else getAssignmentTeam();
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

    private void getAssignmentTeam() {
        edit.setVisibility(View.INVISIBLE);
        startAt_tableAllProcess.setVisibility(View.GONE);
        endAt_tableAllProcess.setVisibility(View.GONE);

        String idTeam = "1";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, domain + "/Assignment/TeamStatusAssigmentManage/" + idTeam, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("Data",response.toString());
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
                            Gson gson = gsonBuilder.create();
                            Type responseType = new TypeToken<ApiResponse<StatusAssigmentOfTeamManageResponse>>(){}.getType();
                            ApiResponse<StatusAssigmentOfTeamManageResponse> apiResponse = gson.fromJson(response.toString(), responseType);
                            StatusAssigmentOfTeamManageResponse assigmentResponse = apiResponse.getData();
                            statusAssigmentOfTeamManageResponse=assigmentResponse;
                            List<StatusAssigmentAndUserManageResponse> statusAssigmentOfTeamManageResponseList =assigmentResponse.getStatusAssigmentAndUser();
                            dataList.clear();

                            for(StatusAssigmentAndUserManageResponse statusAssigmentOfTeamManageResponse:statusAssigmentOfTeamManageResponseList){
                                dataList.add(new ProcessUser(statusAssigmentOfTeamManageResponse.getIdUser(),statusAssigmentOfTeamManageResponse
                                        .getIdAssigmentUser(),statusAssigmentOfTeamManageResponse.getUsername(),statusAssigmentOfTeamManageResponse.getStatus(),statusAssigmentOfTeamManageResponse.getProcess()));
                            }
                            listProcessUserAdapter.notifyDataSetChanged();

                        } catch (Exception e) {
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
