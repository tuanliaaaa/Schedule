package com.example.myapplication.dto.response;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CostResponse {
    private int idCost;
    private String costName;
    private int price;
    private int idTeam;
    private String nameAssigment;
    private Integer idassigment;
    private LocalDate refundDate;
}
