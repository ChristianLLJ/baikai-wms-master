package com.bupt.service.outbound.impl;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.mapper.*;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.BaseService;
import com.bupt.service.authority.impl.CodeServiceImpl;
import com.bupt.service.outbound.ExPickingService;
import com.bupt.statePattern.despatchState.DespatchState;
import com.bupt.statePattern.waveState.WaveState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExPickingServiceImpl extends BaseService<ExPicking, ExPickingDetail, ExPickingDAO, ExPickingDetailDAO> implements ExPickingService {
    @Autowired
    private ExPickingDAO exPickingDAO;
    @Autowired
    private ExPickingDetailDAO exPickingDetailDAO;
    @Autowired
    private DespatchDAO despatchDAO;
    @Autowired
    private WaveDAO waveDAO;
    @Autowired
    private DespatchWaveDAO despatchWaveDAO;
    @Autowired
    private DespatchDetailDAO despatchDetailDAO;
    @Autowired
    private PickTaskDetailDAO pickTaskDetailDAO;
    @Autowired
    private ManualSortSeqDAO manualSortSeqDAO;
    @Autowired
    private CodeServiceImpl codeService;

    @Override
    public HttpResult<?> getExPickingAndDetail(List<HeadAndDetail<ExPicking, ExPickingDetail>> headAndDetails) {
        for (int i = 0; i < headAndDetails.size(); i++) {
            HeadAndDetail<ExPicking, ExPickingDetail> e = headAndDetails.get(i);
            ExPicking exPicking = e.getHead();
            List<ExPickingDetail> details = e.getDetails();
            if (exPicking == null || details.size() == 0) return HttpResult.of(HttpResultCodeEnum.DATA_NULL, e);
            if (exPicking.getExPickingId() == null) {
                headAndDetails.get(i).getHead().setExPickingId(codeService.code("EPI"));
            }
        }
        headAndDetails.forEach(e -> {
            ExPicking exPicking = e.getHead();
            Despatch despatch = despatchDAO.selectByPrimaryKey(exPicking.getDespatchId());
            DespatchState ds = new DespatchState(6);
            if (despatch.getStatus() >= ds.getCode("已拣货") && despatch.getStatus() < ds.getCode("装箱中")) {
                despatch.setStatus((short) ds.getCode("装箱中"));
                despatchDAO.updateByPrimaryKeySelective(despatch);
            }
            List<ExPickingDetail> details = e.getDetails();
            exPickingDAO.insertSelective(exPicking);
            details.forEach(k -> {
                k.setExPickingId(exPicking.getId());
                exPickingDetailDAO.insertSelective(k);
            });
        });
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public HttpResult<?> manualAddExPickingAndDetail(List<PickTaskDetail> pickTaskDetails) {
        List<PickTaskDetail> pickTaskDetailList = new ArrayList<>();
        for (PickTaskDetail pickTaskDetail : pickTaskDetails) {
            PickTaskDetail pd = pickTaskDetailDAO.selectByPrimaryKey(pickTaskDetail.getId());
            if (pd.getIsBoxed()) {
                return HttpResult.of(HttpResultCodeEnum.HAVE_BEEN_BOXED);
            }
            pickTaskDetailList.add(pd);
        }
        pickTaskToExpicking(pickTaskDetailList);
        boolean tag = true;
        PickTaskDetail pdd = pickTaskDetailList.get(0);
        for (PickTaskDetail pd : pickTaskDetailDAO.selectByDespatchId(pdd.getDespatchId())) {
            if (!pd.getIsBoxed()) {
                tag = false;
            }
        }
        if (tag) {
            List<ManualSortSeq> ms = manualSortSeqDAO.selectByDesIdAndAreaIdAndStatus
                    (pdd.getDespatchId(), pdd.getAreaId());
            for (ManualSortSeq m : ms) {
                if (m.getStatus() == 2) {
                    m.setStatus((byte) 3);
                    manualSortSeqDAO.updateByPrimaryKeySelective(m);
                    break;
                }
            }
            ManualSortSeq mss = manualSortSeqDAO.selectByAreaIAndStatus(pdd.getAreaId(), (byte) 1).get(0);
            mss.setStatus((byte) 2);
            manualSortSeqDAO.updateByPrimaryKeySelective(mss);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, tag);
    }

    @Override
    public HttpResult<?> checkExPickingForDespatch() {
        DespatchState ds = new DespatchState(6);
        despatchDAO.selectByStatus((short) ds.getCode("装箱中")).forEach(e -> {
            Boolean tag = true;
            for (DespatchDetail dd : despatchDetailDAO.selectDetailByPid(e.getId())) {
                Integer exSum = exPickingDetailDAO.sumDetailByDesAndSku(e.getId(), dd.getSkuId());
                if (dd.getOrderCnt() != exSum) {
                    tag = false;
                    break;
                }
            }
            Despatch d = new Despatch();
            d.setId(e.getId());
            d.setStatus((short) ds.getCode("已装箱"));
            if (tag) despatchDAO.updateByPrimaryKeySelective(d);
        });
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override//名下发运订单全部装箱完毕--,波次计划更改状态为”已装箱“
    public HttpResult<?> checkExPickingForWave() {
        WaveState ws = new WaveState(7);
        waveDAO.selectByStatus(ws.getCode("装箱中")).forEach(w -> {
            Boolean tag = true;
            for (DespatchWave despatchWave : despatchWaveDAO.selectDespatchIdByWaveId(w.getId())) {
                DespatchState ds = new DespatchState(6);
                if (despatchDAO.selectByPrimaryKey(despatchWave.getDespatchId()).getStatus() != ds.getCode("已装箱")) {
                    tag = false;
                    break;
                }
            }
            if (tag) {
                w.setStatus(ws.getCode("已装箱"));
                waveDAO.updateByPrimaryKeySelective(w);
            }
        });
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public HttpResult<?> searchMergePosition(ExPicking exPicking) {
        DespatchState ds = new DespatchState(6);
        List<ExPicking> exPickings = exPickingDAO.selectMergePosition(exPicking.getDespatchId());
        if (exPickings.size() == 0 || exPickings == null) return HttpResult.of(HttpResultCodeEnum.NOT_BEGIN_MERGING);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, exPickings.get(0).getBoxPosition());
    }

    @Override//人工设置合单位置，可不操作，分拣机自动安排合单位置
    public HttpResult<?> setMergePosition(ExPicking exPicking) {
        List<ExPicking> exPickings = exPickingDAO.selectDetailByPid(exPicking.getDespatchId());
        for (ExPicking e : exPickings) {
            if (e.getIsMerged()) return HttpResult.of(HttpResultCodeEnum.HAVE_BEEN_MERGEDG);
            e.setBoxPosition(exPicking.getBoxPosition());
            e.setIsMerged(false);
            exPickingDAO.updateByPrimaryKeySelective(e);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override//重置合单位置
    public HttpResult<?> resetMergePosition(ExPicking exPicking) {
        List<ExPicking> exPickings = exPickingDAO.selectDetailByPid(exPicking.getDespatchId());
        for (ExPicking e : exPickings) {
            e.setBoxPosition(exPicking.getBoxPosition());
            e.setIsMerged(false);
            exPickingDAO.updateByPrimaryKeySelective(e);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public HttpResult<?> mergeExpicking(List<ExPicking> exPickings) {
        exPickings.forEach(e -> {
            e.setIsMerged(true);
            exPickingDAO.updateByPrimaryKeySelective(e);
        });
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    public void pickTaskToExpicking(List<PickTaskDetail> pickTaskDetails) {
        ExPicking exPicking = new ExPicking();
        Despatch despatch = despatchDAO.selectByPrimaryKey(pickTaskDetails.get(0).getDespatchId());
        Integer waveId = despatchWaveDAO.selectWaveIdByDespatchId(despatch.getId());
        exPicking.setCustomerName(despatch.getReceiverName());
        exPicking.setCustomerId(despatch.getReceiverId());
        exPicking.setDespatchId(despatch.getId());
        exPicking.setWaveNumber(waveId);
        exPicking.setCargoOwnerCode(despatch.getCustomerName());
        exPicking.setWarehouseCode(pickTaskDetails.get(0).getWarehouseCode());
        exPicking.setExPickingId(codeService.code("EPI"));
        exPickingDAO.insertSelective(exPicking);
        for (PickTaskDetail p : pickTaskDetails) {
            ExPickingDetail exPickingDetail = new ExPickingDetail();
            exPickingDetail.setSkuCnt(p.getPieceCnt());
            exPickingDetail.setSkuCode(p.getSkuCode());
            exPickingDetail.setSkuName(p.getSkuName());
            exPickingDetail.setSkuId(p.getSkuId());
            exPickingDetailDAO.insertSelective(exPickingDetail);
            p.setIsBoxed(true);
            pickTaskDetailDAO.updateByPrimaryKeySelective(p);
        }
    }
}
