package com.bupt.state;

import java.util.List;

public class FinishedState extends StateFactory {
    public FinishedState(Integer num) {
        super(num);
    }

    @Override
    public Boolean judge(List<StateEnum> stateEnumList) {
        return null;
    }
}
