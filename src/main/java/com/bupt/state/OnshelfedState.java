package com.bupt.state;

import java.util.List;

public class OnshelfedState extends StateFactory {
    public OnshelfedState(Integer num) {
        super(num);
    }

    @Override
    public Boolean judge(List<StateEnum> stateEnumList) {
        return null;
    }
}
