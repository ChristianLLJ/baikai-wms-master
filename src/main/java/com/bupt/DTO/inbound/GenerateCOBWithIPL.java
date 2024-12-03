package com.bupt.DTO.inbound;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.pojo.InboundPlan;
import com.bupt.pojo.InboundPlanDetail;
import lombok.Data;

@Data
public class GenerateCOBWithIPL {
    HeadAndDetail<InboundPlan, InboundPlanDetail> inboundPlanDetailHeadAndDetail;
    CombineHeadAndPlateDetailAndPackageDetail combineHeadAndPlateDetailAndPackageDetail;
}
