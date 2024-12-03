package com.bupt.mapper;

import com.bupt.pojo.ExPickingDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * ExPickingDetailDAO继承基类
 */
@Mapper
@Repository
public interface ExPickingDetailDAO extends MyBatisBaseDao<ExPickingDetail, Integer> {
    Integer sumDetailByDesAndSku(Integer despatchId,Integer skuId);
    ExPickingDetail selectByExPickingIdAndSkuCode(Integer exPickingId, String skuCode);

}