package com.bupt.strategy.OnshelfStrategy;

import com.bupt.DTO.location.LocationCur;
import com.bupt.pojo.InboundPlanDetail;
import com.bupt.pojo.WarehouseLocation;

public class SpaceLimit {
    public boolean volume(LocationCur locationCur, WarehouseLocation warehouseLocation, InboundPlanDetail inboundPlanDetail){
        if(inboundPlanDetail.getTotalVolumn()<=warehouseLocation.getVolumeCapacity()-locationCur.getVolumn()){
            return true;
        }
        else return false;
    }
    public boolean num(LocationCur locationCur, WarehouseLocation warehouseLocation, InboundPlanDetail inboundPlanDetail){
        if(inboundPlanDetail.getReceivedNum()<=warehouseLocation.getNumCapacity()-locationCur.getNum()){
            return true;
        }
        else return false;
    }
    public boolean weight(LocationCur locationCur, WarehouseLocation warehouseLocation, InboundPlanDetail inboundPlanDetail){
        if(inboundPlanDetail.getTotalWeight()<=warehouseLocation.getWeightCapacity()-locationCur.getWeight()){
            return true;
        }
        else return false;
    }
}
