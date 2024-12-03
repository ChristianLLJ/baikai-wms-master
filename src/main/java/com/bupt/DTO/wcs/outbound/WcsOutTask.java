package com.bupt.DTO.wcs.outbound;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class WcsOutTask implements Serializable {
    private OutboundTask outboundTask;
    private List<OutboundDetail> outboundDetailList;
    private List<OutboundTaskDistribution> outboundTaskDistributionList;
}
