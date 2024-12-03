package com.bupt.statePattern.despatchState;

import java.util.List;

public class DespatchState {
    public DespatchState(Integer num) {
        for (DespatchEnum DespatchEnum : this.despatchEnum.values()) {
            if (num == DespatchEnum.getCode()) this.despatchEnum = DespatchEnum;
        }
    }

    private DespatchEnum despatchEnum;

    public DespatchEnum getState() {
        return this.despatchEnum;
    }

    public void setStateName(Integer num) {

    }

    public Boolean judge(List<DespatchEnum> DespatchEnums) {
        for (DespatchEnum DespatchEnum : DespatchEnums) {
            if (this.getState() == DespatchEnum)
                return true;
        }
        return false;
    }

    //s代码中给出
    public Boolean judge(String s,int code) {
        if (s== despatchEnum.getNameByCode(code))
            return true;
        return false;
    }
    public int getCode(String s) {
        return despatchEnum.getCodeByName(s);
    }
}
