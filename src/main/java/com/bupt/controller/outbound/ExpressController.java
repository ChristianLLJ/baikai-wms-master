package com.bupt.controller.outbound;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.controller.BaseController;
import com.bupt.pojo.Distribution;
import com.bupt.pojo.ExpressDelivery;
import com.bupt.pojo.ExpressDeliveryDetail;
import com.bupt.pojo.NullPojo;
import com.bupt.result.HttpResult;
import com.bupt.service.outbound.ExpressService;
import com.bupt.service.outbound.impl.DistributionServiceImpl;
import com.bupt.service.outbound.impl.ExpressServiceImpl;
import com.bupt.service.util.EasyExcelService;
import com.bupt.service.util.GetMapListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/expresss")
public class ExpressController extends BaseController<ExpressServiceImpl, ExpressDelivery, NullPojo> {
    @Autowired
    private ExpressService expressService;
    @Autowired
    private EasyExcelService easyExcelService;
    @Autowired
    private GetMapListService getMapListService;

    @ResponseBody
    @PostMapping("/getMsgByDistribution")
    public HttpResult<?> getMsgByDistribution(@RequestBody  Distribution distribution) {
        return expressService.generateMsgByDis(distribution);
    }

    @ResponseBody
    @PostMapping("/addExpress")
    public HttpResult<?> addExpress(@RequestBody @Valid ExpressDelivery expressDelivery) {
        return expressService.addExpress(expressDelivery);
    }
    /**
     * @Name 导出 快递面单 表头表细 Excel
     * @Description 筛选后全部导出
     * @Author LIWEMY
     * @Date 20:51 2022/4/18
     * @Param [screenDTO, response]
     * @Return void
     **/
    @ResponseBody
    @PostMapping("/exportExpressExcel")
    public void exportExpressExcel(@RequestBody ScreenDTO<ExpressDelivery> screenDTO, HttpServletResponse response) {
        easyExcelService.exportExcelList("ExpressDeliverys.xlsx", expressService.getHeads(screenDTO),
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
    @PostMapping("/exportExpressExcelSelect")
    public void exportExpressExcelSelect(@RequestBody List<ExpressDelivery> expressDeliveryList, HttpServletResponse response) {
        easyExcelService.exportExcelList("ExpressDeliverys.xlsx", expressService.getHeads
                (expressDeliveryList), response);
    }
}
