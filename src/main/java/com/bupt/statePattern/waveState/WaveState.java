package com.bupt.statePattern.waveState;

import java.util.List;

public class WaveState {
    public WaveState(Integer num) {
        for (WaveEnum e : this.waveEnum.values()) {
            if (num == e.getCode()) this.waveEnum = e;
        }
    }

    private WaveEnum waveEnum;

    public WaveEnum getState() {
        return this.waveEnum;
    }

    public void setStateName(Integer num) {

    }

    public Boolean judge(List<WaveEnum> WaveEnums) {
        for (WaveEnum WaveEnum : WaveEnums) {
            if (this.getState() == WaveEnum)
                return true;
        }
        return false;
    }

    //s代码中给出
    public Boolean judge(String s, int code) {
        if (s == waveEnum.getNameByCode(code))
            return true;
        return false;
    }

    public int getCode(String s) {
        return waveEnum.getCodeByName(s);
    }
}
