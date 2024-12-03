package com.bupt.service.Inwarehouse.impl;

import com.bupt.DTO.InwarehouseDTO.FreezeAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SumAndList;
import com.bupt.mapper.FreezeDetailDAO;
import com.bupt.mapper.InventoryBalanceDAO;
import com.bupt.mapper.StaffDAO;
import com.bupt.mapper.StockFreezeDAO;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.BaseService;
import com.bupt.service.Inwarehouse.FreezeService;
import com.bupt.service.authority.CodeService;
import com.bupt.service.util.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class FreezeServiceImpl extends BaseService<StockFreeze, FreezeDetail, StockFreezeDAO, FreezeDetailDAO> implements FreezeService {
    @Autowired
    private StockFreezeDAO stockFreezeDAO;
    @Autowired
    private FreezeDetailDAO freezeDetailDAO;
    @Autowired
    StaffDAO staffDAO;
    @Autowired
    private InventoryBalanceDAO inventoryBalanceDAO;
    @Autowired
    private CodeService codeService;
    @Autowired
    UtilService utilService;

    @Override
    public Integer addStockFreeze(StockFreeze stockFreeze) {
        stockFreezeDAO.insertSelective(stockFreeze);
        return stockFreeze.getId();
    }

    @Override
    public HttpResult<?> deleteStockFreeze(StockFreeze stockFreeze) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, stockFreezeDAO.deleteByPrimaryKey(stockFreeze.getId()));
    }

    @Override
    public HttpResult<?> submitStockFreeze(StockFreeze stockFreeze) {
        List<FreezeDetail> freezeDetailList = freezeDetailDAO.selectDetailByPid(stockFreeze.getId());
        for (FreezeDetail freezeDetail : freezeDetailList)
            if (freezeDetail.getStatus() == 0) return HttpResult.of(HttpResultCodeEnum.INWAREHOUSE_DETAIL_NULL);
        stockFreeze.setFreezePersonName(staffDAO.selectByPrimaryKey(stockFreeze.getFreezePersonId()).getStaffName());
        stockFreeze.setStatus(3);
        stockFreeze.setFreezeTime(new Date());
        stockFreezeDAO.updateByPrimaryKeySelective(stockFreeze);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public HttpResult<?> updateStockFreeze(StockFreeze stockFreeze) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, stockFreezeDAO.updateByPrimaryKeySelective(stockFreeze));
    }

    @Override
    public List<StockFreeze> screenStockFreeze(ScreenDTO<StockFreeze> screenDTO) {
        if (screenDTO.getSearchDTO() != null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage() - 1) * screenDTO.getSearchDTO().getNum());
        if (screenDTO.getScreen() != null && screenDTO.getScreen() != "")
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        else screenDTO.setScreen("id");
        return stockFreezeDAO.screen(screenDTO);
    }

    @Override
    public Integer screenStockFreezeNum(ScreenDTO<StockFreeze> screenDTO) {
        return stockFreezeDAO.numScreen(screenDTO);
    }

    @Override
    public HttpResult<?> submitFreezeAndDetail(FreezeAndDetail freezeAndDetail) {
        StockFreeze stockFreeze = freezeAndDetail.getStockFreeze();
        if (stockFreeze.getId() != null) return HttpResult.of(HttpResultCodeEnum.FREEZE_HAVE_SUBMIT);
        stockFreeze.setFreezeTime(new Date());
        stockFreeze.setFreezeType(1);
        stockFreeze.setStatus(1);
        stockFreeze.setFreezeCode(codeService.code("FRE"));
        Integer pId = addStockFreeze(stockFreeze);
        List<FreezeDetail> freezeDetailList = freezeAndDetail.getFreezeDetailList();
        for (FreezeDetail freezeDetail : freezeDetailList) {
            freezeDetail.setFreezeId(pId);
            addFreezeDetail(freezeDetail);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public HttpResult<?> auditFreeze(StockFreeze stockFreeze) {
        if (stockFreeze.getStatus() != null) if (stockFreeze.getStatus() == 5 || stockFreeze.getStatus() == 6)
            return HttpResult.of(HttpResultCodeEnum.NOT_AUDIT_TWICE);
        stockFreeze.setCheckPersonName(staffDAO.selectByPrimaryKey(stockFreeze.getCheckPersonId()).getStaffName());
        stockFreeze.setCheckTime(new Date());
        stockFreeze.setStatus(5);
        stockFreezeDAO.updateByPrimaryKeySelective(stockFreeze);
        List<FreezeDetail> freezeDetailList = freezeDetailDAO.selectDetailByPid(stockFreeze.getId());
        //根据库存冻结更新库存余量表
        for (FreezeDetail freezeDetail : freezeDetailList) {
            List<InventoryBalance> inventoryBalanceList = inventoryBalanceDAO.selectByFreezeDetailRange(freezeDetail);
            for (InventoryBalance inventoryBalance : inventoryBalanceList) {
                if (inventoryBalance.getAvailableCnt() != null) {
                    if (inventoryBalance.getFreezeCnt() != null)
                        inventoryBalance.setFreezeCnt(inventoryBalance.getFreezeCnt() + inventoryBalance.getAvailableCnt());
                    else inventoryBalance.setFreezeCnt(inventoryBalance.getAvailableCnt());

                    inventoryBalance.setAvailableCnt(0);
                } else return HttpResult.of(HttpResultCodeEnum.FREEZE_NOT_ENOUGH);
                inventoryBalanceDAO.updateByPrimaryKeySelective(inventoryBalance);
            }
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public HttpResult<?> unAuditFreeze(StockFreeze stockFreeze) {
        if (stockFreeze.getStatus() != null) if (stockFreeze.getStatus() == 5 || stockFreeze.getStatus() == 6)
            return HttpResult.of(HttpResultCodeEnum.NOT_AUDIT_TWICE);
        stockFreeze.setCheckPersonName(staffDAO.selectByPrimaryKey(stockFreeze.getCheckPersonId()).getStaffName());
        stockFreeze.setCheckTime(new Date());
        stockFreeze.setStatus(6);
        stockFreezeDAO.updateByPrimaryKeySelective(stockFreeze);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public HttpResult<?> releaseFreeze(StockFreeze stockFreeze) {
        if (stockFreeze.getStatus() != null) {
            if (stockFreeze.getStatus() == 4) return HttpResult.of(HttpResultCodeEnum.NOT_RELEASE_TWICE);
            if (stockFreeze.getStatus() == 6) return HttpResult.of(HttpResultCodeEnum.UNAUDIT_NOT_RELEASE);
            if (stockFreeze.getStatus() != 5) return HttpResult.of(HttpResultCodeEnum.AUDIT_RELEASE);
        }
        stockFreeze.setReleasePersonName(staffDAO.selectByPrimaryKey(stockFreeze.getReleasePersonId()).getStaffName());
        stockFreeze.setReleaseTime(new Date());
        stockFreeze.setStatus(4);
        stockFreezeDAO.updateByPrimaryKeySelective(stockFreeze);
        List<FreezeDetail> freezeDetailList = freezeDetailDAO.selectDetailByPid(stockFreeze.getId());
        //根据库存冻结更新库存余量表
        for (FreezeDetail freezeDetail : freezeDetailList) {
            List<InventoryBalance> inventoryBalanceList = inventoryBalanceDAO.selectByFreezeDetailRange(freezeDetail);
            if (freezeDetail.getCommodityNum() == null) freezeDetail.setCommodityNum(0);
            for (InventoryBalance inventoryBalance : inventoryBalanceList) {
                if (inventoryBalance.getAvailableCnt() == null) inventoryBalance.setAvailableCnt(0);
                if (inventoryBalance.getFreezeCnt() == 0 || inventoryBalance.getFreezeCnt() == null) continue;
                inventoryBalance.setAvailableCnt(freezeDetail.getCommodityNum() + inventoryBalance.getAvailableCnt());
                inventoryBalance.setFreezeCnt(inventoryBalance.getFreezeCnt() - freezeDetail.getCommodityNum());
                inventoryBalanceDAO.updateByPrimaryKeySelective(inventoryBalance);
            }
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public HttpResult<?> applyFreeze(ScreenDTO<StockFreeze> screenDTO) {
        if (screenDTO.getSearchDTO() != null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage() - 1) * screenDTO.getSearchDTO().getNum());
        if (screenDTO.getScreen() != null && !Objects.equals(screenDTO.getScreen(), ""))
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        else screenDTO.setScreen("id");
        List<StockFreeze> stockFreezeList = stockFreezeDAO.screen(screenDTO);
        if (stockFreezeList.size() == 0) return HttpResult.of(HttpResultCodeEnum.FREEZE_NULL);//没有未冻结的任务
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, stockFreezeList);
    }

    @Override
    public Integer addFreezeDetail(FreezeDetail freezeDetail) {
        return freezeDetailDAO.insertSelective(freezeDetail);
    }

    @Override
    public HttpResult<?> deleteFreezeDetail(FreezeDetail freezeDetail) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, freezeDetailDAO.deleteByPrimaryKey(freezeDetail.getId()));
    }

    @Override
    public HttpResult<?> updateFreezeDetail(FreezeDetail freezeDetail) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, freezeDetailDAO.updateByPrimaryKeySelective(freezeDetail));
    }

    @Override
    public List<FreezeDetail> searchFreezeDetailById(StockFreeze stockFreeze) {
        return freezeDetailDAO.selectDetailByPid(stockFreeze.getId());
    }

    @Override
    public List<FreezeDetail> searchFreezeDetailByRf(ScreenDTO<FreezeDetail> screenDTO) {
        StockFreeze stockFreeze = stockFreezeDAO.selectByPrimaryKey(screenDTO.getPojo().getFreezeId());
        stockFreeze.setStatus(2);
        stockFreezeDAO.updateByPrimaryKeySelective(stockFreeze);
        return screenFreezeDetail(screenDTO).getList();
    }

    @Override
    public SumAndList<FreezeDetail> screenFreezeDetail(ScreenDTO<FreezeDetail> screenDTO) {
        if (screenDTO.getSearchDTO() != null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage() - 1) * screenDTO.getSearchDTO().getNum());
        if (screenDTO.getScreen() != null && screenDTO.getScreen() != "")
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        else screenDTO.setScreen("id");
        List<FreezeDetail> freezeDetails_result = freezeDetailDAO.screen(screenDTO);
        SumAndList<FreezeDetail> freezeDetailSumAndList = new SumAndList<>();
        screenDTO.setSearchDTO(null);
        List<FreezeDetail> freezeDetailList = freezeDetailDAO.screen(screenDTO);
        double comNum = 0;
        for (FreezeDetail freezeDetail : freezeDetailList)
            if (freezeDetail.getCommodityNum() != null) comNum += freezeDetail.getCommodityNum();
        HashMap<String, Double> sums = new HashMap<>();
        sums.put("commodityNum", comNum);
        freezeDetailSumAndList.setSums(sums);
        freezeDetailSumAndList.setList(freezeDetails_result);
        return freezeDetailSumAndList;
    }

    @Override
    public Integer screenFreezeDetailNum(ScreenDTO<FreezeDetail> screenDTO) {
        return freezeDetailDAO.numScreen(screenDTO);
    }

}
