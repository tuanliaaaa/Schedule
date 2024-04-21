package com.example.myapplication.dto.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AssigmentResponse {
    private String teamName;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String description;
    private String status;
    private String assingmentName;
    private String process;
}
