package com.bupt.state;

import java.util.List;

public class CheckingState extends StateFactory {
    public CheckingState(Integer num) {
        super(num);
    }

    @Override
    public Boolean judge(List<StateEnum> stateEnumList) {
        return null;
    }
}
