package com.example.myapplication.dto.request;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Team  implements Serializable {
    private String name;
    private List<Integer> user;
    private int costExpected;
}
