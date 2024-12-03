package com.bupt.state;

import java.util.List;

public class CheckedState extends StateFactory {
    public CheckedState(Integer num) {
        super(num);
    }

    @Override
    public Boolean judge(List<StateEnum> stateEnumList) {
        return null;
    }
}
