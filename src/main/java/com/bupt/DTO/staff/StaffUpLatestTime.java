package com.bupt.DTO.staff;

import lombok.Data;

import java.util.Date;
@Data
public class StaffUpLatestTime {
    private Integer id;
    private Date now;
    public StaffUpLatestTime(Integer id, Date now){
        this.id=id;
        this.now=now;
    }
}
