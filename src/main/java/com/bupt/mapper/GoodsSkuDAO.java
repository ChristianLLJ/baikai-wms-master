package com.bupt.mapper;

import com.bupt.pojo.GoodsSku;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * GoodsSkuDAO继承基类
 */
@Mapper
@Repository
public interface GoodsSkuDAO extends MyBatisBaseDao<GoodsSku, Integer> {
    Integer selectMaxId();
    GoodsSku selectByBarcode(String skuBarcode);
    GoodsSku selectBarcode(String skuBarcode);
    GoodsSku selectBySkuCode(String skuCode);
}