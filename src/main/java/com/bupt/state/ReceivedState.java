package com.bupt.state;

import java.util.List;

public class ReceivedState extends StateFactory {
    public ReceivedState(Integer num) {
        super(num);
    }

    @Override
    public Boolean judge(List<StateEnum> stateEnumList) {
        return null;
    }
}
