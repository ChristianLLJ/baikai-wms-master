package com.bupt.service.inbound.impl;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SumAndList;
import com.bupt.DTO.inbound.CombineHeadAndPlateDetailAndPackageDetail;
import com.bupt.DTO.inbound.DetailAndCode;
import com.bupt.mapper.*;
import com.bupt.pojo.*;
import com.bupt.service.BaseService;
import com.bupt.service.authority.CodeService;
import com.bupt.service.inbound.StackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StackServiceImpl extends BaseService<CombineStack,CombineStackDetail,CombineStackDAO,CombineStackDetailDAO> implements StackService {
    @Autowired
    CombineStackDAO combineStackDAO;
    @Autowired
    CombineStackDetailDAO combineStackDetailDAO;
    @Autowired
    CombineStackPackageDetailDAO combineStackPackageDetailDAO;
    @Autowired
    InboundPlanDAO inboundPlanDAO;
    @Autowired
    InboundPlanDetailDAO inboundPlanDetailDAO;
    @Autowired
    CodeService codeService;
    @Autowired
    InboundPlanServiceImpl inboundPlanService;
    /**
     * 筛选组盘单头
     * @param screenDTO
     * @return
     */
    @Override
    public List<CombineStack> selectCombineStack(ScreenDTO<CombineStack> screenDTO) {
        if(screenDTO.getSearchDTO()!=null)
        screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage()-1)* screenDTO.getSearchDTO().getNum());
        return combineStackDAO.screen(screenDTO);
    }

    /**
     * 筛选组盘单表
     * @param screenDTO
     * @return
     */
    @Override
    public SumAndList<CombineStackDetail> selectCombineStackDetail(ScreenDTO<CombineStackDetail> screenDTO) {
        if(screenDTO.getSearchDTO()!=null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage()-1)* screenDTO.getSearchDTO().getNum());
        List<CombineStackDetail> combineStackDetails = combineStackDetailDAO.screen(screenDTO);
        SumAndList<CombineStackDetail> combineStackDetailSumAndList = new SumAndList<>();
        screenDTO.setSearchDTO(null);
        List<CombineStackDetail> combineStackDetails1 = combineStackDetailDAO.screen(screenDTO);
        double packageNum=0,stackState=0;
        for(int i =0;i<combineStackDetails1.size();i++){
            if(combineStackDetails1.get(i).getPackageNum()!=null)
                packageNum +=combineStackDetails1.get(i).getPackageNum();
            if(combineStackDetails1.get(i).getStackState()!=null&&combineStackDetails1.get(i).getStackState()==1)
                stackState ++;
        }
        HashMap<String,Double> sums = new HashMap<>();
        sums.put("packageNum",packageNum);
        sums.put("stackState",stackState);
        combineStackDetailSumAndList.setList(combineStackDetails);
        combineStackDetailSumAndList.setSums(sums);
        return combineStackDetailSumAndList;
    }

    /**
     * 筛选组盘单头数量
     * @param screenDTO
     * @return
     */
    @Override
    public Integer selectCombineStackDetailNum(ScreenDTO<CombineStackDetail> screenDTO) {
        if(screenDTO.getSearchDTO()!=null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage()-1)* screenDTO.getSearchDTO().getNum());
        return combineStackDetailDAO.numScreen(screenDTO);
    }

    /**
     * 筛选组盘单表数量
     * @param screenDTO
     * @return
     */
    @Override
    public Integer selectCombineStackNum(ScreenDTO<CombineStack> screenDTO) {
        if(screenDTO.getSearchDTO()!=null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage()-1)* screenDTO.getSearchDTO().getNum());
        return combineStackDAO.numScreen(screenDTO);
    }

    /**
     * 筛选组盘单箱表数量
     * @param screenDTO
     * @return
     */
    @Override
    public Integer selectCombineStackPackageDetailNum(ScreenDTO<CombineStackPackageDetail> screenDTO) {
        if(screenDTO.getSearchDTO()!=null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage()-1)* screenDTO.getSearchDTO().getNum());
        return combineStackPackageDetailDAO.numScreen(screenDTO);
    }

    /**
     * 筛选组盘单箱表
     * @param screenDTO
     * @return
     */
    @Override
    public SumAndList<CombineStackPackageDetail> selectCombineStackPackageDetail(ScreenDTO<CombineStackPackageDetail> screenDTO) {
        if(screenDTO.getSearchDTO()!=null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage()-1)* screenDTO.getSearchDTO().getNum());
        List<CombineStackPackageDetail> combineStackPackageDetails = combineStackPackageDetailDAO.screen(screenDTO);
        SumAndList<CombineStackPackageDetail> combineStackPackageDetailSumAndList = new SumAndList<>();
        screenDTO.setSearchDTO(null);
        List<CombineStackPackageDetail> combineStackPackageDetails1 = combineStackPackageDetailDAO.screen(screenDTO);
        double skuNum=0,num=0,isStacked=0;
        for(int i =0;i<combineStackPackageDetails1.size();i++){
            if(combineStackPackageDetails1.get(i).getSkuNum()!=null)
                skuNum +=combineStackPackageDetails1.get(i).getSkuNum();
            if(combineStackPackageDetails1.get(i).getIsStacked()!=null&&combineStackPackageDetails1.get(i).getIsStacked()==true)
                isStacked ++;
            if(combineStackPackageDetails1.get(i).getNum()!=null)
                num +=combineStackPackageDetails1.get(i).getNum();
        }
        HashMap<String,Double> sums = new HashMap<>();
        sums.put("skuNum",skuNum);
        sums.put("isStacked",isStacked);
        sums.put("num",num);
        combineStackPackageDetailSumAndList.setList(combineStackPackageDetails);
        combineStackPackageDetailSumAndList.setSums(sums);
        return combineStackPackageDetailSumAndList;
    }

    /**
     * 取消组盘单
     * @param headAndDetail
     * @return
     */
    @Override
    public Integer cancel(HeadAndDetail<CombineStack, CombineStackDetail> headAndDetail) {
        headAndDetail.getHead().setCombineState(5);
        return combineStackDAO.updateByPrimaryKey(headAndDetail.getHead());
    }

    /**
     * 关闭（完成）组盘单
     * @param combineStack
     * @return
     */
    @Override
    public Integer close(CombineStack combineStack) {
        combineStack.setCombineState(4);
        ScreenDTO<CombineStackDetail> screenDTO = new ScreenDTO<>();
        screenDTO.setPojo(new CombineStackDetail());
        screenDTO.getPojo().setStackId(combineStack.getId());
        List<CombineStackDetail> combineStackDetails = combineStackDetailDAO.screen(screenDTO);
        HashSet<Integer> ids = new HashSet<>();
        for(CombineStackDetail combineStackDetail:combineStackDetails){
            ScreenDTO<CombineStackPackageDetail> screenDTO1 = new ScreenDTO<>();
            screenDTO1.setPojo(new CombineStackPackageDetail());
            screenDTO1.getPojo().setDetailId(combineStackDetail.getId());
            List<CombineStackPackageDetail> combineStackPackageDetails=combineStackPackageDetailDAO.screen(screenDTO1);
            for(CombineStackPackageDetail combineStackPackageDetail:combineStackPackageDetails){
                InboundPlanDetail inboundPlanDetail = inboundPlanDetailDAO.selectByInboundTraceCode(combineStackPackageDetail.getInboundTraceCode()).get(0);
                inboundPlanDetail.setStackId(combineStackDetail.getPackageId());
                inboundPlanDetail.setStackCode(combineStackDetail.getPackageCode());
                inboundPlanDetail.setStackBarcode(combineStackDetail.getPackageBarcode());
                inboundPlanDetail.setIsPlate((byte) 1);
                ids.add(combineStackPackageDetail.getPlanId());
                inboundPlanDetailDAO.updateByPrimaryKey(inboundPlanDetail);
            }
        }
        for(Integer id:ids){
            if(inboundPlanService.checkIsStack(inboundPlanDAO.selectByPrimaryKey(id))){
                InboundPlan tempIp = new InboundPlan();
                tempIp.setId(id);
                tempIp.setInboundState(11);
                inboundPlanDAO.updateState(tempIp);
            }
            else {
                InboundPlan tempIp = new InboundPlan();
                tempIp.setId(id);
                tempIp.setInboundState(10);
                inboundPlanDAO.updateState(tempIp);
            }
        }
        return combineStackDAO.updateByPrimaryKeySelective(combineStack);
    }

    @Override
    public String add(CombineHeadAndPlateDetailAndPackageDetail headAndDetail) {
        //设置表头正在填写
        headAndDetail.getCombineStack().setCombineState(1);
        Integer num=1;
        //如果为第一次
        if(headAndDetail.getCombineStack().getId()==null){
            //生成采购单代码
            headAndDetail.getCombineStack().setCombineCode(codeService.code("COB"));
            headAndDetail.getCombineStack().setCreateTime(new Date());
            //插入表头
            combineStackDAO.insertSelective(headAndDetail.getCombineStack());
            Integer id=headAndDetail.getCombineStack().getId();
            //插入表单
            if(headAndDetail.getCombineStackDetails()!=null) {
                for (CombineStackDetail combineStackDetail : headAndDetail.getCombineStackDetails()) {
                    combineStackDetail.setStackId(id);
                    combineStackDetail.setStackCode(headAndDetail.getCombineStack().getCombineCode());
                    combineStackDetailDAO.insertSelective(combineStackDetail);
                }
            }
            if(headAndDetail.getCombineStackPackageDetails()!=null) {
                for (CombineStackPackageDetail combineStackPackageDetail : headAndDetail.getCombineStackPackageDetails()) {
                    combineStackPackageDetail.setDetailId(headAndDetail.getCombineStackDetails().stream().filter(CombineStackDetail -> CombineStackDetail.getRowNum().equals(combineStackPackageDetail.getRowNum())).collect(Collectors.toList()).get(0).getId());
                    combineStackPackageDetailDAO.insertSelective(combineStackPackageDetail);
                }
            }
        }
        //如果非第一次
        else{
            //表头更新
            combineStackDAO.updateByPrimaryKey(headAndDetail.getCombineStack());
            combineStackDetailDAO.deleteByStackId(headAndDetail.getCombineStack().getId());
            for(CombineStackDetail combineStackDetail:headAndDetail.getCombineStackDetails()){
                combineStackDetail.setStackId(headAndDetail.getCombineStack().getId());
                combineStackDetailDAO.insertSelective(combineStackDetail);
                combineStackPackageDetailDAO.deleteByDetailId(combineStackDetail.getId());
            }
            for(CombineStackPackageDetail combineStackPackageDetail: headAndDetail.getCombineStackPackageDetails()){
                combineStackPackageDetail.setDetailId(headAndDetail.getCombineStackDetails().stream().filter(CombineStackDetail -> CombineStackDetail.getRowNum().equals(combineStackPackageDetail.getRowNum())).collect(Collectors.toList()).get(0).getId());
                combineStackPackageDetailDAO.insertSelective(combineStackPackageDetail);
            }
        }
        return headAndDetail.getCombineStack().getCombineCode();
    }

    @Override
    public String save(CombineHeadAndPlateDetailAndPackageDetail headAndDetail) {
        //设置表头正在填写
        headAndDetail.getCombineStack().setCombineState(0);
        Integer num=1;
        //如果为第一次
        if(headAndDetail.getCombineStack().getId()==null){
            //生成采购单代码
            headAndDetail.getCombineStack().setCombineCode(codeService.code("COB"));
            //插入表头
            combineStackDAO.insertSelective(headAndDetail.getCombineStack());
            Integer id=headAndDetail.getCombineStack().getId();
            //插入表单
            for(CombineStackDetail combineStackDetail:headAndDetail.getCombineStackDetails()){
                combineStackDetail.setStackId(id);
                combineStackDetail.setStackCode(headAndDetail.getCombineStack().getCombineCode());
                combineStackDetailDAO.insertSelective(combineStackDetail);
            }
            for(CombineStackPackageDetail combineStackPackageDetail: headAndDetail.getCombineStackPackageDetails()){
                combineStackPackageDetail.setDetailId(headAndDetail.getCombineStackDetails().stream().filter(CombineStackDetail -> CombineStackDetail.getRowNum().equals(combineStackPackageDetail.getRowNum())).collect(Collectors.toList()).get(0).getId());
                combineStackPackageDetailDAO.insertSelective(combineStackPackageDetail);
            }
        }
        //如果非第一次
        else{
            //表头更新
            combineStackDAO.updateByPrimaryKey(headAndDetail.getCombineStack());
            combineStackDetailDAO.deleteByStackId(headAndDetail.getCombineStack().getId());
            //表单先删除后插入
            for(CombineStackDetail combineStackDetail:headAndDetail.getCombineStackDetails()){
                combineStackDetail.setStackId(headAndDetail.getCombineStack().getId());
                combineStackDetailDAO.insertSelective(combineStackDetail);
                combineStackPackageDetailDAO.deleteByDetailId(combineStackDetail.getId());
            }
            for(CombineStackPackageDetail combineStackPackageDetail: headAndDetail.getCombineStackPackageDetails()){
                combineStackPackageDetail.setDetailId(headAndDetail.getCombineStackDetails().stream().filter(CombineStackDetail -> CombineStackDetail.getRowNum().equals(combineStackPackageDetail.getRowNum())).collect(Collectors.toList()).get(0).getId());
                combineStackPackageDetailDAO.insertSelective(combineStackPackageDetail);
            }
        }
        return headAndDetail.getCombineStack().getCombineCode();
    }

    @Override
    public Integer stackedSet(CombineStackDetail combineStackDetail) {
        combineStackDetail.setStackState(1);
        combineStackDetail.setId(combineStackDetail.getId());
        CombineStack combineStack = combineStackDAO.selectByPrimaryKey(combineStackDetail.getStackId());
        if(combineStack.getSourceType()!=0){
            ScreenDTO<CombineStackPackageDetail> screenDTO = new ScreenDTO<>();
            screenDTO.setPojo(new CombineStackPackageDetail());
            screenDTO.getPojo().setDetailId(combineStackDetail.getId());
            List<CombineStackPackageDetail> combineStackPackageDetails = combineStackPackageDetailDAO.screen(screenDTO);
            InboundPlanDetail tempIPD = new InboundPlanDetail();
            for(int i=0;i<combineStackPackageDetails.size();i++){
                InboundPlanDetail inboundPlanDetail = inboundPlanDetailDAO.selectByInboundTraceCode(combineStackPackageDetails.get(i).getInboundTraceCode()).get(0);
                tempIPD.setId(inboundPlanDetail.getId());
                tempIPD.setStackId(combineStackDetail.getPackageId());
                tempIPD.setStackCode(combineStackDetail.getPackageCode());
                tempIPD.setStackBarcode(combineStackDetail.getPackageBarcode());
                tempIPD.setIsPlate((byte)1);
                inboundPlanDetailDAO.updateByPrimaryKeySelective(tempIPD);
            }
        }
        combineStackDetailDAO.updateByPrimaryKeySelective(combineStackDetail);
        return 1;
    }

    @Override
    public Integer combineStackedSet(CombineStack combineStack) {
        combineStack.setCombineState(4);
        CombineStack tempCombine=combineStackDAO.selectByCombineCode(combineStack.getCombineCode()).get(0);
        combineStack.setId(tempCombine.getId());
        if(combineStack.getSourceType()!=0){
            ScreenDTO<CombineStackDetail> screenDTO = new ScreenDTO<>();
            screenDTO.setPojo(new CombineStackDetail());
            screenDTO.getPojo().setStackId(combineStack.getId());
            List<CombineStackDetail> tempCSD= new LinkedList<>();
            HashSet<Integer> ids = new HashSet<>();
            for(int i =0;i<tempCSD.size();i++){
                ids.add(tempCSD.get(i).getPlanId());
            }
            if(ids.size()!=0){
                InboundPlan inboundPlan = new InboundPlan();
                for(Integer id :ids){
                    inboundPlan.setId(id);
                    if(inboundPlanService.checkIsStack(inboundPlan)){
                        inboundPlan.setInboundState(11);
                    }
                    else inboundPlan.setInboundState(10);
                    inboundPlanDAO.updateState(inboundPlan);
                }
            }
        }
        return combineStackDAO.updateByPrimaryKeySelective(combineStack);
    }

    @Override
    public Boolean stackedCheck(CombineStackDetail combineStackDetail) {
        Integer id = combineStackDetailDAO.selectByPackageBarcode(combineStackDetail.getPackageBarcode()).get(0).getId();
        //如果找到未装箱的返回false 如果没找到返回true
        if(combineStackPackageDetailDAO.checkStacked(id).size()==0)
            return true;
        else return false;
    }

    @Override
    public Integer stackedContainer(CombineStackPackageDetail combineStackPackageDetail) {
        combineStackPackageDetail.setIsStacked(true);
        ScreenDTO<CombineStackPackageDetail> screenDTO = new ScreenDTO<>();
        screenDTO.setPojo(new CombineStackPackageDetail());
        screenDTO.getPojo().setStackBarcode(combineStackPackageDetail.getStackBarcode());
        screenDTO.getPojo().setContainerBarcode(combineStackPackageDetail.getContainerBarcode());
        CombineStackPackageDetail temp = combineStackPackageDetailDAO.screen(screenDTO).get(0);
        CombineStackDetail combineStackDetail = combineStackDetailDAO.selectByPrimaryKey(temp.getDetailId());
        InboundPlanDetail inboundPlanDetail = inboundPlanDetailDAO.selectByInboundTraceCode(temp.getInboundTraceCode()).get(0);
        inboundPlanDetail.setIsPlate((byte)1);
        inboundPlanDetail.setStackId(combineStackDetail.getPackageId());
        inboundPlanDetail.setStackCode(combineStackDetail.getPackageCode());
        inboundPlanDetail.setStackBarcode(combineStackDetail.getPackageBarcode());
        inboundPlanDetailDAO.updateByPrimaryKey(inboundPlanDetail);
        combineStackPackageDetailDAO.updateByStackBarcodeAndContainerBarcode(combineStackPackageDetail);
        return 1;
    }

    @Override
    public Boolean combineStackedCheck(CombineStack combineStack) {
        Integer id = combineStackDAO.selectByCombineCode(combineStack.getCombineCode()).get(0).getId();
        //如果找到未装箱的返回false 如果没找到返回true
        if(combineStackDetailDAO.checkStacked(id).size()==0)
            return true;
        else return false;
    }

    @Override
    public String addStackDetail(DetailAndCode<CombineStackDetail> detailAndCode) {
        detailAndCode.getDetail().setStackId(combineStackDAO.selectByCombineCode(detailAndCode.getCode()).get(0).getId());
        detailAndCode.getDetail().setStackCode(combineStackDAO.selectByCombineCode(detailAndCode.getCode()).get(0).getCombineCode());
        detailAndCode.getDetail().setStackState(0);
        detailAndCode.getDetail().setCombineTraceCode(codeService.code("INS"));
        combineStackDetailDAO.insertSelective(detailAndCode.getDetail());
        return detailAndCode.getDetail().getStackCode();
    }

    @Override
    public Integer addPackageDetail(DetailAndCode<CombineStackPackageDetail> detailAndCode) {
        detailAndCode.getDetail().setDetailId(combineStackDetailDAO.selectByPackageBarcode(detailAndCode.getCode()).get(0).getId());
        detailAndCode.getDetail().setIsStacked(true);
        detailAndCode.getDetail().setTraceCode(codeService.code("INS"));
        combineStackPackageDetailDAO.insertSelective(detailAndCode.getDetail());
        return 1;
    }

    @Override
    public Integer setCombineStackStacking(CombineStack combineStack) {
        combineStack = combineStackDAO.selectByCombineCode(combineStack.getCombineCode()).get(0);
        combineStack.setCombineState(3);
        if(combineStack.getSourceType()!=0){
            ScreenDTO<CombineStackDetail> screenDTO = new ScreenDTO<>();
            screenDTO.setPojo(new CombineStackDetail());
            screenDTO.getPojo().setStackId(combineStack.getId());
            List<CombineStackDetail> tempCSD= new LinkedList<>();
            HashSet<Integer> ids = new HashSet<>();
            for(int i =0;i<tempCSD.size();i++){
                ids.add(tempCSD.get(i).getPlanId());
            }
            if(ids.size()!=0){
                InboundPlan inboundPlan = new InboundPlan();
                for(Integer id :ids){
                    inboundPlan.setId(id);
                    inboundPlan.setInboundState(10);
                    inboundPlanDAO.updateState(inboundPlan);
                }
            }
        }
        return combineStackDAO.updateByPrimaryKey(combineStack);
    }
}
