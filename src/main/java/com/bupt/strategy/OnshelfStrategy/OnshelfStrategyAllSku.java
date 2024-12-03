package com.bupt.strategy.OnshelfStrategy;

import com.bupt.DTO.inbound.SkuIdAndAreaCode;
import com.bupt.DTO.location.LocationCur;
import com.bupt.mapper.*;
import com.bupt.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class OnshelfStrategyAllSku implements OnshelfStrategyInt{
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
    private static OnshelfStrategyAllSku onshelfStrategyAllSku;
    @PostConstruct
    private void init(){
        onshelfStrategyAllSku = this;
        onshelfStrategyAllSku.warehouseLocationDAO = this.warehouseLocationDAO;
        onshelfStrategyAllSku.inboundPlanDAO = this.inboundPlanDAO;
        onshelfStrategyAllSku.onshelfStrategyDetailSpaceRelationDAO = this.onshelfStrategyDetailSpaceRelationDAO;
        onshelfStrategyAllSku.onshelfStrategyDetailLocationRelationDAO = this.onshelfStrategyDetailLocationRelationDAO;
        onshelfStrategyAllSku.inventoryBalanceDAO = this.inventoryBalanceDAO;
        onshelfStrategyAllSku.inboundPlanDetailDAO = this.inboundPlanDetailDAO;
    }
    //先按sku搜索库存余量表，查找出对应的库位，然后判断该库位是否被加锁，然后计算对应库位当前的体积、重量和数量，判断是否能放的下，放的下的放入待选库位并且立即加锁，筛选出所有待选库位后，通过库位限制和空间限制继续筛选，通过后返回第一条数据
    @Override
    public WarehouseLocation selectLocation(OnshelfAdvice onshelfAdvice, OnshelfAdviceDetail onshelfAdviceDetail, OnshelfStrategyDetail onshelfStrategyDetail, List<WarehouseLocation> warehouseLocations) {
        //初始化
        InboundPlanDetail inboundPlanDetail;
        PackingDetail packingDetail;
        CombineStackDetail combineStackDetail;
        List<OnshelfStrategyDetailSpaceRelation> spaceRelations = onshelfStrategyAllSku.onshelfStrategyDetailSpaceRelationDAO.selectByDetailId(onshelfStrategyDetail.getId());
        List<OnshelfStrategyDetailLocationRelation> locationRelations = onshelfStrategyAllSku.onshelfStrategyDetailLocationRelationDAO.selectByDetailId(onshelfStrategyDetail.getId());
        LocationLimit locationLimit =new LocationLimit();
        SpaceLimit spaceLimit = new SpaceLimit();
        //如果是入库单生成上架单
        if(onshelfAdviceDetail.getOnshelfType()==1){
            //获取上架单
            inboundPlanDetail = onshelfStrategyAllSku.inboundPlanDetailDAO.selectByInboundTraceCode(onshelfAdviceDetail.getTraceCode()).get(0);
            //获取对应sku的除款号和颜色sku代码相同的sku库存信息
            SkuIdAndAreaCode skuIdAndAreaCode = new SkuIdAndAreaCode();
            skuIdAndAreaCode.setId(inboundPlanDetail.getSkuId());
            skuIdAndAreaCode.setCode(onshelfAdvice.getOnshelfAreaCode());
            List<InventoryBalance> inventoryBalances = onshelfStrategyAllSku.inventoryBalanceDAO.selectSkuIdAndAreaCode(skuIdAndAreaCode);
            Map<Integer,LocationCur> locationInfo = new HashMap();
            //选出不重复的库位放入哈希表locationInfo中，key为表库位id
            for(int i =0 ;i<inventoryBalances.size();i++){
                LocationCur locationCur =new LocationCur();
                locationCur.setId(inventoryBalances.get(i).getLocationId());
                locationCur.setCode(inventoryBalances.get(i).getLocationCode());
                if(!locationInfo.containsKey(locationCur.getId())){
                    locationInfo.put(locationCur.getId(),locationCur);
                }
            }
            //增加选中库位的上下左右库位
            HashMap<Integer,LocationCur> tempMap = new HashMap<>();
            if(locationInfo.size()!=0){
                for(Integer id: locationInfo.keySet()){
                    LocationCur locationCur =new LocationCur();
                    List<WarehouseLocation> temp ;
                    WarehouseLocation warehouseLocation = onshelfStrategyAllSku.warehouseLocationDAO.selectByPrimaryKey(id);
                    //上
                    warehouseLocation.setRows(warehouseLocation.getRows()+1);
                    temp = onshelfStrategyAllSku.warehouseLocationDAO.selectClose(warehouseLocation);
                    if(temp.size()!=0){
                        locationCur.setId(temp.get(0).getId());
                        locationCur.setCode(temp.get(0).getLocationCode());
                        if(!locationInfo.containsKey(locationCur.getId())){
                            tempMap.put(temp.get(0).getId(),locationCur);
                        }
                    }
                    //下
                    warehouseLocation.setRows(warehouseLocation.getRows()-2);
                    temp = onshelfStrategyAllSku.warehouseLocationDAO.selectClose(warehouseLocation);
                    if(temp.size()!=0){
                        locationCur.setId(temp.get(0).getId());
                        locationCur.setCode(temp.get(0).getLocationCode());
                        if(!locationInfo.containsKey(locationCur.getId())){
                            tempMap.put(temp.get(0).getId(),locationCur);
                        }
                    }
                    //右
                    warehouseLocation.setRows(warehouseLocation.getRows()+1);
                    warehouseLocation.setColumns(warehouseLocation.getColumns()+1);
                    temp = onshelfStrategyAllSku.warehouseLocationDAO.selectClose(warehouseLocation);
                    if(temp.size()!=0){
                        locationCur.setId(temp.get(0).getId());
                        locationCur.setCode(temp.get(0).getLocationCode());
                        if(!locationInfo.containsKey(locationCur.getId())){
                            tempMap.put(temp.get(0).getId(),locationCur);
                        }
                    }
                    //左
                    warehouseLocation.setColumns(warehouseLocation.getColumns()-2);
                    temp = onshelfStrategyAllSku.warehouseLocationDAO.selectClose(warehouseLocation);
                    if(temp.size()!=0){
                        locationCur.setId(temp.get(0).getId());
                        locationCur.setCode(temp.get(0).getLocationCode());
                        if(!locationInfo.containsKey(locationCur.getId())){
                            tempMap.put(temp.get(0).getId(),locationCur);
                        }
                    }
                }
                locationInfo.putAll(tempMap);
                //提取所有待选库位的当前重量、数量、体积、存放sku和入库批次信息
                for(Integer id: locationInfo.keySet()){
                    List<InventoryBalance> inventoryBalances1 = onshelfStrategyAllSku.inventoryBalanceDAO.selectByLocationId(id);
                    LocationCur locationCur = new LocationCur();
                    Set<String> sku = new HashSet<>();
                    Set<String> batch = new HashSet<>();
                    locationCur.setNum(0.0);
                    locationCur.setWeight(0.0);
                    locationCur.setVolumn(0.0);
                    locationCur.setId(id);
                    for(InventoryBalance inventoryBalance:inventoryBalances1){
                        locationCur.setNum(locationCur.getNum()+inventoryBalance.getInventoryCnt());
                        locationCur.setVolumn(locationCur.getVolumn()+inventoryBalance.getVolume());
                        locationCur.setWeight(locationCur.getWeight()+inventoryBalance.getWeight());
                        sku.add(inventoryBalance.getSkuCode());
                        batch.add(inventoryBalance.getProductBatch());
                    }
                    locationInfo.put(id,locationCur);
                }
            }
            //通过库位锁定、库位限制和空间限制进行二次筛选
            Boolean flag = false;
            if(locationInfo.size()!=0){
                for(LocationCur locationCur:locationInfo.values()){
                    flag=false;
                    WarehouseLocation warehouseLocation = onshelfStrategyAllSku.warehouseLocationDAO.selectByPrimaryKey(locationCur.getId());
                    //首先判断该库位是否被锁定
                    if(warehouseLocation.getIsLocked() ==false){
                        //库位限制（可以使用策略模式改进代码观赏性）
                        for(int i =0;i<locationRelations.size();i++){
                            //必须是空库位
                            if(locationRelations.get(i).getSystemDetailId()==174){
                                if(!locationLimit.empty(locationCur)) {
                                    flag=true;
                                    break;
                                }
                            }
                            //不能混码
                            if(locationRelations.get(i).getSystemDetailId()==175){
                                if(!locationLimit.mixSku(locationCur,inboundPlanDetail)){
                                    flag=true;
                                    break;
                                }
                            }
                            //不能混批次
                            if(locationRelations.get(i).getSystemDetailId()==176){
                                if(!locationLimit.mixBatch(locationCur,inboundPlanDAO.selectByPrimaryKey(inboundPlanDetail.getPlanId()))){
                                    flag=true;
                                    break;
                                }
                            }
                        }
                        //空间限制
                        for(int i = 0;i< spaceRelations.size();i++){
                            //数量限制
                            if(spaceRelations.get(i).getSystemDetailId()==178){
                                if(!spaceLimit.num(locationCur,warehouseLocation,inboundPlanDetail)){
                                    flag=true;
                                    break;
                                }
                            }
                            //体积限制
                            if(spaceRelations.get(i).getSystemDetailId()==177){
                                if(!spaceLimit.volume(locationCur,warehouseLocation,inboundPlanDetail)){
                                    flag=true;
                                    break;
                                }
                            }
                            //重量限制
                            if(spaceRelations.get(i).getSystemDetailId()==179){
                                if(!spaceLimit.weight(locationCur,warehouseLocation,inboundPlanDetail)){
                                    flag=true;
                                    break;
                                }
                            }
                        }
                        //通过了所有的限制 锁定库位 直接返回该库位
                        if(flag==false){
                            warehouseLocation.setIsLocked(true);
                            onshelfStrategyAllSku.warehouseLocationDAO.updateByPrimaryKeySelective(warehouseLocation);
                            return warehouseLocation;
                        }
                    }

                }
            }
        }
        return null;
    }
}
