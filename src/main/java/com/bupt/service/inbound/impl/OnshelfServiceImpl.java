package com.bupt.service.inbound.impl;

import com.bupt.DTO.FrontId;
import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SumAndList;
import com.bupt.DTO.inbound.DetailAndCode;
import com.bupt.DTO.inbound.OnshelfStrategyDTO;
import com.bupt.DTO.wcs.Reqcode;
import com.bupt.DTO.wcs.WcsOnshelfReturn;
import com.bupt.mapper.*;
import com.bupt.pojo.*;
import com.bupt.result.BaokaiHttpResult;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.BaseService;
import com.bupt.service.Inwarehouse.StockService;
import com.bupt.service.authority.CodeService;
import com.bupt.service.inbound.OnshelfService;
import com.bupt.service.util.HttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class OnshelfServiceImpl extends BaseService<Onshelf, OnshelfDetail, OnshelfDAO, OnshelfDetailDAO> implements OnshelfService {
    @Autowired
    ContainerDAO containerDAO;
    @Autowired
    CustomDAO customDAO;
    @Autowired
    OnshelfAdviceDAO onshelfAdviceDAO;
    @Autowired
    OnshelfAdviceDetailDAO onshelfAdviceDetailDAO;
    @Autowired
    OnshelfStrategyDAO onshelfStrategyDAO;
    @Autowired
    OnshelfStrategyDetailDAO onshelfStrategyDetailDAO;
    @Autowired
    OnshelfStrategyDetailLocationRelationDAO onshelfStrategyDetailLocationRelationDAO;
    @Autowired
    OnshelfStrategyDetailSpaceRelationDAO onshelfStrategyDetailSpaceRelationDAO;
    @Autowired
    CodeService codeService;
    @Autowired
    OnshelfDAO onshelfDAO;
    @Autowired
    OnshelfDetailDAO onshelfDetailDAO;
    @Autowired
    InboundPlanDetailDAO inboundPlanDetailDAO;
    @Autowired
    InventoryBalanceDAO inventoryBalanceDAO;
    @Autowired
    InboundPlanDAO inboundPlanDAO;
    @Autowired
    WarehouseLocationDAO warehouseLocationDAO;
    @Autowired
    StockService stockService;
    @Autowired
    InventoryTotalDAO inventoryTotalDAO;
    @Autowired
    CombineStackDetailDAO combineStackDetailDAO;
    @Autowired
    CombineStackPackageDetailDAO combineStackPackageDetailDAO;
    @Autowired
    GoodsDAO goodsDAO;
    @Autowired
    HttpService httpService;

    /**
     * 筛选上架建议单头
     *
     * @param screenDTO
     * @return
     */
    @Override
    @Transactional
    public List<OnshelfAdvice> selectOnshelfAdvice(ScreenDTO<OnshelfAdvice> screenDTO) {
        if (screenDTO.getSearchDTO() != null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage() - 1) * screenDTO.getSearchDTO().getNum());
        return onshelfAdviceDAO.screen(screenDTO);
    }

    /**
     * 筛选上架建议单表
     *
     * @param screenDTO
     * @return
     */
    @Override
    @Transactional
    public List<OnshelfAdviceDetail> selectOnshelfAdviceDetail(ScreenDTO<OnshelfAdviceDetail> screenDTO) {
        if (screenDTO.getSearchDTO() != null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage() - 1) * screenDTO.getSearchDTO().getNum());
        return onshelfAdviceDetailDAO.screen(screenDTO);
    }

    /**
     * 筛选上架建议单表数量
     *
     * @param screenDTO
     * @return
     */
    @Override
    @Transactional
    public Integer selectOnshelfAdviceDetailNum(ScreenDTO<OnshelfAdviceDetail> screenDTO) {
        return onshelfAdviceDetailDAO.numScreen(screenDTO);
    }

    /**
     * 筛选上架建议单头数量
     *
     * @param screenDTO
     * @return
     */
    @Override
    @Transactional
    public Integer selectOnshelfAdviceNum(ScreenDTO<OnshelfAdvice> screenDTO) {
        return onshelfAdviceDAO.numScreen(screenDTO);
    }

    @Override
    @Transactional
    public List<Onshelf> selectOnshelf(ScreenDTO<Onshelf> screenDTO) {
        System.out.println(screenDTO);
        if (screenDTO.getSearchDTO() != null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage() - 1) * screenDTO.getSearchDTO().getNum());
        return onshelfDAO.screen(screenDTO);
    }


    @Override
    @Transactional
    public SumAndList<OnshelfDetail> selectOnshelfDetail(ScreenDTO<OnshelfDetail> screenDTO) {
        if (screenDTO.getSearchDTO() != null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage() - 1) * screenDTO.getSearchDTO().getNum());
        List<OnshelfDetail> onshelfDetails = onshelfDetailDAO.screen(screenDTO);
        SumAndList<OnshelfDetail> onshelfDetailSumAndList = new SumAndList<>();
        screenDTO.setSearchDTO(null);
        List<OnshelfDetail> onshelfDetails1 = onshelfDetailDAO.screen(screenDTO);
        double onshelfState = 0;
        for (int i = 0; i < onshelfDetails1.size(); i++) {
            if (onshelfDetails1.get(i).getOnshelfState() != null && onshelfDetails1.get(i).getOnshelfState() == 1)
                onshelfState++;
        }
        HashMap<String, Double> sums = new HashMap<>();
        sums.put("onshelfState", onshelfState);
        onshelfDetailSumAndList.setList(onshelfDetails);
        onshelfDetailSumAndList.setSums(sums);
        return onshelfDetailSumAndList;
    }

    @Override
    @Transactional
    public Integer selectOnshelfNum(ScreenDTO<Onshelf> screenDTO) {
        return onshelfDAO.numScreen(screenDTO);
    }

    @Override
    @Transactional
    public Integer selectOnshelfDetailNum(ScreenDTO<OnshelfDetail> screenDTO) {
        return onshelfDetailDAO.numScreen(screenDTO);
    }

    @Override
    @Transactional
    public List<OnshelfStrategy> selectOnshelfStrategy(ScreenDTO<OnshelfStrategy> screenDTO) {
        if (screenDTO.getSearchDTO() != null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage() - 1) * screenDTO.getSearchDTO().getNum());
        return onshelfStrategyDAO.screen(screenDTO);
    }

    @Override
    @Transactional
    public List<OnshelfStrategyDetail> selectOnshelfStrategyDetail(ScreenDTO<OnshelfStrategyDetail> screenDTO) {
        if (screenDTO.getSearchDTO() != null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage() - 1) * screenDTO.getSearchDTO().getNum());
        return onshelfStrategyDetailDAO.screen(screenDTO);
    }

    @Override
    public Integer selectOnshelfStrategyNum(ScreenDTO<OnshelfStrategy> screenDTO) {
        return onshelfStrategyDAO.numScreen(screenDTO);
    }

    @Override
    public Integer selectOnshelfStrategyDetailNum(ScreenDTO<OnshelfStrategyDetail> screenDTO) {
        return onshelfStrategyDetailDAO.numScreen(screenDTO);
    }

    @Override
    public Integer addOnshelfStrategy(OnshelfStrategyDTO onshelfStrategyDTO) {
        onshelfStrategyDAO.insertSelective(onshelfStrategyDTO.getOnshelfStrategy());
        Integer onshelfStrategyId = onshelfStrategyDTO.getOnshelfStrategy().getId();
        int num = 0;
        for (int i = 0; i < onshelfStrategyDTO.getOnshelfStrategyDetails().size(); i++) {
            onshelfStrategyDTO.getOnshelfStrategyDetails().get(i).setOnshelfStrategyId(onshelfStrategyId);
            num = onshelfStrategyDetailDAO.insertSelective(onshelfStrategyDTO.getOnshelfStrategyDetails().get(i));
            Integer onshelfStrategyDetailId = onshelfStrategyDTO.getOnshelfStrategyDetails().get(i).getId();
            OnshelfStrategyDetailLocationRelation relation = new OnshelfStrategyDetailLocationRelation();
            OnshelfStrategyDetailSpaceRelation relation1 = new OnshelfStrategyDetailSpaceRelation();
            for (int j = 0; j < onshelfStrategyDTO.getLimitLocationIds().get(i).size(); j++) {
                relation.setOnshelfStrategyDetailId(onshelfStrategyDetailId);
                relation.setSystemDetailId(onshelfStrategyDTO.getLimitLocationIds().get(i).get(j));
                relation1.setOnshelfStrategyDetailId(onshelfStrategyDetailId);
                relation1.setSystemDetailId(onshelfStrategyDTO.getLimitSpaceIds().get(i).get(j));
                onshelfStrategyDetailLocationRelationDAO.insertSelective(relation);
                onshelfStrategyDetailSpaceRelationDAO.insertSelective(relation1);
            }
        }
        return num;
    }

    @Override
    public List<OnshelfAdvice> selectOnshelfAdvice(List<OnshelfAdvice> OnshelfAdvices) {
        List<OnshelfAdvice> OnshelfAdvice1 = new LinkedList<>();
        for (OnshelfAdvice OnshelfAdvice : OnshelfAdvices) {
            OnshelfAdvice1.add(onshelfAdviceDAO.selectByPrimaryKey(OnshelfAdvice.getId()));
        }
        return OnshelfAdvice1;
    }

    @Override
    public HeadAndDetail<Onshelf, OnshelfDetail> ONPtoONF(HeadAndDetail<OnshelfAdvice, OnshelfAdviceDetail> onshelfAdviceDetailHeadAndDetail) {
        HeadAndDetail<Onshelf, OnshelfDetail> newHead = new HeadAndDetail<Onshelf, OnshelfDetail>();
        newHead.setHead(new Onshelf());
        newHead.setDetails(new LinkedList<>());
        Onshelf onshelf = newHead.getHead();
        List<OnshelfDetail> onshelfDetails = newHead.getDetails();
        OnshelfAdvice onshelfAdvice = onshelfAdviceDetailHeadAndDetail.getHead();
        List<OnshelfAdviceDetail> onshelfAdviceDetails = onshelfAdviceDetailHeadAndDetail.getDetails();
        onshelf.setOnshelfCode(codeService.code("ONF"));
        onshelf.setAdviceId(onshelfAdvice.getId());
        onshelf.setAdviceCode(onshelfAdvice.getOnshelfCode());
        onshelf.setOnshelfState(1);
        onshelf.setPrintAccount(0);
        onshelf.setIsUsable((byte) 1);
        onshelf.setWarehouseId(onshelfAdvice.getWarehouseId());
        onshelf.setWarehouseCode(onshelfAdvice.getWarehouseCode());
        onshelf.setWarehouseName(onshelfAdvice.getWarehouseName());
        onshelf.setCheckPersonId(onshelfAdvice.getCheckerId());
        onshelf.setCheckPersonName(onshelfAdvice.getCheckerName());
        onshelf.setCreateTime(new Date());
        onshelf.setRemark(onshelfAdvice.getRemark());
        onshelf.setOnshelfAreaId(onshelfAdvice.getOnshelfAreaId());
        onshelf.setOnshelfAreaCode(onshelfAdvice.getOnshelfAreaCode());
        onshelf.setOnshelfAreaName(onshelfAdvice.getOnshelfAreaName());
        onshelfDAO.insertSelective(onshelf);
        Integer id = onshelf.getId();
        for (int i = 0; i < onshelfAdviceDetails.size(); i++) {
            OnshelfDetail onshelfDetail = new OnshelfDetail();
            OnshelfAdviceDetail onshelfAdviceDetail = onshelfAdviceDetails.get(i);
            onshelfDetail.setOnshelfId(id);
            onshelfDetail.setOnshelfState(1);
            onshelfDetail.setTraceCode(onshelfAdviceDetail.getTraceCode());
            onshelfDetail.setTraceCode2(onshelfAdviceDetail.getTraceCode2());
            onshelfDetail.setOnshelfCode(onshelf.getOnshelfCode());
            OnshelfAdviceDetail onshelfAdviceDetail1 = onshelfAdviceDetailDAO.selectByPrimaryKey(onshelfAdviceDetail.getId());
            if (onshelfAdviceDetail.getAdviceLocationId() != onshelfAdviceDetail1.getAdviceLocationId()) {
                WarehouseLocation tempWL = new WarehouseLocation();
                tempWL.setId(onshelfAdviceDetail1.getAdviceLocationId());
                tempWL.setIsLocked(false);
                warehouseLocationDAO.updateByPrimaryKeySelective(tempWL);
                tempWL.setId(onshelfAdviceDetail.getAdviceLocationId());
                tempWL.setIsLocked(true);
                warehouseLocationDAO.updateByPrimaryKeySelective(tempWL);
            }
            onshelfDetail.setFactLocationId(onshelfAdviceDetail.getAdviceLocationId());
            onshelfDetail.setFactLocationName(onshelfAdviceDetail.getAdviceLocationName());
            onshelfDetail.setOnshelfType(onshelfAdviceDetail.getOnshelfType());
            onshelfDetail.setRemark(onshelfAdviceDetail.getRemark());
            onshelfDetail.setInboundBatch(onshelfAdviceDetail.getInboundBatch());
            onshelfDetail.setContainerId(onshelfAdviceDetail.getContainerId());
            onshelfDetail.setContainerCode(onshelfAdviceDetail.getContainerCode());
            onshelfDetail.setContainerBarcode(onshelfAdviceDetail.getContainerBarcode());
            onshelfDetail.setSkuId(onshelfAdviceDetail.getSkuId());
            onshelfDetail.setSkuCode(onshelfAdviceDetail.getSkuCode());
            onshelfDetail.setSkuName(onshelfAdviceDetail.getSkuName());
            onshelfDetail.setUnit(onshelfAdviceDetail.getUnit());
            onshelfDetail.setSkuNum(onshelfAdviceDetail.getSkuNum());
            onshelfDetail.setTotalVolumn(onshelfAdviceDetail.getTotalVolumn());
            onshelfDetail.setTotalWeight(onshelfAdviceDetail.getTotalWeight());
            onshelfDetail.setProductCompany(onshelfAdviceDetail.getProductCompany());
            onshelfDetail.setProductTime(onshelfAdviceDetail.getProductTime());
            onshelfDetail.setProductBatch(onshelfAdviceDetail.getProductBatch());
            onshelfDetails.add(onshelfDetail);
            onshelfDetailDAO.insertSelective(onshelfDetail);
        }
        onshelfAdvice.setOnshelfState(2);
        onshelfAdviceDAO.updateByPrimaryKey(onshelfAdvice);
        return newHead;
    }

    @Override
    public Integer updateInventoryByONF(Onshelf onshelf) {
        onshelf = onshelfDAO.selectByPrimaryKey(onshelf.getId());
        onshelf.setOnshelfState(4);
        onshelfDAO.updateByPrimaryKey(onshelf);
        List<OnshelfDetail> onshelfDetails = onshelfDetailDAO.selectByOnshelfId(onshelf);
        Set<Integer> ids = new HashSet<>();
        for (OnshelfDetail onshelfDetail : onshelfDetails) {
            InboundPlanDetail inboundPlanDetail = inboundPlanDetailDAO.selectByInboundTraceCode(onshelfDetail.getTraceCode()).get(0);
            ids.add(inboundPlanDetail.getPlanId());
            inboundPlanDetail.setPredictReceiveLocation(onshelfDetail.getFactLocationId());
            inboundPlanDetail.setPredictReceiveLocationCode(onshelfDetail.getContainerCode());
            inboundPlanDetail.setIsOnshelf(true);
            inboundPlanDetailDAO.updateByPrimaryKey(inboundPlanDetail);
            onshelfDetail.setOnshelfState(3);
            onshelfDetailDAO.updateByPrimaryKey(onshelfDetail);
            InventoryBalance inventoryBalance = new InventoryBalance();
            InboundPlan inboundPlan = inboundPlanDAO.selectByPrimaryKey(inboundPlanDetail.getPlanId());
            inventoryBalance.setBalanceCode(onshelfDetail.getContainerBarcode());
            inventoryBalance.setType((short) 1);

            inventoryBalance.setWarehouseId(inboundPlan.getWarehouseId());
            inventoryBalance.setWarehouseCode(inboundPlan.getWarehouseCode());
            inventoryBalance.setWarehouseName(inboundPlan.getWarehouseName());
            inventoryBalance.setAreaCode(onshelf.getOnshelfAreaCode());
            inventoryBalance.setAreaId(onshelf.getOnshelfAreaId());
            inventoryBalance.setAreaName(onshelf.getOnshelfAreaName());

            //客户信息
            Custom custom = customDAO.selectByPrimaryKey(inboundPlanDetail.getCustomId());
            inventoryBalance.setCustomId(custom.getId());
            inventoryBalance.setCustomCode(custom.getCustomCode());
            inventoryBalance.setCustomName(custom.getCustomName());
            inventoryBalance.setCommodityId(inboundPlanDetail.getCommodityId());
            inventoryBalance.setCommodityCode(inboundPlanDetail.getCommodityCode());
            inventoryBalance.setCommodityName(goodsDAO.selectByPrimaryKey(inboundPlanDetail.getCommodityId()).getGoodsName());
            inventoryBalance.setAreaCode(onshelf.getOnshelfAreaCode());
            //库位增加
            WarehouseLocation loc = warehouseLocationDAO.selectByPrimaryKey(onshelfDetail.getFactLocationId());
            inventoryBalance.setLocationId(loc.getId());
            inventoryBalance.setLocationCode(loc.getLocationCode());
            inventoryBalance.setLocationName(loc.getLocationName());
            //包装增加
            Container con = containerDAO.selectByPrimaryKey(inboundPlanDetail.getContainerId());
            inventoryBalance.setContainerId(con.getId());
            inventoryBalance.setContainerCode(con.getCode());
            inventoryBalance.setContainerName(con.getName());

            inventoryBalance.setSkuId(inboundPlanDetail.getSkuId());
            inventoryBalance.setSkuName(inboundPlanDetail.getSkuName());
            inventoryBalance.setSkuCode(inboundPlanDetail.getSkuCode());
            inventoryBalance.setUnit(inboundPlanDetail.getUnit());
            inventoryBalance.setAvailableCnt(onshelfDetail.getSkuNum().intValue());
            inventoryBalance.setInventoryCnt(onshelfDetail.getSkuNum().intValue());
            inventoryBalance.setVolume(inboundPlanDetail.getTotalVolumn());
            inventoryBalance.setWeight(inboundPlanDetail.getTotalWeight());
            inventoryBalance.setProductCompany(inboundPlanDetail.getProductCompany());
            inventoryBalance.setProductTime(inboundPlanDetail.getProductTime());
            inventoryBalance.setProductBatch(inboundPlanDetail.getProductBatch());
            inventoryBalance.setTraceCode(inboundPlanDetail.getInboundTraceCode());
            inventoryBalance.setInBoundTime(new Date());
            //todo 删除重复箱码数据-宝开测试
            InventoryBalance inv = inventoryBalanceDAO.selectByInventoryCodeNoType(inventoryBalance.getBalanceCode());
            if (inv != null) {
                inventoryBalance.setId(inv.getId());
                inventoryBalanceDAO.updateByPrimaryKeySelective(inventoryBalance);
            } else{
                inventoryBalanceDAO.insertSelective(inventoryBalance);
            }
        }
        for (Integer id : ids) {
            InboundPlan inboundPlan1 = new InboundPlan();
            inboundPlan1.setId(id);
            inboundPlan1.setInboundState(13);
            inboundPlanDAO.updateState(inboundPlan1);
        }
        OnshelfAdvice onshelfAdvice = onshelfAdviceDAO.selectByPrimaryKey(onshelf.getAdviceId());
        onshelfAdvice.setOnshelfState(3);
        onshelfAdviceDAO.updateByPrimaryKey(onshelfAdvice);
        System.out.println(onshelfAdvice);
        return 1;
    }

    @Override
    @Transactional
    public Integer save(HeadAndDetail<Onshelf, OnshelfDetail> headAndDetail) {
        //设置入库计划单状态为正在填写
        headAndDetail.getHead().setOnshelfState(0);
        Integer num = 0;
        //如果前端没有传进来id,说明为第一次填写
        if (headAndDetail.getHead().getId() == null) {
            //设置入库计划单代码
            //插入表头
            onshelfDAO.insertSelective(headAndDetail.getHead());
            Integer id = headAndDetail.getHead().getId();
        }
        //非第一次填写
        else {
            //对表头做更新操作
            onshelfDAO.updateByPrimaryKey(headAndDetail.getHead());
        }
        return headAndDetail.getHead().getId();
    }

    /**
     * 提交入库计划单
     *
     * @param headAndDetail
     * @return
     */
    @Override
    @Transactional
    public Integer add(HeadAndDetail<Onshelf, OnshelfDetail> headAndDetail) {
        //设置入库计划单状态为已填写
        headAndDetail.getHead().setOnshelfState(1);
        Integer num = 0;
        //如果没有保存过
        if (headAndDetail.getHead().getId() == null) {
            //设置入库计划单代码
            //插入表头
            onshelfDAO.insertSelective(headAndDetail.getHead());
            Integer id = headAndDetail.getHead().getId();
            //插入表单
            if (headAndDetail.getDetails() != null) {
                for (OnshelfDetail onshelfDetail : headAndDetail.getDetails()) {
                    onshelfDetail.setOnshelfId(id);
                    onshelfDetailDAO.insertSelective(onshelfDetail);
                }
            }
        }
        return headAndDetail.getHead().getId();
    }

    @Override
    public Integer packageOnshelfSet(Onshelf onshelf) {
        onshelf.setOnshelfState(4);
        onshelfDAO.updateByPrimaryKey(onshelf);
        List<OnshelfDetail> onshelfDetails = onshelfDetailDAO.selectByOnshelfId(onshelf);
        Set<Integer> ids = new HashSet<>();
        for (OnshelfDetail onshelfDetail : onshelfDetails) {
            if (onshelf.getSourceType() == 1) {
                InboundPlanDetail inboundPlanDetail = inboundPlanDetailDAO.selectByInboundTraceCode(onshelfDetail.getTraceCode()).get(0);
                ids.add(inboundPlanDetail.getPlanId());
                inboundPlanDetail.setPredictReceiveLocation(onshelfDetail.getFactLocationId());
                inboundPlanDetail.setPredictReceiveLocationCode(onshelfDetail.getContainerCode());
                inboundPlanDetailDAO.updateByPrimaryKey(inboundPlanDetail);
                InventoryBalance inventoryBalance = new InventoryBalance();
                InboundPlan inboundPlan = inboundPlanDAO.selectByPrimaryKey(inboundPlanDetail.getPlanId());
                inventoryBalance.setBalanceCode(codeService.code("BAL"));
                inventoryBalance.setType((short) 1);
                //客户信息
                Custom custom = customDAO.selectByPrimaryKey(inboundPlanDetail.getCustomId());
                inventoryBalance.setCustomId(custom.getId());
                inventoryBalance.setCustomCode(custom.getCustomCode());
                inventoryBalance.setCustomName(custom.getCustomName());
                inventoryBalance.setCommodityId(inboundPlanDetail.getCommodityId());
                inventoryBalance.setCommodityCode(inboundPlanDetail.getCommodityCode());
                inventoryBalance.setCommodityName(goodsDAO.selectByPrimaryKey(inboundPlanDetail.getCommodityId()).getGoodsName());
                inventoryBalance.setWarehouseId(inboundPlan.getWarehouseId());
                inventoryBalance.setWarehouseCode(inboundPlan.getWarehouseCode());
                inventoryBalance.setWarehouseName(inboundPlan.getWarehouseName());
                inventoryBalance.setAreaCode(onshelf.getOnshelfAreaCode());
                //库位增加
                WarehouseLocation loc = warehouseLocationDAO.selectByPrimaryKey(onshelfDetail.getFactLocationId());
                inventoryBalance.setLocationId(loc.getId());
                inventoryBalance.setLocationCode(loc.getLocationCode());
                inventoryBalance.setLocationName(loc.getLocationName());
                //包装增加
                Container con = containerDAO.selectByPrimaryKey(inboundPlanDetail.getContainerId());
                inventoryBalance.setContainerId(con.getId());
                inventoryBalance.setContainerCode(con.getCode());
                inventoryBalance.setContainerName(con.getName());
                inventoryBalance.setSkuId(inboundPlanDetail.getSkuId());
                inventoryBalance.setSkuName(inboundPlanDetail.getSkuName());
                inventoryBalance.setSkuCode(inboundPlanDetail.getSkuCode());
                inventoryBalance.setUnit(inboundPlanDetail.getUnit());
                inventoryBalance.setAvailableCnt(inboundPlanDetail.getReceivedNum().intValue());
                inventoryBalance.setVolume(inboundPlanDetail.getTotalVolumn());
                inventoryBalance.setWeight(inboundPlanDetail.getTotalWeight());
                inventoryBalance.setProductCompany(inboundPlanDetail.getProductCompany());
                inventoryBalance.setProductTime(inboundPlanDetail.getProductTime());
                inventoryBalance.setProductBatch(inboundPlanDetail.getProductBatch());
                inventoryBalance.setTraceCode(inboundPlanDetail.getInboundTraceCode());
                inventoryBalance.setInBoundTime(new Date());
                Integer inventoryId = stockService.addInventoryBalance(inventoryBalance);
                InboundPlanDetail tempDetail = new InboundPlanDetail();
                tempDetail.setId(inboundPlanDetail.getId());
                tempDetail.setInventoryId(inventoryId);
                inboundPlanDetailDAO.updateByPrimaryKeySelective(tempDetail);
            } else if (onshelf.getSourceType() == 0) {
                if (onshelfDetail.getOnshelfType() == 0) {
                    InventoryBalance inventoryBalance = onshelfToInv(onshelfDetail, onshelf);
                    inventoryBalance.setAvailableCnt(onshelfDetail.getSkuNum().intValue());
                    inventoryBalance.setVolume(onshelfDetail.getTotalVolumn());
                    inventoryBalance.setWeight(onshelfDetail.getTotalWeight());
                    stockService.addInventoryBalance(inventoryBalance);
                }
                if (onshelfDetail.getOnshelfType() == 1) {
                    CombineStackDetail combineStackDetail = combineStackDetailDAO.selectByPackageBarcode(onshelfDetail.getContainerBarcode()).get(0);
                    ScreenDTO<CombineStackPackageDetail> screenDTO = new ScreenDTO<>();
                    screenDTO.setPojo(new CombineStackPackageDetail());
                    screenDTO.getPojo().setStackBarcode(combineStackDetail.getPackageBarcode());
                    List<CombineStackPackageDetail> combineStackPackageDetails = combineStackPackageDetailDAO.screen(screenDTO);
                    for (int i = 0; i < combineStackPackageDetails.size(); i++) {
                        CombineStackPackageDetail combineStackPackageDetail = combineStackPackageDetails.get(i);
                        InventoryBalance inventoryBalance = onshelfToInv(onshelfDetail, onshelf);
                        inventoryBalance.setAvailableCnt(combineStackPackageDetail.getSkuNum().intValue());
                        inventoryBalance.setVolume(combineStackPackageDetail.getTotalVolumn());
                        inventoryBalance.setWeight(combineStackPackageDetail.getTotalWeight());
                        stockService.addInventoryBalance(inventoryBalance);
                    }
                }
            }
        }
        if (ids.size() != 0) {
            for (Integer id : ids) {
                InboundPlan inboundPlan1 = new InboundPlan();
                inboundPlan1.setId(id);
                inboundPlan1.setInboundState(13);
                inboundPlanDAO.updateState(inboundPlan1);
            }
        }
        return 1;
    }

    public InventoryBalance onshelfToInv(OnshelfDetail onshelfDetail, Onshelf onshelf) {
        InventoryBalance inventoryBalance = new InventoryBalance();
        inventoryBalance.setType((short) 1);
        //客户信息
        Custom custom = customDAO.selectByPrimaryKey(onshelfDetail.getCustomId());
        inventoryBalance.setCustomId(custom.getId());
        inventoryBalance.setCustomCode(custom.getCustomCode());
        inventoryBalance.setCustomName(custom.getCustomName());
        //商品类信息
        inventoryBalance.setCommodityId(onshelfDetail.getCommodityId());
        inventoryBalance.setCommodityCode(onshelfDetail.getCommodityCode());
        inventoryBalance.setCommodityName(goodsDAO.selectByPrimaryKey(onshelfDetail.getCommodityId()).getGoodsName());
        inventoryBalance.setWarehouseId(onshelf.getWarehouseId());
        inventoryBalance.setWarehouseCode(onshelf.getWarehouseCode());
        inventoryBalance.setWarehouseName(onshelf.getWarehouseName());
        inventoryBalance.setAreaCode(onshelf.getOnshelfAreaCode());
        //库位增加
        WarehouseLocation loc = warehouseLocationDAO.selectByPrimaryKey(onshelfDetail.getFactLocationId());
        inventoryBalance.setLocationId(loc.getId());
        inventoryBalance.setLocationCode(loc.getLocationCode());
        inventoryBalance.setLocationName(loc.getLocationName());
        //包装增加
        Container con = containerDAO.selectByPrimaryKey(onshelfDetail.getContainerId());
        inventoryBalance.setContainerId(con.getId());
        inventoryBalance.setContainerCode(con.getCode());
        inventoryBalance.setContainerName(con.getName());
        inventoryBalance.setSkuId(onshelfDetail.getSkuId());
        inventoryBalance.setSkuName(onshelfDetail.getSkuName());
        inventoryBalance.setSkuCode(onshelfDetail.getSkuCode());
        inventoryBalance.setUnit(onshelfDetail.getUnit());
        inventoryBalance.setProductCompany(onshelfDetail.getProductCompany());
        inventoryBalance.setProductTime(onshelfDetail.getProductTime());
        inventoryBalance.setProductBatch(onshelfDetail.getProductBatch());
        inventoryBalance.setInBoundTime(new Date());
        return inventoryBalance;
    }

    @Override
    public Integer packageOnshelf(OnshelfDetail onshelfDetail) {
        onshelfDetail.setOnshelfState(1);
        onshelfDetail.setOnshelfTime(new Date());
        InboundPlanDetail inboundPlanDetail = inboundPlanDetailDAO.selectByInboundTraceCode(onshelfDetail.getTraceCode()).get(0);
        inboundPlanDetail.setPredictReceiveLocation(onshelfDetail.getFactLocationId());
        inboundPlanDetail.setPredictReceiveLocationCode(onshelfDetail.getContainerCode());
        inboundPlanDetailDAO.updateByPrimaryKey(inboundPlanDetail);
        return onshelfDetailDAO.updateByPrimaryKey(onshelfDetail);
    }

    @Override
    public Boolean packageOnshelfCheck(Onshelf onshelf) {
        if (onshelfDetailDAO.checkOnshelf(onshelf.getId()).size() == 0) {
            return true;
        }
        return false;
    }

    @Override
    public Integer addOnshelfDetail(DetailAndCode<OnshelfDetail> detailAndCode) {
        detailAndCode.getDetail().setOnshelfId(onshelfDAO.selectByOnshelfCode(detailAndCode.getCode()).get(0).getId());
        detailAndCode.getDetail().setOnshelfState(1);
        detailAndCode.getDetail().setOnshelfTime(new Date());
        onshelfDetailDAO.insertSelective(detailAndCode.getDetail());
        return 1;
    }

    @Override
    public Integer setOnshelfOnshelfing(Onshelf onshelf) {
        onshelf.setOnshelfState(3);
        List<OnshelfDetail> onshelfDetails = onshelfDetailDAO.selectByOnshelfId(onshelf);
        Set<Integer> ids = new HashSet<>();
        if (onshelf.getSourceType() != 0) {
            for (OnshelfDetail onshelfDetail : onshelfDetails) {
                ids.add(inboundPlanDetailDAO.selectByInboundTraceCode(onshelfDetail.getTraceCode()).get(0).getPlanId());
            }
            for (Integer id : ids) {
                InboundPlan inboundPlan = inboundPlanDAO.selectByPrimaryKey(id);
                if (inboundPlan != null) {
                    inboundPlan.setInboundState(12);
                    inboundPlanDAO.updateState(inboundPlan);
                }
            }
        }
        return onshelfDAO.updateByPrimaryKey(onshelf);
    }

    public HttpResult<?> addOnshelfBatch(List<FrontId> ids) {
        for (int i = 0; i < ids.size(); i++) {
            String code = codeService.code("ONB");
            Onshelf onshelf = new Onshelf();
            onshelf.setId(ids.get(i).getId());
            onshelf.setOnshelfBatch(code);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, ids.size());
    }

    @Override
    public HttpResult<?> wcsTaskExecute(List<String> packageBarcodes) {
        OnshelfDetail onshelfDetail = new OnshelfDetail();
        HashSet<Integer> ids = new HashSet<>();
        int num = 0;
        for (int i = 0; i < packageBarcodes.size(); i++) {
            onshelfDetail.setContainerBarcode(packageBarcodes.get(i));
            onshelfDetail = onshelfDetailDAO.selectByContainerBarcode(onshelfDetail).get(0);
            ids.add(onshelfDetail.getOnshelfId());
            onshelfDetail.setOnshelfState(3);
            onshelfDetailDAO.updateByPrimaryKey(onshelfDetail);
            num++;
        }
        if (ids.size() != 0) {
            for (Integer id : ids) {
                if (onshelfDetailDAO.selectByOnshelfIdAndState(id).size() == 0) {
                    Onshelf onshelf = new Onshelf();
                    onshelf.setId(id);
                    updateInventoryByONF(onshelf);
                }
            }
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, num);
    }

    @Override
    public BaokaiHttpResult<?> baokaiWcsTaskExecute(WcsOnshelfReturn wcsOnshelfReturn) {
        OnshelfDetail onshelfDetail = new OnshelfDetail();
        onshelfDetail.setContainerBarcode(wcsOnshelfReturn.getPalletid());
        onshelfDetail = onshelfDetailDAO.selectByContainerBarcode(onshelfDetail).get(0);
        onshelfDetail.setFactLocationName(wcsOnshelfReturn.getLoc());
        WarehouseLocation warehouseLocation = new WarehouseLocation();
        warehouseLocation.setLocationName(wcsOnshelfReturn.getLoc());
        String areaCode = onshelfDAO.selectByPrimaryKey(onshelfDetail.getOnshelfId()).getOnshelfAreaCode();
        onshelfDetail.setFactLocationId(warehouseLocationDAO.selectByLocationName(warehouseLocation).getId());
        InboundPlanDetail inboundPlanDetail = inboundPlanDetailDAO.selectByInboundTraceCode(onshelfDetail.getTraceCode()).get(0);
        inboundPlanDetail.setPredictReceiveLocation(onshelfDetail.getFactLocationId());
        inboundPlanDetail.setPredictReceiveLocationCode(onshelfDetail.getContainerCode());
        inboundPlanDetailDAO.updateByPrimaryKey(inboundPlanDetail);
        InventoryBalance inventoryBalance = new InventoryBalance();
        InboundPlan inboundPlan = inboundPlanDAO.selectByPrimaryKey(inboundPlanDetail.getPlanId());
        inventoryBalance.setBalanceCode(onshelfDetail.getContainerBarcode());
        inventoryBalance.setType((short) 1);
        inventoryBalance.setCustomId(inboundPlanDetail.getCustomId());
        inventoryBalance.setCustomCode(inboundPlanDetail.getCustomCode());
        inventoryBalance.setCommodityId(inboundPlanDetail.getCommodityId());
        inventoryBalance.setCommodityCode(inboundPlanDetail.getCommodityCode());
        inventoryBalance.setWarehouseCode(inboundPlan.getWarehouseCode());
        inventoryBalance.setAreaCode(areaCode);
        inventoryBalance.setLocationId(onshelfDetail.getFactLocationId());
        inventoryBalance.setLocationCode(onshelfDetail.getFactLocationName());
        inventoryBalance.setContainerId(inboundPlanDetail.getContainerId());
        inventoryBalance.setContainerCode(inboundPlanDetail.getContainerCode());
        inventoryBalance.setSkuId(inboundPlanDetail.getSkuId());
        inventoryBalance.setSkuName(inboundPlanDetail.getSkuName());
        inventoryBalance.setSkuCode(inboundPlanDetail.getSkuCode());
        inventoryBalance.setUnit(inboundPlanDetail.getUnit());
        inventoryBalance.setAvailableCnt(inboundPlanDetail.getReceivedNum().intValue());
        inventoryBalance.setTotalCnt(inboundPlanDetail.getReceivedNum().intValue());
        inventoryBalance.setVolume(inboundPlanDetail.getTotalVolumn());
        inventoryBalance.setWeight(inboundPlanDetail.getTotalWeight());
        inventoryBalance.setProductCompany(inboundPlanDetail.getProductCompany());
        inventoryBalance.setProductTime(inboundPlanDetail.getProductTime());
        inventoryBalance.setProductBatch(inboundPlanDetail.getProductBatch());
        inventoryBalance.setTraceCode(inboundPlanDetail.getInboundTraceCode());
        inventoryBalance.setInBoundTime(new Date());
        stockService.addInventoryBalance(inventoryBalance);
        Reqcode reqcode = new Reqcode("12321321413");
        BaokaiHttpResult baokaiHttpResult = new BaokaiHttpResult();
        baokaiHttpResult.setErrcode(0);
        baokaiHttpResult.setErrmsg("");
        baokaiHttpResult.setData(reqcode);
        return baokaiHttpResult;
    }

    @Override
    public HttpResult<?> reportForm(HeadAndDetail<Onshelf, OnshelfDetail> headAndDetail) {
        return null;
    }
}

