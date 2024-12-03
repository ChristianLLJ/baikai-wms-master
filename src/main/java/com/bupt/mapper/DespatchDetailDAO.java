package com.bupt.mapper;

import com.bupt.DTO.reportForm.OutboundReport;
import com.bupt.pojo.DespatchDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DespatchDetailDAO继承基类
 */
@Mapper
@Repository
public interface DespatchDetailDAO extends MyBatisBaseDao<DespatchDetail, Integer> {
    Integer increasePreDistributionCountById(Integer id, Integer count); //根据skuId增加预配数量

    Integer decreasePreDistributionCountById(Integer id, Integer count); //根据skuId减少预配数量

    Integer increaseDistributionCountById(Integer id, Integer count); //根据skuId增加分配数量

    Integer decreaseDistributionCountById(Integer id, Integer count); //根据skuId减少分配数量

    List<DespatchDetail> selectDetailByPidAndPieceCnt(Integer pid);

    DespatchDetail selectByPidAndSkuCode(Integer pid, String skuCode);

    OutboundReport generateSumOrderCnt(OutboundReport outboundReport);

    OutboundReport gerateSumDespatchedOrderCnt(OutboundReport outboundReport);

    List<DespatchDetail> gerateDespatchDetailList(OutboundReport outboundReport);
}