package com.bupt.statePattern.examineState;

import com.bupt.statePattern.despatchState.DespatchEnum;
import com.bupt.statePattern.waveState.WaveEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExamineEnum {
    SUBMITING(0, "待提交"),
    EXAMINING(1, "待审核"),
    EXAMINED(2, "审核通过"),
    NOTEXAMINED(11, "审核不通过");
    private int code;
    private String name;

    public String getNameByCode(Integer code) {
        for (ExamineEnum e : this.values()) {
            if (code == e.getCode()) return e.getName();
        }
        return "WRONG";
    }

    public int getCodeByName(String s) {
        for (ExamineEnum e : this.values()) {
            if (s == e.getName()) return e.getCode();
        }
        return -1;
    }
}
