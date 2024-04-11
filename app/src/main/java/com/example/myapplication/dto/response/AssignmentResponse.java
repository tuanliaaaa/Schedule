package com.example.myapplication.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentResponse {
    private String teamName;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String description;
}
