package com.example.myapplication.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssigmentUserResponse {
    private Integer idUser;
    private int idAssigmentUser;
    private String username;
    private String status;
    private String process;

}
