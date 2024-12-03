package com.bupt.state;

import java.util.List;

public class PackagedState extends StateFactory {
    public PackagedState(Integer num) {
        super(num);
    }

    @Override
    public Boolean judge(List<StateEnum> stateEnumList) {
        return null;
    }
}
