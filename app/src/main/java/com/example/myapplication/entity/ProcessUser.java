package com.example.myapplication.entity;

import com.example.myapplication.dto.response.AssigmentUserResponse;
import com.example.myapplication.dto.response.ProcessUserResponse;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessUser {
    private Integer idUser;
    private int idAssigmentUser;
    private String username;
    private String status;
    private String process;
    public ProcessUser(AssigmentUserResponse assigmentUserResponse)
    {
        this.idUser=assigmentUserResponse.getIdUser();
        this.idAssigmentUser = assigmentUserResponse.getIdAssigmentUser();
        this.username=assigmentUserResponse.getUsername();
        this.status =assigmentUserResponse.getStatus();
        this.process=assigmentUserResponse.getProcess();
    }
}
