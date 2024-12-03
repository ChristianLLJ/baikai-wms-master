package com.bupt.DTO.inbound;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.pojo.*;
import lombok.Data;

@Data
public class GenerateIPA {
    HeadAndDetail<InboundPlan, InboundPlanDetail> inboundPlanDetailHeadAndDetail;
    HeadAndDetail<PackingTable, PackingDetail> packingDetailHeadAndDetail;
}
