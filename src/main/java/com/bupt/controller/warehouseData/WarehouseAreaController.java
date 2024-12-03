package com.bupt.controller.warehouseData;

import com.bupt.DTO.*;
import com.bupt.controller.BaseController;
import com.bupt.pojo.NullPojo;
import com.bupt.pojo.WarehouseArea;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.warehouseData.impl.AreaServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/warehouseArea")
public class WarehouseAreaController extends BaseController<AreaServiceImpl, WarehouseArea, NullPojo> {
    /**
     * 查询库区名称和代码
     * @param name
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchAreaNameAndCode")
    public HttpResult<?> searchAreaNameAndCode(@RequestBody FrontName name) {
        List<IdAndNameAndCode> idAndNameAndCodes = super.service.searchAreaNameAndCode(name.getName());
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, idAndNameAndCodes);
    }
    /**
     * 查询全部库区
     * @param searchDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchArea")
    public HttpResult<?> searchArea(@RequestBody SearchDTO searchDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, super.service.searchWarehouseArea(searchDTO));
    }
    /**
     * 筛选库区
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectArea")
    public HttpResult<?> selectArea(@RequestBody ScreenDTO<WarehouseArea> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,super.service.selectWarehouseArea(screenDTO));
    }
    /**
     * 筛选库区数量
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectAreaNum")
    public HttpResult<?> selectWarehouseAreaNum(@RequestBody ScreenDTO<WarehouseArea> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,super.service.selectWarehouseAreaNum(screenDTO));
    }
    /**
     * 增加库区
     * @param warehouseArea
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/addArea")
    public HttpResult<?> addArea(@RequestBody WarehouseArea warehouseArea) {
        if (warehouseArea != null) {
            return HttpResult.of(HttpResultCodeEnum.SUCCESS, super.service.addArea(warehouseArea));
        } else return HttpResult.of(HttpResultCodeEnum.SYSTEM_ERROR);
    }
    /**
     * 删除库区
     * @param frontId
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/delArea")
    public HttpResult<?> delArea(@RequestBody FrontId frontId) {
        if (frontId != null) {
            return HttpResult.of(HttpResultCodeEnum.SUCCESS, super.service.delArea(frontId));
        } else return HttpResult.of(HttpResultCodeEnum.SYSTEM_ERROR);
    }
}
