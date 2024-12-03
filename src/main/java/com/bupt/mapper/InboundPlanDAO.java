package com.bupt.mapper;

import com.bupt.DTO.ScreenDTO;
import com.bupt.pojo.InboundPlan;
import com.bupt.pojo.InboundPlanDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * InboundPlanDAO继承基类
 */
@Mapper
@Repository
public interface InboundPlanDAO extends MyBatisBaseDao<InboundPlan, Integer> {
    InboundPlan selectByPlanId(String id);

    Integer updateIsReceived(Integer id);

    Integer updateReceiving(String code);

    Integer updateState(InboundPlan inboundPlan);

    Integer updateNum(InboundPlan inboundPlan);

    Integer SumInboundNum(ScreenDTO<InboundPlan> inboundPlanScreenDTO);
}
