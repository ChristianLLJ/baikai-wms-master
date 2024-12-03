package com.bupt.strategy.OnshelfStrategy;

import com.bupt.DTO.inbound.OSSLocationAndRange;
import com.bupt.DTO.inbound.SkuIdAndAreaCode;
import com.bupt.DTO.location.LocationCur;
import com.bupt.mapper.*;
import com.bupt.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class OnshelfStrategyInGateClose implements OnshelfStrategyInt{
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
    @Override
    //先按sku搜索库存余量表，查找出对应的库位，然后判断该库位是否被加锁，然后计算对应库位当前的体积、重量和数量，判断是否能放的下，放的下的放入待选库位并且立即加锁，筛选出所有待选库位后，通过库位限制和空间限制继续筛选，通过后返回第一条数据
    public WarehouseLocation selectLocation(OnshelfAdvice onshelfAdvice, OnshelfAdviceDetail onshelfAdviceDetail, OnshelfStrategyDetail onshelfStrategyDetail, List<WarehouseLocation> warehouseLocations) {
        //初始化
        InboundPlanDetail inboundPlanDetail;
        PackingDetail packingDetail;
        CombineStackDetail combineStackDetail;
        List<OnshelfStrategyDetailSpaceRelation> spaceRelations = onshelfStrategyDetailSpaceRelationDAO.selectByDetailId(onshelfStrategyDetail.getId());
        List<OnshelfStrategyDetailLocationRelation> locationRelations = onshelfStrategyDetailLocationRelationDAO.selectByDetailId(onshelfStrategyDetail.getId());
        LocationLimit locationLimit =new LocationLimit();
        SpaceLimit spaceLimit = new SpaceLimit();
        Integer range = 5;
        //如果是入库单生成上架单
        if(onshelfAdviceDetail.getOnshelfType()==1){
            //获取上架单
            inboundPlanDetail = inboundPlanDetailDAO.selectByInboundTraceCode(onshelfAdviceDetail.getTraceCode()).get(0);
            //获取该库区的所有入库口信息
            WarehouseLocation tempWarehouseLocation = new WarehouseLocation();
            tempWarehouseLocation.setAreaId(onshelfAdvice.getOnshelfAreaId());
            tempWarehouseLocation.setLocationType(1);
            List<WarehouseLocation> inGates = warehouseLocationDAO.selectAreaInGate(tempWarehouseLocation);
            List<WarehouseLocation> closeGates = new LinkedList<>();
            //根据距离入库口的距离选出候选库位
            for(WarehouseLocation w:inGates){
                ContainerHardware containerHardware = containerHardwareDAO.selectByPrimaryKey(w.getContainerId());
                OSSLocationAndRange ossLocationAndRange = new OSSLocationAndRange();
                ossLocationAndRange.setRange(range);
                if(w.getColumns() == 0) w.setColumns(1);
                if(w.getColumns() == containerHardware.getColumnNumber()+1) w.setColumns(containerHardware.getColumnNumber());
                if(w.getRows() == 0) w.setRows(1);
                if(w.getRows() == containerHardware.getRowNumber()+1) w.setRows(containerHardware.getRowNumber());
                ossLocationAndRange.setW(w);
                closeGates.addAll(warehouseLocationDAO.selectRange(ossLocationAndRange));
            }
            Map<Integer, LocationCur> locationInfo = new HashMap();
            //将库位放入哈希表locationInfo中，key为表库位id
            for(int i =0 ;i<closeGates.size();i++){
                LocationCur locationCur =new LocationCur();
                locationCur.setId(closeGates.get(i).getId());
                locationCur.setCode(closeGates.get(i).getLocationCode());
                locationInfo.put(locationCur.getId(),locationCur);
            }
            //提取所有待选库位的当前重量、数量、体积、存放sku和入库批次信息
            for(Integer id: locationInfo.keySet()){
                List<InventoryBalance> inventoryBalances1 = inventoryBalanceDAO.selectByLocationId(id);
                LocationCur locationCur = new LocationCur();
                Set<String> sku;
                Set<String> batch;
                for(InventoryBalance inventoryBalance:inventoryBalances1){
                    locationCur.setNum(locationCur.getNum()+inventoryBalance.getInventoryCnt());
                    locationCur.setVolumn(locationCur.getVolumn()+inventoryBalance.getVolume());
                    locationCur.setWeight(locationCur.getWeight()+inventoryBalance.getWeight());
                    sku=locationCur.getSku();
                    batch=locationCur.getBatch();
                    sku.add(inventoryBalance.getSkuCode());
                    batch.add(inventoryBalance.getProductBatch());
                    locationCur.setSku(sku);
                    locationCur.setBatch(batch);
                }
                locationInfo.remove(id);
                locationInfo.put(id,locationCur);
            }
            //通过库位锁定、库位限制和空间限制进行二次筛选
            Boolean flag = false;
            if(locationInfo.size()!=0){
                for(LocationCur locationCur:locationInfo.values()){
                    flag=false;
                    WarehouseLocation warehouseLocation = warehouseLocationDAO.selectByPrimaryKey(locationCur.getId());
                    //首先判断该库位是否被锁定
                    if(warehouseLocation.getIsLocked() ==false){
                        //库位限制（可以使用策略模式改进代码观赏性）
                        for(int i =0;i<locationRelations.size();i++){
                            //必须是空库位
                            if(spaceRelations.get(i).getSystemDetailId()==888){
                                if(!locationLimit.empty(locationCur)) {
                                    flag=true;
                                    break;
                                }
                            }
                            //不能混码
                            if(spaceRelations.get(i).getSystemDetailId()==999){
                                if(!locationLimit.mixSku(locationCur,inboundPlanDetail)){
                                    flag=true;
                                    break;
                                }
                            }
                            //不能混批次
                            if(spaceRelations.get(i).getSystemDetailId()==111){
                                if(!locationLimit.mixBatch(locationCur,inboundPlanDAO.selectByPrimaryKey(inboundPlanDetail.getPlanId()))){
                                    flag=true;
                                    break;
                                }
                            }
                        }
                        //空间限制
                        for(int i = 0;i< spaceRelations.size();i++){
                            //数量限制
                            if(spaceRelations.get(i).getSystemDetailId()==1000){
                                if(!spaceLimit.num(locationCur,warehouseLocation,inboundPlanDetail)){
                                    flag=true;
                                    break;
                                }
                            }
                            //体积限制
                            if(spaceRelations.get(i).getSystemDetailId()==1001){
                                if(!spaceLimit.volume(locationCur,warehouseLocation,inboundPlanDetail)){
                                    flag=true;
                                    break;
                                }
                            }
                            //重量限制
                            if(spaceRelations.get(i).getSystemDetailId()==1002){
                                if(!spaceLimit.weight(locationCur,warehouseLocation,inboundPlanDetail)){
                                    flag=true;
                                    break;
                                }
                            }
                        }
                        //通过了所有的限制 锁定库位 直接返回该库位
                        if(flag==false){
                            warehouseLocation.setIsLocked(true);
                            warehouseLocationDAO.updateByPrimaryKeySelective(warehouseLocation);
                            return warehouseLocation;
                        }
                    }

                }
            }
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
