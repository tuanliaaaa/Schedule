package com.example.myapplication.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentManagerResponse {
    private String nameAssignment;
    private Integer idAssignment;

}
