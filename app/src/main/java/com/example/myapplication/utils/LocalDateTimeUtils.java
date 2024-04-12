package com.example.myapplication.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class LocalDateTimeUtils {
    private LocalDateTime localDateTime;
    public LocalDateTimeUtils(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }
    public String getDate(){
        return localDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
    public String getTime(){
        return localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
