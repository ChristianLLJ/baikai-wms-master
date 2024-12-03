package com.bupt.DTO.outBount;

import com.bupt.pojo.DespatchDetail;
import lombok.Data;

@Data
public class DespatchDetailAndCustomerId {
    DespatchDetail despatchDetail;
    int customerId;
}
