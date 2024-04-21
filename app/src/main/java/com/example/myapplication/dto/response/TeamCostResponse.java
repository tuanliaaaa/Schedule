package com.example.myapplication.dto.response;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamCostResponse {
    private Integer idTeam;
    private Integer cost;
    private List<CostResponse> costResponseList;
}
