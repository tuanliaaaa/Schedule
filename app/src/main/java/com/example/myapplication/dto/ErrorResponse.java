package com.example.myapplication.dto;

import java.util.List;

import lombok.Data;

@Data
public class ErrorResponse <T>{
    private int status;
    private String message;
    private T error;
    private String timestamp;
}
