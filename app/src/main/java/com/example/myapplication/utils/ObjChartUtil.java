package com.example.myapplication.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ObjChartUtil {
    private String name;
    private Integer number;
    public void add(Integer n){
        this.number=n+this.number;
    }
}
