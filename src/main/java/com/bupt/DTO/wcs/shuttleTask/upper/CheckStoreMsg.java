package com.bupt.DTO.wcs.shuttleTask.upper;

import lombok.Data;

/**
 * 库存盘点任务信息
 */
@Data
public class CheckStoreMsg {
    public String id = "";     // 任务单号
    public String bid = "";    // 波次号
    public String describe = "";   // 描述
    public String establishTime = "";  // 创建时间
    public String finishTime = "";     // 结束时间
    public String location = "";       // 入库库位 出库库位 盘点库位
    public String priority = "";       // 优先级
    public String skuName = "";        // sku名称
    public String skuNumber = "";      // sku数量
    public String status = "";         // 状态

    public String boxId = "";          // 箱码

    public String checkName = "";      // 盘点数量
    public String originNumber = "";   // 原有数量
    public String operator = "";       // 操作员
    public String skuID = "";          // 商品编号
    public String type = "";           // 类型

    public int putDirection = 0x01;    // 放货方向
    public int putDeep = 0;             // 放货深度
    public int exDirection = 0x01;    // 取货方向
    public int exDeep = 0;              // 取货深度
}
