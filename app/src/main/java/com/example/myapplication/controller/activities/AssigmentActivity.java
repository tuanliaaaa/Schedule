package com.example.myapplication.controller.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.dto.request.AssignmentCreateRequest;
import com.example.myapplication.dto.request.Team;
import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssigmentActivity extends Activity {
    private EditText inputAssignment_assignment;
    private EditText inputForPeople_assignment;
    private EditText inputStartDate_assignment;
    private EditText inputStartTime_assignment;
    private EditText inputEndDate_assignment;
    private EditText inputEndTime_assignment;
    private EditText inputDescription_assignment;

    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigment);
        ImageView linearLayout = findViewById(R.id.save_assigment);

        // Thiết lập sự kiện lắng nghe cho LinearLayout
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRequestQueue = Volley.newRequestQueue(getApplicationContext());
                postAssignment();
                // Tạo Intent để chuyển từ MainActivity sang Activity mới
                Intent intent = new Intent(AssigmentActivity.this, TableAllProcessActivity.class);
                startActivity(intent); // Bắt đầu Activity mới
            }
        });
    }

    public  void postAssignment(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.1.25:8080/Assignment/Team/8",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public byte[] getBody() {
                //get User
                List<Integer> users = new ArrayList<>();
                inputForPeople_assignment = findViewById(R.id.inputForPeople_assigment);
                String peopleID = String.valueOf(inputForPeople_assignment.getText());
                String[] peopleIDArray = peopleID.split(",");
                for (String id : peopleIDArray){
                    users.add(Integer.parseInt(id.trim()));
                }

                //get assignment name
                inputAssignment_assignment = findViewById(R.id.inputAssignment_assigment);
                String assignment = String.valueOf(inputAssignment_assignment.getText());

                //get Description
                inputDescription_assignment = findViewById(R.id.inputDescription_assigment);
                String description = String.valueOf(inputDescription_assignment.getText());

                //get startDate and endDate
                inputStartDate_assignment = findViewById(R.id.inputStartDate_assigment);
                inputStartTime_assignment = findViewById(R.id.inputStartTime_assigment);
                String startDateText = inputStartDate_assignment.getText().toString();
                String startTimeText = inputStartTime_assignment.getText().toString();
                String startDateTimeText = startDateText + " " + startTimeText;

                inputEndDate_assignment = findViewById(R.id.inputEndDate_assigment);
                inputEndTime_assignment = findViewById(R.id.inputEndTime_assigment);
                String endDateText = inputEndDate_assignment.getText().toString();
                String endTimeText = inputEndTime_assignment.getText().toString();
                String endDateTimeText = endDateText + " " + endTimeText;

                AssignmentCreateRequest newAssignment = new AssignmentCreateRequest(
                        startDateTimeText, endDateTimeText, description, users
                );
                System.out.println("newAssignment: " + newAssignment);
                // Serialize team object to JSON
                Gson gson = new Gson();
                String json = gson.toJson(newAssignment);

                // Send JSON data in POST request body
                return json.getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Add authorization token to headers
                headers.put("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuaGkiLCJyb2xlIjpbXSwiaWF0IjoxNzEyNTkwOTQ5LCJleHAiOjE3MTI1OTQ1NDl9.fCvwvmFbCppd9qeSny6mDNgN4jELTMLu07b3n6q_u1c");
                return headers;
            }
        };

        // Add the request to the RequestQueue
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(stringRequest);
    }
}
