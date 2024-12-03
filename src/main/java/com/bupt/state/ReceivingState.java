package com.bupt.state;

import java.util.List;

public class ReceivingState extends StateFactory {
    public ReceivingState(Integer num) {
        super(num);
    }

    @Override
    public StateEnum getState() {
        return null;
    }

    @Override
    public Boolean judge(List<StateEnum> stateEnumList) {
        return null;
    }
}
