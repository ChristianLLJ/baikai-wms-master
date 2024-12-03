package com.bupt.DTO.inbound;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.pojo.InboundPlan;
import com.bupt.pojo.InboundPlanDetail;
import com.bupt.pojo.OnshelfAdvice;
import com.bupt.pojo.OnshelfAdviceDetail;
import lombok.Data;

@Data
public class GenerateONPWithIPL {
    HeadAndDetail<InboundPlan, InboundPlanDetail> inboundPlanDetailHeadAndDetail;
    HeadAndDetail<OnshelfAdvice, OnshelfAdviceDetail> onshelfAdviceDetailHeadAndDetail;
}
