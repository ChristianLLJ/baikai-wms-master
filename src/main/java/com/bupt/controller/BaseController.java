package com.bupt.controller;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.HeadCode;
import com.bupt.DTO.ScreenDTO;
import com.bupt.pojo.BasePojo;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.BaseService;
import com.bupt.service.authority.CodeService;
import com.bupt.service.util.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@SuppressWarnings({"all"})
public class BaseController<Service extends BaseService, Table extends BasePojo, Detail extends BasePojo> {
    @Autowired(required = false)
    public Service service;
    @Autowired
    CodeService codeService;
    @Autowired
    private UtilService utilService;

    @ResponseBody
    @PostMapping("/screenTable")
    public HttpResult<?> selectTable(@RequestBody ScreenDTO<Table> screenDTO) {
        if (screenDTO.getScreen() != null && screenDTO.getScreen() != "") {
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        }
        List<Table> records = service.screenTable(screenDTO);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, records);
    }


    @ResponseBody
    @PostMapping("/screenDetail")
    public HttpResult<?> selectDetail(@RequestBody ScreenDTO<Detail> screenDTO) {
        if (screenDTO.getScreen() != null && screenDTO.getScreen() != "") {
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        }
        List<Detail> records = service.screenDetail(screenDTO);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, records);
    }


//    @ResponseBody
//    @PostMapping("/screenSku")
//    public HttpResult<?> selectSku(@RequestBody ScreenDTO<Detail> screenDTO) {
//        SearchDTO searchDTO = screenDTO.getSearchDTO();
//        screenDTO.setSearchDTO(null);
//        List<Detail> records = service.screenDetail(screenDTO);
//        HashMap<Integer, Detail> result = new HashMap<>();
//        for (int i = 0; i < records.size(); i++) {
//            Detail detail = records.get(i);
//            if (result.containsKey(records.get(i).getSkuId())) {
//                detail = result.get(records.get(i).getSkuId());
//                detail.setNum(detail.getNum() + records.get(i).getNum());
//                detail.setTotalNetWeight(detail.getTotalNetWeight() + records.get(i).getTotalNetWeight());
//                detail.setTotalVolumn(detail.getTotalVolumn() + records.get(i).getTotalVolumn());
//                detail.setTotalWeight(detail.getTotalWeight() + detail.getTotalWeight());
//            }
//            result.put(records.get(i).getSkuId(), detail);
//        }
//        return HttpResult.of(HttpResultCodeEnum.SUCCESS, result.values());
//
//    }


    @ResponseBody
    @PostMapping("/screenTableNum")
    public HttpResult<?> selectTableNum(@RequestBody ScreenDTO<Table> screenDTO) {
        if (screenDTO.getScreen() != null && screenDTO.getScreen() != "") {
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        }
        Integer num = service.numScreenTable(screenDTO);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, num);
    }


    @ResponseBody
    @PostMapping("/screenDetailNum")
    public HttpResult<?> selectDetailNum(@RequestBody ScreenDTO<Detail> screenDTO) {
        if (screenDTO.getScreen() != null && screenDTO.getScreen() != "") {
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        }
        Integer num = service.numScreenDetail(screenDTO);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, num);
    }

    @ResponseBody
    @PostMapping("/add")
    public HttpResult<?> add(@RequestBody @Valid Table table) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, service.add(table));
    }


    @ResponseBody
    @PostMapping("/del")
    public HttpResult<?> del(@RequestBody Table table) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, service.del(table));
    }


    @ResponseBody
    @PostMapping("/upd")
    public HttpResult<?> upd(@RequestBody Table table) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, service.upd(table));
    }


    @ResponseBody
    @PostMapping("/updTable")
    public HttpResult<?> updTable(@RequestBody @Valid HeadAndDetail<Table, Detail> headAndDetail) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, service.updTable(headAndDetail));
    }


    @ResponseBody
    @PostMapping("/delTable")
    public HttpResult<?> delTable(@RequestBody Table table) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, service.delTable(table));
    }

    @ResponseBody
    @PostMapping("/updDetail")
    public HttpResult<?> updDetail(@RequestBody @Valid List<Detail> details) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, service.updDetail(details));
    }


    @ResponseBody
    @PostMapping("/delDetail")
    public HttpResult<?> delDetail(@RequestBody List<Detail> details) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, service.delDetail(details));
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/insertDetail")
    public HttpResult<?> insertDetail(@RequestBody @Valid List<Detail> details) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, service.insertDetail(details));
    }


    @ResponseBody
    @PostMapping("/getHeadCode")
    public HttpResult<?> getHeadCode(@RequestBody HeadCode code) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, codeService.code(code.getCode()));
    }
}
