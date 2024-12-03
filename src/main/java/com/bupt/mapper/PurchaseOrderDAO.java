package com.bupt.mapper;

import com.bupt.pojo.PurchaseOrder;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * PurchaseOrderDAO继承基类
 */
@Mapper
@Repository
public interface PurchaseOrderDAO extends MyBatisBaseDao<PurchaseOrder, Integer> {
    PurchaseOrder selectPurchaseCode(PurchaseOrder purchaseOrder);
}
