package com.bupt.state;

import java.util.List;

public class StackedState extends StateFactory {
    public StackedState(Integer num) {
        super(num);
    }

    @Override
    public Boolean judge(List<StateEnum> stateEnumList) {
        return null;
    }
}
