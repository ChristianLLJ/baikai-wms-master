package com.bupt.DTO.inbound;

import com.bupt.pojo.CombineStack;
import com.bupt.pojo.CombineStackDetail;
import com.bupt.pojo.CombineStackPackageDetail;
import lombok.Data;

import java.util.List;

@Data
public class CombineHeadAndPlateDetailAndPackageDetail {
    CombineStack combineStack;
    List<CombineStackDetail> combineStackDetails;
    List<CombineStackPackageDetail> combineStackPackageDetails;
}
