package com.bupt.service.outbound.impl;

import com.bupt.DTO.DespatchAndDetailOneToOne;
import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.mapper.*;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.BaseService;
import com.bupt.service.authority.impl.CodeServiceImpl;
import com.bupt.service.outbound.DespatchNewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DespatchImplNewService extends BaseService<Despatch, DespatchDetail, DespatchDAO, DespatchDetailDAO> implements DespatchNewService {
    @Autowired
    private DespatchDAO despatchDAO;
    @Autowired
    private DespatchDetailDAO despatchDetailDAO;
    @Autowired
    private DespatchWaveDAO despatchWaveDAO;
    @Autowired
    private ReplenishDAO replenishDAO;
    @Autowired
    private InventoryBalanceDAO inventoryBalanceDAO;
    @Autowired
    private WaveDAO waveDAO;
    @Autowired
    private WarehouseLocationDAO warehouseLocationDAO;
    @Autowired
    private PickTaskShortageDAO pickTaskShortageDAO;
    @Autowired
    private WaveRuleDAO waveRuleDAO;
    @Autowired
    private CodeServiceImpl codeService;
    @Autowired
    private StaffDAO staffDAO;
    @Autowired
    private ManualSortSeqDAO manualSortSeqDAO;
    @Autowired
    private PickTaskDetailDAO pickTaskDetailDAO;

    @Transactional
    @Override
    public HttpResult<?> addDesAndDetail(HeadAndDetail<Despatch, DespatchDetail> headAndDetail) {
        Despatch head = headAndDetail.getHead();
        head.setStatus((short) 1);
        if (head.getDespatchId() == null) head.setDespatchId(codeService.code("DES"));
        despatchDAO.insertSelective(head);
        headAndDetail.getDetails().forEach(e -> {
            e.setPid(head.getId());
            despatchDetailDAO.insertSelective(e);
        });
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, headAndDetail);
    }

    @Transactional
    @Override
    public Boolean judgeStatus(List<Despatch> despatchList, short status) {
        for (int i = 0; i < despatchList.size(); i++) {
            Despatch despatch = despatchDAO.selectByPrimaryKey(despatchList.get(i).getId());
            if (Objects.equals(despatch.getStatus(), status)) {
                return true;
            }
        }
        return false;
    }

    /*
     * 发运订单合并波次，同种 sku 统计 数量 , 未释放订单不能
     * */
    @Transactional
    @Override
    public List<DespatchDetail> merageDespatch(List<Despatch> despatchList) {
        List<DespatchDetail> despatchDetailList = new ArrayList<>();
        for (Despatch despatch : despatchList) {
            /*if (despatchDAO.selectByPrimaryKey(despatch.getId()).getStatus() != (short) 2) {
                return null;
            }*/
            despatchDetailList.addAll(despatchDetailDAO.selectDetailByPid(despatch.getId()));
        }
        for (Despatch despatch : despatchList) {
            despatch.setStatus((short) 1);
            despatchDAO.updateByPrimaryKeySelective(despatch);
        }
        despatchDetailList.sort(Comparator.comparing(DespatchDetail::getSkuCode));
        List<DespatchDetail> despatchDetails = new ArrayList<>();
        int size = despatchDetailList.size();
        for (int i = 0; i < size; i++) {
            DespatchDetail despatchDetail = despatchDetailList.get(i);
            for (int j = i + 1; j < size; j++) {
                DespatchDetail despatchDetail1 = despatchDetailList.get(j);
                if (Objects.equals(despatchDetail.getSkuCode(), despatchDetail1.getSkuCode())) {
                    despatchDetail.setOrderCnt(despatchDetail.getOrderCnt() + despatchDetail1.getOrderCnt());
                    despatchDetail.setWeight(despatchDetail.getWeight() + despatchDetail1.getWeight());
                    despatchDetail.setVolume(despatchDetail1.getVolume() + despatchDetail.getVolume());
                } else {
                    i = j - 1;
                    break;
                }
            }
            if (i != (size - 1) || !Objects.equals(despatchDetailList.get(i).getSkuCode(), despatchDetailList.get(i - 1).getSkuCode())) {
                despatchDetails.add(despatchDetail);
            }
        }
        return despatchDetails;
    }

    @Transactional
    @Override
    public HttpResult<?> despatchAssign(List<Despatch> despatchList, String distributionRule) {
        despatchList.forEach(e -> {
            Despatch despatch = new Despatch();
            despatchDetailDAO.selectDetailByPid(e.getId()).forEach(d -> {
                d.setDistributionCnt(d.getOrderCnt());
                d.setDistributionRule(distributionRule);
                despatchDetailDAO.updateByPrimaryKeySelective(d);
            });
            despatch.setStatus((short) 5);
            despatch.setId(e.getId());
            despatchDAO.updateByPrimaryKeySelective(despatch);
        });
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }


    @Transactional
    @Override
    public List<Despatch> searchDespatchByWaveId(Wave wave) {
        List<Despatch> despatchs = new ArrayList<>();
        despatchWaveDAO.selectDetailByPid(wave.getId()).forEach(e -> {
            despatchs.add(despatchDAO.selectByPrimaryKey(e.getDespatchId()));
        });
        return despatchs;
    }

    @Transactional
    @Override
    public HttpResult<?> despatchPreAssign(List<Despatch> despatchList, String preDistributionRule) {
        //controller中之前应该已经判断是否库存充足
        despatchList.forEach(e -> {
            Despatch despatch = new Despatch();
            despatchDetailDAO.selectDetailByPid(e.getId()).forEach(d -> {
                d.setPreDistributionCnt(d.getOrderCnt());
                d.setPreDistributionRule(preDistributionRule);
                despatchDetailDAO.updateByPrimaryKeySelective(d);
            });
            despatch.setStatus((short) 4);
            despatch.setId(e.getId());
            despatchDAO.updateByPrimaryKeySelective(despatch);
        });
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    private Replenish pickTaskShortageToReplenish(PickTaskShortage pickTaskShortage) {
        Replenish replenish = new Replenish();
        replenish.setReplenishId(codeService.code("REP"));
        replenish.setPid(pickTaskShortage.getWaveId());
        replenish.setReplenishStatus((short) 1);//已创建
        replenish.setShortageAreaId(pickTaskShortage.getShortageAreaId());
        replenish.setShortageAreaCode(pickTaskShortage.getShortageAreaCode());
        replenish.setShortageAreaName(pickTaskShortage.getShortageAreaName());
        replenish.setProposalCnt(pickTaskShortage.getShortageCnt());
        replenish.setCustomerId(pickTaskShortage.getCustomerId());
        replenish.setCustomerName(pickTaskShortage.getCustomerName());
        replenish.setSkuBarCode(pickTaskShortage.getSkuBarcode());
        replenish.setWarehouseId(pickTaskShortage.getWarehouseId());
        replenish.setWarehouseCode(pickTaskShortage.getWarehouseCode());
        replenish.setWarehouseName(pickTaskShortage.getWarehouseName());
        return replenish;
    }

    @Transactional
    @Override
    public HttpResult<?> cancelDespatch() {
        //todo 订单取消
        return null;
    }

    @Override
    public HttpResult<?> checkSuccess(List<Despatch> despatchList) {
        List<Despatch> despatchs = new ArrayList<>();
        Boolean tag = false;
        for (Despatch k : despatchList) {
            Despatch e = despatchDAO.selectByPrimaryKey(k.getId());
            e.setReviewerId(k.getReviewerId());
            if (e.getVerifyStatus() != 1) {
                despatchs.add(e);
                tag = true;
                continue;
            }
            Despatch despatch = new Despatch();
            despatch.setId(e.getId());
            despatch.setVerifyStatus((byte) 2);
            despatch.setReviewerId(e.getReviewerId());
            despatch.setReviewerName(staffDAO.selectByPrimaryKey(e.getReviewerId()).getStaffName());
            despatch.setReviewerTime(new Date());
            despatchDAO.updateByPrimaryKeySelective(despatch);
        }
        return tag ? HttpResult.of(HttpResultCodeEnum.FAIL_CHECK_ALL, despatchs) : HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public HttpResult<?> checkReject(List<Despatch> despatchList) {
        List<Despatch> despatchs = new ArrayList<>();
        Boolean tag = false;
        for (Despatch k : despatchList) {
            Despatch e = despatchDAO.selectByPrimaryKey(k.getId());
            e.setReviewerId(k.getReviewerId());
            if (e.getVerifyStatus() != 1) {
                despatchs.add(e);
                tag = true;
                continue;
            }
            Despatch despatch = new Despatch();
            despatch.setId(e.getId());
            despatch.setVerifyStatus((byte) 11);
            despatch.setReviewerId(e.getReviewerId());
            despatch.setReviewerName(staffDAO.selectByPrimaryKey(e.getReviewerId()).getStaffName());
            despatch.setReviewerTime(new Date());
            despatch.setRemarks(k.getRemarks());
            despatchDAO.updateByPrimaryKeySelective(despatch);
        }
        return tag ? HttpResult.of(HttpResultCodeEnum.FAIL_CHECK_ALL, despatchs) : HttpResult.of(HttpResultCodeEnum.SUCCESS);

    }

    @Override
    public HttpResult<?> updateDesAndDetail(HeadAndDetail<Despatch, DespatchDetail> headAndDetail) {
        Despatch d = despatchDAO.selectByPrimaryKey(headAndDetail.getHead().getId());
        if (d.getStatus() > 1 || d.getVerifyStatus() == 2) return HttpResult.of(HttpResultCodeEnum.CAN_NOT_UPD);
        if (d.getVerifyStatus() == 3) {
            headAndDetail.getHead().setRemarks("审核退回重新提交订单");
            headAndDetail.getHead().setVerifyStatus((byte) 1);//todo 标记-审核人不覆盖
        }
        headAndDetail.getHead().setStatus((short) 1);
        despatchDAO.updateByPrimaryKeySelective(headAndDetail.getHead());
        headAndDetail.getDetails().forEach(e -> {
            despatchDetailDAO.updateByPrimaryKeySelective(e);
        });
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public HttpResult<?> insertDetails(List<DespatchDetail> despatchDetails) {
        Despatch d = despatchDAO.selectByPrimaryKey(despatchDetails.get(0).getPid());
        if (d.getStatus() > 1 || d.getVerifyStatus() == 2) return HttpResult.of(HttpResultCodeEnum.CAN_NOT_UPD);
        despatchDetails.forEach(e -> {
            despatchDetailDAO.insertSelective(e);
        });
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public HttpResult<?> delDesDetails(List<DespatchDetail> despatchDetails) {
        Despatch d = despatchDAO.selectByPrimaryKey(despatchDetails.get(0).getPid());
        if (d.getStatus() > 1 || d.getVerifyStatus() == 2) return HttpResult.of(HttpResultCodeEnum.CAN_NOT_UPD);
        despatchDetails.forEach(e -> {
            despatchDetailDAO.deleteByPrimaryKey(e.getId());
        });
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public List<DespatchAndDetailOneToOne> convertOnrtoone(List<DespatchDetail> despatchDetails) {
        List<DespatchAndDetailOneToOne> goal = new ArrayList<>();
        for (DespatchDetail despatchDetail : despatchDetails) {
            Despatch despatch = despatchDAO.selectByPrimaryKey(despatchDetail.getPid());
            DespatchAndDetailOneToOne tem = new DespatchAndDetailOneToOne();
            tem.setCity(despatch.getCity());
            tem.setAddress(despatch.getAddress());
            tem.setCarrierId(despatch.getCarrierId());
            tem.setCreateTime(despatch.getCreateTime());
            tem.setMoney(despatchDetail.getMoney());
            tem.setDespatchId(despatch.getDespatchId());
            tem.setSite(despatch.getSite());
            tem.setNetWeight(despatchDetail.getNetWeight());
            tem.setProvince(despatch.getProvince());
            tem.setType(despatch.getType());
            tem.setUnit(despatchDetail.getUnit());
            tem.setRowId(despatchDetail.getRowId());
            tem.setReceiverId(despatch.getReceiverId());
            tem.setPieceCnt(despatchDetail.getPieceCnt());
            tem.setReceiverName(despatch.getReceiverName());
            tem.setSkuCode(despatchDetail.getSkuCode());
            tem.setWarehouse(despatch.getWarehouse());
            tem.setWarehouseName(despatch.getWarehouseName());
            tem.setWarehouseId(despatch.getWarehouseId());
            tem.setVolume(despatchDetail.getVolume());
            tem.setVerifyStatus(despatch.getVerifyStatus());
            tem.setStatus(despatch.getStatus());
            tem.setRequestDeliveryTime(despatch.getRequestDeliveryTime());
            tem.setExpectSendTime(despatch.getExpectSendTime());
            tem.setIsPreDistributed(despatch.getIsPreDistributed());
            tem.setOrderCnt(despatchDetail.getOrderCnt());
            tem.setTakeCnt(despatchDetail.getTakeCnt());
            tem.setDeliverCnt(despatchDetail.getDeliverCnt());
            tem.setPreDistributionCnt(despatchDetail.getPreDistributionCnt());
            tem.setDistributionCnt(despatchDetail.getDistributionCnt());
            tem.setSettlerId(despatch.getSettlerId());
            tem.setOrderId(despatch.getOrderId());
            tem.setSkuBarcode(despatchDetail.getSkuBarcode());
            tem.setSkuId(despatchDetail.getSkuId());
            tem.setSkuName(despatchDetail.getSkuName());
            tem.setWeight(despatchDetail.getWeight());
            tem.setCustomerId(despatch.getCustomerId());
            tem.setCustomerName(despatch.getCustomerName());
            goal.add(tem);
        }
        return goal;
    }

    @Override
    public HttpResult<?> findDesInPicking(ScreenDTO<Despatch> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, despatchDAO.screenToBox(screenDTO));
    }

    @Override
    public HttpResult<?> findDesToPick(Despatch despatch) {
        List<DespatchDetail> goal = new ArrayList<>();
        for (DespatchDetail dd : despatchDetailDAO.selectDetailByPid(despatch.getId())) {
            //根据发运订单查找已分拣出的对应sku数量
            Integer sumCnt = pickTaskDetailDAO.sumPieceCntByDesIdAndSkuIdPicked(dd.getPid(), dd.getSkuId());
            if (sumCnt == null) sumCnt = 0;
            if (sumCnt < dd.getPieceCnt()) {
                dd.setPieceCnt(dd.getPieceCnt() - sumCnt);
                goal.add(dd);
            }
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, goal);
    }

    @Override
    public HttpResult<?> findDesBeenPicked(Despatch despatch) {
        List<DespatchDetail> goal = new ArrayList<>();
        for (DespatchDetail dd : despatchDetailDAO.selectDetailByPid(despatch.getId())) {
            //根据发运订单查找已分拣出的对应sku数量
            Integer sumCnt = pickTaskDetailDAO.sumPieceCntByDesIdAndSkuIdPicked(dd.getPid(), dd.getSkuId());
            if (sumCnt != null && sumCnt > 0) {
                dd.setPieceCnt(sumCnt);
                goal.add(dd);
            }
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, goal);
    }

    @Override
    public HttpResult<?> runManualPickingList(ScreenDTO<Despatch> screenDTO) {
        List<ManualSortSeq> manualSortSeqList = new ArrayList<>();
        boolean tag = true;
        for (Despatch des : despatchDAO.screenToBox(screenDTO)) {
            if (!manualSortSeqDAO.selectByDesId(des.getId()).isEmpty()) {
                continue;
            }
            ManualSortSeq manualSortSeq = new ManualSortSeq();
            manualSortSeq.setDespatchId(des.getId());
            String code = codeService.code("MSS");
            manualSortSeq.setPriority(Long.parseLong(code.substring(3)));
            Integer areaId = pickTaskDetailDAO.selectByDespatchId(des.getId()).get(0).getAreaId();
            manualSortSeq.setWarehouseAreaId(areaId);
            manualSortSeqList.add(manualSortSeq);
            manualSortSeqDAO.insertSelective(manualSortSeq);
            if (tag) {
                sendManualpickTaskToWcs(manualSortSeq);
                tag = false;
            }
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, manualSortSeqList);
    }

    @Override
    public HttpResult<?> findNowManualPicking(ManualSortSeq manualSortSeq) {
        ManualSortSeq nowMseq = manualSortSeqDAO.selectByAreaIAndStatus
                (manualSortSeq.getWarehouseAreaId(), (byte) 2).get(0);
        List<PickTaskDetail> pickTaskDetails = pickTaskDetailDAO.selectByDesIdAndAreaIdNotBox
                (nowMseq.getWarehouseAreaId(), nowMseq.getDespatchId());
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, pickTaskDetails);
    }

    public void sendManualpickTaskToWcs(ManualSortSeq manualSortSeq) {
        //todo 任务分配给小车,设置manualSortSeq状态为2
        if (manualSortSeqDAO.selectByAreaIAndStatus(manualSortSeq.getWarehouseAreaId(), (byte) 2).isEmpty()) {
            manualSortSeq.setStatus((byte) 2);
            manualSortSeqDAO.updateByPrimaryKeySelective(manualSortSeq);
        }
    }

    @Override
    public HttpResult<?> getHaveBeenBoxed(ManualSortSeq manualSortSeq) {
        ManualSortSeq nowMseq = manualSortSeqDAO.selectByAreaIAndStatus
                (manualSortSeq.getWarehouseAreaId(), (byte) 2).get(0);
        List<PickTaskDetail> pickTaskDetails = pickTaskDetailDAO.selectByDesIdAndAreaIdInBox
                (nowMseq.getWarehouseAreaId(), nowMseq.getDespatchId());
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, pickTaskDetails);
    }

    @Override
    public HttpResult<?> getNotPick(ManualSortSeq manualSortSeq) {
        ManualSortSeq nowMseq = manualSortSeqDAO.selectByAreaIAndStatus
                (manualSortSeq.getWarehouseAreaId(), (byte) 2).get(0);
        List<PickTaskDetail> pickTaskDetails = pickTaskDetailDAO.selectByDesIdAndAreaIdNotPick
                (nowMseq.getWarehouseAreaId(), nowMseq.getDespatchId());
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, pickTaskDetails);
    }
}
