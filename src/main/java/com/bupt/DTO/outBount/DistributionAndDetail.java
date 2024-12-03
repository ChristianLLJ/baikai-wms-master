package com.bupt.DTO.outBount;

import com.bupt.pojo.Distribution;
import com.bupt.pojo.DistributionDetail;
import lombok.Data;

import java.util.List;

@Data
public class DistributionAndDetail {
    private Distribution distribution;
    private List<DistributionDetail> distributionDetailList;
}
