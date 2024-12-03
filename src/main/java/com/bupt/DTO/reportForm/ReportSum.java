package com.bupt.DTO.reportForm;

import lombok.Data;

@Data
public class ReportSum {
    Integer inboundSum;
    Integer receiveSum;
    Integer distributionSum;//总分配数
    Integer freezeSum;//总冻结数
    Integer availableSum;//总可用数
    Integer totalSum;//总库存数
}
