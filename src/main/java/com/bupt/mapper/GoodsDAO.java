package com.bupt.mapper;

import com.bupt.pojo.Goods;
import com.bupt.pojo.GoodsSku;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * GoodsDAO继承基类
 */
@Mapper
@Repository
public interface GoodsDAO extends MyBatisBaseDao<Goods, Integer> {
     Integer selectMaxId();
     Goods selectBarcode(String barcode);
     Goods selectCode(String code);
}