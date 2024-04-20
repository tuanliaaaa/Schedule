package com.example.myapplication.entity;

import com.example.myapplication.dto.response.AssignmentOfUserResponse;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssigmentUser {
    private Integer idAssigment;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String description;
    private String status;
    private String process;
    private String nameAssignment;
    public  AssigmentUser(AssignmentOfUserResponse assignmentOfUserResponse){
        this.idAssigment=assignmentOfUserResponse.getIdAssigment();
        this.startAt=assignmentOfUserResponse.getStartAt();
        this.endAt=assignmentOfUserResponse.getEndAt();
        this.description=assignmentOfUserResponse.getDescription();
        this.status=assignmentOfUserResponse.getStatus();
        this.process =assignmentOfUserResponse.getProcess();
        this.nameAssignment=assignmentOfUserResponse.getNameAssignment();
    }
}
