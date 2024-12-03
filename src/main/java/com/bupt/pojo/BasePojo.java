package com.bupt.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

@Data
public class BasePojo {
    private Integer id;
    @JsonIgnore(value = false)
    private Integer checkPersonId;
    @JsonIgnore
    String checkPersonName;
    @JsonIgnore
    Date checkTime;
    @JsonIgnore
    Integer skuId;
    @JsonIgnore
    String skuName;
//    @JsonIgnore
//    Double num;
//    @JsonIgnore
//    Double totalWeight;
//    @JsonIgnore
//    Double totalNetWeight;
//    @JsonIgnore
//    Double totalVolumn;
}
