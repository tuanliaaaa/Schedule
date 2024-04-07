package com.example.myapplication.dto.response;

import java.util.List;

public class CostResponse {
    private int idCost;
    private String costName;
    private int price;
    private int idTeam;



    public CostResponse(int idCost, String costName, int price, int idTeam) {
        this.idCost = idCost;
        this.costName = costName;
        this.price = price;
        this.idTeam = idTeam;
    }

    public int getIdCost() {
        return idCost;
    }

    public void setIdCost(int idCost) {
        this.idCost = idCost;
    }

    public String getCostName() {
        return costName;
    }

    public void setCostName(String costName) {
        this.costName = costName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(int idTeam) {
        this.idTeam = idTeam;
    }
}
