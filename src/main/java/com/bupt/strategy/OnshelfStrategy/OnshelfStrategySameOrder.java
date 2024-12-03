package com.bupt.strategy.OnshelfStrategy;

import com.bupt.DTO.location.LocationCur;
import com.bupt.mapper.*;
import com.bupt.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class OnshelfStrategySameOrder implements OnshelfStrategyInt{
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
    @Autowired
    WarehouseAreaDAO warehouseAreaDAO;
    private static OnshelfStrategySameOrder onshelfStrategySameOrder;

    @PostConstruct
    private void init() {
        onshelfStrategySameOrder = this;
        onshelfStrategySameOrder.containerHardwareDAO = this.containerHardwareDAO;
        onshelfStrategySameOrder.inboundPlanDAO = this.inboundPlanDAO;
        onshelfStrategySameOrder.inventoryBalanceDAO = this.inventoryBalanceDAO;
        onshelfStrategySameOrder.warehouseLocationDAO = this.warehouseLocationDAO;
        onshelfStrategySameOrder.onshelfStrategyDetailLocationRelationDAO = this.onshelfStrategyDetailLocationRelationDAO;
        onshelfStrategySameOrder.onshelfStrategyDetailSpaceRelationDAO = this.onshelfStrategyDetailSpaceRelationDAO;
        onshelfStrategySameOrder.inboundPlanDetailDAO = this.inboundPlanDetailDAO;
        onshelfStrategySameOrder.warehouseAreaDAO = this.warehouseAreaDAO;
    }
    @Override
    public WarehouseLocation selectLocation(OnshelfAdvice onshelfAdvice, OnshelfAdviceDetail onshelfAdviceDetail, OnshelfStrategyDetail onshelfStrategyDetail, List<WarehouseLocation> warehouseLocations) {
        InboundPlanDetail inboundPlanDetail;
        PackingDetail packingDetail;
        CombineStackDetail combineStackDetail;
        List<OnshelfStrategyDetailSpaceRelation> spaceRelations = onshelfStrategyDetailSpaceRelationDAO.selectByDetailId(onshelfStrategyDetail.getId());
        List<OnshelfStrategyDetailLocationRelation> locationRelations = onshelfStrategyDetailLocationRelationDAO.selectByDetailId(onshelfStrategyDetail.getId());
        LocationLimit locationLimit =new LocationLimit();
        SpaceLimit spaceLimit = new SpaceLimit();
        //如果是入库单生成上架单
        if(onshelfAdviceDetail.getOnshelfType()==1){
            //获取上架单
            inboundPlanDetail = inboundPlanDetailDAO.selectByInboundTraceCode(onshelfAdviceDetail.getTraceCode()).get(0);
            //获取对应sku的库存信息
            List<InventoryBalance> inventoryBalances = inventoryBalanceDAO.selectBySkuId(inboundPlanDetail.getSkuId());
            Map<Integer, LocationCur> locationInfo = new HashMap<>();
            //选出不重复的库位放入哈希表locationInfo中，key为表库位id
            for (InventoryBalance balance : inventoryBalances) {
                LocationCur locationCur = new LocationCur();
                locationCur.setId(balance.getLocationId());
                locationCur.setCode(balance.getLocationCode());
                if (!locationInfo.containsKey(locationCur.getId())) {
                    locationInfo.put(locationCur.getId(), locationCur);
                }
            }
            //增加选中库位的上下左右库位
            for(Integer id: locationInfo.keySet()){
                LocationCur locationCur =new LocationCur();
                List<WarehouseLocation> temp ;
                WarehouseLocation warehouseLocation = warehouseLocationDAO.selectByPrimaryKey(id);
                //上
                warehouseLocation.setRows(warehouseLocation.getRows()+1);
                temp = warehouseLocationDAO.selectClose(warehouseLocation);
                locationCur.setId(temp.get(0).getId());
                locationCur.setCode(temp.get(0).getLocationCode());
                if(!locationInfo.containsKey(locationCur.getId())){
                    locationInfo.put(temp.get(0).getId(),locationCur);
                }
                //下
                warehouseLocation.setRows(warehouseLocation.getRows()-2);
                temp = warehouseLocationDAO.selectClose(warehouseLocation);
                locationCur.setId(temp.get(0).getId());
                locationCur.setCode(temp.get(0).getLocationCode());
                if(!locationInfo.containsKey(locationCur.getId())){
                    locationInfo.put(temp.get(0).getId(),locationCur);
                }
                //右
                warehouseLocation.setRows(warehouseLocation.getRows()+1);
                warehouseLocation.setColumns(warehouseLocation.getColumns()+1);
                temp = warehouseLocationDAO.selectClose(warehouseLocation);
                locationCur.setId(temp.get(0).getId());
                locationCur.setCode(temp.get(0).getLocationCode());
                if(!locationInfo.containsKey(locationCur.getId())){
                    locationInfo.put(temp.get(0).getId(),locationCur);
                }
                //左
                warehouseLocation.setColumns(warehouseLocation.getColumns()-2);
                temp = warehouseLocationDAO.selectClose(warehouseLocation);
                locationCur.setId(temp.get(0).getId());
                locationCur.setCode(temp.get(0).getLocationCode());
                if(!locationInfo.containsKey(locationCur.getId())){
                    locationInfo.put(temp.get(0).getId(),locationCur);
                }
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
                        for (OnshelfStrategyDetailSpaceRelation spaceRelation : spaceRelations) {
                            //数量限制
                            if (spaceRelation.getSystemDetailId() == 1000) {
                                if (!spaceLimit.num(locationCur, warehouseLocation, inboundPlanDetail)) {
                                    flag = true;
                                    break;
                                }
                            }
                            //体积限制
                            if (spaceRelation.getSystemDetailId() == 1001) {
                                if (!spaceLimit.volume(locationCur, warehouseLocation, inboundPlanDetail)) {
                                    flag = true;
                                    break;
                                }
                            }
                            //重量限制
                            if (spaceRelation.getSystemDetailId() == 1002) {
                                if (!spaceLimit.weight(locationCur, warehouseLocation, inboundPlanDetail)) {
                                    flag = true;
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
    public List<OnshelfAdviceDetail> preSelectLocations(InboundPlan inboundPlan,List<InboundPlanDetail> inboundPlanDetails,OnshelfAdvice onshelfAdvice,List<OnshelfAdviceDetail> onshelfAdviceDetails){
        //初始化
        WarehouseLocation tempWarehouseLocation = new WarehouseLocation();//锚定库位变量
        tempWarehouseLocation.setAreaId(onshelfAdvice.getOnshelfAreaId());//设置锚定库区
        List<WarehouseLocation> tempLocations = new LinkedList<>();//锚定空库位集合变量
        List<WarehouseLocation> emptyLocations = new LinkedList<>();//确定空库位集合
        List<ContainerHardware> containerHardwares = onshelfStrategySameOrder.containerHardwareDAO.selectByAreaId(onshelfAdvice.getOnshelfAreaId());//库区内的所有容地
        Double volume = 0.0;//体积限制
        Double cur=0.0;//锚定体积变量
        Integer need=1;//空库位所需个数
        boolean flag = false;//循环跳出
        tempWarehouseLocation.setLocationType(1);
        //遍历库区内的每个容地每一层每一个行道是否有足够的空库位放一整批货
        for(ContainerHardware containerHardware:containerHardwares){
            tempWarehouseLocation.setContainerId(containerHardware.getId());//设置锚定容地
            volume=onshelfStrategySameOrder.warehouseLocationDAO.selectContainerOne(tempWarehouseLocation).get(0).getVolumeCapacity();//获取容地的单库位体积限制
            //获取所需库位个数
            for(InboundPlanDetail inboundPlanDetail:inboundPlanDetails){
                cur=cur+inboundPlanDetail.getTotalVolumn();
                if(cur>volume){
                    cur=inboundPlanDetail.getTotalVolumn();
                    need++;
                }
            }
            //对每一层查找判断
            for(int layer=1;layer<=containerHardware.getLayer();layer++){
                tempWarehouseLocation.setLayer(layer);
                for(int row = 1;row<=containerHardware.getColumnNumber();row++){
                    int rowEmptyNum = 0;
                    tempWarehouseLocation.setRows(row);
                    tempLocations = onshelfStrategySameOrder.warehouseLocationDAO.selectByAreaContainerLayerRow(tempWarehouseLocation);
                    for(WarehouseLocation w :tempLocations){
                        List<InventoryBalance> inventoryBalances =onshelfStrategySameOrder.inventoryBalanceDAO.selectByLocationId(w.getId());
                        if(inventoryBalances.size()==0){
                            rowEmptyNum++;

                        }
                    }
                    //找到了能放下一批货的空库位集合
                    if(rowEmptyNum>=need){
                        flag = true;
                        for(WarehouseLocation w :tempLocations){
                            if(onshelfStrategySameOrder.inventoryBalanceDAO.selectByLocationId(w.getId()).size()==0){
                                emptyLocations.add(w);
                            }
                        }
                        break;
                    }
                }
                if(flag==true) break;
            }
            if(flag==true) break;
            need=0;
        }
        if(flag==false) return null;
        int order=0;
        cur=0.0;
        //设置上架建议的库位明细
        for(int i = 0 ; i<inboundPlanDetails.size();i++){
            InboundPlanDetail inboundPlanDetail = inboundPlanDetails.get(i);
            OnshelfAdviceDetail onshelfAdviceDetail = onshelfAdviceDetails.get(i);
            cur=cur+inboundPlanDetail.getTotalVolumn();
            if(cur>volume){
                order++;
                cur=inboundPlanDetail.getTotalVolumn();
            }
            WarehouseLocation ttemp = new WarehouseLocation();
            if(emptyLocations.size()!=0){
                ttemp.setId(emptyLocations.get(order).getId());
                ttemp.setIsLocked(true);
                onshelfStrategySameOrder.warehouseLocationDAO.updateByPrimaryKeySelective(ttemp);
                onshelfAdviceDetail.setAdviceLocationId(emptyLocations.get(order).getId());
                onshelfAdviceDetail.setAdviceLocationName(emptyLocations.get(order).getLocationName());
            }
        }
        return onshelfAdviceDetails;
    }
}
