package com.bupt.mapper;

import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.inbound.SkuCodeAndAreaCode;
import com.bupt.DTO.inbound.SkuIdAndAreaCode;
import com.bupt.DTO.reportForm.ReportSum;
import com.bupt.pojo.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
/**
 * InventoryBalanceDAO继承基类
 */
@Mapper
@Repository
public interface InventoryBalanceDAO extends MyBatisBaseDao<InventoryBalance, Integer> {
    //根据库位id判断是否未空库位
    List<InventoryBalance> selectByLocationId(Integer Id);

    List<InventoryBalance> selectBySkuId(Integer skuId);

    List<InventoryBalance> selectByAreaCodeAndWarehouseCodeAndSkuId(String areaCode, String warehouseCode, Integer skuId);

    List<InventoryBalance> selectByAreaCodeAndWarehouseCodeAndSkuIdAndCustomId(Integer skuId, Integer customId, String areaCode, String warehouseCode);//分配使用

    Integer selectAvailableCntByAreaCodeAndWarehouseCodeAndSkuIdAndCustomId(Integer skuId, Integer customId, String areaCode, String warehouseCode);//分配可用数量总和

    List<InventoryBalance> selectByWarehouseCodeAndSkuIdAndCustomId(Integer skuId, Integer customId, String warehouseCode);

    //根据条件查找一条库存信息
    InventoryBalance selectByAreaCoAndWareCoAndSkuIdAndCusIdLimit(Integer skuId, Integer customId, String areaCode, String warehouseCode);

    InventoryBalance selectByAreaCoAndSkuIdAndCusIdLimit(Integer skuId, Integer customId, String areaCode);

    List<InventoryBalance> selectByAreaCoAndSkuIdAndCusIdFullBoxLimit(Integer skuId, Integer customId,
                                                                      String areaCode, Integer orderCnt);

    InventoryBalance selectByInventoryCode(String balanceCode);

    InventoryBalance selectByInventoryCodeType(String balanceCode,Short type);

    InventoryBalance selectByInventoryCodeNoType(String balanceCode);


    List<InventoryBalance> selectLikeSkuCode(SkuCodeAndAreaCode skuCodeAndAreaCode);

    List<InventoryBalance> fuzzyQuery(ScreenDTO<InventoryBalance> screenDTO);//模糊查询

    List<InventoryBalance> selectSkuIdAndAreaCode(SkuIdAndAreaCode skuIdAndAreaCode);

    List<InventoryBalance> selectHistorySkuIdAndAreaCode(SkuIdAndAreaCode skuIdAndAreaCode);

    Integer selectLocationIdByLocationCode(String locationCode);

    Integer selectCustomIdByCustomCode(String customCode);

    Integer selectCommodityIdByCommodityCode(String CommodityCode);

    List<InventoryBalance> selectByInventoryRange(StockInventory stockInventory);//筛选仓库、客户、产品、库位进行盘点

    List<InventoryBalance> selectByFreezeRange(StockFreeze stockFreeze); //筛选客户,产品，库位，跟踪号和生产批次进行冻结--停用

    List<InventoryBalance> selectByFreezeDetailRange(FreezeDetail freezeDetail);//筛选客户,产品，库位，跟踪号和生产批次冻结余量表

    List<InventoryBalance> selectByMoveRange(InventoryMoveDetail inventoryMoveDetail); //筛选客户,产品，库位，跟踪号和生产批次移动余量表

    List<InventoryBalance> selectByChangeRange(StockChangeDetail stockChangeDetail); //筛选客户,产品，库位，跟踪号和生产批次调整余量表

    List<InventoryBalance> selectByDateRange(Date start, Date end); //根据给定日期范围进行筛选

    Integer selectInventoryBySkuId(Integer skuId); //根据skuId查询总数量

    Integer updatePreDistributionById(Integer count, Integer id); //根据可用数量更新预配数量

    Integer updateDistributionById(Integer count, Integer id); //根据id更新分配数量

    Integer updateFreezeCntById(Integer count, Integer id); //根据可用数量更新冻结数量

    Integer updateAvailableCntAndDistributionCntById(Integer count, Integer id);//根据id更新可用数量和分配数量

    Integer selectAvailableCountById(Integer id); //根据Id查询可用数量

    Integer updateSourceLocationByMove(Integer sourceLocationId, Integer count); //库存移动更新来源仓库数量

    Integer updateTargetLocationByMove(Integer targetLocationId, Integer count); //库存移动更新目的仓库数量

    InventoryBalance selectBySkuIdOrderByInBoundTimeLimit(Integer skuId, String warehouseCode);

    InventoryBalance selectBySkuIdOrderByProductTimeLimit(Integer skuId, String warehouseCode);

    List<InventoryBalance> selectGroupBySkuIdAndCustomIdAndWarehouseCode();//根据skuId、customId和仓库查询总的可用数量

    //根据仓库、库区、库位、sku进行分组
    List<InventoryBalance> selectGroupByWarehouseCodeAndAreaCodeAndSkuIdAndLocation();

    List<InventoryBalance> selectGroupBySkuIdAndCustomIdAndWarehouseCodeAndAreaCode(); //根据skuId、customId、areaCode和仓库查询总的可用数量

    List<InventoryBalance> selectAll();

    List<InventoryBalance> selectByCurrentDate(Date date);//返回当日入库的货物

    List<InventoryBalance> selectGroupBySelective(ScreenDTO<InventoryBalance> screenDTO);//根据指定字段进行分组

    //List<InventoryBalance> selectGroupByAlarm(ScreenDTO<InventoryBalance> screenDTO);//按照预警进行分组

    ReportSum sumInventoryBalanceNum(ScreenDTO<InventoryBalance> screenDTO);//按照查询条件统计报表

    List<InventoryBalance> selectByRetentionAlarm();

    List<InventoryBalance> selectByCountAlarm();

    List<InventoryBalance> selectByValidAlarm();
}