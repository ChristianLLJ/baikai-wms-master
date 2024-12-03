package com.bupt.state;

import java.util.List;

public class OnshelfingState extends StateFactory {
    public OnshelfingState(Integer num) {
        super(num);
    }

    @Override
    public Boolean judge(List<StateEnum> stateEnumList) {
        return null;
    }
}
