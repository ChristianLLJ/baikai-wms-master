package com.bupt.strategy.OnshelfStrategy;

import com.bupt.pojo.OnshelfAdvice;
import com.bupt.pojo.OnshelfAdviceDetail;
import com.bupt.pojo.OnshelfStrategyDetail;
import com.bupt.pojo.WarehouseLocation;

import java.util.List;


public interface OnshelfStrategyInt {
    WarehouseLocation selectLocation(OnshelfAdvice onshelfAdvice,OnshelfAdviceDetail onshelfAdviceDetail, OnshelfStrategyDetail onshelfStrategyDetail,List<WarehouseLocation> warehouseLocations);
}
