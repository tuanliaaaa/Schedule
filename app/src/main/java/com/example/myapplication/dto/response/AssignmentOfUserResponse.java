package com.example.myapplication.dto.response;


import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AssignmentOfUserResponse {
    private Integer idAssigment;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String description;
    private String status;
    private String process;
    private String nameAssignment;
}
