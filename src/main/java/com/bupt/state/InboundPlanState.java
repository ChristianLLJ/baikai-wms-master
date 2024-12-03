package com.bupt.state;

import com.bupt.pojo.InboundPlan;

import java.util.List;

public interface InboundPlanState {

    StateEnum getState();
    String getMsg();

    InboundPlanState createFrom(InboundPlan inboundPlan);

    InboundPlanState audit(InboundPlan inboundPlan);

    InboundPlanState check(InboundPlan inboundPlan);

    InboundPlanState receive(InboundPlan inboundPlan);

    InboundPlanState packing(InboundPlan inboundPlan);

    InboundPlanState stacking(InboundPlan inboundPlan);

    InboundPlanState onshelfing(InboundPlan inboundPlan);
}
