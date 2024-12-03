package com.bupt.mapper;

import com.bupt.pojo.PickTaskDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * PickTaskDetailDAO继承基类
 */
@Mapper
@Repository
public interface PickTaskDetailDAO extends MyBatisBaseDao<PickTaskDetail, Integer> {
    Integer sumTotalCntBySkuAndPid(Integer pid, Integer skuId);

    List<PickTaskDetail> selectByDespatchId(int despatchId);

    List<PickTaskDetail> selectByDespatchIdAndPieceDevice(int despatchId);


    List<PickTaskDetail> selectByLocationId(int locationId);

    List<PickTaskDetail> selectDetailByBalanceCodeAndDesId(String inventoryBalanceCode, Integer despatchId);

    //查找分拣未装箱的订单信息
    List<PickTaskDetail> selectByDesIdAndAreaIdNotBox(Integer areaId, Integer despatchId);
    List<PickTaskDetail> selectByDesIdAndAreaIdNotPick(Integer areaId, Integer despatchId);

    List<PickTaskDetail> selectByDesIdAndAreaIdInBox(Integer areaId, Integer despatchId);

    //人工使用
    List<PickTaskDetail> selectDetailByBalanceCodeManual(String inventoryBalanceCode);

    //分拣机用
    List<PickTaskDetail> selectDetailByBalanceCode(String inventoryBalanceCode);

    //统计拆零数量按照发运订单和skuid筛选
    Integer sumPieceCntByDesIdAndSkuIdPicked(Integer despatchId, Integer skuId);


}