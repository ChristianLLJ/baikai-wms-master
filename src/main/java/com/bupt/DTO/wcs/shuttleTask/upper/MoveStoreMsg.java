package com.bupt.DTO.wcs.shuttleTask.upper;

import lombok.Data;

/**
 * 库内移动任务信息
 */
@Data
public class MoveStoreMsg {
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

    public int putDirection = 0x01;    // 放货方向
    public int putDeep = 0;             // 放货深度
    public int exDirection = 0x01;    // 取货方向
    public int exDeep = 0;              // 取货深度
}
