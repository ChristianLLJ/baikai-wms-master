package com.bupt.service.outbound;

import com.bupt.DTO.DespatchAndDetailOneToOne;
import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;

import java.util.List;

public interface DespatchNewService {
    /*
     * Despatch操作  1创建中、2已创建、3已分配波次、4已预配、5已分配、 6拣货中、7已拣货、8装箱中、9已装箱、10装车中、11已装车、12配送中、13已配送、14已发运、15已取消
     * */
    HttpResult<?> addDesAndDetail(HeadAndDetail<Despatch, DespatchDetail> headAndDetail);

    Boolean judgeStatus(List<Despatch> despatchList, short status);

    List<DespatchDetail> merageDespatch(List<Despatch> despatchList);

    List<Despatch> searchDespatchByWaveId(Wave wave);

    //预分配
    HttpResult<?> despatchPreAssign(List<Despatch> despatchList, String preDistributionRule);

    HttpResult<?> despatchAssign(List<Despatch> despatchList, String distributionRule);

    HttpResult<?> cancelDespatch();

    HttpResult<?> checkSuccess(List<Despatch> despatchList);

    //审核不通过
    HttpResult<?> checkReject(List<Despatch> despatchList);

    HttpResult<?> updateDesAndDetail(HeadAndDetail<Despatch, DespatchDetail> headAndDetail);

    HttpResult<?> insertDetails(List<DespatchDetail> despatchDetails);

    //todo 发运订单明细删除 会导致tob/c数量问题，暂时不用删除按钮
    HttpResult<?> delDesDetails(List<DespatchDetail> despatchDetails);

    List<DespatchAndDetailOneToOne> convertOnrtoone(List<DespatchDetail> despatchDetails);

    HttpResult<?> findDesInPicking(ScreenDTO<Despatch> screenDTO);
    HttpResult<?> findDesToPick(Despatch despatch);
    HttpResult<?> findDesBeenPicked(Despatch despatch);

    HttpResult<?> runManualPickingList(ScreenDTO<Despatch> screenDTO);

    //查找当前正在分拣的订单，需要库区id
    HttpResult<?> findNowManualPicking(ManualSortSeq manualSortSeq);

    HttpResult<?> getHaveBeenBoxed(ManualSortSeq manualSortSeq);

    HttpResult<?> getNotPick(ManualSortSeq manualSortSeq);

}

