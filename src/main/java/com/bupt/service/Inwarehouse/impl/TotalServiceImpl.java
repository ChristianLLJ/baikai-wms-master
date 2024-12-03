package com.bupt.service.Inwarehouse.impl;

import com.bupt.DTO.*;
import com.bupt.mapper.*;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.Inwarehouse.TotalService;
import com.bupt.service.authority.CodeService;
import com.bupt.service.util.UtilService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TotalServiceImpl implements TotalService {
    @Autowired
    private InventoryTotalDAO inventoryTotalDAO;
    @Autowired
    private InventoryBalanceDAO inventoryBalanceDAO;
    @Autowired
    private DespatchDAO despatchDAO;
    @Autowired
    private DespatchDetailDAO despatchDetailDAO;
    @Autowired
    private DespatchWaveDAO despatchWaveDAO;
    @Autowired
    private PickTaskDAO pickTaskDAO;
    @Autowired
    private PickTaskDetailDAO pickTaskDetailDAO;
    @Autowired
    private PickTaskMessageDAO pickTaskMessageDAO;
    @Autowired
    private PickTaskShortageDAO pickTaskShortageDAO;
    @Autowired
    private WaveDAO waveDAO;
    @Autowired
    private PreDistributionRecordsDAO preDistributionRecordsDAO;
    @Autowired
    private CodeService codeService;
    @Autowired
    UtilService utilService;

    @Override
    public Integer addInventoryTotal(InventoryTotal inventoryTotal) {
        inventoryTotalDAO.insert(inventoryTotal);
        return inventoryTotal.getId();
    }

    @Override
    public HttpResult<?> deleteInventoryTotal(InventoryTotal inventoryTotal) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, inventoryTotalDAO.deleteByPrimaryKey(inventoryTotal.getId()));
    }

    @Override
    public HttpResult<?> updateInventoryTotal(InventoryTotal inventoryTotal) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, inventoryTotalDAO.updateByPrimaryKeySelective(inventoryTotal));
    }

    @Override
    public List<InventoryTotal> screenInventoryTotal(ScreenDTO<InventoryTotal> screenDTO) {
        screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage() - 1) * screenDTO.getSearchDTO().getNum());
        List<InventoryTotal> inventoryTotalList = inventoryTotalDAO.screen(screenDTO);
        List<InventoryTotal> inventoryTotals = new ArrayList<>();
        for (InventoryTotal inventoryTotal : inventoryTotalList)
            inventoryTotals.add(inventoryTotalDAO.selectByPrimaryKey(inventoryTotal.getId()));
        return inventoryTotals;
    }

    @Override
    public Integer screenInventoryTotalNum(ScreenDTO<InventoryTotal> screenDTO) {
        return inventoryTotalDAO.numScreen(screenDTO);
    }

    PreDistributionRecords despatchToPreDisRecordsSuccess(Despatch despatch, DespatchDetail despatchDetail, String rule, Integer rowId) {
        PreDistributionRecords preDistributionRecords = new PreDistributionRecords();
        preDistributionRecords.setPreDistributionRule(rule);
        preDistributionRecords.setPreDistributionCnt(despatchDetail.getOrderCnt());
        preDistributionRecords.setPreDistributionRuleId(1);//暂定
        preDistributionRecords.setChineseDescribe(despatchDetail.getChineseDescribe());
        preDistributionRecords.setEnglishDescribe(despatchDetail.getEnglishDescribe());
        preDistributionRecords.setOtherName(despatchDetail.getOtherName());
        preDistributionRecords.setWarehouseName(despatch.getWarehouseName());
        preDistributionRecords.setWarehouseCode(despatch.getWarehouse());
        preDistributionRecords.setSkuId(despatchDetail.getSkuId());
        preDistributionRecords.setSkuCode(despatchDetail.getSkuCode());
        preDistributionRecords.setIsCompleted(true);
        preDistributionRecords.setPid(despatch.getId());
        preDistributionRecords.setUnit(despatchDetail.getUnit());
        preDistributionRecords.setDespatchCode(despatch.getDespatchId());
        preDistributionRecords.setStatus((short) 1);
        preDistributionRecords.setRowId(rowId);
        preDistributionRecords.setPreDistributionCode(codeService.code("PRD"));
        return preDistributionRecords;
    }

    PreDistributionRecords despatchToPreDisRecordsFailed(Despatch despatch, DespatchDetail despatchDetail, String rule, Integer rowId) {
        PreDistributionRecords preDistributionRecords = new PreDistributionRecords();
        preDistributionRecords.setPreDistributionRule(rule);
        preDistributionRecords.setPreDistributionCnt(despatchDetail.getOrderCnt());
        preDistributionRecords.setPreDistributionRuleId(1);//暂定
        preDistributionRecords.setChineseDescribe(despatchDetail.getChineseDescribe());
        preDistributionRecords.setEnglishDescribe(despatchDetail.getEnglishDescribe());
        preDistributionRecords.setOtherName(despatchDetail.getOtherName());
        preDistributionRecords.setWarehouseName(despatch.getWarehouseName());
        preDistributionRecords.setWarehouseCode(despatch.getWarehouse());
        preDistributionRecords.setSkuId(despatchDetail.getSkuId());
        preDistributionRecords.setSkuCode(despatchDetail.getSkuCode());
        preDistributionRecords.setIsCompleted(true);
        preDistributionRecords.setPid(despatch.getId());
        preDistributionRecords.setUnit(despatchDetail.getUnit());
        preDistributionRecords.setDespatchCode(despatch.getDespatchId());
        preDistributionRecords.setStatus((short) 0);//失败
        preDistributionRecords.setRowId(rowId);
        preDistributionRecords.setPreDistributionCode(codeService.code("PRD"));
        return preDistributionRecords;
    }


    @Override
    public HttpResult<?> screenPredistributionRecords(ScreenDTO<PreDistributionRecords> screenDTO) {
        SearchDTO searchDTO = screenDTO.getSearchDTO();
        searchDTO.setPage((searchDTO.getPage() - 1) * searchDTO.getNum());
        screenDTO.setSearchDTO(searchDTO);
        List<PreDistributionRecords> screen = preDistributionRecordsDAO.screen(screenDTO);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, screen);
    }

    @Override
    public HttpResult<?> screenPredistributionRecordsNum(ScreenDTO<PreDistributionRecords> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, preDistributionRecordsDAO.numScreen(screenDTO));
    }

    //同一客户、同一仓库下查询库区内可用数量是否充足
    public Boolean checkAvailableCntByDespatch(List<Despatch> despatchList, Wave wave, PickTaskMessage pickTaskMessage) {
        List<DespatchDetail> despatchDetailList = new ArrayList<>();
        Integer customId = despatchList.get(0).getCustomerId();
        String customName = despatchList.get(0).getCustomerName();
        //将所有发运订单表细合并成一个list
        for (Despatch despatch : despatchList)
            despatchDetailList.addAll(despatchDetailDAO.selectDetailByPid(despatch.getId()));
        //根据skuId进行排序
        despatchDetailList.sort(Comparator.comparing(DespatchDetail::getSkuId));
        if (despatchDetailList.size() == 0) return false;
        Integer skuId = despatchDetailList.get(0).getSkuId();//获取表细的skuId
        Integer count = despatchDetailList.get(0).getOrderCnt();//订货数总和
        List<DespatchDetail> despatchDetails = despatchDetailList.subList(1, despatchDetailList.size());
        boolean flag = true;
        for (DespatchDetail despatchDetail : despatchDetails) {
            if (!Objects.equals(despatchDetail.getSkuId(), skuId)) {
                Integer areaCount = inventoryBalanceDAO.selectAvailableCntByAreaCodeAndWarehouseCodeAndSkuIdAndCustomId(skuId, customId, pickTaskMessage.getWarehouseAreaCode(), wave.getWarehouse());
                if (areaCount == null) areaCount = 0;
                if (count > areaCount) {
                    //生成缺货单
                    flag = false;
                    PickTaskShortage pickTaskShortage = new PickTaskShortage();
                    pickTaskShortage.setStatus((short) 0);
                    pickTaskShortage.setWaveId(wave.getId());
                    pickTaskShortage.setSkuId(skuId);
                    pickTaskShortage.setSkuCode(despatchDetail.getSkuCode());
                    pickTaskShortage.setSkuName(despatchDetail.getSkuName());
                    pickTaskShortage.setSkuBarcode(despatchDetail.getSkuBarcode());
                    pickTaskShortage.setShortageCnt(count - areaCount);
                    pickTaskShortage.setWarehouseCode(wave.getWarehouse());
                    pickTaskShortage.setWarehouseName(wave.getWarehouseName());
                    pickTaskShortage.setCustomerId(customId);
                    pickTaskShortage.setCustomerName(customName);
                    pickTaskShortage.setShortageAreaCode(pickTaskMessage.getWarehouseAreaCode());
                    pickTaskShortage.setShortageAreaId(pickTaskMessage.getWarehouseAreaId());//库区id
                    pickTaskShortage.setShortageAreaName(pickTaskMessage.getWarehouseAreaName());
                    pickTaskShortage.setCreateTime(new Date());
                    pickTaskShortageDAO.insert(pickTaskShortage);
                } else {
                    skuId = despatchDetail.getSkuId();
                    count = despatchDetail.getOrderCnt();
                }
            } else count += despatchDetail.getOrderCnt();
        }
        //处理最后一条数据
        DespatchDetail despatchDetail = despatchDetailList.get(despatchDetailList.size() - 1);
        Integer areaCount = inventoryBalanceDAO.selectAvailableCntByAreaCodeAndWarehouseCodeAndSkuIdAndCustomId(skuId, customId, pickTaskMessage.getWarehouseAreaCode(), wave.getWarehouse());
        if (areaCount == null) areaCount = 0;
        if (count > areaCount) {
            flag = false;
            PickTaskShortage pickTaskShortage = new PickTaskShortage();
            pickTaskShortage.setStatus((short) 0);
            pickTaskShortage.setWaveId(wave.getId());
            pickTaskShortage.setSkuId(skuId);
            pickTaskShortage.setSkuCode(despatchDetail.getSkuCode());
            pickTaskShortage.setSkuName(despatchDetail.getSkuName());
            pickTaskShortage.setSkuBarcode(despatchDetail.getSkuBarcode());
            pickTaskShortage.setShortageCnt(count - areaCount);
            pickTaskShortage.setWarehouseCode(wave.getWarehouse());
            pickTaskShortage.setWarehouseName(wave.getWarehouseName());
            pickTaskShortage.setCustomerId(customId);
            pickTaskShortage.setCustomerName(customName);
            pickTaskShortage.setShortageAreaCode(pickTaskMessage.getWarehouseAreaCode());
            pickTaskShortage.setShortageAreaId(pickTaskMessage.getWarehouseAreaId());//库区id
            pickTaskShortage.setShortageAreaName(pickTaskMessage.getWarehouseAreaName());
            pickTaskShortage.setCreateTime(new Date());
            pickTaskShortageDAO.insert(pickTaskShortage);
        }
        if (!flag) return false;
        else return true;
    }

    //相同客户生成分拣任务单
    public void generatePickTask(@NotNull List<Despatch> despatchList, Wave wave, PickTaskMessage pickTaskMessage) {
        Integer customId = despatchList.get(0).getCustomerId();
        //String customName = despatchList.get(0).getCustomerName();
        List<DespatchDetail> despatchDetailList = new ArrayList<>();
        //将所有发运订单表细合并成一个list
        for (Despatch despatch : despatchList)
            despatchDetailList.addAll(despatchDetailDAO.selectDetailByPid(despatch.getId()));
        PickTask pickTask = new PickTask();
        pickTask.setPid(wave.getId());
        pickTask.setStatus((short) 3);//拣货完成
        pickTask.setDistributionTime(new Date());
        pickTask.setTaskId(codeService.code("PIC"));
        pickTask.setStatus((short) 0);
        pickTaskDAO.insert(pickTask);
        int row_no = 0;//行号
        //先对整箱进行分配
        for (DespatchDetail despatchDetail : despatchDetailList) {
            //按照先入先出出库
            List<InventoryBalance> inventoryBalanceList = inventoryBalanceDAO.selectByAreaCodeAndWarehouseCodeAndSkuIdAndCustomId(despatchDetail.getSkuId(), customId, pickTaskMessage.getWarehouseAreaCode(), wave.getWarehouse());
            Integer orderCount = despatchDetail.getOrderCnt();
            for (InventoryBalance inventoryBalance : inventoryBalanceList) {
                if (orderCount == 0) break;
                if (orderCount - inventoryBalance.getAvailableCnt() >= 0) {
                    PickTaskDetail pickTaskDetail = inventoryBalanceToPickTaskDetail(inventoryBalance, pickTaskMessage);
                    pickTaskDetail.setPid(pickTask.getId());
                    pickTaskDetail.setDespatchId(despatchDetail.getPid());
                    pickTaskDetail.setRowNo(++row_no);
                    pickTaskDetail.setSkuId(despatchDetail.getSkuId());
                    pickTaskDetail.setSkuCode(despatchDetail.getSkuCode());
                    pickTaskDetail.setReceiverId(despatchDAO.selectByPrimaryKey(despatchDetail.getPid()).getReviewerId());
                    pickTaskDetail.setFullTag(true);//整箱标志
                    pickTaskDetail.setPieceCnt(0);//拆零数量
                    pickTaskDetail.setPickupTag(false);//拆箱标志
                    pickTaskDetail.setPickTaskRule("先入先出规则");
                    //todo 分拣规则
                    orderCount -= inventoryBalance.getAvailableCnt();
                    inventoryBalanceDAO.updateAvailableCntAndDistributionCntById(inventoryBalance.getAvailableCnt(), inventoryBalance.getId());
                    pickTaskDetailDAO.insert(pickTaskDetail);
                } else {
                    despatchDetail.setPieceCnt(orderCount + despatchDetail.getPieceCnt());//保存拆零数
                    despatchDetailDAO.updateByPrimaryKeySelective(despatchDetail);
                    break;
                }
            }
        }
        //按照skuId对发运订单表细排序
        despatchDetailList.sort(Comparator.comparing(DespatchDetail::getSkuId));
        //对零数进行分配
        Integer skuId = despatchDetailList.get(0).getSkuId();
        String skuCode = despatchDetailList.get(0).getSkuCode();
        Integer count = despatchDetailList.get(0).getPieceCnt();
        List<DespatchDetail> despatchDetails = despatchDetailList.subList(1, despatchDetailList.size());
        for (DespatchDetail despatchDetail : despatchDetails) {
            if (!Objects.equals(despatchDetail.getSkuId(), skuId)) {
                List<InventoryBalance> inventoryBalanceList = inventoryBalanceDAO.selectByAreaCodeAndWarehouseCodeAndSkuIdAndCustomId(skuId, customId, pickTaskMessage.getWarehouseAreaCode(), wave.getWarehouse());
                for (InventoryBalance inventoryBalance : inventoryBalanceList) {
                    if (count <= 0) break;
                    PickTaskDetail pickTaskDetail = inventoryBalanceToPickTaskDetail(inventoryBalance, pickTaskMessage);
                    //能出整箱
                    if (count - inventoryBalance.getAvailableCnt() >= 0) {
                        pickTaskDetail.setFullTag(true);//整箱标志
                        pickTaskDetail.setPieceCnt(0);//拆零数量
                        pickTaskDetail.setPickupTag(false);//拆箱标志
                        count -= inventoryBalance.getAvailableCnt();
                    }
                    //不能出整箱
                    else {
                        pickTaskDetail.setFullTag(false);//整箱标志
                        pickTaskDetail.setPieceCnt(count);//拆零数量
                        pickTaskDetail.setPickupTag(true);//拆箱标志
                        count = 0;
                    }
                    //不用设置发运订单id，因为非整箱用于拆零使用
                    pickTaskDetail.setPid(pickTask.getId());
                    pickTaskDetail.setRowNo(++row_no);
                    pickTaskDetail.setSkuId(skuId);
                    pickTaskDetail.setSkuCode(skuCode);
                    pickTaskDetail.setPickTaskRule("先入先出规则");
                    //todo
                    //不管是否整箱，都是令分配数量直接等于可用数量，也是直接下整箱
                    inventoryBalanceDAO.updateAvailableCntAndDistributionCntById(inventoryBalance.getAvailableCnt(), inventoryBalance.getId());
                    pickTaskDetailDAO.insert(pickTaskDetail);
                }
                skuId = despatchDetail.getSkuId();
                skuCode = despatchDetail.getSkuCode();
                count = despatchDetail.getPieceCnt();
            } else count += despatchDetail.getPieceCnt();
        }
        //处理最后一条数据
        List<InventoryBalance> inventoryBalanceList = inventoryBalanceDAO.selectByAreaCodeAndWarehouseCodeAndSkuIdAndCustomId(skuId, customId, pickTaskMessage.getWarehouseAreaCode(), wave.getWarehouse());
        for (InventoryBalance inventoryBalance : inventoryBalanceList) {
            if (count <= 0) break;
            PickTaskDetail pickTaskDetail = inventoryBalanceToPickTaskDetail(inventoryBalance, pickTaskMessage);
            //能出整箱
            if (count - inventoryBalance.getAvailableCnt() >= 0) {
                pickTaskDetail.setFullTag(true);//整箱标志
                pickTaskDetail.setPieceCnt(0);//拆零数量
                pickTaskDetail.setPickupTag(false);//拆箱标志
                count -= inventoryBalance.getAvailableCnt();
            }
            //不能出整箱
            else {
                pickTaskDetail.setFullTag(false);//整箱标志
                pickTaskDetail.setPieceCnt(count);//拆零数量
                pickTaskDetail.setPickupTag(true);//拆箱标志
                count = 0;
            }
            //不用设置发运订单id，因为非整箱用于拆零使用
            pickTaskDetail.setPid(pickTask.getId());
            pickTaskDetail.setRowNo(++row_no);
            pickTaskDetail.setSkuId(skuId);
            pickTaskDetail.setSkuCode(skuCode);
            pickTaskDetail.setPickTaskRule("先入先出规则");
            //todo
            //不管是否整箱，都是令分配数量直接等于可用数量，也是直接下整箱
            inventoryBalanceDAO.updateAvailableCntAndDistributionCntById(inventoryBalance.getAvailableCnt(), inventoryBalance.getId());
            pickTaskDetailDAO.insert(pickTaskDetail);
        }
        //这里针对补货成功不会生成分拣任务单输入信息
        if (wave.getStatus() != 14) {
            pickTaskMessage.setPid(wave.getId());
            pickTaskMessage.setWarehouseCode(wave.getWarehouse());
            pickTaskMessage.setWarehouseName(wave.getWarehouseName());
            pickTaskMessage.setIsNotSingleArea(false);
            pickTaskMessage.setIsFullSearch(false);
            pickTaskMessage.setCreateTime(new Date());
            pickTaskMessage.setPickTaskRule("先入先出规则");
            pickTaskMessageDAO.insert(pickTaskMessage);
        }
        wave.setStatus(2);
        waveDAO.updateByPrimaryKeySelective(wave);
    }

    PickTaskDetail inventoryBalanceToPickTaskDetail(InventoryBalance inventoryBalance, PickTaskMessage pickTaskMessage) {
        PickTaskDetail pickTaskDetail = new PickTaskDetail();
        pickTaskDetail.setCustomerId(inventoryBalance.getCustomId());
        pickTaskDetail.setCustomerName(inventoryBalance.getCustomCode());
        pickTaskDetail.setUnit(inventoryBalance.getUnit());
        pickTaskDetail.setInboxCnt(inventoryBalance.getInventoryCnt());
        pickTaskDetail.setVolume(inventoryBalance.getVolume());
        pickTaskDetail.setWeight(inventoryBalance.getWeight());
        pickTaskDetail.setInventoryBalanceId(inventoryBalance.getId());
        pickTaskDetail.setWarehouseCode(inventoryBalance.getWarehouseCode());
        pickTaskDetail.setAreaCode(inventoryBalance.getAreaCode());
        pickTaskDetail.setLocationGroupCode(inventoryBalance.getLocationGroupCode());
        pickTaskDetail.setLocationId(inventoryBalance.getLocationId());
        pickTaskDetail.setLocationCode(inventoryBalance.getLocationCode());
        pickTaskDetail.setContainerId(inventoryBalance.getContainerId());
        pickTaskDetail.setContainerCode(inventoryBalance.getContainerCode());
        pickTaskDetail.setFullBoxDeviceId(pickTaskMessage.getFullBoxDeviceId());
        pickTaskDetail.setFullBoxDevice(pickTaskMessage.getFullboxDevice());
        pickTaskDetail.setPieceDeviceId(pickTaskMessage.getPieceDeviceId());
        pickTaskDetail.setPieceDevice(pickTaskMessage.getPieceDevice());
        pickTaskDetail.setCreateTime(new Date());
        return pickTaskDetail;
    }
}
