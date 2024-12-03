package com.bupt.service.inbound.impl;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SumAndList;
import com.bupt.DTO.inbound.DetailAndCode;
import com.bupt.mapper.*;
import com.bupt.pojo.*;
import com.bupt.service.BaseService;
import com.bupt.service.authority.CodeService;
import com.bupt.service.inbound.QualityCheckService;
import com.bupt.service.util.AuditReasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class QualityCheckServiceImpl extends BaseService<QualityCheck,QualityCheckDetail,QualityCheckDAO,QualityCheckDetailDAO> implements QualityCheckService {
    @Autowired
    QualityCheckDAO qualityCheckDAO;
    @Autowired
    QualityCheckDetailDAO qualityCheckDetailDAO;
    @Autowired
    InboundPlanDAO inboundPlanDAO;
    @Autowired
    CodeService codeService;
    @Autowired
    InboundPlanDetailDAO inboundPlanDetailDAO;
    @Autowired
    StaffDAO staffDAO;
    @Autowired
    AuditReasonService auditReasonService;

    /**
     * 分页筛选质检单表头
     *
     * @param screenDTO
     * @return
     */
    @Override
    public List<QualityCheck> selectQualityCheck(ScreenDTO<QualityCheck> screenDTO) {
        if (screenDTO.getSearchDTO() != null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage() - 1) * screenDTO.getSearchDTO().getNum());
        return qualityCheckDAO.screen(screenDTO);
    }

    @Override
    public List<QualityCheck> selectQualityCheck(List<QualityCheck> qualityChecks) {
        List<QualityCheck> qualityChecks1 = new LinkedList<>();
        for (QualityCheck qualityCheck : qualityChecks) {
            qualityChecks1.add(qualityCheckDAO.selectByPrimaryKey(qualityCheck.getId()));
        }
        return qualityChecks1;
    }

    /**
     * 分页筛选质检单表单
     *
     * @param screenDTO
     * @return
     */
    @Override
    public SumAndList<QualityCheckDetail> selectQualityCheckDetail(ScreenDTO<QualityCheckDetail> screenDTO) {
        if (screenDTO.getSearchDTO() != null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage() - 1) * screenDTO.getSearchDTO().getNum());
        List<QualityCheckDetail> qualityCheckDetails = qualityCheckDetailDAO.screen(screenDTO);
        SumAndList<QualityCheckDetail> qualityCheckDetailSumAndList = new SumAndList<>();
        screenDTO.setSearchDTO(null);
        List<QualityCheckDetail> qualityCheckDetails1 = qualityCheckDetailDAO.screen(screenDTO);
        double predictNum = 0, predictCheckNum = 0, checkedNum = 0, randomCheckNum = 0, passNum = 0, unpassNum = 0, isPass = 0;
        for (int i = 0; i < qualityCheckDetails1.size(); i++) {
            if (qualityCheckDetails1.get(i).getPredictNum() != null)
                predictNum += qualityCheckDetails1.get(i).getPredictNum();
            if (qualityCheckDetails1.get(i).getPredictCheckNum() != null)
                predictCheckNum += qualityCheckDetails1.get(i).getPredictCheckNum();
            if (qualityCheckDetails1.get(i).getCheckedNum() != null)
                checkedNum += qualityCheckDetails1.get(i).getCheckedNum();
            if (qualityCheckDetails1.get(i).getRandomCheckNum() != null)
                randomCheckNum += qualityCheckDetails1.get(i).getRandomCheckNum();
            if (qualityCheckDetails1.get(i).getPassNum() != null)
                passNum += qualityCheckDetails1.get(i).getPassNum();
            if (qualityCheckDetails1.get(i).getUnpassNum() != null)
                unpassNum += qualityCheckDetails1.get(i).getUnpassNum();
            if (qualityCheckDetails1.get(i).getIsPass() != null && qualityCheckDetails1.get(i).getIsPass() == 1)
                isPass++;
        }
        HashMap<String, Double> sums = new HashMap<>();
        sums.put("predictNum", predictNum);
        sums.put("predictCheckNum", predictCheckNum);
        sums.put("checkedNum", checkedNum);
        sums.put("randomCheckNum", randomCheckNum);
        sums.put("passNum", passNum);
        sums.put("unpassNum", unpassNum);
        sums.put("isPass", isPass);
        qualityCheckDetailSumAndList.setList(qualityCheckDetails);
        qualityCheckDetailSumAndList.setSums(sums);
        return qualityCheckDetailSumAndList;
    }

    /**
     * 筛选质检单单头数量
     *
     * @param screenDTO
     * @return
     */
    @Override
    public Integer selectQualityCheckNum(ScreenDTO<QualityCheck> screenDTO) {
        return qualityCheckDAO.numScreen(screenDTO);
    }

    /**
     * 筛选质检单单表数量
     *
     * @param screenDTO
     * @return
     */
    @Override
    public Integer selectQualityCheckDetailNum(ScreenDTO<QualityCheckDetail> screenDTO) {
        return qualityCheckDetailDAO.numScreen(screenDTO);
    }

    /**
     * 根据id获取质检单
     *
     * @param id
     * @return
     */
    @Override
    public QualityCheck selectByPrimaryId(Integer id) {
        return qualityCheckDAO.selectByPrimaryKey(id);
    }

    /**
     * 根据质检单号获取质检单
     *
     * @param id
     * @return
     */
    @Override
    public List<QualityCheckDetail> selectByCheckId(Integer id) {
        return qualityCheckDetailDAO.selectByCheckId(id);
    }

    /**
     * 质检单单表设置已质检
     *
     * @param qualityCheck
     * @return
     */
    @Override
    public Integer checkAddDetail(QualityCheck qualityCheck) {
        //找到质检单明细
        List<QualityCheckDetail> qualityCheckDetails = qualityCheckDetailDAO.selectByCheckId(qualityCheck.getId());
        //如果质检单为已填写状态
        if (qualityCheck.getCheckState() == 3 || qualityCheck.getCheckState() == 2) {
            //设置质检单正在质检状态，对应入库计划单正在质检状态
            qualityCheck.setCheckState(2);
            qualityCheckDAO.updateByPrimaryKeySelective(qualityCheck);
        }
        //单表质检状态更新为已质检
        for (QualityCheckDetail qualityCheckDetail : qualityCheckDetails) {
            InboundPlan inboundPlan = new InboundPlan();
            inboundPlan.setId(qualityCheckDetail.getPlanId());
            inboundPlan.setInboundState(4);
            inboundPlanDAO.updateByPrimaryKeySelective(inboundPlan);
            qualityCheckDetail.setIsPass((byte) 1);
            qualityCheckDetailDAO.updateByPrimaryKeySelective(qualityCheckDetail);
        }
        return 1;
    }

    /**
     * 计算质检合格和不合格数量
     *
     * @param qualityCheck
     * @return
     */
    @Override
    public List<Double> checkSum(QualityCheck qualityCheck) {
        Double pass = 0.0;
        Double unpass = 0.0;
        //计算质检单总合格数量和总不合格数量
        List<QualityCheckDetail> qualityCheckDetails = qualityCheckDetailDAO.selectPassNum(qualityCheck.getId());
        for (QualityCheckDetail qualityCheckDetail : qualityCheckDetails) {
            pass = pass + qualityCheckDetail.getPassNum();
            unpass = unpass + qualityCheckDetail.getUnpassNum();
        }
        //结果返回到表头保存
        List<Double> res = new LinkedList<>();
        qualityCheck.setTotalPassNum(pass);
        qualityCheck.setTotalUnpassNum(unpass);
        qualityCheckDAO.updateByPrimaryKeySelective(qualityCheck);
        //返回给前端
        res.add(pass);
        res.add(unpass);
        return res;
    }

    /**
     * 质检单取消
     *
     * @param headAndDetail
     * @return
     */
    @Override
    public Integer cancel(HeadAndDetail<QualityCheck, QualityCheckDetail> headAndDetail) {
        headAndDetail.getHead().setCheckState(5);
        return qualityCheckDAO.updateByPrimaryKeySelective(headAndDetail.getHead());
    }

    /**
     * 质检单完成（关闭）
     *
     * @param
     * @return
     */
    @Override
    public Integer close(DetailAndCode<QualityCheck> detailAndCode) {
        detailAndCode.getDetail().setCheckState(4);
        List<QualityCheckDetail> qualityCheckDetails = qualityCheckDetailDAO.selectByCheckId(detailAndCode.getDetail().getId());
        for (QualityCheckDetail qualityCheckDetail : qualityCheckDetails) {
            InboundPlan inboundPlan = new InboundPlan();
            inboundPlan.setId(qualityCheckDetail.getPlanId());
            inboundPlan.setInboundState(4);
            inboundPlanDAO.updateByPrimaryKeySelective(inboundPlan);
            List<InboundPlanDetail> inboundPlanDetails = inboundPlanDetailDAO.selectByPlanId(inboundPlan.getId());
            for (int i = 0; i < inboundPlanDetails.size(); i++) {
                inboundPlanDetails.get(i).setIsChecked((byte) 1);
                inboundPlanDetailDAO.updateByPrimaryKey(inboundPlanDetails.get(i));
            }
        }
        return qualityCheckDAO.updateByPrimaryKeySelective(detailAndCode.getDetail());
    }

    public Integer unaudit(DetailAndCode<QualityCheck> detailAndCode) {
        QualityCheck qualityCheck = new QualityCheck();
        qualityCheck.setId(detailAndCode.getDetail().getId());
        qualityCheck.setCheckState(4);
        qualityCheck.setCloseReason("审核不通过");
        qualityCheck.setCheckPersonName(staffDAO.selectByPrimaryKey(detailAndCode.getDetail().getCheckPersonId()).getStaffName());
        qualityCheck.setCheckTime(new Date());
        qualityCheckDAO.updateByPrimaryKeySelective(qualityCheck);
        AuditReason auditReason = new AuditReason();
        auditReason.setTableId(detailAndCode.getDetail().getId());
        auditReason.setTableCode(detailAndCode.getDetail().getCheckCode());
        auditReason.setCreateTime(qualityCheck.getCreateTime());
        auditReason.setAuditReason(detailAndCode.getCode());
        return auditReasonService.add(auditReason);
    }

    public Integer qualityUnpass(QualityCheck qualityCheck) {
        QualityCheck temp = new QualityCheck();
        temp.setId(qualityCheck.getId());
        temp.setIsPass(false);
        temp.setCheckState(4);
        temp.setCloseReason("质检不通过");
        temp.setCheckerName(staffDAO.selectByPrimaryKey(qualityCheck.getCheckPersonId()).getStaffName());
        temp.setCheckTime(new Date());
        qualityCheckDAO.updateByPrimaryKeySelective(temp);
        List<QualityCheckDetail> qualityCheckDetails = qualityCheckDetailDAO.selectByCheckId(qualityCheck.getId());
        HashSet<Integer> ids = new HashSet<>();
        for (int i = 0; i < qualityCheckDetails.size(); i++) {
            ids.add(qualityCheckDetails.get(i).getPlanId());
        }
        for (Integer id : ids) {
            InboundPlan inboundPlan = inboundPlanDAO.selectByPrimaryKey(id);
            inboundPlan.setInboundState(14);
            inboundPlan.setCloseReason("质检未通过");
            inboundPlanDAO.updateByPrimaryKey(inboundPlan);
        }
        return ids.size();
    }

    @Override
    @Transactional
    public Integer save(HeadAndDetail<QualityCheck,QualityCheckDetail> headAndDetail) {
        //设置入库计划单状态为正在填写
        headAndDetail.getHead().setCheckState(0);
        Integer num = 0;
        //如果前端没有传进来id,说明为第一次填写
        if (headAndDetail.getHead().getId() == null) {
            //设置入库计划单代码
            //插入表头
            qualityCheckDAO.insertSelective(headAndDetail.getHead());
        }
        //非第一次填写
        else {
            //对表头做更新操作
            qualityCheckDAO.updateByPrimaryKey(headAndDetail.getHead());
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
    public Integer add(HeadAndDetail<QualityCheck, QualityCheckDetail> headAndDetail) {
        //设置入库计划单状态为已填写
        headAndDetail.getHead().setCheckState(1);
        Integer num = 0;
        //如果没有保存过
        if (headAndDetail.getHead().getId() == null) {
            //设置入库计划单代码
            //插入表头
            qualityCheckDAO.insertSelective(headAndDetail.getHead());
            Integer id = headAndDetail.getHead().getId();
            //插入表单
            if(headAndDetail.getDetails()!=null) {
                for (QualityCheckDetail qualityCheckDetail : headAndDetail.getDetails()) {
                    qualityCheckDetail.setCheckId(id);
                    qualityCheckDetailDAO.insertSelective(qualityCheckDetail);
                    num++;
                }
            }
        }
        //已经保存过
        else {
            //更新表头
            qualityCheckDAO.updateByPrimaryKey(headAndDetail.getHead());
        }
        return headAndDetail.getHead().getId();
    }
}
