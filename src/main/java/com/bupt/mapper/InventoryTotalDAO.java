package com.bupt.mapper;

import com.bupt.pojo.InventoryTotal;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * InventoryTotalDAO继承基类
 */
@Mapper
@Repository
public interface InventoryTotalDAO extends MyBatisBaseDao<InventoryTotal, Integer> {

    Integer selectAvailableCountBySkuId(Integer customId, Integer skuId, String wareHouse, String taskStatus); //分配查询可用数量

    Integer selectPreDistributionAvailableCountBySkuId(Integer customId, Integer skuId, String wareHouse); //预配查询可用数量

    Integer increasePreDistributionCountBySkuId(Integer customId, Integer skuId, Integer count, String wareHouse); //根据skuId增加预配数量

    Integer decreasePreDistributionCountBySkuId(Integer customId, Integer skuId, Integer count, String wareHouse); //根据skuId减少预配数量

    Integer decreaseAvailableCountBySkuId(Integer customId, Integer skuId, Integer count, String wareHouse);//根据skuId减少可用数量

    Integer increaseAvailableCountBySkuId(Integer customId, Integer skuId, Integer count, String wareHouse);//根据skuId增加可用数量

    List<InventoryTotal> selectBySkuId(Integer id);

    List<InventoryTotal> selectAllById(); //返回所有的inventoryTotal

}
