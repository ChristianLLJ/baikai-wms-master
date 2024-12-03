package com.bupt.DTO.outBount;

import com.bupt.pojo.ExpressDelivery;
import com.bupt.pojo.ExpressDeliveryDetail;
import lombok.Data;

import java.util.List;

@Data
public class ExpressDeliveryAndDetail {
    private ExpressDelivery expressDelivery;
    private List<ExpressDeliveryDetail> expressDeliveryDetailList;
}
