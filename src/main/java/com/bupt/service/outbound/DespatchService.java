package com.bupt.service.outbound;

import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.WaveScreenDTO;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;

import java.util.List;

public interface DespatchService {
    /*
     * Despatch操作  1创建中、2已创建、3已分配波次、4已预配、5已分配、 6拣货中、7已拣货、8装箱中、9已装箱、10装车中、11已装车、12配送中、13已配送、14已发运、15已取消
     * */
    List<Despatch> serachDespatchList(List<Despatch> despatches);

    List<Despatch> screenDespatch(ScreenDTO<Despatch> screenDTO);

    Integer screenDespatchNum(ScreenDTO<Despatch> screenDTO);

    List<DespatchDetail> merageDespatch(List<Despatch> despatchList);

    List<Despatch> searchAllDespatchByWaveId(ScreenDTO<DespatchWave> screenDTO);

    Integer searchAllDespatchByWaveIdNum(ScreenDTO<DespatchWave> screenDTO);

    /*
     * DespatchDetail操作
     * */

    HttpResult<?> deleteDespatchDetail(DespatchDetail despatchDetail);

    HttpResult<?> updateDespatchDetail(DespatchDetail despatchDetail);

    List<DespatchDetail> selectDetailByDespatchId(Integer despatchId);

    List<DespatchDetail> screenDespatchDetail(ScreenDTO<DespatchDetail> screenDTO);

    Integer screenDespatchDetailNum(ScreenDTO<DespatchDetail> screenDTO);

    /*
     * Wave 波次 操作
     * */
    Integer addWave(Wave wave);

    //多选模拟生成波次表头
    WaveRule newWaveByDespatchList();

    //一单到底模拟生成波次表头
    WaveRule newWaveByDespatch();

    //1、人工合并波次
    HttpResult<?> generateWave(List<Despatch> despatchList, WaveRule waveRule);

    //人工一单到底波次
    HttpResult<?> oneDespatchWaveManual(List<Despatch> despatchList, WaveRule waveRule);

    /**
     * 2、生产日期波次
     * 3、入库日期波次
     * 4、摘果波次
     * 摘果式拣选法是针对每一份订单(即每个客户)进行拣选，拣货人员或设备巡回于各个货物储位，将所需的货物取出，形似摘果。
     * 摘果法特点：1.每人每次只处理一份订单或一个客户；2.简单易操作。适用：品种少，订单量大。
     * 5、播种波次
     * 播种式分拣是把多份订单(多个客户的要货需求)集合成一批，先把其中每种商品的数量分别汇总，再逐个品种对所有客户进行分货，形似播种，因此称其为“商品别汇总分播”更为恰当。
     * 播种法特点：1.每次处理多份订单或多个客户。2.操作复杂，难度系数大。适用：订单品种和数量都比较多的拣选。
     */
    Wave generateWaveByStrategy(WaveRule waveRule);

    HttpResult<?> deleteWave(Wave wave);

    HttpResult<?> examineWave(List<Wave> waves);

    //审核不通过
    HttpResult<?> examineWaveReject(List<Wave> waves);

    //审核不通过后，发运订单回退到"已创建"
    HttpResult<?> RejectDespatch(List<Wave> waves);

    HttpResult<?> updateWaveAndDespatch(Wave wave, List<Despatch> despatchList);

    HttpResult<?> deleteDespatchInWave(List<Despatch> despatchList);


    List<Wave> screenWave(WaveScreenDTO<Wave> screenDTO);

    Integer screenWaveNum(WaveScreenDTO<Wave> screenDTO);

    Wave selectById(Integer waveId);

    //DespatchWave 操作
    Integer addDespatchWave(List<Despatch> despatchList, Integer waveId);

    HttpResult<?> deleteDespatchWave(DespatchWave despatchWave);

    HttpResult<?> updateDespatchWave(DespatchWave despatchWave);

    List<DespatchWave> screenDespatchWave(ScreenDTO<DespatchWave> screenDTO);

    Integer screenDespatchWaveNum(ScreenDTO<DespatchWave> screenDTO);

    /*
     * Replenish:补货单 操作全部自选
     * */

    //根据一条缺货单生成补货单-只选择来源库区
    List<Replenish> generateReplenish(PickTaskShortage pickTaskShortage, Replenish replenish);

    //一个库区所有缺货sku都充足
    HttpResult<?> generateReplenishByWave(Wave wave, Replenish replenish);

    HttpResult<?> deleteReplenish(Replenish replenish);

    //判断库区是否充足
    HttpResult<?> judgeInventoryBalanceByArea(Wave wave, Replenish replenish);

    HttpResult<?> updateReplenish(Replenish replenish);

    List<Replenish> screenReplenish(ScreenDTO<Replenish> screenDTO);

    Integer screenReplenishNum(ScreenDTO<Replenish> screenDTO);

}
