package com.bupt.state;

import com.bupt.pojo.InboundPlan;

import java.util.List;

public abstract class CreatingState implements InboundPlanState {
    String msg = "";
    @Override
    public StateEnum getState() {
        return StateEnum.CREATING;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public InboundPlanState check(InboundPlan inboundPlan) {
        return null;
    }

    @Override
    public InboundPlanState createFrom(InboundPlan inboundPlan) {
        return null;
    }
}
