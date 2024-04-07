package com.example.myapplication.dto.request;

import java.io.Serializable;
import java.util.List;

public class Team  implements Serializable {
    private String name;
    private List<Integer> user;
    public  Team(String name, List<Integer> user){
        this.name=name;
        this.user=user;
    }
    public List<Integer> getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUser(List<Integer> user) {
        this.user = user;
    }
}
