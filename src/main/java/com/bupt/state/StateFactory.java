package com.bupt.state;

import java.util.List;

public  class StateFactory {
    public StateFactory(Integer num){
        for(StateEnum stateEnum:StateEnum.values()){
            if(num==stateEnum.getCode()) this.stateEnum=stateEnum;
        }
    }
    private StateEnum stateEnum;
    public StateEnum getState(){
        return this.stateEnum;
    }
    public void setStateName(Integer num){

    }
    public Boolean judge(List<StateEnum> stateEnumList){
        for(StateEnum stateEnum:stateEnumList){
            if(this.getState()==stateEnum)
                return true;
        }
        return false;
    }
}
