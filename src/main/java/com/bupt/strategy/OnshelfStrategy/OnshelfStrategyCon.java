package com.bupt.strategy.OnshelfStrategy;

import com.bupt.pojo.OnshelfAdvice;
import com.bupt.pojo.OnshelfAdviceDetail;
import com.bupt.pojo.OnshelfStrategyDetail;
import com.bupt.pojo.WarehouseLocation;

import java.util.List;
public class OnshelfStrategyCon {
    private OnshelfStrategyInt onshelfStrategyInt;
    public OnshelfStrategyCon(OnshelfStrategyInt onshelfStrategyInt){
        this.onshelfStrategyInt = onshelfStrategyInt;
    }
    public WarehouseLocation selectLocation(OnshelfAdvice onshelfAdvice,OnshelfAdviceDetail onshelfAdviceDetail, OnshelfStrategyDetail onshelfStrategyDetail,List<WarehouseLocation> warehouseLocations){
        return onshelfStrategyInt.selectLocation(onshelfAdvice,onshelfAdviceDetail,onshelfStrategyDetail,warehouseLocations);
    }
}
