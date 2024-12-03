package com.bupt.service.inbound;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SumAndList;
import com.bupt.DTO.inbound.DetailAndCode;
import com.bupt.pojo.InboundPlan;
import com.bupt.pojo.InboundPlanDetail;
import com.bupt.pojo.QualityCheck;
import com.bupt.pojo.QualityCheckDetail;

import java.util.List;

public interface QualityCheckService {
    List<QualityCheck> selectQualityCheck(ScreenDTO<QualityCheck> screenDTO);

    List<QualityCheck> selectQualityCheck(List<QualityCheck> qualityChecks);

    Integer selectQualityCheckNum(ScreenDTO<QualityCheck> screenDTO);

    SumAndList<QualityCheckDetail> selectQualityCheckDetail(ScreenDTO<QualityCheckDetail> screenDTO);

    Integer selectQualityCheckDetailNum(ScreenDTO<QualityCheckDetail> screenDTO);

    QualityCheck selectByPrimaryId(Integer id);

    List<QualityCheckDetail> selectByCheckId(Integer id);

    Integer checkAddDetail(QualityCheck qualityCheck);

    List<Double> checkSum(QualityCheck qualityCheck);

    Integer cancel(HeadAndDetail<QualityCheck, QualityCheckDetail> headAndDetail);

    Integer close(DetailAndCode<QualityCheck> detailAndCode);

    Integer save(HeadAndDetail<QualityCheck,QualityCheckDetail> headAndDetail);

    Integer add(HeadAndDetail<QualityCheck,QualityCheckDetail> headAndDetail);
}
