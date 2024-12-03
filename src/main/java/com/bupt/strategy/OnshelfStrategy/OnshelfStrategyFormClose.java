package com.bupt.strategy.OnshelfStrategy;

import com.bupt.DTO.inbound.SkuCodeAndAreaCode;
import com.bupt.DTO.location.LocationCur;
import com.bupt.mapper.*;
import com.bupt.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OnshelfStrategyFormClose implements OnshelfStrategyInt{
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
            String skuExSizeAndColor = inboundPlanDetail.getSkuCode().substring(0,11);
            SkuCodeAndAreaCode skuCodeAndAreaCode = new SkuCodeAndAreaCode();
            skuCodeAndAreaCode.setSkuCode(skuExSizeAndColor);
            skuCodeAndAreaCode.setAreaCode(onshelfAdvice.getOnshelfAreaCode());
            List<InventoryBalance> inventoryBalances = inventoryBalanceDAO.selectLikeSkuCode(skuCodeAndAreaCode);
            Map<Integer, LocationCur> locationInfo = new HashMap();
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
