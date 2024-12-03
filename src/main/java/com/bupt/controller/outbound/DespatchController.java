package com.bupt.controller.outbound;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.controller.BaseController;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.outbound.DespatchNewService;
import com.bupt.service.outbound.impl.DespatchImplNewService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/despatch")
public class DespatchController extends BaseController<DespatchImplNewService, Despatch, DespatchDetail> {
    @Autowired
    private DespatchNewService despatchService;

    /**
     * @Name 新增表头表细
     * @Description 完结
     * @Author LIWEMY
     * @Date 14:38 2022/11/5
     * @Param [headAndDetail]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/addDesAndDetail")
    public HttpResult<?> addDesAndDetail(@RequestBody @Valid HeadAndDetail<Despatch, DespatchDetail> headAndDetail) {
        return despatchService.addDesAndDetail(headAndDetail);
    }

    /**
     * @Name 审核通过
     * @Description
     * @Author LIWEMY
     * @Date 21:41 2022/11/15
     * @Param [despatchList]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/checkDespatch")
    public HttpResult<?> checkDespatch(@RequestBody List<Despatch> despatchList) {
        return despatchService.checkSuccess(despatchList);
    }

    /**
     * @Name 审核不通过
     * @Description
     * @Author LIWEMY
     * @Date 13:43 2022/12/28
     * @Param [despatchList]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/checkDespatchReject")
    public HttpResult<?> checkDespatchReject(@RequestBody List<Despatch> despatchList) {
        return despatchService.checkReject(despatchList);
    }

    /**
     * @Name 修改表头表细
     * @Description
     * @Author LIWEMY
     * @Date 15:00 2022/12/28
     * @Param [headAndDetail]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/updDespatch")
    public HttpResult<?> updDespatch(@RequestBody HeadAndDetail<Despatch, DespatchDetail> headAndDetail) {
        return despatchService.updateDesAndDetail(headAndDetail);
    }

    /**
     * @Name 插入表细
     * @Description
     * @Author LIWEMY
     * @Date 15:20 2022/12/28
     * @Param [despatchDetails]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/insertDetails")
    public HttpResult<?> insertDetails(@RequestBody List<DespatchDetail> despatchDetails) {
        return despatchService.insertDetails(despatchDetails);
    }
}
