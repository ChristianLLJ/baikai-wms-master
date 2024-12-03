package com.bupt.state;

import java.util.List;

public class StackingState extends StateFactory {
    public StackingState(Integer num) {
        super(num);
    }

    @Override
    public Boolean judge(List<StateEnum> stateEnumList) {
        for(StateEnum stateEnum:stateEnumList){
            if(this.getState()==stateEnum)
                return true;
        }
        return false;
    }
}
