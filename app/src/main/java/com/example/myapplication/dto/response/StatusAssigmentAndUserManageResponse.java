package com.example.myapplication.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusAssigmentAndUserManageResponse {
    private int idAssigment;
    private String nameAssignment;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String description;
    private Integer idUser;
    private int idAssigmentUser;
    private String username;
    private String status;
    private String process;
}
