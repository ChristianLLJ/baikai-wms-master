package com.bupt.strategy.OnshelfStrategy;

import com.bupt.DTO.location.LocationCur;
import com.bupt.pojo.InboundPlan;
import com.bupt.pojo.InboundPlanDetail;
import com.bupt.pojo.WarehouseLocation;
import lombok.Data;

import java.util.Map;
@Data
public class LocationLimit {
    public boolean  empty(LocationCur locationCur){
        if(locationCur.getNum()==0) return true;
        else return false;
    }
    public boolean mixSku(LocationCur locationCur, InboundPlanDetail inboundPlanDetail){
        if(locationCur.getSku().size()>1) return false;
        if(locationCur.getSku().size()==0) return true;
        if(locationCur.getSku().contains(inboundPlanDetail.getSkuCode())) return true;
        else return false;
    }
    public boolean mixBatch(LocationCur locationCur, InboundPlan inboundPlan){
        if(locationCur.getBatch().size()>1) return false;
        if(locationCur.getBatch().size()==0) return true;
        if(locationCur.getBatch().contains(inboundPlan.getInboundBatch())) return true;
        else return false;
    }
}
