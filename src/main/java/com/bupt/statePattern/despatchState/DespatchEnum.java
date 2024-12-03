package com.bupt.statePattern.despatchState;

import com.bupt.statePattern.waveState.WaveEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DespatchEnum {
    CREATING(0,"创建中"),
    CREATED(1,"已创建"),
    GENERATEDWAVE(2,"已分配波次"),
    PICKINGTASK(3,"已生成分拣任务"),
    SENDPICKINGTASK(4,"已发送WCS分拣任务"),
    RESEIVINGPICKFEEDBACK(5,"已收到WCS分拣确认"),
    PICKING(6,"拣货中"),
    PICKED(7,"已拣货"),
    BOXING(8,"装箱中"),
    BOXED(9,"已装箱"),
    LOADING(10,"装车中"),
    LOADED(11,"已装车"),
    DISTRIBUTING(12,"配送单生成中"),
    DISTRIBUTED(13,"配送单已生成"),
    EXPRESSING(14,"快递对接中"),
    EXPRESSED(15,"快递待发运"),
    DESPATCHED(16,"已发运"),
    FINISHED(17,"已完成"),

    CANCELAFTERCREATED(21,"创建后取消"),
    CANCELAFTERWAVE(22,"波次后取消"),
    CANCELAFTERBOXED(23,"装箱后取消"),
    CANCELAFTERDISTRIBUTED(24,"配送单生成后取消"),
    CANCELAFTEREXPRESS(25,"对接快递后取消"),
    CANCELAFTERLOADING(26,"装车后取消"),

    ;
    private int code;
    private String name;

    public String getNameByCode(Integer code) {
        for (DespatchEnum d : this.values()) {
            if (code == d.getCode()) return d.getName();
        }
        return "WRONG";
    }

    public int getCodeByName(String s) {
        for (DespatchEnum d : this.values()) {
            if (s == d.getName()) return d.getCode();
        }
        return -1;
    }
}
