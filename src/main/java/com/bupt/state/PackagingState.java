package com.bupt.state;

import java.util.List;

public class PackagingState extends StateFactory {
    public PackagingState(Integer num) {
        super(num);
    }

    @Override
    public Boolean judge(List<StateEnum> stateEnumList) {
        return null;
    }
}
