package com.example.myapplication.dto.request;


import java.time.LocalDateTime;
import java.util.List;

public class AssignmentCreateRequest {
    private String startAt;

    private String endAt;

    private String description;



    private List<Integer> usersId;

    public AssignmentCreateRequest(){

    }
    public AssignmentCreateRequest(String startAt, String endAt, String description, List<Integer> usersId) {
        this.startAt = startAt;
        this.endAt = endAt;
        this.description = description;
        this.usersId = usersId;
    }

    public String getStartAt() {
        return startAt;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public String getEndAt() {
        return endAt;
    }

    public void setEndAt(String endAt) {
        this.endAt = endAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Integer> getUsersId() {
        return usersId;
    }

    public void setUsersId(List<Integer> usersId) {
        this.usersId = usersId;
    }
}
