package com.example.myapplication.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamCreateResponse {
    private String teamName;

    private List<UserInTeamResponse> userInTeamResponse;
    private Integer costExpected;
}
