package com.bupt.state;

import java.util.List;

public class CreatedState extends StateFactory {

    public CreatedState(Integer num) {
        super(num);
    }

    @Override
    public Boolean judge(List<StateEnum> stateEnumList) {
        return null;
    }
}
