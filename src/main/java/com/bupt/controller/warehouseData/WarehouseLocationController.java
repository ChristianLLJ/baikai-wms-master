package com.bupt.controller.warehouseData;

import com.bupt.DTO.*;
import com.bupt.controller.BaseController;
import com.bupt.pojo.NullPojo;
import com.bupt.pojo.WarehouseLocation;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.warehouseData.impl.LocationServiceImpl;
import org.apache.ibatis.jdbc.Null;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/warehouseLocation")
public class WarehouseLocationController extends BaseController<LocationServiceImpl, WarehouseLocation, NullPojo> {
    /**
     * 查询库位名称和代码
     * @param name
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchLocationNameAndCode")
    public HttpResult<?> searchLocationNameAndCode(@RequestBody FrontName name) {
        List<IdAndNameAndCode> idAndNameAndCodes = super.service.searchLocationNameAndCode(name.getName());
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, idAndNameAndCodes);
    }
    /**
     * 查询全部库位
     * @param searchDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchLocation")
    public HttpResult<?> searchLocation(@RequestBody SearchDTO searchDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, super.service.searchLocation(searchDTO));
    }
    /**
     * 筛选库位
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectLocation")
    public HttpResult<?> selectLocation(@RequestBody ScreenDTO<WarehouseLocation> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,super.service.selectWarehouseLocation(screenDTO));
    }
    /**
     * 筛选库位数量
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectLocationNum")
    public HttpResult<?> selectWarehouseLocationNum(@RequestBody ScreenDTO<WarehouseLocation> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,super.service.selectWarehouseLocationNum(screenDTO));
    }
    /**
     * 增加库位
     * @param warehouseLocation
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/addLocation")
    public HttpResult<?> addLocation(@Validated @RequestBody WarehouseLocation warehouseLocation) {
        if (warehouseLocation != null) {
            return HttpResult.of(HttpResultCodeEnum.SUCCESS, super.service.addLocation(warehouseLocation));
        } else return HttpResult.of(HttpResultCodeEnum.SYSTEM_ERROR);
    }
    /**
     * 删除库位
     * @param frontId
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/delLocation")
    public HttpResult<?> delLocation(@RequestBody FrontId frontId) {
        if (frontId != null) {
            return HttpResult.of(HttpResultCodeEnum.SUCCESS, super.service.delLocation(frontId));
        } else return HttpResult.of(HttpResultCodeEnum.SYSTEM_ERROR);
    }
    @CrossOrigin
    @ResponseBody
    @PostMapping("/importLocation")
    public HttpResult<?> importLocation(@RequestBody WarehouseLocation warehouseLocation){
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,super.service.importLocation(warehouseLocation));
    }
}
