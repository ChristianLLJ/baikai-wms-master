package com.bupt.controller.outbound;

import com.bupt.DTO.ScreenDTO;
import com.bupt.controller.BaseController;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;
import com.bupt.service.outbound.DistributionService;
import com.bupt.service.outbound.impl.DistributionServiceImpl;
import com.bupt.service.util.EasyExcelService;
import com.bupt.service.util.GetMapListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/distribution")
public class DistributionController extends BaseController<DistributionServiceImpl, Distribution, NullPojo> {
    @Autowired
    private DistributionService distributionService;
    @Autowired
    private EasyExcelService easyExcelService;
    @Autowired
    private GetMapListService getMapListService;

    @ResponseBody
    @PostMapping("/addDistribution")
    public HttpResult<?> updateExpressDelivery(@RequestBody List<Distribution> distributions) {
        return distributionService.addDistribution(distributions);
    }
    @ResponseBody
    @PostMapping("/exportDistributionExcel")
    public void exportDistributionExcel(@RequestBody ScreenDTO<Distribution> screenDTO, HttpServletResponse response) {
        easyExcelService.exportExcelList("Distributions.xlsx", distributionService.getHeads(screenDTO),
                response);
    }

    /**
     * @Name 导出 快递面单 表头表细 Excel
     * @Description 自选几项导出
     * @Author LIWEMY
     * @Date 17:52 2022/5/20
     * @Param [expressDeliveryList, response]
     * @Return void
     **/
    @ResponseBody
    @PostMapping("/exportDistributionExcelSelect")
    public void exportDistributionExcelSelect(@RequestBody List<Distribution> distributions, HttpServletResponse response) {
        easyExcelService.exportExcelList("Distributions.xlsx", distributionService.getHeads
                (distributions), response);
    }
}
