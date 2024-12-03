package com.bupt.service.Inwarehouse.impl;

import com.bupt.DTO.ChangeScreenDTO;
import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.InwarehouseDTO.StockChangeAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SumAndList;
import com.bupt.DTO.wcs.inwarehouse.WcsStockChangeTask;
import com.bupt.mapper.InventoryBalanceDAO;
import com.bupt.mapper.StaffDAO;
import com.bupt.mapper.StockChangeDAO;
import com.bupt.mapper.StockChangeDetailDAO;
import com.bupt.pojo.InventoryBalance;
import com.bupt.pojo.StockChange;
import com.bupt.pojo.StockChangeDetail;
import com.bupt.pojo.StockInventoryDetail;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.BaseService;
import com.bupt.service.Inwarehouse.ChangeService;
import com.bupt.service.authority.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ChangeServiceImpl extends BaseService<StockChange, StockChangeDetail, StockChangeDAO, StockChangeDetailDAO> implements ChangeService {
    @Autowired
    private StockChangeDAO stockChangeDAO;
    @Autowired
    private StockChangeDetailDAO stockChangeDetailDAO;
    @Autowired
    private InventoryBalanceDAO inventoryBalanceDAO;
    @Autowired
    private CodeService codeService;
    @Autowired
    StaffDAO staffDAO;

    @Override
    public Integer addStockChange(StockChange stockChange) {
        stockChangeDAO.insertSelective(stockChange);
        return stockChange.getId();
    }

    @Override
    public HttpResult<?> deleteStockChange(StockChange stockChange) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, stockChangeDAO.deleteByPrimaryKey(stockChange.getId()));
    }

    @Override
    public HttpResult<?> updateStockChange(StockChange stockChange) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, stockChangeDAO.updateByPrimaryKeySelective(stockChange));
    }

    @Override
    public HttpResult<?> submitStockChange(StockChange stockChange) {
        List<StockChangeDetail> stockChangeDetailList = stockChangeDetailDAO.selectDetailByPid(stockChange.getId());
        for (StockChangeDetail stockChangeDetail : stockChangeDetailList)
            if (stockChangeDetail.getStatus() == 0) return HttpResult.of(HttpResultCodeEnum.INWAREHOUSE_DETAIL_NULL);
        stockChange.setChangePersonName(staffDAO.selectByPrimaryKey(stockChange.getChangePersonId()).getStaffName());
        stockChange.setChangeState(3);
        stockChangeDAO.updateByPrimaryKeySelective(stockChange);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public List<StockChange> screenStockChange(ScreenDTO<StockChange> screenDTO) {
        if (screenDTO.getSearchDTO() != null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage() - 1) * screenDTO.getSearchDTO().getNum());
        return stockChangeDAO.screen(screenDTO);
    }

    @Override
    public Integer screenStockChangeNum(ScreenDTO<StockChange> screenDTO) {
        return stockChangeDAO.numScreen(screenDTO);
    }

    @Override
    @Transactional
    public HttpResult<?> submitStockChangeAndDetail(StockChangeAndDetail stockChangeAndDetail) {
        StockChange stockChange = stockChangeAndDetail.getStockChange();
        if (stockChange.getId() != null) return HttpResult.of(HttpResultCodeEnum.CHANGE_HAVE_SUBMIT);
        stockChange.setCreateTime(new Date());
        stockChange.setChangeState(1);
        stockChange.setChangeCode(codeService.code("CHG"));
        Integer pId = addStockChange(stockChange);
        for (StockChangeDetail stockChangeDetail : stockChangeAndDetail.getStockChangeDetailList()) {
            stockChangeDetail.setChangeId(pId);
            addStockChangeDetail(stockChangeDetail);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public HttpResult<?> auditChange(StockChange stockChange) {
        if (stockChange.getChangeState() != null)
            if (stockChange.getChangeState() == 5 || stockChange.getChangeState() == 6)
                return HttpResult.of(HttpResultCodeEnum.NOT_AUDIT_TWICE);
        stockChange.setCheckPersonName(staffDAO.selectByPrimaryKey(stockChange.getCheckPersonId()).getStaffName());
        stockChange.setCheckTime(new Date());
        stockChange.setChangeState(5);
        stockChangeDAO.updateByPrimaryKeySelective(stockChange);
        List<StockChangeDetail> stockChangeDetailList = stockChangeDetailDAO.selectDetailByPid(stockChange.getId());
        //根据库存改变表更新库存余量表
        for (StockChangeDetail stockChangeDetail : stockChangeDetailList) {
            List<InventoryBalance> inventoryBalanceList = inventoryBalanceDAO.selectByChangeRange(stockChangeDetail);
            for (InventoryBalance inventoryBalance : inventoryBalanceList) {
                if (stockChangeDetail.getChangeNum() != null) {
                    inventoryBalance.setTotalCnt(inventoryBalance.getTotalCnt() - inventoryBalance.getAvailableCnt() + stockChangeDetail.getChangeNum());
                    inventoryBalance.setAvailableCnt(stockChangeDetail.getChangeNum());
                }
                if (stockChangeDetail.getChangeWeight() != null)
                    inventoryBalance.setWeight(stockChangeDetail.getChangeWeight());
                if (stockChangeDetail.getChangeVolumn() != null)
                    inventoryBalance.setVolume(stockChangeDetail.getChangeVolumn());
                if (stockChangeDetail.getChangePrice() != null)
                    inventoryBalance.setPrice(stockChangeDetail.getChangePrice());
                inventoryBalanceDAO.updateByPrimaryKeySelective(inventoryBalance);
            }
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public HttpResult<?> unAuditChange(StockChange stockChange) {
        if (stockChange.getChangeState() != null)
            if (stockChange.getChangeState() == 5 || stockChange.getChangeState() == 6)
                return HttpResult.of(HttpResultCodeEnum.NOT_AUDIT_TWICE);
        stockChange.setCheckPersonName(staffDAO.selectByPrimaryKey(stockChange.getCheckPersonId()).getStaffName());
        stockChange.setChangeState(6);
        stockChange.setCheckTime(new Date());
        stockChangeDAO.updateByPrimaryKeySelective(stockChange);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public HttpResult<?> applyChange(ScreenDTO<StockChange> screenDTO) {
        if (screenDTO.getSearchDTO() != null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage() - 1) * screenDTO.getSearchDTO().getNum());
        List<StockChange> stockChangeList = stockChangeDAO.screen(screenDTO);
        if (stockChangeList.size() == 0) return HttpResult.of(HttpResultCodeEnum.CHANGE_NULL);//没有未改变的任务
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, stockChangeList);
    }

    @Override
    public HttpResult<?> wcsGetStockChangeTask(ScreenDTO<StockChange> screenDTO) {
        List<WcsStockChangeTask> wcsStockChangeTaskList = new LinkedList<>();
        screenDTO.getPojo().setChangeState(5);//筛选已经审核通过的改变单
        List<StockChange> stockChangeList = screenStockChange(screenDTO);
        for (StockChange stockChange : stockChangeList) {
            stockChange.setChangeState(2);//改变中
            stockChangeDAO.updateByPrimaryKeySelective(stockChange);
            wcsStockChangeTaskList.add(stockChangeToWcsStockChange(stockChange));
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, wcsStockChangeTaskList);
    }

    @Override
    public HttpResult<?> wcsStockChangeTaskExecute(HeadAndDetail<com.bupt.DTO.wcs.inwarehouse.StockChange, com.bupt.DTO.wcs.inwarehouse.StockChangeDetail> headAndDetail) {
        com.bupt.DTO.wcs.inwarehouse.StockChange wcsStockChange = headAndDetail.getHead();
        StockChange stockChange = stockChangeDAO.selectByChangeCode(wcsStockChange.getWmsChangeCode());
        stockChange.setChangeState(3);//已完成
        stockChangeDAO.updateByPrimaryKeySelective(stockChange);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    WcsStockChangeTask stockChangeToWcsStockChange(StockChange stockChange) {
        WcsStockChangeTask wcsStockChangeTask = new WcsStockChangeTask();
        com.bupt.DTO.wcs.inwarehouse.StockChange wcsStockChange = new com.bupt.DTO.wcs.inwarehouse.StockChange();
        wcsStockChange.setChangeState(stockChange.getChangeState());
        wcsStockChange.setCreateTime(stockChange.getCreateTime());
        wcsStockChange.setChangeReason(stockChange.getChangeReason());
        wcsStockChange.setChangePersonId(stockChange.getChangePersonId());
        wcsStockChange.setChangePersonName(stockChange.getChangePersonName());
        wcsStockChange.setCheckerId(stockChange.getCheckPersonId());
        wcsStockChange.setCheckerName(stockChange.getCheckPersonName());
        wcsStockChange.setCheckTime(stockChange.getCheckTime());
        wcsStockChange.setWmsChangeCode(stockChange.getChangeCode());
        List<com.bupt.DTO.wcs.inwarehouse.StockChangeDetail> wcsStockChangeDetailList = new LinkedList<>();
        List<StockChangeDetail> stockChangeDetailList = searchStockChangeDetailById(stockChange);
        for (StockChangeDetail stockChangeDetail : stockChangeDetailList) {
            com.bupt.DTO.wcs.inwarehouse.StockChangeDetail wcsStockChangeDetail = new com.bupt.DTO.wcs.inwarehouse.StockChangeDetail();
            wcsStockChangeDetail.setUnit(stockChangeDetail.getUnit());
            wcsStockChangeDetail.setChangeNum(stockChangeDetail.getChangeNum());
            wcsStockChangeDetail.setLocationId(stockChangeDetail.getLocationId());
            wcsStockChangeDetail.setLocationCode(stockChangeDetail.getLocationCode());
            wcsStockChangeDetail.setLocationName(stockChangeDetail.getLocationName());
            wcsStockChangeDetail.setSkuId(stockChangeDetail.getSkuId());
            wcsStockChangeDetail.setSkuCode(stockChangeDetail.getSkuCode());
            wcsStockChangeDetail.setSkuName(stockChangeDetail.getSkuName());
            wcsStockChangeDetailList.add(wcsStockChangeDetail);
        }
        wcsStockChangeTask.setStockChange(wcsStockChange);
        wcsStockChangeTask.setStockChangeDetailList(wcsStockChangeDetailList);
        return wcsStockChangeTask;
    }

    @Override
    @Transactional
    public Integer addStockChangeDetail(StockChangeDetail stockChangeDetail) {
        return stockChangeDetailDAO.insertSelective(stockChangeDetail);
    }

    @Override
    @Transactional
    public HttpResult<?> deleteStockChangeDetail(StockChangeDetail stockChangeDetail) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, stockChangeDetailDAO.deleteByPrimaryKey(stockChangeDetail.getId()));
    }

    @Override
    public HttpResult<?> updateStockChangeDetail(StockChangeDetail stockChangeDetail) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, stockChangeDetailDAO.updateByPrimaryKeySelective(stockChangeDetail));
    }

    @Override
    public SumAndList<StockChangeDetail> screenStockChangeDetail(ScreenDTO<StockChangeDetail> screenDTO) {
        if (screenDTO.getSearchDTO() != null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage() - 1) * screenDTO.getSearchDTO().getNum());
        List<StockChangeDetail> stockChangeDetails_result = stockChangeDetailDAO.screen(screenDTO);
        SumAndList<StockChangeDetail> stockChangeDetailSumAndList = new SumAndList<>();
        screenDTO.setSearchDTO(null);
        List<StockChangeDetail> stockChangeDetailList = stockChangeDetailDAO.screen(screenDTO);
        double chaNum = 0, comNum = 0, chaWeight = 0, chaVol = 0, chaPri = 0;
        for (StockChangeDetail stockChangeDetail : stockChangeDetailList) {
            if (stockChangeDetail.getChangeNum() != null) chaNum += stockChangeDetail.getChangeNum();
            if (stockChangeDetail.getCommodityNum() != null) comNum += stockChangeDetail.getCommodityNum();
            if (stockChangeDetail.getChangeWeight() != null) chaWeight += stockChangeDetail.getChangeWeight();
            if (stockChangeDetail.getChangeVolumn() != null) chaVol += stockChangeDetail.getChangeVolumn();
            if (stockChangeDetail.getChangePrice() != null) chaPri += stockChangeDetail.getChangePrice();
        }
        HashMap<String, Double> sums = new HashMap<>();
        sums.put("changeNum", chaNum);
        sums.put("commodityNum", comNum);
        sums.put("changeWeight", chaWeight);
        sums.put("changeVolumn", chaVol);
        sums.put("changePrice", chaPri);
        stockChangeDetailSumAndList.setSums(sums);
        stockChangeDetailSumAndList.setList(stockChangeDetails_result);
        return stockChangeDetailSumAndList;
    }

    @Override
    public Integer screenStockChangeDetailNum(ScreenDTO<StockChangeDetail> screenDTO) {
        return stockChangeDetailDAO.numScreen(screenDTO);
    }

    @Override
    public List<StockChangeDetail> searchStockChangeDetailById(StockChange stockChange) {
        return stockChangeDetailDAO.selectDetailByPid(stockChange.getId());
    }

    @Override
    public List<StockChangeDetail> searchStockChangeDetailByRf(ScreenDTO<StockChangeDetail> screenDTO) {
        StockChange stockChange = stockChangeDAO.selectByPrimaryKey(screenDTO.getPojo().getChangeId());
        stockChange.setChangeState(2);
        stockChangeDAO.updateByPrimaryKeySelective(stockChange);
        return screenStockChangeDetail(screenDTO).getList();
    }

    @Override
    public StockChangeAndDetail changeByDifference(ChangeScreenDTO<StockChange> changeScreenDTO) {
        List<StockChangeDetail> stockChangeDetailList = new ArrayList<>();
        StockChangeAndDetail stockChangeAndDetail = new StockChangeAndDetail();
        StockChange stockChange = changeScreenDTO.getPojo();
        stockChange.setChangeState(1);//根据差异单生成调整单
        stockChange.setChangeReason("根据盘点差异生成调整单");
        stockChange.setChangePersonName(staffDAO.selectByPrimaryKey(stockChange.getChangePersonId()).getStaffName());
        stockChange.setChangeCode(codeService.code("CHG"));
        stockChange.setCreateTime(new Date());
        stockChangeAndDetail.setStockChange(stockChange);
        stockChangeDAO.insertSelective(stockChange);
        for (StockInventoryDetail stockInventoryDetail : changeScreenDTO.getStockInventoryDetailList()) {
            StockChangeDetail stockChangeDetail = new StockChangeDetail();
            stockChangeDetail.setChangeId(stockChange.getId());
            stockChangeDetail.setCustomId(stockInventoryDetail.getCustomId());
            stockChangeDetail.setCustomName(stockInventoryDetail.getCustomName());
            stockChangeDetail.setCommodityId(stockInventoryDetail.getCommodityId());
            stockChangeDetail.setCommodityName(stockInventoryDetail.getCommodityName());
            stockChangeDetail.setChangeNum(stockInventoryDetail.getInventoryNum());//改变数量
            stockChangeDetail.setLocationId(stockInventoryDetail.getLocationId());
            stockChangeDetail.setLocationCode(stockInventoryDetail.getLocationName());
            stockChangeDetail.setContainerId(stockInventoryDetail.getContainerId());
            stockChangeDetail.setContainerCode(stockInventoryDetail.getContainerName());
            stockChangeDetail.setSkuId(stockInventoryDetail.getSkuId());
            stockChangeDetail.setSkuName(stockInventoryDetail.getSkuName());
            stockChangeDetail.setSkuCode(stockInventoryDetail.getSkuCode());
            stockChangeDetail.setProductCompany(stockInventoryDetail.getProductCompany());
            stockChangeDetail.setProductTime(stockInventoryDetail.getProductTime());
            stockChangeDetail.setProductBatch(stockInventoryDetail.getProductBatch());
            stockChangeDetail.setCommodityNum((double) stockInventoryDetail.getSystemNum());//商品数量
            stockChangeDetail.setTraceCode(stockInventoryDetail.getTraceCode());
            stockChangeDetailDAO.insert(stockChangeDetail);
            stockChangeDetailList.add(stockChangeDetail);
        }
        stockChangeAndDetail.setStockChangeDetailList(stockChangeDetailList);
        return stockChangeAndDetail;
    }
}
