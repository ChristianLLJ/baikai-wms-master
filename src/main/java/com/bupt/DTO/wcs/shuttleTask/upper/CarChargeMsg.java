package com.bupt.DTO.wcs.shuttleTask.upper;

import lombok.Data;

/**
 * 小车充电任务信息
 */
@Data
public class CarChargeMsg {
    public String id = "";     // 任务单号
    public String describe = "";   // 描述
    public String establishTime = "";  // 创建时间
    public String finishTime = "";     // 结束时间
    public String skuName = "";        // sku名称
    public String skuID = "";          // 商品编号
    public String status = "";         // 状态
    public String number = "";         // 数量
    public String operator = "";       // 操作员
    public String origin = "";         // 来源库位
    public String target = "";         // 目标库位
}
