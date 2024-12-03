package com.bupt.strategy.OnshelfStrategy;

import com.bupt.DTO.inbound.SkuIdAndAreaCode;
import com.bupt.DTO.location.LocationCur;
import com.bupt.mapper.*;
import com.bupt.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
@Component
public class OnshelfStrategyDefault implements OnshelfStrategyInt{
    @Autowired
    InboundPlanDetailDAO inboundPlanDetailDAO;
    @Autowired
    InventoryBalanceDAO inventoryBalanceDAO;
    @Autowired
    WarehouseLocationDAO warehouseLocationDAO;
    @Autowired
    OnshelfStrategyDetailLocationRelationDAO onshelfStrategyDetailLocationRelationDAO;
    @Autowired
    OnshelfStrategyDetailSpaceRelationDAO onshelfStrategyDetailSpaceRelationDAO;
    @Autowired
    InboundPlanDAO inboundPlanDAO;
    @Autowired
    ContainerHardwareDAO containerHardwareDAO;
    private static OnshelfStrategyDefault onshelfStrategyDefault;

    @PostConstruct
    private void init(){
        onshelfStrategyDefault=this;
        onshelfStrategyDefault.inventoryBalanceDAO = this.inventoryBalanceDAO;
        onshelfStrategyDefault.containerHardwareDAO = this.containerHardwareDAO;
        onshelfStrategyDefault.onshelfStrategyDetailLocationRelationDAO = this.onshelfStrategyDetailLocationRelationDAO;
        onshelfStrategyDefault.onshelfStrategyDetailSpaceRelationDAO = this.onshelfStrategyDetailSpaceRelationDAO;
        onshelfStrategyDefault.inboundPlanDAO = this.inboundPlanDAO;
        onshelfStrategyDefault.inboundPlanDetailDAO = this.inboundPlanDetailDAO;
        onshelfStrategyDefault.warehouseLocationDAO = this.warehouseLocationDAO;
    }
    @Override
    //先按sku搜索库存余量表，查找出对应的库位，然后判断该库位是否被加锁，然后计算对应库位当前的体积、重量和数量，判断是否能放的下，放的下的放入待选库位并且立即加锁，筛选出所有待选库位后，通过库位限制和空间限制继续筛选，通过后返回第一条数据
    public WarehouseLocation selectLocation(OnshelfAdvice onshelfAdvice, OnshelfAdviceDetail onshelfAdviceDetail, OnshelfStrategyDetail onshelfStrategyDetail, List<WarehouseLocation> warehouseLocations) {
        //初始化
        //如果是入库单生成上架单
        if(onshelfAdviceDetail.getOnshelfType()==1){
            //获取上架单
            WarehouseLocation tempWarehouseLocation = new WarehouseLocation();
            tempWarehouseLocation.setLocationType(0);
            tempWarehouseLocation.setAreaId(onshelfAdvice.getOnshelfAreaId());
            //获取对应sku的除款号和颜色sku代码相同的sku库存信息
            List<ContainerHardware> containerHardwares = onshelfStrategyDefault.containerHardwareDAO.selectByAreaId(onshelfAdvice.getOnshelfAreaId());
            //搜索同库区、空库位
            for(ContainerHardware ch:containerHardwares){
                tempWarehouseLocation.setContainerId(ch.getId());
                for(int i =1;i<=ch.getLayer();i++){
                    tempWarehouseLocation.setLayer(i);
                    for(int j=1;j<=ch.getRowNumber();j++){
                        tempWarehouseLocation.setRows(j);
                        for(int k=1;k<=ch.getColumnNumber();k++){
                            tempWarehouseLocation.setColumns(k);
                            List<WarehouseLocation> list =onshelfStrategyDefault.warehouseLocationDAO.selectByLocation(tempWarehouseLocation);
                            if(list.size()==0) continue;
                            tempWarehouseLocation = list.get(0);
                            if(tempWarehouseLocation.getIsLocked()==true) continue;
                            if(onshelfStrategyDefault.inventoryBalanceDAO.selectByLocationId(tempWarehouseLocation.getId()).size()==0){
                                WarehouseLocation temp = new WarehouseLocation();
                                temp.setId(tempWarehouseLocation.getId());
                                temp.setIsLocked(true);
                                onshelfStrategyDefault.warehouseLocationDAO.updateByPrimaryKeySelective(temp);
                                return tempWarehouseLocation;
                            }
                        }
                    }
                }
            }
            return null;
        }
        //装箱单生成
        else if (onshelfAdviceDetail.getOnshelfType()==2){

        }
        //码盘单生成
        else if(onshelfAdviceDetail.getOnshelfType()==3){

        }
        return new WarehouseLocation();
    }
}
