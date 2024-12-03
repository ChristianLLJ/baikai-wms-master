package com.bupt.service.Inwarehouse.impl;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.InwarehouseDTO.InventoryMoveAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SumAndList;
import com.bupt.DTO.wcs.inwarehouse.TInventoryMove;
import com.bupt.DTO.wcs.inwarehouse.TInventoryMoveDetail;
import com.bupt.DTO.wcs.inwarehouse.WcsInventoryMoveTask;
import com.bupt.mapper.InventoryBalanceDAO;
import com.bupt.mapper.InventoryMoveDAO;
import com.bupt.mapper.InventoryMoveDetailDAO;
import com.bupt.mapper.StaffDAO;
import com.bupt.pojo.InventoryBalance;
import com.bupt.pojo.InventoryMove;
import com.bupt.pojo.InventoryMoveDetail;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.BaseService;
import com.bupt.service.Inwarehouse.MoveService;
import com.bupt.service.authority.CodeService;
import com.bupt.service.util.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class MoveServiceImpl extends BaseService<InventoryMove, InventoryMoveDetail, InventoryMoveDAO, InventoryMoveDetailDAO> implements MoveService {
    @Autowired
    private InventoryMoveDAO inventoryMoveDAO;
    @Autowired
    private InventoryMoveDetailDAO inventoryMoveDetailDAO;
    @Autowired
    private InventoryBalanceDAO inventoryBalanceDAO;
    @Autowired
    private CodeService codeService;
    @Autowired
    UtilService utilService;
    @Autowired
    StaffDAO staffDAO;

    @Override
    @Transactional
    public Integer addInventoryMove(InventoryMove inventoryMove) {
        inventoryMoveDAO.insertSelective(inventoryMove);
        return inventoryMove.getId();
    }

    @Override
    @Transactional
    public HttpResult<?> deleteInventoryMove(InventoryMove inventoryMove) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, inventoryMoveDAO.deleteByPrimaryKey(inventoryMove.getId()));
    }

    @Override
    public HttpResult<?> submitInventoryMove(InventoryMove inventoryMove) {
        inventoryMove.setMoveTime(new Date());
        inventoryMove.setStatus(3);
        inventoryMove.setMovePersonName(staffDAO.selectByPrimaryKey(inventoryMove.getMovePersonId()).getStaffName());
        List<InventoryMoveDetail> inventoryMoveDetailList = inventoryMoveDetailDAO.selectDetailByPid(inventoryMove.getId());
        for (InventoryMoveDetail inventoryMoveDetail : inventoryMoveDetailList)
            if (inventoryMoveDetail.getStatus() == 0) return HttpResult.of(HttpResultCodeEnum.INWAREHOUSE_DETAIL_NULL);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    @Transactional
    public HttpResult<?> updateInventoryMove(InventoryMove inventoryMove) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, inventoryMoveDAO.updateByPrimaryKeySelective(inventoryMove));
    }

    @Override
    public List<InventoryMove> screenInventoryMove(ScreenDTO<InventoryMove> screenDTO) {
        if (screenDTO.getSearchDTO() != null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage() - 1) * screenDTO.getSearchDTO().getNum());
        return inventoryMoveDAO.screen(screenDTO);
    }

    @Override
    public Integer screenInventoryMoveNum(ScreenDTO<InventoryMove> screenDTO) {
        return inventoryMoveDAO.numScreen(screenDTO);
    }

    @Override
    public HttpResult<?> applyMove(ScreenDTO<InventoryMove> screenDTO) {
        if (screenDTO.getSearchDTO() != null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage() - 1) * screenDTO.getSearchDTO().getNum());
        if (screenDTO.getScreen() != null && screenDTO.getScreen() != "")
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        else screenDTO.setScreen("id");
        List<InventoryMove> inventoryMoveList = inventoryMoveDAO.screen(screenDTO);//获取未移库的任务
        if (inventoryMoveList.size() == 0) return HttpResult.of(HttpResultCodeEnum.INVENTORY_NULL);//没有未移库的任务
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, inventoryMoveList);
    }

    @Override
    public HttpResult<?> wcsGetInventoryMoveTask(ScreenDTO<InventoryMove> screenDTO) {
        List<WcsInventoryMoveTask> wcsInventoryMoveTaskList = new LinkedList<>();
        screenDTO.getPojo().setStatus(5);//筛选已经审核通过的移库单
        List<InventoryMove> inventoryMoveList = screenInventoryMove(screenDTO);
        for (InventoryMove inventoryMove : inventoryMoveList) {
            inventoryMove.setStatus(2);//移库中
            inventoryMoveDAO.updateByPrimaryKeySelective(inventoryMove);
            wcsInventoryMoveTaskList.add(inventoryMoveToWcsInventoryMove(inventoryMove));
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, wcsInventoryMoveTaskList);
    }

    @Override
    public HttpResult<?> wcsInventoryMoveTaskExecute(HeadAndDetail<TInventoryMove, TInventoryMoveDetail> headAndDetail) {
        TInventoryMove tInventoryMove = headAndDetail.getHead();
        InventoryMove inventoryMove = inventoryMoveDAO.selectByMoveCode(tInventoryMove.getWmsMoveCode());
        inventoryMove.setStatus(3);//已完成
        inventoryMoveDAO.updateByPrimaryKeySelective(inventoryMove);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    WcsInventoryMoveTask inventoryMoveToWcsInventoryMove(InventoryMove inventoryMove) {
        WcsInventoryMoveTask wcsInventoryMoveTask = new WcsInventoryMoveTask();
        TInventoryMove tInventoryMove = new TInventoryMove();
        tInventoryMove.setStatus(inventoryMove.getStatus());
        tInventoryMove.setType(inventoryMove.getType());
        tInventoryMove.setCreateTime(inventoryMove.getCreateTime());
        tInventoryMove.setMovePersonId(inventoryMove.getMovePersonId());
        tInventoryMove.setMovePersonName(inventoryMove.getMovePersonName());
        tInventoryMove.setCheckerId(inventoryMove.getCheckPersonId());
        tInventoryMove.setCheckerName(inventoryMove.getCheckPersonName());
        tInventoryMove.setCheckTime(inventoryMove.getCheckTime());
        tInventoryMove.setWmsMoveCode(inventoryMove.getMoveCode());
        List<TInventoryMoveDetail> tInventoryMoveDetailList = new LinkedList<>();
        List<InventoryMoveDetail> inventoryMoveDetailList = searchInventoryMoveDetailById(inventoryMove);
        for (InventoryMoveDetail inventoryMoveDetail : inventoryMoveDetailList) {
            TInventoryMoveDetail tInventoryMoveDetail = new TInventoryMoveDetail();
            tInventoryMoveDetail.setSourceLocationId(inventoryMoveDetail.getSourceLocationId());
            tInventoryMoveDetail.setSourceLocationCode(inventoryMoveDetail.getSourceLocationCode());
            tInventoryMoveDetail.setSourceLocationName(inventoryMoveDetail.getSourceLocationName());
            tInventoryMoveDetail.setTargetLocationId(inventoryMoveDetail.getTargetLocationId());
            tInventoryMoveDetail.setTargetLocationCode(inventoryMoveDetail.getTargetLocationCode());
            tInventoryMoveDetail.setTargetLocationName(inventoryMoveDetail.getTargetLocationName());
            tInventoryMoveDetail.setContainerId(inventoryMoveDetail.getContainerId());
            tInventoryMoveDetail.setPackageCode(inventoryMoveDetail.getContainerCode());
            tInventoryMoveDetail.setPackageName(inventoryMoveDetail.getContainerName());
            tInventoryMoveDetail.setSkuId(inventoryMoveDetail.getSkuId());
            tInventoryMoveDetail.setSkuCode(inventoryMoveDetail.getSkuCode());
            tInventoryMoveDetail.setSkuName(inventoryMoveDetail.getSkuName());
            tInventoryMoveDetail.setMoveCnt(inventoryMoveDetail.getMoveCnt());
            tInventoryMoveDetail.setVolume(inventoryMoveDetail.getVolume());
            tInventoryMoveDetail.setWeight(inventoryMoveDetail.getWeight());
            tInventoryMoveDetailList.add(tInventoryMoveDetail);
        }
        wcsInventoryMoveTask.setTInventoryMove(tInventoryMove);
        wcsInventoryMoveTask.setTInventoryMoveDetailList(tInventoryMoveDetailList);
        return wcsInventoryMoveTask;
    }

    @Override
    @Transactional
    public HttpResult<?> submitInventoryMoveAndDetail(InventoryMoveAndDetail inventoryMoveAndDetail) {
        InventoryMove inventoryMove = inventoryMoveAndDetail.getInventoryMove();
        if (inventoryMove.getId() != null) return HttpResult.of(HttpResultCodeEnum.MOVE_HAVE_SUBMIT);
        inventoryMove.setCreateTime(new Date());
        inventoryMove.setType(1);
        inventoryMove.setStatus(1);
        inventoryMove.setMoveCode(codeService.code("MOV"));
        Integer pId = addInventoryMove(inventoryMove);
        for (InventoryMoveDetail inventoryMoveDetail : inventoryMoveAndDetail.getInventoryMoveDetailList()) {
            inventoryMoveDetail.setInventoryMoveId(pId);
            addInventoryMoveDetail(inventoryMoveDetail);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public HttpResult<?> auditMove(InventoryMove inventoryMove) {
        if (inventoryMove.getStatus() != null) if (inventoryMove.getStatus() == 5 || inventoryMove.getStatus() == 6)
            return HttpResult.of(HttpResultCodeEnum.NOT_AUDIT_TWICE);
        inventoryMove.setCheckPersonName(staffDAO.selectByPrimaryKey(inventoryMove.getCheckPersonId()).getStaffName());
        inventoryMove.setCheckTime(new Date());
        inventoryMove.setStatus(5);
        inventoryMoveDAO.updateByPrimaryKeySelective(inventoryMove);
        List<InventoryMoveDetail> inventoryMoveDetailList = inventoryMoveDetailDAO.selectDetailByPid(inventoryMove.getId());
        //根据库存移动修改库存余量表单
        for (InventoryMoveDetail inventoryMoveDetail : inventoryMoveDetailList) {
            List<InventoryBalance> inventoryBalanceList = inventoryBalanceDAO.selectByMoveRange(inventoryMoveDetail);
            for (InventoryBalance inventoryBalance : inventoryBalanceList) {
                if (inventoryMoveDetail.getTargetWarehouseId() != null)
                    inventoryBalance.setWarehouseId(inventoryMoveDetail.getTargetWarehouseId());
                if (inventoryMoveDetail.getTargetWarehouseCode() != null)
                    inventoryBalance.setWarehouseCode(inventoryMoveDetail.getTargetWarehouseCode());
                if (inventoryMoveDetail.getTargetWarehouseName() != null)
                    inventoryBalance.setWarehouseName(inventoryMoveDetail.getTargetWarehouseName());
                if (inventoryMoveDetail.getTargetAreaId() != null)
                    inventoryBalance.setAreaId(inventoryMoveDetail.getTargetAreaId());
                if (inventoryMoveDetail.getTargetAreaCode() != null)
                    inventoryBalance.setAreaCode(inventoryMoveDetail.getTargetAreaCode());
                if (inventoryMoveDetail.getTargetAreaName() != null)
                    inventoryBalance.setAreaName(inventoryMoveDetail.getTargetAreaName());
                if (inventoryMoveDetail.getTargetLocationGroupId() != null)
                    inventoryBalance.setLocationGroupId(inventoryMoveDetail.getTargetLocationGroupId());
                if (inventoryMoveDetail.getTargetLocationGroupCode() != null)
                    inventoryBalance.setLocationGroupCode(inventoryMoveDetail.getTargetLocationGroupCode());
                if (inventoryMoveDetail.getTargetLocationGroupName() != null)
                    inventoryBalance.setLocationGroupName(inventoryMoveDetail.getTargetLocationGroupName());
                if (inventoryMoveDetail.getTargetLocationId() != null)
                    inventoryBalance.setLocationId(inventoryMoveDetail.getTargetLocationId());
                if (inventoryMoveDetail.getTargetLocationCode() != null)
                    inventoryBalance.setLocationCode(inventoryMoveDetail.getTargetLocationCode());
                if (inventoryMoveDetail.getTargetLocationName() != null)
                    inventoryBalance.setLocationName(inventoryMoveDetail.getTargetLocationName());
                inventoryBalanceDAO.updateByPrimaryKeySelective(inventoryBalance);
            }
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public HttpResult<?> unAuditMove(InventoryMove inventoryMove) {
        if (inventoryMove.getStatus() != null) if (inventoryMove.getStatus() == 5 || inventoryMove.getStatus() == 6)
            return HttpResult.of(HttpResultCodeEnum.NOT_AUDIT_TWICE);
        inventoryMove.setCheckPersonName(staffDAO.selectByPrimaryKey(inventoryMove.getCheckPersonId()).getStaffName());
        inventoryMove.setCheckTime(new Date());
        inventoryMove.setStatus(6);
        inventoryMoveDAO.updateByPrimaryKeySelective(inventoryMove);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    @Transactional
    public Integer addInventoryMoveDetail(InventoryMoveDetail inventoryMoveDetail) {
        return inventoryMoveDetailDAO.insertSelective(inventoryMoveDetail);
    }

    @Override
    @Transactional
    public HttpResult<?> deleteInventoryMoveDetail(InventoryMoveDetail inventoryMoveDetail) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, inventoryMoveDetailDAO.deleteByPrimaryKey(inventoryMoveDetail.getId()));
    }

    @Override
    @Transactional
    public HttpResult<?> updateInventoryMoveDetail(InventoryMoveDetail inventoryMoveDetail) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, inventoryMoveDetailDAO.updateByPrimaryKeySelective(inventoryMoveDetail));
    }

    @Override
    public List<InventoryMoveDetail> searchInventoryMoveDetailById(InventoryMove inventoryMove) {
        return inventoryMoveDetailDAO.selectDetailByPid(inventoryMove.getId());
    }

    @Override
    public List<InventoryMoveDetail> searchInventoryMoveDetailByRf(ScreenDTO<InventoryMoveDetail> screenDTO) {
        InventoryMove inventoryMove = inventoryMoveDAO.selectByPrimaryKey(screenDTO.getPojo().getInventoryMoveId());
        inventoryMove.setStatus(2);//移动中
        inventoryMoveDAO.updateByPrimaryKeySelective(inventoryMove);
        return screenInventoryMoveDetail(screenDTO).getList();
    }

    @Override
    public SumAndList<InventoryMoveDetail> screenInventoryMoveDetail(ScreenDTO<InventoryMoveDetail> screenDTO) {
        if (screenDTO.getSearchDTO() != null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage() - 1) * screenDTO.getSearchDTO().getNum());
        if (screenDTO.getScreen() != null && !Objects.equals(screenDTO.getScreen(), ""))
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        else screenDTO.setScreen("id");
        List<InventoryMoveDetail> inventoryMoveDetails_result = inventoryMoveDetailDAO.screen(screenDTO);
        SumAndList<InventoryMoveDetail> inventoryMoveDetailSumAndList = new SumAndList<>();
        screenDTO.setSearchDTO(null);
        List<InventoryMoveDetail> inventoryMoveDetailList = inventoryMoveDetailDAO.screen(screenDTO);
        double movCnt = 0, vol = 0, wei = 0;
        for (InventoryMoveDetail inventoryMoveDetail : inventoryMoveDetailList) {
            if (inventoryMoveDetail.getMoveCnt() != null) movCnt += inventoryMoveDetail.getMoveCnt();
            if (inventoryMoveDetail.getVolume() != null) vol += inventoryMoveDetail.getVolume();
            if (inventoryMoveDetail.getWeight() != null) wei += inventoryMoveDetail.getWeight();
        }
        HashMap<String, Double> sums = new HashMap<>();
        sums.put("moveCnt", movCnt);
        sums.put("volume", vol);
        sums.put("weight", wei);
        inventoryMoveDetailSumAndList.setList(inventoryMoveDetails_result);
        inventoryMoveDetailSumAndList.setSums(sums);
        return inventoryMoveDetailSumAndList;
    }

    @Override
    public Integer screenInventoryMoveDetailNum(ScreenDTO<InventoryMoveDetail> screenDTO) {
        return inventoryMoveDetailDAO.numScreen(screenDTO);
    }
}
