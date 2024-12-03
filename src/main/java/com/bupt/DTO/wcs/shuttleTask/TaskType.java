package com.bupt.DTO.wcs.shuttleTask;

/**
 * wms下发任务类型
 */
public enum TaskType {
    UNDEFINED(0, "未定义"),          // 未定义
    INBOUND(1, "入库"),            // 入库
    OUTBOUND(2, "出库"),           // 出库
    MOVE_LOCATION(3, "移库"),      // 移库
    CHECK(4, "盘点"),              // 盘点
    CHARGE(5, "充电");             // 充电

    String name;
    int code;

    TaskType(int code,String s){
        this.name = s;
        this.code = code;
    }
    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public static TaskType getTaskType(int value) {
        for (TaskType examType : TaskType.values()) {
            if (value == examType.getCode()) {
                return examType;
            }
        }
        return null;
    }
}
