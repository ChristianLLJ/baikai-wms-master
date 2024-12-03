package com.bupt.service.outbound;

import com.bupt.DTO.ScreenDTO;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;

import java.util.List;

public interface DistributionService {
    /*
     * Distribution 配送单 操作
     * */
    //新增，关联装箱单相关信息
    HttpResult<?> addDistribution(List<Distribution> distributions);

    //导出筛选
    List<Distribution> getHeads(ScreenDTO<Distribution> screenDTO);

    //导出自选
    List<Distribution> getHeads(List<Distribution> distributions);

}

