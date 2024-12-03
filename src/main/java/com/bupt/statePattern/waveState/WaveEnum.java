package com.bupt.statePattern.waveState;

import com.bupt.statePattern.examineState.ExamineEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WaveEnum {
    CREATED(1,"已创建"),
    PICKINGTASK(2,"已生成分拣任务"),
    SENDPICKINGTASK(3,"已发送WCS分拣任务"),
    RESEIVINGPICKFEEDBACK(4,"已收到WCS分拣确认"),
    PICKING(5,"拣货中"),
    PICKED(6,"已拣货"),
    BOXING(7,"装箱中"),
    BOXED(8,"已装箱"),
    DESPATCHING(9,"发运中"),
    DESPATCHED(10,"已发运"),
    FINISHED(11,"已完成"),


    CANCEL(21,"已取消"), //只能在已创建状态之前才能取消
    WAREHOUSESHORTAGE(22,"仓库缺货"),
    AREASHORTAGE(23,"库区缺货"),
    REPLENISHSUCCESS(24,"补货成功"),
    ;
    private int code;
    private String name;
    public String getNameByCode(Integer code) {
        for (WaveEnum e : this.values()) {
            if (code == e.getCode()) return e.getName();
        }
        return "WRONG";
    }
    public int getCodeByName(String s) {
        for (WaveEnum e : this.values()) {
            if (s == e.getName()) return e.getCode();
        }
        return -1;
    }
}
