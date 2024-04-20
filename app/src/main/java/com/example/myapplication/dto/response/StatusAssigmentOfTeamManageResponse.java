package com.example.myapplication.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusAssigmentOfTeamManageResponse {
    private Integer idTeam;
    private String teamName;

    private List<StatusAssigmentAndUserManageResponse> statusAssigmentAndUser;
}
