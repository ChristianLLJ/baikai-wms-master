package com.bupt.service.inbound.impl;

import com.bupt.DTO.*;
import com.bupt.DTO.inbound.GenerateIPLDTO;
import com.bupt.mapper.*;
import com.bupt.pojo.InboundPlan;
import com.bupt.pojo.InboundPlanDetail;
import com.bupt.pojo.PurchaseOrder;
import com.bupt.pojo.PurchaseOrderDetail;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.BaseService;
import com.bupt.service.authority.CodeService;
import com.bupt.service.inbound.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PurchaseOrderServiceImpl extends BaseService<PurchaseOrder, PurchaseOrderDetail, PurchaseOrderDAO, PurchaseOrderDetailDAO> implements PurchaseOrderService {
    @Autowired
    PurchaseOrderDAO purchaseOrderDAO;
    @Autowired
    PurchaseOrderDetailDAO purchaseOrderDetailDAO;
    @Autowired
    CodeService codeService;
    @Autowired
    InboundPlanDAO inboundPlanDAO;
    @Autowired
    InboundPlanDetailDAO inboundPlanDetailDAO;
    @Autowired
    StaffDAO staffDAO;

    /**
     * 筛选采购订单头
     *
     * @param screenDTO
     * @return
     */
    @Override
    public List<PurchaseOrder> selectPurchaseOrder(ScreenDTO<PurchaseOrder> screenDTO) {
        if (screenDTO.getSearchDTO() != null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage() - 1) * screenDTO.getSearchDTO().getNum());
        return purchaseOrderDAO.screen(screenDTO);
    }

    /**
     * 筛选采购订单头数量
     *
     * @param screenDTO
     * @return
     */
    @Override
    public Integer selectPurchaseOrderNum(ScreenDTO<PurchaseOrder> screenDTO) {
        return purchaseOrderDAO.numScreen(screenDTO);
    }

    /**
     * 筛选采购订单表
     *
     * @param screenDTO
     * @return
     */
    @Override
    public SumAndList<PurchaseOrderDetail> selectPurchaseOrderDetail(ScreenDTO<PurchaseOrderDetail> screenDTO) {
        if (screenDTO.getSearchDTO() != null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage() - 1) * screenDTO.getSearchDTO().getNum());
        List<PurchaseOrderDetail> purchaseOrderDetails = purchaseOrderDetailDAO.screen(screenDTO);
        SumAndList<PurchaseOrderDetail> purchaseOrderDetailSumAndList = new SumAndList<>();
        screenDTO.setSearchDTO(null);
        List<PurchaseOrderDetail> purchaseOrderDetails1 = purchaseOrderDetailDAO.screen(screenDTO);
        System.out.println(purchaseOrderDetails1.size());
        double preNum = 0, factNum = 0, volume = 0, weight = 0, netWeight = 0, money = 0;
        for (int i = 0; i < purchaseOrderDetails1.size(); i++) {
            if (purchaseOrderDetails1.get(i).getPredictNum() != null)
                preNum += purchaseOrderDetails1.get(i).getPredictNum();
            if (purchaseOrderDetails1.get(i).getFactNum() != null)
                factNum += purchaseOrderDetails1.get(i).getFactNum();
            if (purchaseOrderDetails1.get(i).getTotalVolumn() != null)
                volume += purchaseOrderDetails1.get(i).getTotalVolumn();
            if (purchaseOrderDetails1.get(i).getTotalWeight() != null)
                weight += purchaseOrderDetails1.get(i).getTotalWeight();
            if (purchaseOrderDetails1.get(i).getTotalNetWeight() != null)
                netWeight += purchaseOrderDetails1.get(i).getTotalNetWeight();
            if (purchaseOrderDetails1.get(i).getTotalMoney() != null)
                money += purchaseOrderDetails1.get(i).getTotalMoney();
        }
        HashMap<String, Double> sums = new HashMap<>();
        sums.put("predictNum", preNum);
        sums.put("factNum", factNum);
        sums.put("totalVolumn", volume);
        sums.put("totalWeight", weight);
        sums.put("totalNetWeight", netWeight);
        sums.put("totalMoney", money);
        purchaseOrderDetailSumAndList.setList(purchaseOrderDetails);
        purchaseOrderDetailSumAndList.setSums(sums);
        return purchaseOrderDetailSumAndList;
    }

    /**
     * 筛选采购订单表数量
     *
     * @param screenDTO
     * @return
     */
    @Override
    public Integer selectPurchaseOrderDetailNum(ScreenDTO<PurchaseOrderDetail> screenDTO) {
        return purchaseOrderDetailDAO.numScreen(screenDTO);
    }

    /**
     * 采购订单保存
     *
     * @param headAndDetail
     * @return
     */
    @Override
    public Integer save(HeadAndDetail<PurchaseOrder, PurchaseOrderDetail> headAndDetail) {
        //设置入库计划单状态为正在填写
        headAndDetail.getHead().setOrderState(0);
        Integer num = 0;
        //如果前端没有传进来id,说明为第一次填写
        if (headAndDetail.getHead().getId() == null) {
            //设置入库计划单代码
            //插入表头
            purchaseOrderDAO.insertSelective(headAndDetail.getHead());
        }
        //非第一次填写
        else {
            //对表头做更新操作
            purchaseOrderDAO.updateByPrimaryKey(headAndDetail.getHead());
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
    public Integer add(HeadAndDetail<PurchaseOrder, PurchaseOrderDetail> headAndDetail) {
        //设置入库计划单状态为已填写
        headAndDetail.getHead().setOrderState(1);
        //如果没有保存过
        if (headAndDetail.getHead().getId() == null) {
            //设置入库计划单代码
            //插入表头
            purchaseOrderDAO.insertSelective(headAndDetail.getHead());
            Integer id = headAndDetail.getHead().getId();
            //插入表单
            for (PurchaseOrderDetail purchaseOrderDetail : headAndDetail.getDetails()) {
                purchaseOrderDetail.setOrderId(id);
                purchaseOrderDetailDAO.insertSelective(purchaseOrderDetail);
            }
        }
        return headAndDetail.getHead().getId();
    }

    /**
     * 采购订单取消
     *
     * @param headAndDetail
     * @return
     */
    @Override
    public Integer cancel(HeadAndDetail<PurchaseOrder, PurchaseOrderDetail> headAndDetail) {
        headAndDetail.getHead().setOrderState(4);
        return purchaseOrderDAO.updateByPrimaryKeySelective(headAndDetail.getHead());
    }

    /**
     * 采购订单完成（关闭）
     *
     * @param
     * @return
     */
    @Override
    public Integer close(PurchaseOrder purchaseOrder) {
        purchaseOrder.setOrderState(3);
        return purchaseOrderDAO.updateByPrimaryKeySelective(purchaseOrder);
    }

    /**
     * 采购订单生成入库计划单
     *
     * @param generateIPLDTO
     * @return
     */
    @Override
    public Integer generate(GenerateIPLDTO generateIPLDTO) {
        Integer containerNum = 0;
        Double skuNum = 0.0;
        Double num = 0.0;
        //设置入库计划单表头
        generateIPLDTO.getInboundPlanDetailHeadAndDetail().getHead().setInboundState(1);
        generateIPLDTO.getInboundPlanDetailHeadAndDetail().getHead().setPlanId(codeService.code("IPL"));
        generateIPLDTO.getInboundPlanDetailHeadAndDetail().getHead().setCreateTime(new Date());
        generateIPLDTO.getInboundPlanDetailHeadAndDetail().getHead().setIsAllChecke(false);
        generateIPLDTO.getInboundPlanDetailHeadAndDetail().getHead().setIsReceived(false);
        generateIPLDTO.getInboundPlanDetailHeadAndDetail().getHead().setIsUsable((byte) 1);
        generateIPLDTO.getInboundPlanDetailHeadAndDetail().getHead().setIsPackaged(false);
        generateIPLDTO.getInboundPlanDetailHeadAndDetail().getHead().setIsStacked(false);
        generateIPLDTO.getInboundPlanDetailHeadAndDetail().getHead().setSourceType(1);
        inboundPlanDAO.insertSelective(generateIPLDTO.getInboundPlanDetailHeadAndDetail().getHead());
        Integer id = generateIPLDTO.getInboundPlanDetailHeadAndDetail().getHead().getId();
        //设置入库计划单表单
        for (int i = 0; i < generateIPLDTO.getPurchaseOrderDetailHeadAndDetail().getDetails().size(); i++) {
            PurchaseOrder purchaseOrder = purchaseOrderDAO.selectByPrimaryKey(generateIPLDTO.getPurchaseOrderDetailHeadAndDetail().getDetails().get(i).getOrderId());
            purchaseOrder.setOrderState(2);
            purchaseOrderDAO.updateByPrimaryKey(purchaseOrder);
            generateIPLDTO.getInboundPlanDetailHeadAndDetail().getDetails().get(i).setPlanId(id);
            generateIPLDTO.getInboundPlanDetailHeadAndDetail().getDetails().get(i).setIsReceived((byte) 0);
            generateIPLDTO.getInboundPlanDetailHeadAndDetail().getDetails().get(i).setPurchaseRow(generateIPLDTO.getPurchaseOrderDetailHeadAndDetail().getDetails().get(i).getId());
            generateIPLDTO.getInboundPlanDetailHeadAndDetail().getDetails().get(i).setPurchaseId(generateIPLDTO.getPurchaseOrderDetailHeadAndDetail().getDetails().get(i).getOrderId());
            generateIPLDTO.getInboundPlanDetailHeadAndDetail().getDetails().get(i).setPurchaseCode(purchaseOrder.getOrderId());
            generateIPLDTO.getInboundPlanDetailHeadAndDetail().getDetails().get(i).setCommodityCode(generateIPLDTO.getPurchaseOrderDetailHeadAndDetail().getDetails().get(i).getCommodityCode());
            generateIPLDTO.getInboundPlanDetailHeadAndDetail().getDetails().get(i).setCommodityId(generateIPLDTO.getPurchaseOrderDetailHeadAndDetail().getDetails().get(i).getCommodityId());
            generateIPLDTO.getInboundPlanDetailHeadAndDetail().getDetails().get(i).setCommodityName(generateIPLDTO.getPurchaseOrderDetailHeadAndDetail().getDetails().get(i).getCommodityName());
            generateIPLDTO.getInboundPlanDetailHeadAndDetail().getDetails().get(i).setIsChecked((byte) 0);
            generateIPLDTO.getInboundPlanDetailHeadAndDetail().getDetails().get(i).setIsPlate((byte) 0);
            generateIPLDTO.getInboundPlanDetailHeadAndDetail().getDetails().get(i).setPredictReceiveLocation(null);
            generateIPLDTO.getInboundPlanDetailHeadAndDetail().getDetails().get(i).setPredictReceiveLocationCode(null);
            generateIPLDTO.getInboundPlanDetailHeadAndDetail().getDetails().get(i).setSkuCode(generateIPLDTO.getPurchaseOrderDetailHeadAndDetail().getDetails().get(i).getSkuCode());
            generateIPLDTO.getInboundPlanDetailHeadAndDetail().getDetails().get(i).setSkuId(generateIPLDTO.getPurchaseOrderDetailHeadAndDetail().getDetails().get(i).getSkuId());
            generateIPLDTO.getInboundPlanDetailHeadAndDetail().getDetails().get(i).setSkuName(generateIPLDTO.getPurchaseOrderDetailHeadAndDetail().getDetails().get(i).getSkuName());
            generateIPLDTO.getInboundPlanDetailHeadAndDetail().getDetails().get(i).setTotalNetWeight(generateIPLDTO.getPurchaseOrderDetailHeadAndDetail().getDetails().get(i).getTotalNetWeight());
            generateIPLDTO.getInboundPlanDetailHeadAndDetail().getDetails().get(i).setTotalVolumn(generateIPLDTO.getPurchaseOrderDetailHeadAndDetail().getDetails().get(i).getTotalVolumn());
            generateIPLDTO.getInboundPlanDetailHeadAndDetail().getDetails().get(i).setTotalWeight(generateIPLDTO.getPurchaseOrderDetailHeadAndDetail().getDetails().get(i).getTotalWeight());
            generateIPLDTO.getInboundPlanDetailHeadAndDetail().getDetails().get(i).setUnit(generateIPLDTO.getPurchaseOrderDetailHeadAndDetail().getDetails().get(i).getUnit());
            generateIPLDTO.getInboundPlanDetailHeadAndDetail().getDetails().get(i).setContainerId(generateIPLDTO.getPurchaseOrderDetailHeadAndDetail().getDetails().get(i).getContainerId());
            generateIPLDTO.getInboundPlanDetailHeadAndDetail().getDetails().get(i).setContainerCode(generateIPLDTO.getPurchaseOrderDetailHeadAndDetail().getDetails().get(i).getContainerCode());
            generateIPLDTO.getInboundPlanDetailHeadAndDetail().getDetails().get(i).setContainerName(generateIPLDTO.getPurchaseOrderDetailHeadAndDetail().getDetails().get(i).getContainerName());
            if (generateIPLDTO.getInboundPlanDetailHeadAndDetail().getDetails().get(i).getContainerId() != null) {
                generateIPLDTO.getInboundPlanDetailHeadAndDetail().getDetails().get(i).setIsPackaged(true);
            } else generateIPLDTO.getInboundPlanDetailHeadAndDetail().getDetails().get(i).setIsPackaged(false);
            generateIPLDTO.getInboundPlanDetailHeadAndDetail().getDetails().get(i).setNum(generateIPLDTO.getPurchaseOrderDetailHeadAndDetail().getDetails().get(i).getPredictNum());
            generateIPLDTO.getInboundPlanDetailHeadAndDetail().getDetails().get(i).setReceivedNum(0.0);
            generateIPLDTO.getInboundPlanDetailHeadAndDetail().getDetails().get(i).setCustomId(purchaseOrder.getCustomId());
            generateIPLDTO.getInboundPlanDetailHeadAndDetail().getDetails().get(i).setCustomCode(purchaseOrder.getCustomCode());
            generateIPLDTO.getInboundPlanDetailHeadAndDetail().getDetails().get(i).setCustomName(purchaseOrder.getCustomName());
            generateIPLDTO.getInboundPlanDetailHeadAndDetail().getDetails().get(i).setSupplierId(purchaseOrder.getSupplierId());
            generateIPLDTO.getInboundPlanDetailHeadAndDetail().getDetails().get(i).setSupplierCode(purchaseOrder.getSupplierCode());
            generateIPLDTO.getInboundPlanDetailHeadAndDetail().getDetails().get(i).setSupplierName(purchaseOrder.getSupplierName());
            containerNum = generateIPLDTO.getPurchaseOrderDetailHeadAndDetail().getDetails().get(i).getPredictContainerNum() + containerNum;
            num = generateIPLDTO.getPurchaseOrderDetailHeadAndDetail().getDetails().get(i).getPredictNum() + num;
            skuNum = skuNum + 1;
            generateIPLDTO.getInboundPlanDetailHeadAndDetail().getDetails().get(i).setInboundTraceCode(codeService.code("INS"));
            PurchaseOrderDetail purchaseOrderDetail = new PurchaseOrderDetail();
            purchaseOrderDetail.setId(generateIPLDTO.getPurchaseOrderDetailHeadAndDetail().getDetails().get(i).getId());
            purchaseOrderDetail.setIsGenerate(true);
            purchaseOrderDetailDAO.updateByPrimaryKeySelective(purchaseOrderDetail);
            inboundPlanDetailDAO.insertSelective(generateIPLDTO.getInboundPlanDetailHeadAndDetail().getDetails().get(i));
        }
        InboundPlan tempInboundPlan = new InboundPlan();
        tempInboundPlan.setId(id);
        tempInboundPlan.setPredictContainerNum(containerNum);
        tempInboundPlan.setPredictNum(num);
        tempInboundPlan.setPredictSkuNum(skuNum);
        inboundPlanDAO.updateByPrimaryKeySelective(tempInboundPlan);
        return generateIPLDTO.getPurchaseOrderDetailHeadAndDetail().getDetails().size();
    }

    @Override
    public List<PurchaseOrder> selectPurchaseOrder(List<PurchaseOrder> purchaseOrders) {
        List<PurchaseOrder> purchaseOrders1 = new LinkedList<>();
        for (PurchaseOrder purchaseOrder : purchaseOrders) {
            purchaseOrders1.add(purchaseOrderDAO.selectByPrimaryKey(purchaseOrder.getId()));
        }
        return purchaseOrders1;
    }

    public HttpResult<?> importJindiePUR(List<JindieERPPurchaseOrder> jindieERPPurchaseOrders, ScreenDTO<JindieERPPurchaseOrder> screenDTO) {
        HashMap<String, Integer> heads = new HashMap<>();
        List<HeadAndDetail<PurchaseOrder, PurchaseOrderDetail>> headAndDetailList = new LinkedList<>();
        List<HeadAndDetail<PurchaseOrder, PurchaseOrderDetail>> result = new LinkedList<>();
        int order = 0;
        for (JindieERPPurchaseOrder jindieERPPurchaseOrder : jindieERPPurchaseOrders) {
            if (!heads.containsKey(jindieERPPurchaseOrder.getFBillNo())) {
                heads.put(jindieERPPurchaseOrder.getFBillNo(), order);
                order++;
                HeadAndDetail<PurchaseOrder, PurchaseOrderDetail> headAndDetail = new HeadAndDetail<>();
                headAndDetail.setHead(new PurchaseOrder());
                headAndDetail.setDetails(new LinkedList<>());
                headAndDetail.getHead().setOrderId(jindieERPPurchaseOrder.getFBillNo());
                headAndDetail.getHead().setOrderState(1);
                //headAndDetail.getHead().setOrderType();
                headAndDetail.getHead().setSupplierName(jindieERPPurchaseOrder.getFSupplierId());
                headAndDetail.getHead().setWarehouseName(jindieERPPurchaseOrder.getFPurchaseOrgId());
                headAndDetailList.add(headAndDetail);
            }
            HeadAndDetail<PurchaseOrder, PurchaseOrderDetail> headAndDetail = headAndDetailList.get(heads.get(jindieERPPurchaseOrder.getFBillNo()));
            PurchaseOrderDetail purchaseOrderDetail = new PurchaseOrderDetail();
            purchaseOrderDetail.setSkuName(jindieERPPurchaseOrder.getFMaterialName());
            purchaseOrderDetail.setUnit(jindieERPPurchaseOrder.getFUnitId());
            purchaseOrderDetail.setPredictNum(jindieERPPurchaseOrder.getFQty());
            purchaseOrderDetail.setTotalMoney(jindieERPPurchaseOrder.getFEntryAmount());
            purchaseOrderDetail.setOrderId(heads.get(jindieERPPurchaseOrder.getFBillNo()));
            purchaseOrderDetail.setIsGenerate(false);
            headAndDetail.getDetails().add(purchaseOrderDetail);
        }
        for (int i = 0; i < headAndDetailList.size(); i++) {
            HeadAndDetail<PurchaseOrder, PurchaseOrderDetail> headAndDetail = headAndDetailList.get(i);
            headAndDetail.getHead().setCreateTime(new Date());
            headAndDetail.getHead().setAddPersonName(staffDAO.selectByPrimaryKey(headAndDetail.getHead().getAddPersonId()).getStaffName());
            Integer id = purchaseOrderDAO.insert(headAndDetail.getHead());
            for (int j = 0; j < headAndDetail.getDetails().size(); j++) {
                PurchaseOrderDetail purchaseOrderDetail = headAndDetail.getDetails().get(j);
                purchaseOrderDetail.setOrderId(id);
                purchaseOrderDetailDAO.insert(purchaseOrderDetail);
            }
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    public HttpResult<?> importPUR(List<HeadAndDetail<PurchaseOrder, PurchaseOrderDetail>> headAndDetailList) {
        for (int i = 0; i < headAndDetailList.size(); i++) {
            HeadAndDetail<PurchaseOrder, PurchaseOrderDetail> headAndDetail = headAndDetailList.get(i);
            headAndDetail.getHead().setCreateTime(new Date());
            headAndDetail.getHead().setAddPersonName(staffDAO.selectByPrimaryKey(headAndDetail.getHead().getAddPersonId()).getStaffName());
            Integer id = purchaseOrderDAO.insert(headAndDetail.getHead());
            for (int j = 0; j < headAndDetail.getDetails().size(); j++) {
                PurchaseOrderDetail purchaseOrderDetail = headAndDetail.getDetails().get(j);
                purchaseOrderDetail.setOrderId(id);
                purchaseOrderDetailDAO.insert(purchaseOrderDetail);
            }
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }
}
