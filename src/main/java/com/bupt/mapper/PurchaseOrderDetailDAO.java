package com.bupt.mapper;

import com.bupt.pojo.PurchaseOrder;
import com.bupt.pojo.PurchaseOrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * PurchaseOrderDetailDAO继承基类
 */
@Mapper
@Repository
public interface PurchaseOrderDetailDAO extends MyBatisBaseDao<PurchaseOrderDetail, Integer> {
    Integer deleteByOrderId(Integer id);

    Integer selectByOrderId(PurchaseOrderDetail purchaseOrderDetail);
}
