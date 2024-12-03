package com.bupt.DTO.wcs.shuttleTask.upper;


import lombok.Data;

/**
 * 出库任务信息
 */
@Data
public class ExStoreMsg {
    public String id = "";     // 任务单号
    public String bid = "";    // 波次号
    public String describe = "";   // 描述
    public String establishTime = "";  // 创建时间
    public String finishTime = "";     // 结束时间
    public String location = "";       // 出库库位
    public String priority = "";       // 优先级
    public String skuName = "";        // sku名称
    public String skuNumber = "";      // sku数量
    public String status = "";         // 状态
    public String boxId = "";          // 箱码
//    public int exDirection = 0x01;    // 取货方向
//    public int exDeep = 0;              // 取货深度
}
