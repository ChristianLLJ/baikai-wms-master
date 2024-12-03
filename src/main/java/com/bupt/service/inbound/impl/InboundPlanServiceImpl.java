package com.bupt.service.inbound.impl;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SumAndList;
import com.bupt.DTO.inbound.*;
import com.bupt.mapper.*;
import com.bupt.pojo.*;
import com.bupt.service.BaseService;
import com.bupt.service.Inwarehouse.StockService;
import com.bupt.service.authority.CodeService;
import com.bupt.service.inbound.InboundPlanService;
import com.bupt.service.util.AuditReasonService;
import com.bupt.strategy.OnshelfStrategy.OnshelfStrategyCon;
import com.bupt.strategy.OnshelfStrategy.OnshelfStrategyDefault;
import com.bupt.strategy.OnshelfStrategy.OnshelfStrategyInt;
import com.bupt.strategy.OnshelfStrategy.OnshelfStrategySameOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InboundPlanServiceImpl extends BaseService<InboundPlan,InboundPlanDetail,InboundPlanDAO,InboundPlanDetailDAO> implements InboundPlanService {
    @Autowired
    InboundPlanDAO inboundPlanDAO;
    @Autowired
    InboundPlanDetailDAO inboundPlanDetailDAO;
    @Autowired
    CodeService codeService;
    @Autowired
    QualityCheckDAO qualityCheckDAO;
    @Autowired
    QualityCheckDetailDAO qualityCheckDetailDAO;
    @Autowired
    StockService stockService;
    @Autowired
    PackingTableDAO packingTableDAO;
    @Autowired
    PackingDetailDAO packingDetailDAO;
    @Autowired
    ContainerDAO containerDAO;
    @Autowired
    CombineStackDAO combineStackDAO;
    @Autowired
    CombineStackDetailDAO combineStackDetailDAO;
    @Autowired
    CombineStackPackageDetailDAO combineStackPackageDetailDAO;
    @Autowired
    OnshelfAdviceDAO onshelfAdviceDAO;
    @Autowired
    OnshelfAdviceDetailDAO onshelfAdviceDetailDAO;
    @Autowired
    OnshelfStrategyDAO onshelfStrategyDAO;
    @Autowired
    OnshelfStrategyDetailDAO onshelfStrategyDetailDAO;
    @Autowired
    OnshelfStrategyDetailSpaceRelationDAO onshelfStrategyDetailSpaceRelationDAO;
    @Autowired
    OnshelfStrategyDetailLocationRelationDAO onshelfStrategyDetailLocationRelationDAO;
    @Autowired
    InboundDAO inboundDAO;
    @Autowired
    AuditReasonService auditReasonService;
    @Autowired
    InboundDetailDAO inboundDetailDAO;
    @Autowired
    StaffDAO staffDAO;
    @Autowired
    InventoryBalanceDAO inventoryBalanceDAO;
    /**
     * 分页筛选入库计划单头
     * @param screenDTO
     * @return
     */
    @Override
    public List<InboundPlan> selectInboundPlan(ScreenDTO<InboundPlan> screenDTO) {
        if(screenDTO.getSearchDTO()!=null)
        screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage()-1)* screenDTO.getSearchDTO().getNum());
        return inboundPlanDAO.screen(screenDTO);
    }

    @Override
    public List<InboundPlan> selectInboundPlan(List<InboundPlan> inboundPlans) {
        List<InboundPlan> inboundPlans1 = new LinkedList<>();
        for(InboundPlan inboundPlan : inboundPlans){
            inboundPlans1.add(inboundPlanDAO.selectByPrimaryKey(inboundPlan.getId()));
        }
        return inboundPlans1;
    }

    /**
     * 分页筛选入库计划单表
     * @param screenDTO
     * @return
     */
    @Override
    public SumAndList<InboundPlanDetail> selectInboundPlanDetail(ScreenDTO<InboundPlanDetail> screenDTO) {
        if(screenDTO.getSearchDTO()!=null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage()-1)* screenDTO.getSearchDTO().getNum());
        List<InboundPlanDetail> inboundPlanDetails = inboundPlanDetailDAO.screen(screenDTO);
        SumAndList<InboundPlanDetail> inboundPlanDetailSumAndList = new SumAndList<>();
        screenDTO.setSearchDTO(null);
        List<InboundPlanDetail> inboundPlanDetails1 = inboundPlanDetailDAO.screen(screenDTO);
        double isChecked=0,isPlate=0,volume=0,weight=0,netWeight=0,isReceived=0,isPackaged=0,num=0,receivedNum=0,isOnshelf=0;
        for(int i =0;i<inboundPlanDetails1.size();i++){
            if(inboundPlanDetails1.get(i).getIsChecked()!=null&&inboundPlanDetails1.get(i).getIsChecked()==1)
                isChecked ++;
            if(inboundPlanDetails1.get(i).getIsPlate()!=null&&inboundPlanDetails1.get(i).getIsPlate()==1)
                isPlate ++;
            if(inboundPlanDetails1.get(i).getTotalVolumn()!=null)
                volume+=inboundPlanDetails1.get(i).getTotalVolumn();
            if (inboundPlanDetails1.get(i).getTotalWeight()!=null)
                weight+=inboundPlanDetails1.get(i).getTotalWeight();
            if(inboundPlanDetails1.get(i).getTotalNetWeight()!=null)
                netWeight += inboundPlanDetails1.get(i).getTotalNetWeight();
            if(inboundPlanDetails1.get(i).getIsReceived()!=null&&inboundPlanDetails1.get(i).getIsReceived()==1)
                isReceived ++;
            if(inboundPlanDetails1.get(i).getIsPackaged()!=null&&inboundPlanDetails1.get(i).getIsPackaged()==true)
                isPackaged ++;
            if(inboundPlanDetails1.get(i).getNum()!=null)
                num += inboundPlanDetails1.get(i).getNum();
            if(inboundPlanDetails1.get(i).getReceivedNum()!=null)
                receivedNum += inboundPlanDetails1.get(i).getReceivedNum();
            if(inboundPlanDetails1.get(i).getIsOnshelf()!=null&&inboundPlanDetails1.get(i).getIsOnshelf()==true)
                isOnshelf ++;
        }
        HashMap<String,Double> sums = new HashMap<>();
        sums.put("isChecked",isChecked);
        sums.put("isPlate",isPlate);
        sums.put("totalVolumn",volume);
        sums.put("totalWeight",weight);
        sums.put("totalNetWeight",netWeight);
        sums.put("isReceived",isReceived);
        sums.put("isPackaged",isPackaged);
        sums.put("num",num);
        sums.put("receivedNum",receivedNum);
        sums.put("isOnshelf",isOnshelf);
        inboundPlanDetailSumAndList.setList(inboundPlanDetails);
        inboundPlanDetailSumAndList.setSums(sums);
        return inboundPlanDetailSumAndList;
    }

    /**
     * 分页筛选入库计划单头总数
     * @param screenDTO
     * @return
     */
    @Override
    public Integer selectInboundPlanNum(ScreenDTO<InboundPlan> screenDTO) {
        return inboundPlanDAO.numScreen(screenDTO);
    }

    /**
     * 分页筛选入库计划单表数量
     * @param screenDTO
     * @return
     */
    @Override
    public Integer selectInboundPlanDetailNum(ScreenDTO<InboundPlanDetail> screenDTO) {
        return inboundPlanDetailDAO.numScreen(screenDTO);
    }

    @Override
    public List<Inbound> selectInbound(ScreenDTO<Inbound> screenDTO) {
        if(screenDTO.getSearchDTO()!=null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage()-1)* screenDTO.getSearchDTO().getNum());
        return inboundDAO.screen(screenDTO);
    }

    @Override
    public List<Inbound> selectInbound(List<Inbound> inboundPlans) {
        List<Inbound> inboundPlans1 = new LinkedList<>();
        for(Inbound inbound : inboundPlans){
            inboundPlans1.add(inboundDAO.selectByPrimaryKey(inbound.getId()));
        }
        return inboundPlans1;
    }

    @Override
    public List<InboundDetail> selectInboundDetail(ScreenDTO<InboundDetail> screenDTO) {
        if(screenDTO.getSearchDTO()!=null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage()-1)* screenDTO.getSearchDTO().getNum());
        return inboundDetailDAO.screen(screenDTO);
    }

    @Override
    public Integer selectInboundNum(ScreenDTO<Inbound> screenDTO) {
        return inboundDAO.numScreen(screenDTO);
    }

    @Override
    public Integer selectInboundDetailNum(ScreenDTO<InboundDetail> screenDTO) {
        return inboundDetailDAO.numScreen(screenDTO);
    }

    /**
     * 入库计划单保存
     * @param headAndDetail
     * @return
     */
    @Transactional
    @Override
    public Integer save(HeadAndDetail<InboundPlan, InboundPlanDetail> headAndDetail) {
        //设置入库计划单状态为正在填写
        headAndDetail.getHead().setInboundState(0);
        Integer num=0;
        //如果前端没有传进来id,说明为第一次填写
        if(headAndDetail.getHead().getId()==null){
            //设置入库计划单代码
            //插入表头
            inboundPlanDAO.insertSelective(headAndDetail.getHead());
            Integer id=headAndDetail.getHead().getId();
        }
        //非第一次填写
        else{
            //对表头做更新操作
            inboundPlanDAO.updateByPrimaryKey(headAndDetail.getHead());
        }
        return headAndDetail.getHead().getId();
    }

    /**
     * 提交入库计划单
     * @param headAndDetail
     * @return
     */
    @Override
    @Transactional
    public Integer  add(HeadAndDetail<InboundPlan, InboundPlanDetail> headAndDetail) {
        //设置入库计划单状态为已填写
        headAndDetail.getHead().setInboundState(1);
        //如果没有保存过
        if(headAndDetail.getHead().getId()==null){
            //设置入库计划单代码
            //插入表头
            inboundPlanDAO.insertSelective(headAndDetail.getHead());
            Integer id=headAndDetail.getHead().getId();
            //插入表单
            if(headAndDetail.getDetails()!=null){
                for(InboundPlanDetail inboundPlanDetail:headAndDetail.getDetails()){
                    inboundPlanDetail.setPlanId(id);
                    inboundPlanDetail.setInboundTraceCode(codeService.code("INS"));
                    inboundPlanDetailDAO.insertSelective(inboundPlanDetail);
                }
            }
        }
        return headAndDetail.getHead().getId();
    }
    /**
     * 入库计划单取消
     * @param headAndDetail
     * @return
     */
    @Override
    public Integer cancel(HeadAndDetail<InboundPlan, InboundPlanDetail> headAndDetail) {
        headAndDetail.getHead().setInboundState(999);
        return inboundPlanDAO.updateByPrimaryKeySelective(headAndDetail.getHead());
    }

    /**
     * 入库计划单已完成或已关闭
     * @param
     * @return
     */
    @Override
    public Integer close(DetailAndCode<InboundPlan> detailAndCode) {
        detailAndCode.getDetail().setInboundState(14);
        detailAndCode.getDetail().setCloseReason("正常结单");
        return inboundPlanDAO.updateByPrimaryKeySelective(detailAndCode.getDetail());
    }

    public Integer unaudit(DetailAndCode<InboundPlan> detailAndCode){
        InboundPlan inboundPlan  = new InboundPlan();
        inboundPlan.setId(detailAndCode.getDetail().getId());
        inboundPlan.setInboundState(14);
        inboundPlan.setCloseReason("审核不通过");
        inboundPlan.setCheckPersonName(staffDAO.selectByPrimaryKey(detailAndCode.getDetail().getCheckPersonId()).getStaffName());
        inboundPlan.setCheckTime(new Date());
        inboundPlanDAO.updateByPrimaryKeySelective(inboundPlan);
        AuditReason auditReason = new AuditReason();
        auditReason.setTableId(detailAndCode.getDetail().getId());
        auditReason.setTableCode(detailAndCode.getDetail().getPlanId());
        auditReason.setCreateTime(inboundPlan.getCreateTime());
        auditReason.setAuditReason(detailAndCode.getCode());
        return auditReasonService.add(auditReason);
    }
    /**
     * 生成质检单
     * @param generateCHE
     * @return
     */
    @Override
    public Integer generateCHE(GenerateCHE generateCHE) {
        //初始化
        InboundPlan inboundPlan=generateCHE.getInboundPlanDetailHeadAndDetail().getHead();
        List<InboundPlanDetail> inboundPlanDetails=generateCHE.getInboundPlanDetailHeadAndDetail().getDetails();
        QualityCheck qualityCheck=generateCHE.getQualityCheckDetailHeadAndDetail().getHead();
        List<QualityCheckDetail> qualityCheckDetails = generateCHE.getQualityCheckDetailHeadAndDetail().getDetails();
        int num=1;
        //如果质检类型为全检
        if(qualityCheck.getCheckType()==1){
            //生成质检代码
            //设置参数生成表头
            qualityCheck.setCheckState(1);
            qualityCheck.setCreateTime(new Date());
            qualityCheck.setSourceType(1);
            num =qualityCheckDAO.insertSelective(qualityCheck);
            Integer id = qualityCheck.getId();
            //生成表单
            for(int i=0;i<inboundPlanDetails.size();i++){
                qualityCheckDetails.get(i).setCheckId(id);
                qualityCheckDetails.get(i).setCheckCode(qualityCheck.getCheckCode());
                qualityCheckDetails.get(i).setCommodityId(inboundPlanDetails.get(i).getCommodityId());
                qualityCheckDetails.get(i).setCommodityCode(inboundPlanDetails.get(i).getCommodityCode());
                qualityCheckDetails.get(i).setCommodityName(inboundPlanDetails.get(i).getCommodityName());
                qualityCheckDetails.get(i).setSkuId(inboundPlanDetails.get(i).getSkuId());
                qualityCheckDetails.get(i).setSkuCode(inboundPlanDetails.get(i).getSkuCode());
                qualityCheckDetails.get(i).setSkuName(inboundPlanDetails.get(i).getSkuName());
                qualityCheckDetails.get(i).setContainerId(inboundPlanDetails.get(i).getContainerId());
                qualityCheckDetails.get(i).setContainerCode(inboundPlanDetails.get(i).getContainerCode());
                qualityCheckDetails.get(i).setPredictCheckNum(inboundPlanDetails.get(i).getNum());
                qualityCheckDetails.get(i).setPredictNum(inboundPlanDetails.get(i).getNum());
                qualityCheckDetails.get(i).setContainerName(inboundPlanDetails.get(i).getContainerName());
                qualityCheckDetails.get(i).setContainerBarcode(inboundPlanDetails.get(i).getContainerBarcode());
                qualityCheckDetails.get(i).setUnit(inboundPlanDetails.get(i).getUnit());
                qualityCheckDetails.get(i).setProductBatch(inboundPlanDetails.get(i).getProductBatch());
                qualityCheckDetails.get(i).setProductFactory(inboundPlanDetails.get(i).getProductCompany());
                qualityCheckDetails.get(i).setProductTime(inboundPlanDetails.get(i).getProductTime());
                qualityCheckDetails.get(i).setCustomCode(inboundPlanDetails.get(i).getCustomCode());
                qualityCheckDetails.get(i).setCustomId(inboundPlanDetails.get(i).getCustomId());
                qualityCheckDetails.get(i).setCustomName(inboundPlanDetails.get(i).getCustomName());
                qualityCheckDetails.get(i).setSupplierCode(inboundPlanDetails.get(i).getSupplierCode());
                qualityCheckDetails.get(i).setSupplierName(inboundPlanDetails.get(i).getSupplierName());
                qualityCheckDetails.get(i).setSupplierId(inboundPlanDetails.get(i).getSupplierId());
                qualityCheckDetails.get(i).setInboundTraceCode(inboundPlanDetails.get(i).getInboundTraceCode());
                InboundPlanDetail tempIPD = inboundPlanDetailDAO.selectByInboundTraceCode(inboundPlanDetails.get(i).getInboundTraceCode()).get(0);
                InboundPlan tempIP = inboundPlanDAO.selectByPrimaryKey(tempIPD.getPlanId());
                qualityCheckDetails.get(i).setPlanId(tempIP.getId());
                qualityCheckDetails.get(i).setPlanCode(tempIP.getPlanId());
                qualityCheckDetailDAO.insertSelective(qualityCheckDetails.get(i));
            }
        }
        //如果质检类型为抽检
        else if(qualityCheck.getCheckType()==2){
            //生成质检代码
            //生成质检表头
            qualityCheck.setCheckState(1);
            qualityCheck.setCreateTime(new Date());
            qualityCheck.setSourceType(1);
            num = qualityCheckDAO.insertSelective(qualityCheck);
            Integer id = qualityCheck.getId();
            //获取不重复的随机数
            int n= inboundPlanDetails.size();
            int[] range=new int[n];
            for(int i=0;i<n;i++){
                range[i]=i;
            }
            Random r = new Random();
            for(int i = 0; i < n; i++)
            {
                int in = r.nextInt(n - i) + i;
                int t = range[in];
                range[in] = range [i];
                range[i] = t;
            }
            //根据随机结果生成表单
            for(int i = 0;i<qualityCheck.getRandomNum();i++){
                qualityCheckDetails.get(i).setCheckId(id);
                qualityCheckDetails.get(i).setCheckCode(qualityCheck.getCheckCode());
                qualityCheckDetails.get(i).setCommodityId(inboundPlanDetails.get(range[i]).getCommodityId());
                qualityCheckDetails.get(i).setCommodityCode(inboundPlanDetails.get(range[i]).getCommodityCode());
                qualityCheckDetails.get(i).setCommodityName(inboundPlanDetails.get(range[i]).getCommodityName());
                qualityCheckDetails.get(i).setSkuId(inboundPlanDetails.get(range[i]).getSkuId());
                qualityCheckDetails.get(i).setSkuCode(inboundPlanDetails.get(range[i]).getSkuCode());
                qualityCheckDetails.get(i).setSkuName(inboundPlanDetails.get(range[i]).getSkuName());
                qualityCheckDetails.get(i).setPredictCheckNum(inboundPlanDetails.get(i).getNum());
                qualityCheckDetails.get(i).setPredictNum(inboundPlanDetails.get(i).getNum());
                qualityCheckDetails.get(i).setContainerId(inboundPlanDetails.get(range[i]).getContainerId());
                qualityCheckDetails.get(i).setContainerCode(inboundPlanDetails.get(range[i]).getContainerCode());
                qualityCheckDetails.get(i).setContainerName(inboundPlanDetails.get(range[i]).getContainerName());
                qualityCheckDetails.get(i).setContainerBarcode(inboundPlanDetails.get(range[i]).getContainerBarcode());
                qualityCheckDetails.get(i).setUnit(inboundPlanDetails.get(range[i]).getUnit());
                qualityCheckDetails.get(i).setProductBatch(inboundPlanDetails.get(range[i]).getProductBatch());
                qualityCheckDetails.get(i).setProductFactory(inboundPlanDetails.get(range[i]).getProductCompany());
                qualityCheckDetails.get(i).setProductTime(inboundPlanDetails.get(range[i]).getProductTime());
                qualityCheckDetails.get(i).setCustomCode(inboundPlanDetails.get(i).getCustomCode());
                qualityCheckDetails.get(i).setCustomId(inboundPlanDetails.get(i).getCustomId());
                qualityCheckDetails.get(i).setCustomName(inboundPlanDetails.get(i).getCustomName());
                qualityCheckDetails.get(i).setSupplierCode(inboundPlanDetails.get(i).getSupplierCode());
                qualityCheckDetails.get(i).setSupplierName(inboundPlanDetails.get(i).getSupplierName());
                qualityCheckDetails.get(i).setSupplierId(inboundPlanDetails.get(i).getSupplierId());
                qualityCheckDetails.get(i).setInboundTraceCode(inboundPlanDetails.get(i).getInboundTraceCode());
                qualityCheckDetails.get(i).setPredictCheckNum(qualityCheckDetails.get(i).getRandomCheckNum());
                qualityCheckDetails.get(i).setPredictNum(inboundPlanDetails.get(i).getNum());
                InboundPlanDetail tempIPD = inboundPlanDetailDAO.selectByInboundTraceCode(inboundPlanDetails.get(i).getInboundTraceCode()).get(0);
                InboundPlan tempIP = inboundPlanDAO.selectByPrimaryKey(tempIPD.getPlanId());
                qualityCheckDetails.get(i).setPlanId(tempIP.getId());
                qualityCheckDetails.get(i).setPlanCode(tempIP.getPlanId());
                qualityCheckDetailDAO.insertSelective(qualityCheckDetails.get(i));
            }
        }
        return num;
    }

    /**
     * 根据入库计划单生成装箱单
     * @param generateIPA
     * @return
     */
    @Override
    public Integer generateIPA(GenerateIPA generateIPA) {
        //初始化
        InboundPlan inboundPlan=generateIPA.getInboundPlanDetailHeadAndDetail().getHead();
        List<InboundPlanDetail> inboundPlanDetails=generateIPA.getInboundPlanDetailHeadAndDetail().getDetails();
        PackingTable packingTable=generateIPA.getPackingDetailHeadAndDetail().getHead();
        List<PackingDetail> packingDetails = generateIPA.getPackingDetailHeadAndDetail().getDetails();
        //入库装箱单表头生成 需要填写装箱类型 是否混码装箱 装箱人信息 备注
        packingTable.setPackingState(1);
        packingTable.setPackingTime(new Date());
        packingTable.setCreateTime(new Date());
        packingTable.setSourceType(1);
        packingTableDAO.insertSelective(packingTable);
        //设置入库计划单状态为装箱中
        Integer packingId=packingTable.getId();
        HashSet<String> judgeSet = new HashSet<>();
        //入库装箱单单表生成 需要填写包装信息 sku数量 备注
        for(int i = 0 ; i < packingDetails.size();i++){
            PackingDetail packingDetail = packingDetails.get(i);
            InboundPlanDetail inboundPlanDetail = inboundPlanDetails.stream().filter(e -> e.getInboundTraceCode().equals(packingDetail.getInboundTraceCode())).collect(Collectors.toList()).get(0);
            if(judgeSet.contains(packingDetail.getInboundTraceCode())){
               inboundPlanDetail.setId(inboundPlanDetail.getId());
               inboundPlanDetail.setSkuSplitContainerNum(inboundPlanDetail.getSkuSplitContainerNum()+1);
            }
            judgeSet.add(packingDetail.getInboundTraceCode());
            InboundPlan tempIp = inboundPlanDAO.selectByPrimaryKey(inboundPlanDetail.getPlanId());
            packingDetail.setPlanId(tempIp.getId());
            packingDetail.setPlanCode(tempIp.getPlanId());
            packingDetail.setPackingId(packingId);
            packingDetail.setSkuId(inboundPlanDetail.getSkuId());
            packingDetail.setSkuCode(inboundPlanDetail.getSkuCode());
            packingDetail.setSkuName(inboundPlanDetail.getSkuName());
            packingDetail.setPackingCode(packingTable.getPackingId());
            packingDetail.setProductBatch(inboundPlanDetail.getProductBatch());
            packingDetail.setProductTime(inboundPlanDetail.getProductTime());
            packingDetail.setProductCompany(inboundPlanDetail.getProductCompany());
            packingDetail.setIsPacked(false);
            packingDetail.setCustomId(inboundPlanDetail.getCustomId());
            packingDetail.setCustomCode(inboundPlanDetail.getCustomCode());
            packingDetail.setCustomName(inboundPlanDetail.getCustomName());
            packingDetail.setSupplierId(inboundPlanDetail.getSupplierId());
            packingDetail.setSupplierCode(inboundPlanDetail.getSupplierCode());
            packingDetail.setSupplierName(inboundPlanDetail.getSupplierName());
            packingDetails.get(i).setTraceCode(codeService.code("INP"));
            packingDetailDAO.insertSelective(packingDetails.get(i));
        }
        InboundPlanDetail tempIPD = new InboundPlanDetail();
        for(int i = 0 ;i<inboundPlanDetails.size();i++){
            tempIPD.setId(inboundPlanDetails.get(i).getId());
            tempIPD.setSkuSplitContainerNum(inboundPlanDetails.get(i).getSkuSplitContainerNum());
            inboundPlanDetailDAO.updateByPrimaryKeySelective(tempIPD);
        }
        return 1;
    }

    /**
     * 根据id获取质检单
     * @param id
     * @return
     */
    @Override
    public InboundPlan selectByPrimaryId(Integer id) {
        return inboundPlanDAO.selectByPrimaryKey(id);
    }

    /**
     * 根据入库计划单id获取表单
     * @param id
     * @return
     */
    @Override
    public List<InboundPlanDetail> selectByPlanId(Integer id) {
        return inboundPlanDetailDAO.selectByPlanId(id);
    }

    /**
     * 插入入库计划单（未使用）
     * @param inboundPlan
     * @param inboundPlanDetails
     * @return
     */
    @Override
    public String insertIPL(InboundPlan inboundPlan ,List<InboundPlanDetail> inboundPlanDetails) {
        //生成入库计划单号
        String planId = codeService.code("IPL");
        inboundPlan.setPlanId(planId);
        //插入表头
        inboundPlanDAO.insertSelective(inboundPlan);
        Integer id =inboundPlan.getId();
        //插入表单
        for(InboundPlanDetail inboundPlanDetail:inboundPlanDetails){
            inboundPlanDetail.setPlanId(id);
            inboundPlanDetailDAO.insertSelective(inboundPlanDetail);
        }
        return planId;
    }

    /**
     * 根据入库计划单号获取入库计划单（用于rf)
     * @param inboundPlan
     * @return
     */
    @Override
    public HeadAndDetail<InboundPlan, InboundPlanDetail> getInboundPlan(InboundPlan inboundPlan) {
        HeadAndDetail<InboundPlan,InboundPlanDetail> headAndDetail= new HeadAndDetail<>();
        headAndDetail.setHead(inboundPlanDAO.selectByPlanId(inboundPlan.getPlanId()));
        headAndDetail.setDetails(inboundPlanDetailDAO.selectByPlanId(headAndDetail.getHead().getId()));
        return headAndDetail;
    }

    /**
     *
     * 按容器收货
     * @param
     * @return
     */
    @Override
    public Integer receiveDetailContainer(InboundPlanDetail inboundPlanDetail) {
        //设置表单已收货
        InboundPlanDetail temp = inboundPlanDetailDAO.selectByContainerBarcode(inboundPlanDetail.getContainerBarcode()).get(0);
        temp.setIsReceived((byte)1);
        temp.setReceiveTime(new Date());
        Integer num = inboundPlanDetailDAO.updateByPrimaryKey(inboundPlanDetail);
        InboundPlan inboundPlan = new InboundPlan();
        inboundPlan.setId(inboundPlanDAO.selectByPrimaryKey(temp.getPlanId()).getId());
        inboundPlan.setReceivedContainerNum(1);
        inboundPlan.setReceivedNum(temp.getNum());
        inboundPlan.setReceivedSkuNum(1.0);
        inboundPlanDAO.updateNum(inboundPlan);
        return num;
    }

    /**
     *
     * 按sku收货
     * @param
     * @return
     */
    @Override
    public Integer receiveDetailSKU(DetailAndCode<InboundPlanDetail> detailAndCode) {
        detailAndCode.getDetail().setPlanId(inboundPlanDAO.selectByPlanId(detailAndCode.getCode()).getId());
        InboundPlan inboundPlan = new InboundPlan();
        inboundPlan.setId(inboundPlanDAO.selectByPrimaryKey(detailAndCode.getDetail().getPlanId()).getId());
        inboundPlan.setReceivedNum(detailAndCode.getDetail().getReceivedNum());
        inboundPlan.setReceivedSkuNum(1.0);
        inboundPlanDAO.updateNum(inboundPlan);
        detailAndCode.getDetail().setIsReceived((byte)1);
        detailAndCode.getDetail().setReceiveTime(new Date());
        return inboundPlanDetailDAO.updateByPrimaryKey(detailAndCode.getDetail());
    }

    /**
     * 整表收货
     * @param inboundPlan
     * @return
     */

    @Override
    public Integer receiveTable(InboundPlan inboundPlan) {
        //根据入库码找到入库计划单
        InboundPlan inboundPlan1=inboundPlanDAO.selectByPlanId(inboundPlan.getPlanId());
        Integer id = inboundPlan1.getId();
        //设置表头已收货
        inboundPlanDAO.updateIsReceived(id);
        inboundPlan.setCreateTime(new Date());
        inboundPlan.setId(id);
        //设置表单已收货
        inboundPlanDetailDAO.updateIsReceived(inboundPlan);
        return 1;
    }

    /**
     * 设置收货
     * @param inboundPlan
     * @return
     */
    @Override
    public Integer receiveSet(InboundPlan inboundPlan) {
        //获得入库计划单
        InboundPlan inboundPlan1 = inboundPlanDAO.selectByPlanId(inboundPlan.getPlanId());
        Integer id = inboundPlan1.getId();
        return inboundPlanDAO.updateIsReceived(id);
    }

    /**
     * 设置质检前检查表单是否全部已收货
     * @param inboundPlan
     * @return
     */
    @Override
    public Boolean receivedCheck(InboundPlan inboundPlan) {
        Integer id = inboundPlanDAO.selectByPlanId(inboundPlan.getPlanId()).getId();
        //如果找到已收货的返回false 如果没找到返回true
        if(inboundPlanDetailDAO.checkReceived(id).size()==0)
            return true;
        else return false;
    }

    /**
     * 取消收货
     * @param headAndDetail
     * @return
     */
    @Override
    public Integer cancelReceive(HeadAndDetail<InboundPlan, InboundPlanDetail> headAndDetail) {
        //表头取消收货
        headAndDetail.getHead().setInboundState(6);
        headAndDetail.getHead().setIsReceived(false);
        //表单取消收货
        for(InboundPlanDetail inboundPlanDetail: headAndDetail.getDetails()){
            inboundPlanDetail.setReceiveTime(null);
            inboundPlanDetail.setReceivedNum(0.0);
            inboundPlanDetail.setIsReceived((byte)0);
            inboundPlanDetailDAO.updateByPrimaryKeySelective(inboundPlanDetail);
        }
        inboundPlanDAO.updateByPrimaryKeySelective(headAndDetail.getHead());
        return 1;
    }

    /**
     * 生成入库计划单入库批次
     * @param inboundPlans
     * @return
     */
    @Override
    public String  addInboundBatch(List<InboundPlan> inboundPlans) {
        String code = codeService.code("INB");
        for(int i = 0 ;i < inboundPlans.size();i++){
            inboundPlans.get(i).setInboundBatch(code);
            inboundPlanDAO.updateByPrimaryKeySelective(inboundPlans.get(i));
        }
        return code;
    }

    /**
     * 根据入库计划单生成组盘单
     * @param generateCOBWithIPL
     * @return
     */
    @Override
    public Integer generateCOB(GenerateCOBWithIPL generateCOBWithIPL) {
        //初始化
        InboundPlan inboundPlan = generateCOBWithIPL.getInboundPlanDetailHeadAndDetail().getHead();
        List<InboundPlanDetail> inboundPlanDetails = generateCOBWithIPL.getInboundPlanDetailHeadAndDetail().getDetails();
        CombineStack combineStack = generateCOBWithIPL.getCombineHeadAndPlateDetailAndPackageDetail().getCombineStack();

        List<CombineStackDetail> combineStackDetails = generateCOBWithIPL.getCombineHeadAndPlateDetailAndPackageDetail().getCombineStackDetails();
        List<CombineStackPackageDetail> combineStackPackageDetails = generateCOBWithIPL.getCombineHeadAndPlateDetailAndPackageDetail().getCombineStackPackageDetails();
        //组盘单头需要填写组盘人信息
        combineStack.setCombineCode(codeService.code("COB"));
        combineStack.setCombineState(1);
        combineStack.setCreateTime(new Date());
        combineStackDAO.insertSelective(combineStack);
        Integer combineId=combineStack.getId();
        //组盘码盘表需要填写码盘包装信息、是否混sku码盘、码箱数量前端统计、行号前端填写
        for(CombineStackDetail combineStackDetail:combineStackDetails){
            combineStackDetail.setStackState(0);
            combineStackDetail.setStackId(combineId);
            combineStackDetail.setStackCode(combineStack.getCombineCode());
            combineStackDetail.setCombineTraceCode(codeService.code("INC"));
            combineStackDetailDAO.insertSelective(combineStackDetail);
        }
        //组盘码盘箱表需要填写行号
        for(int i = 0;i < combineStackPackageDetails.size();i++){
            CombineStackPackageDetail combineStackPackageDetail= combineStackPackageDetails.get(i);
            InboundPlanDetail inboundPlanDetail = inboundPlanDetails.get(i);

            CombineStackDetail combineStackDetail = combineStackDetails.stream().filter(CombineStackDetail -> CombineStackDetail.getRowNum().equals(combineStackPackageDetail.getRowNum())).collect(Collectors.toList()).get(0);
            combineStackPackageDetail.setDetailId(combineStackDetail.getId());
            combineStackPackageDetail.setContainerId(inboundPlanDetail.getContainerId());
            combineStackPackageDetail.setContainerCode(inboundPlanDetail.getContainerCode());
            combineStackPackageDetail.setContainerBarcode(inboundPlanDetail.getContainerBarcode());
            combineStackPackageDetail.setNum(inboundPlanDetail.getReceivedNum());
            combineStackPackageDetail.setPlanId(inboundPlanDetail.getPlanId());
            combineStackPackageDetail.setPlanCode(inboundPlanDAO.selectByPrimaryKey(inboundPlanDetail.getPlanId()).getPlanId());
            combineStackPackageDetail.setInboundTraceCode(inboundPlanDetail.getInboundTraceCode());
            combineStackPackageDetail.setStackBarcode(combineStackDetail.getPackageBarcode());
            CombineStackDetail tempCSPD = new CombineStackDetail();
            tempCSPD.setId(combineStackDetail.getId());
            tempCSPD.setPlanId(inboundPlanDetail.getPlanId());
            tempCSPD.setPlanCode(combineStackPackageDetail.getPlanCode());
            combineStackDetailDAO.updateByPrimaryKeySelective(tempCSPD);
            combineStackPackageDetailDAO.insertSelective(combineStackPackageDetail);
        }
        return 1;
    }

    @Override
    public HeadAndDetail<OnshelfAdvice, OnshelfAdviceDetail> generateONP(GenerateONPWithIPL generateONPWithIPL) {
        //初始化
        OnshelfAdvice onshelfAdvice = generateONPWithIPL.getOnshelfAdviceDetailHeadAndDetail().getHead();
        List<OnshelfAdviceDetail> onshelfAdviceDetails = generateONPWithIPL.getOnshelfAdviceDetailHeadAndDetail().getDetails();
        InboundPlan inboundPlan = generateONPWithIPL.getInboundPlanDetailHeadAndDetail().getHead();
        List<InboundPlanDetail> inboundPlanDetails = generateONPWithIPL.getInboundPlanDetailHeadAndDetail().getDetails();
        //表单赋值
        onshelfAdvice.setOnshelfCode(codeService.code("ONP"));
        onshelfAdvice.setOnshelfState(1);
        onshelfAdvice.setIsUsable((byte)1);
        onshelfAdvice.setCreateTime(new Date());
        //插入表头
        onshelfAdviceDAO.insertSelective(onshelfAdvice);
        Integer onshelfAdviceId = onshelfAdvice.getId();
        //获取策略信息
        OnshelfStrategy onshelfStrategy = onshelfStrategyDAO.selectByPrimaryKey(onshelfAdvice.getOnshelfStrategyId());

        List<OnshelfStrategyDetail> onshelfStrategyDetails = onshelfStrategyDetailDAO.selectByOnshelfStrategyId(onshelfAdvice.getOnshelfStrategyId());

        List<WarehouseLocation> warehouseLocations = new LinkedList<>();
        Boolean sameOrderFlag = false;
        if(onshelfStrategyDetails.get(0).getRuleDetailCode().equals("OnshelfStrategySameOrder")){
            OnshelfStrategySameOrder onshelfStrategySameOrder = new OnshelfStrategySameOrder();
            sameOrderFlag = true;
            onshelfStrategySameOrder.preSelectLocations(inboundPlan,inboundPlanDetails,onshelfAdvice,onshelfAdviceDetails);
        }
        //按优先级执行
        //一批货整放为整表策略
        onshelfStrategyDetails.sort(Comparator.comparing(OnshelfStrategyDetail::getPriority));
        for(int i = 0; i < inboundPlanDetails.size();i++){
            OnshelfAdviceDetail onshelfAdviceDetail = onshelfAdviceDetails.get(i);
            InboundPlanDetail inboundPlanDetail = inboundPlanDetails.get(i);
            onshelfAdviceDetail.setOnshelfId(onshelfAdviceId);
            onshelfAdviceDetail.setTraceCode(inboundPlanDetail.getInboundTraceCode());
            onshelfAdviceDetail.setOnshelfCode(onshelfAdvice.getOnshelfCode());
            onshelfAdviceDetail.setInboundBatch(inboundPlan.getInboundBatch());
            onshelfAdviceDetail.setOnshelfType(1);
            onshelfAdviceDetail.setContainerId(inboundPlanDetail.getContainerId());
            onshelfAdviceDetail.setContainerCode(inboundPlanDetail.getContainerCode());
            onshelfAdviceDetail.setContainerBarcode(inboundPlanDetail.getContainerBarcode());
            onshelfAdviceDetail.setCustomId(inboundPlanDetail.getCustomId());
            onshelfAdviceDetail.setCustomCode(inboundPlanDetail.getCustomCode());
            onshelfAdviceDetail.setCommodityId(inboundPlanDetail.getCommodityId());
            onshelfAdviceDetail.setCommodityCode(inboundPlanDetail.getCommodityCode());
            onshelfAdviceDetail.setSkuId(inboundPlanDetail.getSkuId());
            onshelfAdviceDetail.setSkuCode(inboundPlanDetail.getSkuCode());
            onshelfAdviceDetail.setSkuName(inboundPlanDetail.getSkuName());
            onshelfAdviceDetail.setUnit(inboundPlanDetail.getUnit());
            onshelfAdviceDetail.setSkuNum(inboundPlanDetail.getReceivedNum());
            onshelfAdviceDetail.setTotalVolumn(inboundPlanDetail.getTotalVolumn());
            onshelfAdviceDetail.setTotalWeight(inboundPlanDetail.getTotalWeight());
            onshelfAdviceDetail.setProductCompany(inboundPlanDetail.getProductCompany());
            onshelfAdviceDetail.setProductTime(inboundPlanDetail.getProductTime());
            onshelfAdviceDetail.setProductBatch(inboundPlanDetail.getProductBatch());

            if(!sameOrderFlag){
                for(int j = 0 ; j< onshelfStrategyDetails.size(); j++){
                    try {
                        //按策略名找到对应的策略类
                        Class c = Class.forName("com.bupt.strategy.OnshelfStrategy."+onshelfStrategyDetails.get(j).getRuleDetailCode());
                        //新建实例
                        OnshelfStrategyCon onshelfStrategyCon = new OnshelfStrategyCon((OnshelfStrategyInt) c.newInstance());
                        //按该策略筛选
                        WarehouseLocation location = onshelfStrategyCon.selectLocation(onshelfAdvice,onshelfAdviceDetail,onshelfStrategyDetails.get(j),warehouseLocations);
                        //如果找到了就直接退出
                        if(location!=null){
                            onshelfAdviceDetail.setAdviceLocationId(location.getId());
                            onshelfAdviceDetail.setAdviceLocationName(location.getLocationCode());
                            break;
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                //todo --默认回原库位，宝开测试专用
                InventoryBalance inv = inventoryBalanceDAO.selectByInventoryCodeNoType(onshelfAdviceDetail.getContainerBarcode());
                if (inv!=null){
                    System.out.println(inv);
                    onshelfAdviceDetail.setAdviceLocationId(inv.getLocationId());
                    onshelfAdviceDetail.setAdviceLocationName(inv.getLocationName());
                }

                //执行完所有的策略后如果还是没有找到则执行默认策略
                if(onshelfAdviceDetail.getAdviceLocationId()==null){
                    OnshelfStrategyInt onshelfStrategyInt = new OnshelfStrategyDefault();
                    OnshelfStrategyCon onshelfStrategyCon = new OnshelfStrategyCon(onshelfStrategyInt);
                    WarehouseLocation location = onshelfStrategyCon.selectLocation(onshelfAdvice,onshelfAdviceDetail,null,null);
                    onshelfAdviceDetail.setAdviceLocationId(location.getId());
                    onshelfAdviceDetail.setAdviceLocationName(location.getLocationName());
                }
            }
            onshelfAdviceDetailDAO.insertSelective(onshelfAdviceDetail);
        }
        return generateONPWithIPL.getOnshelfAdviceDetailHeadAndDetail();
    }

    @Override
    public Integer addInboundPlanDetail(DetailAndCode<InboundPlanDetail> detailAndCode) {
        detailAndCode.getDetail().setPlanId(inboundPlanDAO.selectByPlanId(detailAndCode.getCode()).getId());
        detailAndCode.getDetail().setIsReceived((byte)1);
        detailAndCode.getDetail().setReceiveTime(new Date());
        detailAndCode.getDetail().setInboundTraceCode(codeService.code("INS"));
        inboundPlanDetailDAO.insertSelective(detailAndCode.getDetail());
        return 1;
    }

    @Override
    public Integer setInboundPlanReceiving(InboundPlan inboundPlan) {
        inboundPlan = inboundPlanDAO.selectByPlanId(inboundPlan.getPlanId());
        inboundPlan.setInboundState(5);
        return inboundPlanDAO.updateByPrimaryKey(inboundPlan);
    }

    public Boolean checkIsCheck(InboundPlan inboundPlan){
        List<InboundPlanDetail> inboundPlanDetails = inboundPlanDetailDAO.selectByPlanId(inboundPlan.getId());
        for(int i =0;i<inboundPlanDetails.size();i++){
            if(inboundPlanDetails.get(i).getIsChecked()==0) return false;
        }
        return true;
    }
    public Boolean checkIsPackage(InboundPlan inboundPlan){
        List<InboundPlanDetail> inboundPlanDetails = inboundPlanDetailDAO.selectByPlanId(inboundPlan.getId());
        for(int i =0;i<inboundPlanDetails.size();i++){
            if(inboundPlanDetails.get(i).getIsPackaged()==false) return false;
        }
        return true;
    }
    public Boolean checkIsStack(InboundPlan inboundPlan){
        List<InboundPlanDetail> inboundPlanDetails = inboundPlanDetailDAO.selectByPlanId(inboundPlan.getId());
        for(int i =0;i<inboundPlanDetails.size();i++){
            if(inboundPlanDetails.get(i).getIsPlate()==0) return false;
        }
        return true;
    }
    public Boolean checkIsOnshelf(InboundPlan inboundPlan){
        List<InboundPlanDetail> inboundPlanDetails = inboundPlanDetailDAO.selectByPlanId(inboundPlan.getId());
        for(int i =0;i<inboundPlanDetails.size();i++){
            if(inboundPlanDetails.get(i).getIsOnshelf()==false) return false;
        }
        return true;
    }
}

