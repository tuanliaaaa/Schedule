package com.example.myapplication.controller.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.myapplication.dto.response.AssignmentResponse;
import com.example.myapplication.dto.response.CostResponse;
import com.example.myapplication.dto.response.StatusAssigmentAndUserManageResponse;
import com.example.myapplication.dto.response.TeamCostResponse;
import com.example.myapplication.dto.response.TeamCreateResponse;
import com.example.myapplication.utils.ExcelUltil;
import com.example.myapplication.utils.LocalDateAdapter;
import com.example.myapplication.utils.LocalDateTimeAdapter;
import com.example.myapplication.utils.LocalDateTimeUtils;
import com.example.myapplication.utils.ObjChartUtil;
import com.example.myapplication.view.PieChartView;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ChartMoneyManageActivity extends Activity {
    private String token;
    private String domain;
    private TextView inputStartDate_assignment;
    private TextView inputEndDate_assignment;
    private RequestQueue mRequestQueue;
    private Integer check=0,check1=0;
    private LocalDate from,to;
    private PieChartView pieChartView;
    private boolean isClickedStartDay = false,isClickedEndDay=false;
    private TextView valueActuallySpent_chartMoneyManage;
    private TextView valueEstimateSpent_ChartMoneyManage;
    private Float vlEs;
    private TextView valueMax1,valueMax2,valueMax3,valueMax4,valueMax5;
    private TextView nameMax1,nameMax2,nameMax3,nameMax4,nameMax5;
    private LinearLayout max1,max2,max3,max4,max5;
    private ImageView export_tableAllSpent;
    private Integer idTeam;
    private Map<Integer,List<CostResponse>> listMap=new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chartmoneymanage);
        idTeam=getIntent().getIntExtra("idTeam",0);
        nameMax1=findViewById(R.id.nameMax1);
        export_tableAllSpent=findViewById(R.id.export_tableAllSpent);
         nameMax2 = findViewById(R.id.nameMax2);
         nameMax3 = findViewById(R.id.nameMax3);
         nameMax4 = findViewById(R.id.nameMax4);
         nameMax5 = findViewById(R.id.nameMax5);
         valueMax1 = findViewById(R.id.valueMax1);
         valueMax2 = findViewById(R.id.valueMax2);
         valueMax3 = findViewById(R.id.valueMax3);
         valueMax4 = findViewById(R.id.valueMax4);
         valueMax5 = findViewById(R.id.valueMax5);
        max1=findViewById(R.id.max1);
        max2=findViewById(R.id.max2);
        max3=findViewById(R.id.max3);
        max4=findViewById(R.id.max4);
        max5=findViewById(R.id.max5);

        checkLogin();
        valueActuallySpent_chartMoneyManage=findViewById(R.id.valueActuallySpent_chartMoneyManage);
        valueEstimateSpent_ChartMoneyManage=findViewById(R.id.valueEstimateSpent_ChartMoneyManage);
        Button linearLayout1 = findViewById(R.id.buttonViewSpent_chartMoneyManage);
        pieChartView = findViewById(R.id.pieChartView);
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        domain= getResources().getString(R.string.domain);
        inputStartDate_assignment = findViewById(R.id.inputStartDate_chartMoneyManage);
        inputEndDate_assignment = findViewById(R.id.inputEndDate_chartMoneyManage);

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
        getCost();
        export_tableAllSpent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ExcelUltil().createExcelAndDownloaded(ChartMoneyManageActivity.this,listMap,"Spent.xlsx");
            }
        });
        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChartMoneyManageActivity.this, TableAllSpentActivity.class);
                intent.putExtra("idTeam",idTeam);
                startActivity(intent); // Bắt đầu Activity mới
            }
        });
    }
    private void checkLogin(){
        SharedPreferences sharedPref = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        token = sharedPref.getString("Token", null);
        if(token == null){
            finish();
            Intent intent = new Intent(ChartMoneyManageActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
    private  void clearToken(){
        SharedPreferences sharedPref = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("Token");
        editor.apply();
        finish();
        Intent intent = new Intent(ChartMoneyManageActivity.this, LoginActivity.class);
        startActivity(intent);
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
                    from=LocalDate.of(year, month+1, dayOfMonth);
                    getCost();
                }
            }, year, month, dayOfMonth);

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
                    to=LocalDate.of(year, month+1, dayOfMonth);

                    getCost();
                }
            }, year, month, dayOfMonth);

            datePickerDialog.show();
        }catch (Exception e){
            Log.e("Error","Lỗi ở onclick inputEndDate");
            Toast.makeText(getApplicationContext(),"không thể mở hộp thoại date",Toast.LENGTH_LONG).show();
        }

    }
    public  void getCost(){
//        loading_assigment.setVisibility(View.VISIBLE);

            try {
                String url=domain+"/Cost/team/"+String.valueOf(idTeam);
                if(check==0)check=1;
                else{
                    url+="?fromDate="+from.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+"&toDate="+to.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                }
                vlEs=0.0F;
                max1.setVisibility(View.GONE);
                max2.setVisibility(View.GONE);
                max3.setVisibility(View.GONE);
                max4.setVisibility(View.GONE);
                max5.setVisibility(View.GONE);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("success", "in onResponse");
                                Log.d("Data",response.toString());
                                GsonBuilder gsonBuilder = new GsonBuilder();
                                gsonBuilder.registerTypeAdapter(LocalDate.class,new LocalDateAdapter());
                                gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
                                Gson gson = gsonBuilder.create();


                                try{
                                    Type responseType = new TypeToken<ApiResponse<TeamCostResponse>>(){}.getType();
                                    ApiResponse<TeamCostResponse> apiResponse = gson.fromJson(response.toString(), responseType);
                                    List<CostResponse> costResponseList = apiResponse.getData().getCostResponseList();
                                    Map<Integer,List<CostResponse>> a= new HashMap<>();
                                    Map<Integer, ObjChartUtil> listMapResponse = new HashMap<>();
                                    for (CostResponse costResponse:costResponseList) {
                                        if (listMapResponse.containsKey(costResponse.getIdassigment())) {
                                            List<CostResponse> b= a.get(costResponse.getIdassigment());
                                            b.add(costResponse);
                                            ObjChartUtil obj = listMapResponse.get(costResponse.getIdassigment());
                                            obj.add(costResponse.getPrice());
                                        } else {
                                            List<CostResponse> new1= new ArrayList<>();
                                            new1.add(costResponse);
                                            a.put(costResponse.getIdassigment(),new1);
                                            // Nếu key chưa tồn tại, tạo một danh sách mới và thêm vào map
                                           ObjChartUtil obj1= new ObjChartUtil(costResponse.getNameAssigment(),costResponse.getPrice());

                                            listMapResponse.put(costResponse.getIdassigment(), obj1);
                                        }
                                    }
                                    if(check1==0)
                                    {
                                        listMap=a;
                                        check1=1;
                                    }
                                    if(costResponseList.size()>0)
                                    {
                                        inputStartDate_assignment.setText(costResponseList.get(0).getRefundDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                                       from=costResponseList.get(0).getRefundDate();
                                       to=costResponseList.get(costResponseList.size()-1).getRefundDate();
                                        inputEndDate_assignment.setText(costResponseList.get(costResponseList.size()-1).getRefundDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

                                    }
                                    List<Map.Entry<Integer, ObjChartUtil>> list = new ArrayList<>(listMapResponse.entrySet());

                                    // Sắp xếp List bằng cách sử dụng Comparator
                                    Collections.sort(list, new Comparator<Map.Entry<Integer, ObjChartUtil>>() {
                                        @Override
                                        public int compare(Map.Entry<Integer, ObjChartUtil> o1, Map.Entry<Integer, ObjChartUtil> o2) {
                                            return o2.getValue().getNumber().compareTo(o1.getValue().getNumber());
                                        }
                                    });
                                    List<Float> values = new ArrayList<>();
                                    Float sum= 0.0F;
                                    Integer count=0;
                                    List<String> labels = new ArrayList<>();
                                    List<Integer> colors = new ArrayList<>();
                                    vlEs=0.0F;
                                    for(int i=0;i<list.size();i++)
                                    {
                                        vlEs+=list.get(i).getValue().getNumber();
                                        if(i>=4)sum+=list.get(i).getValue().getNumber();
                                    }
                                    valueEstimateSpent_ChartMoneyManage=findViewById(R.id.valueEstimateSpent_ChartMoneyManage);
                                    valueEstimateSpent_ChartMoneyManage.setText(String.valueOf(vlEs));

                                    if (list.size() >= 1) {
                                        max1.setVisibility(View.VISIBLE);
                                        colors.add(Color.BLUE);
                                        labels.add(String.valueOf(list.get(0).getValue().getName()));
                                        values.add((float)list.get(0).getValue().getNumber());
                                        valueMax1.setText(String.valueOf(list.get(0).getValue().getNumber()));
                                        nameMax1.setText(String.valueOf(list.get(0).getValue().getName()));

                                        if (list.size() >= 2) {
                                            max2.setVisibility(View.VISIBLE);
                                            colors.add(Color.YELLOW);
                                            labels.add(String.valueOf(list.get(1).getValue().getName()));
                                            values.add((float)list.get(1).getValue().getNumber());
                                            valueMax2.setText(String.valueOf(list.get(1).getValue().getNumber()));
                                            nameMax2.setText(String.valueOf(list.get(1).getValue().getName()));
                                            if (list.size() >= 3) {
                                                max3.setVisibility(View.VISIBLE);

                                                colors.add(Color.RED);
                                                labels.add(String.valueOf(list.get(2).getValue().getName()));
                                                values.add((float)list.get(2).getValue().getNumber());
                                                valueMax3.setText(String.valueOf(list.get(2).getValue().getNumber()));
                                                nameMax3.setText(String.valueOf(list.get(2).getValue().getName()));
                                                if (list.size() >= 4) {
                                                    max4.setVisibility(View.VISIBLE);

                                                    colors.add(Color.GREEN);
                                                    labels.add(String.valueOf(list.get(3).getValue().getName()));
                                                    values.add((float)list.get(3).getValue().getNumber());
                                                    valueMax4.setText(String.valueOf(list.get(3).getValue().getNumber()));
                                                    nameMax4.setText(String.valueOf(list.get(3).getValue().getName()));
                                                    if (list.size() >= 5) {
                                                        max5.setVisibility(View.VISIBLE);

                                                        valueMax5.setText(String.valueOf(sum));
//                                                        nameMax5.setText(String.valueOf(list.get(4).getValue().getName()));
                                                        values.add(sum);
                                                        labels.add("others");
                                                        colors.add(Color.rgb(204,204,204));
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    // Cung cấp dữ liệu cho PieChartView
                                    pieChartView.setValues(values);
                                    pieChartView.setColors(colors);
                                    pieChartView.setLabels(labels);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            valueActuallySpent_chartMoneyManage.setText(String.valueOf(apiResponse.getData().getCost())+" VND");
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
//                                loading_assigment.setVisibility(View.INVISIBLE);

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
            } catch (Exception e) {
                Log.e("Error","không đúng định dạng Json");
            }
    }
}
