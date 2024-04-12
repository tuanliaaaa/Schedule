package com.example.myapplication.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InforResponse {
    private int iduser;
    private List<RoleResponse> roles;
}
