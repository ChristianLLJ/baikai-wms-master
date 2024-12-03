package com.bupt.service.inbound;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SumAndList;
import com.bupt.DTO.inbound.*;
import com.bupt.pojo.*;

import java.util.List;

public interface InboundPlanService {
    List<InboundPlan> selectInboundPlan(ScreenDTO<InboundPlan> screenDTO);

    List<InboundPlan> selectInboundPlan(List<InboundPlan> inboundPlans);

    Integer selectInboundPlanNum(ScreenDTO<InboundPlan> screenDTO);

    SumAndList<InboundPlanDetail> selectInboundPlanDetail(ScreenDTO<InboundPlanDetail> screenDTO);

    Integer selectInboundPlanDetailNum(ScreenDTO<InboundPlanDetail> screenDTO);

    List<Inbound> selectInbound(ScreenDTO<Inbound> screenDTO);

    List<Inbound> selectInbound(List<Inbound> inboundPlans);

    Integer selectInboundNum(ScreenDTO<Inbound> screenDTO);

    List<InboundDetail> selectInboundDetail(ScreenDTO<InboundDetail> screenDTO);

    Integer selectInboundDetailNum(ScreenDTO<InboundDetail> screenDTO);

    Integer save(HeadAndDetail<InboundPlan,InboundPlanDetail> headAndDetail);

    Integer add(HeadAndDetail<InboundPlan,InboundPlanDetail> headAndDetail);


    Integer cancel(HeadAndDetail<InboundPlan,InboundPlanDetail> headAndDetail);

    Integer close(DetailAndCode<InboundPlan> detailAndCode);

    Integer generateCHE(GenerateCHE generateCHE);

    Integer generateIPA(GenerateIPA generateIPA);

    InboundPlan selectByPrimaryId(Integer id);

    List<InboundPlanDetail> selectByPlanId(Integer id);

    String insertIPL(InboundPlan inboundPlan,List<InboundPlanDetail> inboundPlanDetails);

    HeadAndDetail<InboundPlan,InboundPlanDetail> getInboundPlan(InboundPlan inboundPlan);

    Integer receiveDetailContainer(InboundPlanDetail inboundPlanDetail);

    Integer receiveDetailSKU(DetailAndCode<InboundPlanDetail> detailAndCode);

    Integer receiveTable(InboundPlan inboundPlan);

    Integer receiveSet(InboundPlan inboundPlan);

    Boolean receivedCheck(InboundPlan inboundPlan);

    Integer cancelReceive(HeadAndDetail<InboundPlan,InboundPlanDetail> headAndDetail);

    Integer generateCOB(GenerateCOBWithIPL generateCOBWithIPL);

    String addInboundBatch(List<InboundPlan> inboundPlans);

    HeadAndDetail<OnshelfAdvice, OnshelfAdviceDetail> generateONP(GenerateONPWithIPL generateONPWithIPL);

    Integer addInboundPlanDetail(DetailAndCode<InboundPlanDetail> detailAndCode);

    Integer setInboundPlanReceiving(InboundPlan inboundPlan);
}
