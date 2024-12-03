package com.bupt.mapper;

import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.reportForm.ReportSum;
import com.bupt.pojo.InboundPlan;
import com.bupt.pojo.InboundPlanDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * InboundPlanDetailDAO继承基类
 */
@Mapper
@Repository
public interface InboundPlanDetailDAO extends MyBatisBaseDao<InboundPlanDetail, Integer> {
    Integer deleteByPlanId(Integer id);

    List<InboundPlanDetail> selectByPlanId(Integer id);

    Integer updateByPlanId(InboundPlanDetail inboundPlanDetail);

    Integer updateByPlanIdSku(InboundPlanDetail inboundPlanDetail);

    Integer updateIsReceived(InboundPlan inboundPlan);

    List<Integer> checkReceived(Integer id);

    List<InboundPlanDetail> selectByInboundTraceCode(String traceCode);

    List<InboundPlanDetail> selectByContainerBarcode(String code);

    Integer deleteBySplitupOne(Integer id);

    InboundPlanDetail selectByPlanIdSku(InboundPlanDetail inboundPlanDetail);

    List<InboundPlanDetail> selectByIsPackagedOrIsPlate(ScreenDTO<InboundPlanDetail> screenDTO);

    Integer selectByIsPackagedOrIsPlateNum(ScreenDTO<InboundPlanDetail> screenDTO);

    List<InboundPlanDetail> selectSkuSum();

    ReportSum SumInboundNum(ScreenDTO<InboundPlanDetail> screenDTO);

    List<InboundPlanDetail> screenInbound(ScreenDTO<InboundPlanDetail> screenDTO);
}
