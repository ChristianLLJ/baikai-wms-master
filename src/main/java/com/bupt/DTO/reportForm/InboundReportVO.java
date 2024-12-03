package com.bupt.DTO.reportForm;

import com.bupt.pojo.InboundPlan;
import com.bupt.pojo.InboundPlanDetail;
import lombok.Data;

import java.util.List;

@Data
public class InboundReportVO {
    Integer inboundNum;
    Integer receiveNum;
    Integer onshelfNum;
    Integer receiveDiff;
    Integer onshelfDiff;
    List<InboundPlanDetail> inboundPlanDetailList;
}
