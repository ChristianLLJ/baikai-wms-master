package com.bupt.service.inbound;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SumAndList;
import com.bupt.DTO.inbound.CombineHeadAndPlateDetailAndPackageDetail;
import com.bupt.DTO.inbound.DetailAndCode;
import com.bupt.pojo.CombineStack;
import com.bupt.pojo.CombineStackDetail;
import com.bupt.pojo.CombineStackPackageDetail;

import java.util.List;

public interface StackService {
    List<CombineStack> selectCombineStack(ScreenDTO<CombineStack> screenDTO);

    Integer selectCombineStackNum(ScreenDTO<CombineStack> screenDTO);

    SumAndList<CombineStackDetail> selectCombineStackDetail(ScreenDTO<CombineStackDetail> screenDTO);

    Integer selectCombineStackDetailNum(ScreenDTO<CombineStackDetail> screenDTO);

    SumAndList<CombineStackPackageDetail> selectCombineStackPackageDetail(ScreenDTO<CombineStackPackageDetail> screenDTO);

    Integer selectCombineStackPackageDetailNum(ScreenDTO<CombineStackPackageDetail> screenDTO);

    Integer cancel(HeadAndDetail<CombineStack,CombineStackDetail> headAndDetail);

    Integer close(CombineStack combineStack);

    String save(CombineHeadAndPlateDetailAndPackageDetail combineHeadAndPlateDetailAndPackageDetail);

    String add(CombineHeadAndPlateDetailAndPackageDetail combineHeadAndPlateDetailAndPackageDetail);

    Boolean stackedCheck(CombineStackDetail combineStackDetail);

    Integer stackedSet(CombineStackDetail combineStackDetail);

    Integer stackedContainer(CombineStackPackageDetail combineStackPackageDetail);

    Boolean combineStackedCheck(CombineStack combineStack);

    Integer combineStackedSet(CombineStack combineStack);

    Integer addPackageDetail(DetailAndCode<CombineStackPackageDetail> detailAndCode);

    String addStackDetail(DetailAndCode<CombineStackDetail> detailAndCode);

    Integer setCombineStackStacking(CombineStack combineStack);
}
