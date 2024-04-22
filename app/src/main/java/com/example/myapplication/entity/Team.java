package com.example.myapplication.entity;

import com.example.myapplication.dto.response.TeamResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Team {
    private Integer idTeam;
    private String teamName;
    public Team(TeamResponse teamResponse){
        this.idTeam=teamResponse.getIdTeam();
        this.teamName=teamResponse.getTeamName();
    }
}
