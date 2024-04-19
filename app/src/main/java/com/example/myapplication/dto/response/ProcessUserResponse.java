package com.example.myapplication.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessUserResponse {
    private int idAssigment;
    private String nameAssignment;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String description;
    private List<AssigmentUserResponse> userStatus;
}
