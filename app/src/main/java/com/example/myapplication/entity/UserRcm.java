package com.example.myapplication.entity;

import com.example.myapplication.dto.response.RecommendUserResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRcm {
    private String username;
    private Integer idUser;
    private String status;
    private String avatar;
   public UserRcm(RecommendUserResponse response){
       this.idUser=response.getIdUser();
       this.username=response.getUsername();
       this.status="0";
       this.avatar=response.getAvatar();

   }
}
