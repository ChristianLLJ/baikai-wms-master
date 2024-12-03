package com.bupt.DTO.wcs.shuttleTask;


/**
 * 任务状态
 */
public enum TaskStateEnum {
    /*

    */
    noStartState(0, "未开始"),              // 未开始
    InExecutionState(1, "执行中"),        //执行中
    CompletedState(2, "已完成"),        // 已完成
    inFaultState(3, "故障中");           // 故障中

    String name;
    int code;

    TaskStateEnum(int code,String s){
        this.name = s;
        this.code = code;
    }
    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public static TaskStateEnum getTaskType(int value) {
        for (TaskStateEnum examType : TaskStateEnum.values()) {
            if (value == examType.getCode()) {
                return examType;
            }
        }
        return null;
    }
}
