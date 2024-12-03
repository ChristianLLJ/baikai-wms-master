package com.bupt.DTO.inbound;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.pojo.CombineStack;
import com.bupt.pojo.CombineStackDetail;
import com.bupt.pojo.PackingDetail;
import com.bupt.pojo.PackingTable;
import lombok.Data;

@Data
public class GenerateCOB {
    HeadAndDetail<PackingTable, PackingDetail> packingDetailHeadAndDetail;
    CombineHeadAndPlateDetailAndPackageDetail combineHeadAndPlateDetailAndPackageDetail;
}
