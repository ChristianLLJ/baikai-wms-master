package com.bupt.controller.reportForm;

import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.reportForm.InboundReportVO;
import com.bupt.DTO.reportForm.ReportSum;
import com.bupt.mapper.InboundPlanDAO;
import com.bupt.mapper.InboundPlanDetailDAO;
import com.bupt.mapper.OnshelfDAO;
import com.bupt.mapper.OnshelfDetailDAO;
import com.bupt.pojo.InboundPlan;
import com.bupt.pojo.InboundPlanDetail;
import com.bupt.pojo.Onshelf;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/inboundReport")
public class inboundReportController {
    @Autowired
    InboundPlanDAO inboundPlanDAO;
    @Autowired
    InboundPlanDetailDAO inboundPlanDetailDAO;
    @Autowired
    OnshelfDAO onshelfDAO;
    @Autowired
    OnshelfDetailDAO onshelfDetailDAO;

    @ResponseBody
    @RequestMapping("/inboundReport")
    public HttpResult<?> inboundReport(@RequestBody ScreenDTO<InboundPlanDetail> screenDTO) {
        InboundReportVO inboundReportVO = new InboundReportVO();
        ReportSum reportSum = new ReportSum();
        int inboundSum = 0, receiveSum = 0, onshelfNum = 0;
        reportSum = inboundPlanDetailDAO.SumInboundNum(screenDTO);
        if (reportSum == null) {
            inboundSum = 0;
            receiveSum = 0;
        } else {
            inboundSum = reportSum.getInboundSum();
            receiveSum = reportSum.getReceiveSum();
        }
        inboundReportVO.setInboundNum(inboundSum);
        inboundReportVO.setReceiveNum(receiveSum);
        Integer num = onshelfDetailDAO.SumOnshelfNum(screenDTO);
        if (num != null) onshelfNum = num;
        inboundReportVO.setOnshelfNum(onshelfNum);
        inboundReportVO.setReceiveDiff(inboundReportVO.getInboundNum() - inboundReportVO.getReceiveNum());
        inboundReportVO.setOnshelfDiff(inboundReportVO.getReceiveNum() - inboundReportVO.getOnshelfNum());
        inboundReportVO.setInboundPlanDetailList(inboundPlanDetailDAO.screenInbound(screenDTO));
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, inboundReportVO);
    }
}
