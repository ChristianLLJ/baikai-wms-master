package com.bupt.DTO.inbound;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.pojo.InboundPlan;
import com.bupt.pojo.InboundPlanDetail;
import com.bupt.pojo.QualityCheck;
import com.bupt.pojo.QualityCheckDetail;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Data
public class GenerateCHE {
    @Valid
    HeadAndDetail<InboundPlan, InboundPlanDetail> inboundPlanDetailHeadAndDetail;
    @Valid
    HeadAndDetail<QualityCheck, QualityCheckDetail> qualityCheckDetailHeadAndDetail;
}
