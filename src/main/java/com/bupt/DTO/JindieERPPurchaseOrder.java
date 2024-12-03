package com.bupt.DTO;

import lombok.Data;

import java.util.Date;
@Data
public class JindieERPPurchaseOrder {
    String FBillNo;//单据编号
    String FDocumentStatus;//单据状态
    String FBillTypeID;//单据类型
    String fSupplierId;//供应商
    Date FDate;//采购日期
    String FPurchaseOrgId;//采购组织
    String FCloseStatus;//关闭日期
    Integer FMaterialId;//物料编码
    String FMaterialName;//物料名称
    String FUnitId;//单位
    Double FQty;//数量
    Double FEntryAmount;//金额
    Date FDeliveryDate;//交付日期
    String FMRPCloseStatus;//业务关闭

}
