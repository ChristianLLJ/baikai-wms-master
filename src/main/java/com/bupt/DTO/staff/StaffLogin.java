package com.bupt.DTO.staff;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StaffLogin {
    private String username;
    private String password;
    /*
    public StaffLogin(String username,String password){
        this.password=password;
        this.username=username;
    }

     */
}
