package com.bupt.service.inbound.impl;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SumAndList;
import com.bupt.DTO.inbound.DetailAndCode;
import com.bupt.DTO.inbound.GenerateCOB;
import com.bupt.mapper.*;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.BaseService;
import com.bupt.service.authority.CodeService;
import com.bupt.service.inbound.InPackageService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InPackageServiceImpl extends BaseService<PackingTable,PackingDetail,PackingTableDAO,PackingDetailDAO> implements InPackageService {
    @Autowired
    PackingTableDAO packingTableDAO;
    @Autowired
    PackingDetailDAO packingDetailDAO;
    @Autowired
    InboundPlanDAO inboundPlanDAO;
    @Autowired
    CodeService codeService;
    @Autowired
    CombineStackDAO combineStackDAO;
    @Autowired
    CombineStackDetailDAO combineStackDetailDAO;
    @Autowired
    CombineStackPackageDetailDAO combineStackPackageDetailDAO;
    @Autowired
    ContainerDAO containerDAO;
    @Autowired
    InboundPlanDetailDAO inboundPlanDetailDAO;
    @Autowired
    InboundPlanServiceImpl inboundPlanService;
    /**
     * 筛选入库装箱单头
     * @param screenDTO
     * @return
     */
    @Override
    public List<PackingTable> selectPackingTable(ScreenDTO<PackingTable> screenDTO) {
        if(screenDTO.getSearchDTO()!=null)
        screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage()-1)* screenDTO.getSearchDTO().getNum());
        return packingTableDAO.screen(screenDTO);
    }

    /**
     * 筛选入库装箱单表
     * @param screenDTO
     * @return
     */
    @Override
    public SumAndList<PackingDetail> selectPackingTableDetail(ScreenDTO<PackingDetail> screenDTO) {
        if(screenDTO.getSearchDTO()!=null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage()-1)* screenDTO.getSearchDTO().getNum());
        List<PackingDetail> packingDetails = packingDetailDAO.screen(screenDTO);
        SumAndList<PackingDetail> packingDetailSumAndList = new SumAndList<>();
        screenDTO.setSearchDTO(null);
        List<PackingDetail> packingDetails1 = packingDetailDAO.screen(screenDTO);
        double commodityNum=0,isPacked=0;
        for(int i =0;i<packingDetails1.size();i++){
            if(packingDetails1.get(i).getCommodityNum()!=null)
                commodityNum +=packingDetails1.get(i).getCommodityNum();
            if(packingDetails1.get(i).getIsPacked()!=null&&packingDetails1.get(i).getIsPacked()==true)
                isPacked ++;
        }
        HashMap<String,Double> sums = new HashMap<>();
        sums.put("commodityNum",commodityNum);
        sums.put("isPacked",isPacked);
        packingDetailSumAndList.setList(packingDetails);
        packingDetailSumAndList.setSums(sums);
        return packingDetailSumAndList;
    }

    /**
     * 筛选入库装箱单头数量
     * @param screenDTO
     * @return
     */
    @Override
    public Integer selectPackingTableDetailNum(ScreenDTO<PackingDetail> screenDTO) {
        return packingDetailDAO.numScreen(screenDTO);
    }

    /**
     * 筛选入库装箱单表数量
     * @param screenDTO
     * @return
     */
    @Override
    public Integer selectPackingTableNum(ScreenDTO<PackingTable> screenDTO) {
        return packingTableDAO.numScreen(screenDTO);
    }

    /**
     * 装箱单取消
     * @param headAndDetail
     * @return
     */
    @Override
    public Integer cancel(HeadAndDetail<PackingTable,PackingDetail> headAndDetail) {
        headAndDetail.getHead().setPackingState(4);
        return packingTableDAO.updateByPrimaryKeySelective(headAndDetail.getHead());
    }

    /**
     * 装箱单完成
     * @param packingTable
     * @return
     */
    @SneakyThrows
    @Override
    public Integer close(PackingTable packingTable) {
        packingTable.setPackingState(4);
        ScreenDTO<PackingDetail> screenDTO = new ScreenDTO<>();
        screenDTO.setPojo(new PackingDetail());
        screenDTO.getPojo().setPackingId(packingTable.getId());
        List<PackingDetail> packingDetails = packingDetailDAO.screen(screenDTO);
        List<InboundPlanDetail> inboundPlanDetails = new LinkedList<>();
        Set<Integer> ids = new HashSet<>();
        for(int i =0;i<packingDetails.size();i++){
            PackingDetail packingDetail = packingDetails.get(i);
            ids.add(packingDetail.getPlanId());
            InboundPlanDetail inboundPlanDetail = inboundPlanDetailDAO.selectByInboundTraceCode(packingDetail.getInboundTraceCode()).get(0);
            if(inboundPlanDetail.getSkuSplitContainerNum()==1){
                inboundPlanDetail.setContainerId(packingDetail.getPackageId());
                inboundPlanDetail.setContainerCode(packingDetail.getPackageCode());
                inboundPlanDetail.setContainerBarcode(packingDetail.getPackageBarcode());
                inboundPlanDetail.setIsPackaged(true);
                inboundPlanDetailDAO.updateByPrimaryKey(inboundPlanDetail);
            }
            else {
                InboundPlanDetail tempInboundPlanDetail = (InboundPlanDetail) inboundPlanDetail.clone();
                tempInboundPlanDetail.setId(null);
                tempInboundPlanDetail.setTotalWeight(tempInboundPlanDetail.getTotalWeight()*packingDetail.getCommodityNum()/inboundPlanDetail.getReceivedNum());
                tempInboundPlanDetail.setTotalVolumn(tempInboundPlanDetail.getTotalVolumn()*packingDetail.getCommodityNum()/inboundPlanDetail.getReceivedNum());
                tempInboundPlanDetail.setTotalNetWeight(tempInboundPlanDetail.getTotalNetWeight()*packingDetail.getCommodityNum()/inboundPlanDetail.getReceivedNum());
                tempInboundPlanDetail.setContainerId(packingDetail.getPackageId());
                tempInboundPlanDetail.setContainerCode(packingDetail.getPackageCode());
                tempInboundPlanDetail.setContainerBarcode(packingDetail.getPackageBarcode());
                tempInboundPlanDetail.setInboundTraceCode(codeService.code("INS"));
                tempInboundPlanDetail.setSkuSplitContainerNum(1);
                tempInboundPlanDetail.setReceivedNum(packingDetail.getCommodityNum().doubleValue());
                tempInboundPlanDetail.setIsPackaged(true);
                inboundPlanDetails.add(tempInboundPlanDetail);
            }
        }
        for(Integer id:ids){
            inboundPlanDetailDAO.deleteBySplitupOne(id);
            InboundPlan inboundPlan = inboundPlanDAO.selectByPrimaryKey(id);
            inboundPlan.setInboundState(9);
            inboundPlanDAO.updateState(inboundPlan);
        }

        for(int i=0;i<inboundPlanDetails.size();i++){
            inboundPlanDetailDAO.insert(inboundPlanDetails.get(i));
        }
        return packingTableDAO.updateByPrimaryKeySelective(packingTable);
    }

    /**
     * 根据入库装箱单生成组盘单
     * @param generateCOB
     * @return
     */
    @Override
    @Transactional
    public Integer generateCOB(GenerateCOB generateCOB) {
        //初始化
        PackingTable packingTable = generateCOB.getPackingDetailHeadAndDetail().getHead();
        List<PackingDetail> packingDetails = generateCOB.getPackingDetailHeadAndDetail().getDetails();
        CombineStack combineStack = generateCOB.getCombineHeadAndPlateDetailAndPackageDetail().getCombineStack();
        List<CombineStackDetail> combineStackDetails = generateCOB.getCombineHeadAndPlateDetailAndPackageDetail().getCombineStackDetails();
        List<CombineStackPackageDetail> combineStackPackageDetails = generateCOB.getCombineHeadAndPlateDetailAndPackageDetail().getCombineStackPackageDetails();
        InboundPlan inboundPlan = new InboundPlan();
        //组盘单头需要填写组盘人信息
        combineStack.setCombineState(1);
        combineStack.setWarehouseId(packingTable.getWarehouseId());
        combineStack.setWarehouseCode(packingTable.getWarehouseCode());
        int num = combineStackDAO.insertSelective(combineStack);
        Integer combineId=combineStack.getId();
        //组盘码盘表需要填写码盘包装信息、是否混sku码盘、码箱数量前端统计、行号前端填写
        for(CombineStackDetail combineStackDetail:combineStackDetails){
            combineStackDetail.setStackState(0);
            combineStackDetail.setStackId(combineId);
            combineStackDetail.setStackCode(combineStack.getCombineCode());
            combineStackDetailDAO.insertSelective(combineStackDetail);
        }
        //组盘码盘箱表需要填写行号
        for(int i = 0;i < combineStackPackageDetails.size();i++){
            CombineStackPackageDetail combineStackPackageDetail= combineStackPackageDetails.get(i);
            PackingDetail packingDetail = packingDetails.get(i);
            Integer detailId = combineStackDetails.stream().filter(CombineStackDetail -> CombineStackDetail.getRowNum()==combineStackPackageDetail.getRowNum()).collect(Collectors.toList()).get(0).getId();
            combineStackPackageDetail.setDetailId(detailId);
            combineStackPackageDetail.setContainerId(packingDetail.getPackageId());
            combineStackPackageDetail.setContainerCode(packingDetail.getPackageCode());
            combineStackPackageDetail.setContainerBarcode(packingDetail.getPackageBarcode());
            Float cNum = packingDetail.getCommodityNum();
            if(cNum!=null) combineStackPackageDetail.setNum(cNum.doubleValue());
            combineStackPackageDetail.setTraceCode(packingDetail.getTraceCode());
            combineStackPackageDetailDAO.insertSelective(combineStackPackageDetail);
        }
        return num;
    }
    @Override
    public List<PackingTable> selectPackingTable(List<PackingTable> packingTables) {
        List<PackingTable> PackingTable1 = new LinkedList<>();
        for(PackingTable PackingTable : packingTables){
            PackingTable1.add(packingTableDAO.selectByPrimaryKey(PackingTable.getId()));
        }
        return PackingTable1;
    }

    @Override
    @Transactional
    public Integer save(HeadAndDetail<PackingTable,PackingDetail> headAndDetail) {
        //设置入库计划单状态为正在填写
        headAndDetail.getHead().setPackingState(0);
        Integer num=0;
        //如果前端没有传进来id,说明为第一次填写
        if(headAndDetail.getHead().getId()==null){
            //设置入库计划单代码
            //插入表头
            packingTableDAO.insertSelective(headAndDetail.getHead());
            Integer id=headAndDetail.getHead().getId();
        }
        //非第一次填写
        else{
            //对表头做更新操作
            packingTableDAO.updateByPrimaryKey(headAndDetail.getHead());
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
    public Integer  add(HeadAndDetail<PackingTable, PackingDetail> headAndDetail) {
        //设置入库计划单状态为已填写
        headAndDetail.getHead().setPackingState(1);
        //如果没有保存过
        if(headAndDetail.getHead().getId()==null){
            //设置入库计划单代码
            //插入表头
            packingTableDAO.insertSelective(headAndDetail.getHead());
            Integer id=headAndDetail.getHead().getId();
            //插入表单
            if(headAndDetail.getDetails()!=null) {
                for (PackingDetail packingDetail : headAndDetail.getDetails()) {
                    packingDetail.setPackingId(id);
                    packingDetailDAO.insertSelective(packingDetail);
                }
            }
        }
        return headAndDetail.getHead().getId();
    }

    @Override
    public Boolean packedCheck(PackingTable packingTable) {
        Integer id = packingTableDAO.selectByPackingCode(packingTable.getPackingId()).get(0).getId();
        //如果找到未装箱的返回false 如果没找到返回true
        if(packingDetailDAO.checkPacked(id).size()==0)
            return true;
        else return false;
    }

    @Override
    public Integer packedSet(PackingTable packingTable) {

        packingTable.setPackingState(3);
        PackingTable tempPacking=packingTableDAO.selectByPackingCode(packingTable.getPackingId()).get(0);
        packingTable.setId(tempPacking.getId());
        ScreenDTO<PackingDetail> screenDTO = new ScreenDTO<>();
        screenDTO.setPojo(new PackingDetail());
        screenDTO.getPojo().setPackingId(tempPacking.getId());
        List<PackingDetail> packingDetails = packingDetailDAO.screen(screenDTO);
        HashSet<Integer> ids = new HashSet<>();
        if(packingTable.getSourceType()!=0){
            for(int i = 0 ;i<packingDetails.size();i++){
                ids.add(packingDetails.get(i).getPlanId());
            }
            for(Integer id:ids){
                InboundPlan inboundPlan = inboundPlanDAO.selectByPrimaryKey(id);
                if(inboundPlanService.checkIsPackage(inboundPlan)==true){
                    inboundPlan.setInboundState(9);
                    inboundPlanDAO.updateState(inboundPlan);
                }
                else {
                    inboundPlan.setInboundState(8);
                    inboundPlanDAO.updateState(inboundPlan);
                }
            }
        }
        return packingTableDAO.updateByPrimaryKeySelective(packingTable);
    }

    @Override
    public Integer packedContainer(PackingDetail packingDetail) {
        packingDetail.setIsPacked(true);
        InboundPlanDetail inboundPlanDetail = inboundPlanDetailDAO.selectByInboundTraceCode(packingDetail.getInboundTraceCode()).get(0);
        inboundPlanDetail.setContainerId(packingDetail.getPackageId());
        inboundPlanDetail.setContainerCode(packingDetail.getPackageCode());
        inboundPlanDetail.setContainerBarcode(packingDetail.getPackageBarcode());
        inboundPlanDetail.setIsPackaged(true);
        inboundPlanDetailDAO.updateByPrimaryKey(inboundPlanDetail);
        return packingDetailDAO.updateIsPackedByPackageCodeAndSkuCode(packingDetail);
    }

    @Override
    public Integer addPackingDetail(DetailAndCode<PackingDetail> detailAndCode) {
        detailAndCode.getDetail().setPackingId(packingTableDAO.selectByPackingCode(detailAndCode.getCode()).get(0).getId());
        detailAndCode.getDetail().setPackingCode(packingTableDAO.selectByPackingCode(detailAndCode.getCode()).get(0).getPackingId());
        detailAndCode.getDetail().setIsPacked(true);
        detailAndCode.getDetail().setTraceCode(codeService.code("INS"));
        packingDetailDAO.insertSelective(detailAndCode.getDetail());
        return 1;
    }

    @Override
    public Integer setPackingTablePacking(PackingTable packingTable) {
        packingTable = packingTableDAO.selectByPackingCode(packingTable.getPackingId()).get(0);
        packingTable.setPackingState(2);
        Set<Integer> ids = new HashSet<>();
        ScreenDTO<PackingDetail> screenDTO = new ScreenDTO<>();
        screenDTO.setPojo(new PackingDetail());
        screenDTO.getPojo().setPackingId(packingTable.getId());
        List<PackingDetail> packingDetails = packingDetailDAO.screen(screenDTO);
        if(packingTable.getSourceType()!=0){
            for(PackingDetail packingDetail:packingDetails){
                ids.add(packingDetail.getPlanId()) ;
            }
            for(Integer id :ids){
                InboundPlan inboundPlan = inboundPlanDAO.selectByPrimaryKey(id);
                if(inboundPlan!=null){
                    inboundPlan.setInboundState(7);
                    inboundPlanDAO.updateState(inboundPlan);
                }
            }
        }
        return packingTableDAO.updateByPrimaryKey(packingTable);
    }

    public HttpResult<?> selectIsSingle(ScreenDTO<InboundPlan> screenDTO){
        ScreenDTO<InboundPlanDetail> detailScreenDTO = new ScreenDTO<>();
        detailScreenDTO.getPojo().setIsSingle(true);
        List<InboundPlanDetail> inboundPlanDetails = inboundPlanDetailDAO.screen(detailScreenDTO);
        List<InboundPlan> inboundPlans = new LinkedList<>();
        HashSet<Integer> ids = new HashSet<>();
        for(int i = 0;i<inboundPlanDetails.size();i++){
            ids.add(inboundPlanDetails.get(i).getPlanId());
        }
        if(ids.size()!=0){
            for(Integer id:ids){
                inboundPlans.add(inboundPlanDAO.selectByPrimaryKey(id));
            }
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,inboundPlans);
    }
}
