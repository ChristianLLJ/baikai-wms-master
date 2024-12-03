package com.bupt.service.Inwarehouse.impl;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.InventoryDTO;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SumAndList;
import com.bupt.DTO.wcs.inwarehouse.WcsStockInventoryTask;
import com.bupt.mapper.*;
import com.bupt.pojo.InventoryBalance;
import com.bupt.pojo.StockInventory;
import com.bupt.pojo.StockInventoryDetail;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.BaseService;
import com.bupt.service.Inwarehouse.InventoryService;
import com.bupt.service.authority.CodeService;
import com.bupt.service.util.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InventoryServiceImpl extends BaseService<StockInventory, StockInventoryDetail, StockInventoryDAO, StockInventoryDetailDAO> implements InventoryService {
    @Autowired
    private StockInventoryDAO stockInventoryDAO;
    @Autowired
    private StockInventoryDetailDAO stockInventoryDetailDAO;
    @Autowired
    private WarehouseLocationDAO warehouseLocationDAO;
    @Autowired
    private InventoryBalanceDAO inventoryBalanceDAO;
    @Autowired
    private CodeService codeService;
    @Autowired
    UtilService utilService;
    @Autowired
    StaffDAO staffDAO;

    @Override
    public Integer addStockInventory(StockInventory stockInventory) {
        stockInventory.setCheckTime(new Date());
        stockInventoryDAO.insertSelective(stockInventory);
        return stockInventory.getId();
    }

    @Override
    public HttpResult<?> deleteStockInventory(StockInventory stockInventory) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, stockInventoryDAO.deleteByPrimaryKey(stockInventory.getId()));
    }

    @Override
    public HttpResult<?> updateStockInventory(StockInventory stockInventory) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, stockInventoryDAO.updateByPrimaryKeySelective(stockInventory));
    }

    @Override
    public HttpResult<?> submitStockInventory(StockInventory stockInventory) {
        List<StockInventoryDetail> stockInventoryDetailList = stockInventoryDetailDAO.selectDetailByPid(stockInventory.getId());
        for (StockInventoryDetail stockInventoryDetail : stockInventoryDetailList)
            if (stockInventoryDetail.getStatus() == 0) return HttpResult.of(HttpResultCodeEnum.INWAREHOUSE_DETAIL_NULL);
        boolean flag = true;
        for (StockInventoryDetail stockInventoryDetail : stockInventoryDetailList) {
            if (!Objects.equals(stockInventoryDetail.getInventoryNum(), stockInventoryDetail.getSystemNum())) {
                flag = false;
                break;
            }
        }
        stockInventory.setInventoryPersonName(staffDAO.selectByPrimaryKey(stockInventory.getInventoryPersonId()).getStaffName());
        if (flag) stockInventory.setInventoryDes("盘点一致");
        else stockInventory.setInventoryDes("盘点不一致");
        stockInventory.setInventoryState(3);
        stockInventoryDAO.updateByPrimaryKeySelective(stockInventory);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public List<StockInventory> screenStockInventory(ScreenDTO<StockInventory> screenDTO) {
        if (screenDTO.getSearchDTO() != null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage() - 1) * screenDTO.getSearchDTO().getNum());
        return stockInventoryDAO.screen(screenDTO);
    }

    @Override
    public Integer screenStockInventoryNum(ScreenDTO<StockInventory> screenDTO) {
        return stockInventoryDAO.numScreen(screenDTO);
    }

    /**
     * @Description 全库盘点
     * @Author lc
     * @Date 14:35 2022/4/21
     **/
    @Override
    public HttpResult<?> fullInventory(StockInventory stockInventory) {
        List<InventoryBalance> inventoryBalanceList = inventoryBalanceDAO.selectAll();//库存余量表所有条目
        if (inventoryBalanceList.size() == 0) return HttpResult.of(HttpResultCodeEnum.INVENTORY_NULL_FAILED);
        stockInventory.setInventoryType(1);//全库盘点
        stockInventory.setInventoryState(1);
        stockInventory.setInventoryPersonName(staffDAO.selectByPrimaryKey(stockInventory.getInventoryPersonId()).getStaffName());
        stockInventory.setApplyTime(new Date());
        stockInventory.setInventoryCode(codeService.code("INV"));
        stockInventoryDAO.insert(stockInventory);
        inventoryBalanceToStockInventoryDetail(inventoryBalanceList, stockInventory);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    /**
     * @Description 当日盘点
     * @Author lc
     * @Date 14:57 2022/4/22
     **/
    @Override
    public HttpResult<?> currentDayInventory(StockInventory stockInventory) {
        List<InventoryBalance> inventoryBalanceList = inventoryBalanceDAO.selectByCurrentDate(new Date());
        if (inventoryBalanceList.size() == 0) return HttpResult.of(HttpResultCodeEnum.INVENTORY_NULL_FAILED);
        stockInventory.setInventoryType(2);//当日盘点
        stockInventory.setInventoryState(1);
        stockInventory.setInventoryPersonName(staffDAO.selectByPrimaryKey(stockInventory.getInventoryPersonId()).getStaffName());
        stockInventory.setApplyTime(new Date());
        stockInventory.setInventoryCode(codeService.code("INV"));
        stockInventoryDAO.insert(stockInventory);
        inventoryBalanceToStockInventoryDetail(inventoryBalanceList, stockInventory);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    /**
     * @Description 动态盘点
     * @Author lc
     * @Date 16:57 2022/4/22
     **/
    @Override
    public HttpResult<?> dynamicInventory(InventoryDTO inventoryDTO) {
        List<InventoryBalance> inventoryBalanceList = inventoryDTO.getInventoryBalanceList();
        StockInventory stockInventory = inventoryDTO.getStockInventory();
        if (inventoryBalanceList.size() == 0) return HttpResult.of(HttpResultCodeEnum.INVENTORY_NULL_FAILED);
        stockInventory.setInventoryType(3);//动态盘点
        stockInventory.setInventoryState(1);
        stockInventory.setInventoryCode(codeService.code("INV"));
        stockInventory.setApplyTime(new Date());
        stockInventory.setInventoryPersonName(staffDAO.selectByPrimaryKey(stockInventory.getInventoryPersonId()).getStaffName());
        stockInventoryDAO.insert(stockInventory);
        inventoryBalanceToStockInventoryDetail(inventoryBalanceList, stockInventory);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    /***
     *@Description 将库存盘点任务发送给WCS
     *@Author lc
     *@Date 15:23 2023/4/15
     *@Param [screenDTO]
     *@Return com.bupt.result.HttpResult<?>
     **/
    @Override
    public HttpResult<?> wcsGetStockInventoryTask(ScreenDTO<StockInventory> screenDTO) {
        List<WcsStockInventoryTask> wcsStockInventoryTaskList = new LinkedList<>();
        screenDTO.getPojo().setInventoryState(5);//筛选已经审核通过的盘点单
        List<StockInventory> stockInventoryList = screenStockInventory(screenDTO);
        for (StockInventory stockInventory : stockInventoryList) {
            stockInventory.setInventoryState(2);//盘点中
            stockInventoryDAO.updateByPrimaryKeySelective(stockInventory);
            wcsStockInventoryTaskList.add(stockInventoryToWcsStockInventory(stockInventory));
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, wcsStockInventoryTaskList);
    }

    /***
     *@Description 盘点结果回传
     *@Author lc
     **/
    @Override
    public HttpResult<?> wcsStockInventoryTaskExecute(HeadAndDetail<com.bupt.DTO.wcs.inwarehouse.StockInventory, com.bupt.DTO.wcs.inwarehouse.StockInventoryDetail> headAndDetail) {
        com.bupt.DTO.wcs.inwarehouse.StockInventory wcsStockInventory = headAndDetail.getHead();
        StockInventory stockInventory = stockInventoryDAO.selectByInventoryCode(wcsStockInventory.getWmsInventoryCode());
        List<StockInventoryDetail> stockInventoryDetailList = stockInventoryDetailDAO.selectDetailByPid(stockInventory.getId());
        List<com.bupt.DTO.wcs.inwarehouse.StockInventoryDetail> wcsStockInventoryDetailList = headAndDetail.getDetails();
        int size = Math.min(wcsStockInventoryDetailList.size(), stockInventoryDetailList.size());
        stockInventory.setInventoryState(3);//已完成
        int count = 0;//用来记录盘点不一致的条目
        for (int i = 0, j = 0; i < size && j < size; i++, j++) {
            if (wcsStockInventoryDetailList.get(i).getInventoryNum() != null) {
                stockInventoryDetailList.get(j).setInventoryNum(wcsStockInventoryDetailList.get(i).getInventoryNum());
                stockInventoryDetailList.get(j).setStatus(1);
                stockInventoryDetailDAO.updateByPrimaryKeySelective(stockInventoryDetailList.get(j));
            }
            if (!Objects.equals(wcsStockInventoryDetailList.get(i).getInventoryNum(), wcsStockInventoryDetailList.get(i).getSystemNum()))
                count++;
        }
        String str = Integer.toString(count);
        stockInventory.setInventoryDes(str + "条表细盘点不一致");
        stockInventoryDAO.updateByPrimaryKeySelective(stockInventory);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, count);
    }

    public WcsStockInventoryTask stockInventoryToWcsStockInventory(StockInventory stockInventory) {
        WcsStockInventoryTask wcsStockInventoryTask = new WcsStockInventoryTask();
        com.bupt.DTO.wcs.inwarehouse.StockInventory wcsStockInventory = new com.bupt.DTO.wcs.inwarehouse.StockInventory();
        wcsStockInventory.setInventoryType(stockInventory.getInventoryType());
        wcsStockInventory.setInventoryState(stockInventory.getInventoryState());
        wcsStockInventory.setApplyTime(stockInventory.getApplyTime());
        wcsStockInventory.setCheckerId(stockInventory.getCheckPersonId());
        wcsStockInventory.setCheckerName(stockInventory.getCheckPersonName());
        wcsStockInventory.setCheckTime(stockInventory.getCheckTime());
        wcsStockInventory.setInventoryPersonName(stockInventory.getInventoryPersonName());
        wcsStockInventory.setWmsInventoryCode(stockInventory.getInventoryCode());
        List<com.bupt.DTO.wcs.inwarehouse.StockInventoryDetail> wcsStockInventoryDetailList = new LinkedList<>();
        List<StockInventoryDetail> stockInventoryDetailList = searchStockInventoryDetailById(stockInventory);
        for (StockInventoryDetail stockInventoryDetail : stockInventoryDetailList) {
            com.bupt.DTO.wcs.inwarehouse.StockInventoryDetail wcsStockInventoryDetail = new com.bupt.DTO.wcs.inwarehouse.StockInventoryDetail();
            wcsStockInventoryDetail.setLocationId(stockInventoryDetail.getLocationId());
            wcsStockInventoryDetail.setLocationCode(stockInventoryDetail.getLocationCode());
            wcsStockInventoryDetail.setLocationName(stockInventoryDetail.getLocationName());
            wcsStockInventoryDetail.setSkuId(stockInventoryDetail.getSkuId());
            wcsStockInventoryDetail.setSkuCode(stockInventoryDetail.getSkuCode());
            wcsStockInventoryDetail.setSkuName(stockInventoryDetail.getSkuName());
            wcsStockInventoryDetail.setPackageId(stockInventoryDetail.getContainerId());
            wcsStockInventoryDetail.setPackageCode(stockInventoryDetail.getContainerCode());
            wcsStockInventoryDetail.setPackageName(stockInventoryDetail.getContainerName());
            wcsStockInventoryDetail.setSystemNum(stockInventoryDetail.getSystemNum());
            wcsStockInventoryDetailList.add(wcsStockInventoryDetail);
        }
        wcsStockInventoryTask.setStockInventory(wcsStockInventory);
        wcsStockInventoryTask.setStockInventoryDetailList(wcsStockInventoryDetailList);
        return wcsStockInventoryTask;
    }

    /**
     * @Description 指定日期盘点
     * @Author lc
     * @Date 16:57 2022/4/22
     **/
    @Override
    public HttpResult<?> selectDateInventory(StockInventory stockInventory) {
        List<InventoryBalance> inventoryBalanceList = inventoryBalanceDAO.selectByDateRange(stockInventory.getMoveStartTime(), stockInventory.getMoveFinishTime());
        if (inventoryBalanceList.size() == 0) return HttpResult.of(HttpResultCodeEnum.INVENTORY_NULL_FAILED);
        stockInventory.setInventoryType(4);//指定日期盘点
        stockInventory.setInventoryState(1);
        stockInventory.setInventoryCode(codeService.code("INV"));
        stockInventory.setApplyTime(new Date());
        stockInventory.setInventoryPersonName(staffDAO.selectByPrimaryKey(stockInventory.getInventoryPersonId()).getStaffName());
        stockInventoryDAO.insert(stockInventory);
        inventoryBalanceToStockInventoryDetail(inventoryBalanceList, stockInventory);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    /**
     * 申请盘点
     *
     * @return
     */
    @Override
    public HttpResult<?> applyInventory(ScreenDTO<StockInventory> screenDTO) {
        if (screenDTO.getSearchDTO() != null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage() - 1) * screenDTO.getSearchDTO().getNum());
        List<StockInventory> stockInventoryList = stockInventoryDAO.screen(screenDTO);//获取未盘点的任务
        if (stockInventoryList.size() == 0) return HttpResult.of(HttpResultCodeEnum.INVENTORY_NULL);//没有未盘点的任务
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, stockInventoryList);
    }


    void inventoryBalanceToStockInventoryDetail(List<InventoryBalance> inventoryBalanceList, StockInventory stockInventory) {
        for (InventoryBalance inventoryBalance : inventoryBalanceList) {
            StockInventoryDetail stockInventoryDetail = new StockInventoryDetail();
            stockInventoryDetail.setInventoryId(stockInventory.getId());
            stockInventoryDetail.setCustomId(inventoryBalance.getCustomId());
            stockInventoryDetail.setCustomCode(inventoryBalance.getCustomCode());
            stockInventoryDetail.setCustomName(inventoryBalance.getCustomName());
            stockInventoryDetail.setCommodityId(inventoryBalance.getCommodityId());
            stockInventoryDetail.setCommodityCode(inventoryBalance.getCommodityCode());
            stockInventoryDetail.setCommodityName(inventoryBalance.getCommodityCode());
            stockInventoryDetail.setWarehouseId(inventoryBalance.getWarehouseId());
            stockInventoryDetail.setWarehouseCode(inventoryBalance.getWarehouseCode());
            stockInventoryDetail.setWarehouseName(inventoryBalance.getWarehouseName());
            stockInventoryDetail.setLocationId(inventoryBalance.getLocationId());
            stockInventoryDetail.setLocationCode(inventoryBalance.getLocationCode());
            stockInventoryDetail.setLocationName(inventoryBalance.getLocationName());
            stockInventoryDetail.setContainerId(inventoryBalance.getContainerId());
            stockInventoryDetail.setContainerCode(inventoryBalance.getContainerCode());
            stockInventoryDetail.setContainerName(inventoryBalance.getContainerName());
            stockInventoryDetail.setSkuId(inventoryBalance.getSkuId());
            stockInventoryDetail.setSkuCode(inventoryBalance.getSkuCode());
            stockInventoryDetail.setSkuName(inventoryBalance.getSkuName());
            stockInventoryDetail.setSystemNum(inventoryBalance.getTotalCnt());
            stockInventoryDetail.setProductCompany(inventoryBalance.getProductCompany());
            stockInventoryDetail.setProductTime(inventoryBalance.getProductTime());
            stockInventoryDetail.setProductBatch(inventoryBalance.getProductBatch());
            stockInventoryDetail.setTraceCode(inventoryBalance.getTraceCode());
            stockInventoryDetailDAO.insert(stockInventoryDetail);
        }
    }

    @Override
    public List<StockInventoryDetail> screenDifference(StockInventory stockInventory) {
        List<StockInventoryDetail> stockInventoryDetails = stockInventoryDetailDAO.selectDetailByPid(stockInventory.getId());
        stockInventoryDetails.removeIf(stockInventoryDetail -> Objects.equals(stockInventoryDetail.getSystemNum(), stockInventoryDetail.getInventoryNum()));
        return stockInventoryDetails;
    }


    @Override
    public Integer addStockInventoryDetail(StockInventoryDetail stockInventoryDetail) {
        return stockInventoryDetailDAO.insertSelective(stockInventoryDetail);
    }

    @Override
    public HttpResult<?> deleteStockInventoryDetail(StockInventoryDetail stockInventoryDetail) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, stockInventoryDetailDAO.deleteByPrimaryKey(stockInventoryDetail.getId()));
    }

    @Override
    public HttpResult<?> updateStockInventoryDetail(StockInventoryDetail stockInventoryDetail) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, stockInventoryDetailDAO.updateByPrimaryKeySelective(stockInventoryDetail));
    }

    @Override
    public List<StockInventoryDetail> searchStockInventoryDetailById(StockInventory stockInventory) {
        return stockInventoryDetailDAO.selectDetailByPid(stockInventory.getId());
    }

    @Override
    public List<StockInventoryDetail> searchStockInventoryDetailByRf(ScreenDTO<StockInventoryDetail> screenDTO) {
        StockInventory stockInventory = stockInventoryDAO.selectByPrimaryKey(screenDTO.getPojo().getInventoryId());
        stockInventory.setInventoryState(2);
        stockInventoryDAO.updateByPrimaryKeySelective(stockInventory);
        return screenStockInventoryDetail(screenDTO).getList();
    }

    @Override
    public SumAndList<StockInventoryDetail> screenStockInventoryDetail(ScreenDTO<StockInventoryDetail> screenDTO) {
        if (screenDTO.getSearchDTO() != null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage() - 1) * screenDTO.getSearchDTO().getNum());
        if (screenDTO.getScreen() != null && screenDTO.getScreen() != "")
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        else screenDTO.setScreen("id");
        List<StockInventoryDetail> stockInventoryDetails_result = stockInventoryDetailDAO.screen(screenDTO);
        SumAndList<StockInventoryDetail> stockInventoryDetailSumAndList = new SumAndList<>();
        screenDTO.setSearchDTO(null);
        List<StockInventoryDetail> stockInventoryDetailList = stockInventoryDetailDAO.screen(screenDTO);
        double sysNum = 0, invNum = 0;
        for (StockInventoryDetail stockInventoryDetail : stockInventoryDetailList) {
            if (stockInventoryDetail.getSystemNum() != null) sysNum += stockInventoryDetail.getSystemNum();
            if (stockInventoryDetail.getInventoryNum() != null) invNum += stockInventoryDetail.getInventoryNum();
        }
        HashMap<String, Double> sums = new HashMap<>();
        sums.put("systemNum", sysNum);
        sums.put("inventoryNum", invNum);
        stockInventoryDetailSumAndList.setSums(sums);
        stockInventoryDetailSumAndList.setList(stockInventoryDetails_result);
        return stockInventoryDetailSumAndList;
    }

    @Override
    public Integer screenStockInventoryDetailNum(ScreenDTO<StockInventoryDetail> screenDTO) {
        return stockInventoryDetailDAO.numScreen(screenDTO);
    }
}
