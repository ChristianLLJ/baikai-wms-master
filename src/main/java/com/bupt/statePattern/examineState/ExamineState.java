package com.bupt.statePattern.examineState;

import java.util.List;

public class ExamineState {
    public ExamineState(Integer num) {
        for (ExamineEnum e: this.examineEnum.values()) {
            if (num == e.getCode()) this.examineEnum = e;
        }
    }

    private ExamineEnum examineEnum;

    public ExamineEnum getState() {
        return this.examineEnum;
    }

    public void setStateName(Integer num) {

    }

    public Boolean judge(List<ExamineEnum> ExamineEnums) {
        for (ExamineEnum ExamineEnum : ExamineEnums) {
            if (this.getState() == ExamineEnum)
                return true;
        }
        return false;
    }

    //s代码中给出
    public Boolean judge(String s,int code) {
        if (s== examineEnum.getNameByCode(code))
            return true;
        return false;
    }
}
