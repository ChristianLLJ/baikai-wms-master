package com.bupt.DTO.outBount;

import com.bupt.pojo.ExPicking;
import com.bupt.pojo.ExPickingDetail;
import lombok.Data;

import java.util.List;

@Data
public class ExPickingAndDetail {
    private ExPicking exPicking;

    private List<ExPickingDetail> exPickingDetailList;
}
