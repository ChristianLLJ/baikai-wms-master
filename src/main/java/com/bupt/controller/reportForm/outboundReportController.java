package com.bupt.controller.reportForm;

import com.bupt.DTO.SearchDTO;
import com.bupt.DTO.reportForm.OutboundReport;
import com.bupt.mapper.*;
import com.bupt.pojo.DespatchDetail;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.outbound.DespatchNewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/outboundReport")
public class outboundReportController {
    @Autowired
    DespatchDetailDAO despatchDetailDAO;
    @Autowired
    private DespatchNewService despatchNewService;

    @CrossOrigin
    @ResponseBody
    @RequestMapping("/outboundReport")
    public HttpResult<?> outboundReport(@RequestBody OutboundReport outboundReport){
        SearchDTO searchDTO = outboundReport.getSearchDTO();
        if (searchDTO != null) {
            searchDTO.setPage((searchDTO.getPage() - 1) * searchDTO.getNum());
            outboundReport.setSearchDTO(searchDTO);
        }
        OutboundReport total = despatchDetailDAO.generateSumOrderCnt(outboundReport);
        List<DespatchDetail> despatchDetails = despatchDetailDAO.gerateDespatchDetailList(outboundReport);
        OutboundReport out = despatchDetailDAO.gerateSumDespatchedOrderCnt(outboundReport);
        if (out.getSkuOfOutCnt()==null)out.setSkuOfOutCnt(0);
        if (total.getSkuOfTotalCnt()==null)total.setSkuOfTotalCnt(0);
        outboundReport.setTotalDesNum(total.getTotalDesNum());
        outboundReport.setSkuOfTotalCnt(total.getSkuOfTotalCnt());
        outboundReport.setSkuOfOutCnt(out.getSkuOfOutCnt());
        outboundReport.setDesNumToOut(out.getDesNumToOut());
        outboundReport.setDesNumWaitOut(outboundReport.getTotalDesNum()-outboundReport.getDesNumToOut());
        outboundReport.setSkuWaitoutCnt(outboundReport.getSkuOfTotalCnt()-outboundReport.getSkuOfOutCnt());
        outboundReport.setDespatchDetailList(despatchNewService.convertOnrtoone(despatchDetails));
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,outboundReport);
    }
}
