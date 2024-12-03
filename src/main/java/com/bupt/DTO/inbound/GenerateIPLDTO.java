package com.bupt.DTO.inbound;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.pojo.InboundPlan;
import com.bupt.pojo.InboundPlanDetail;
import com.bupt.pojo.PurchaseOrder;
import com.bupt.pojo.PurchaseOrderDetail;
import lombok.Data;

@Data
public class GenerateIPLDTO{
    HeadAndDetail<PurchaseOrder, PurchaseOrderDetail> purchaseOrderDetailHeadAndDetail;
    HeadAndDetail<InboundPlan, InboundPlanDetail> inboundPlanDetailHeadAndDetail;
}
