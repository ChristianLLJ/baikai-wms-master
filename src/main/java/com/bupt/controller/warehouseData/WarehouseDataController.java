package com.bupt.controller.warehouseData;

import com.bupt.DTO.*;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.warehouseData.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 仓库数据相关接口
 */
@RestController
@RequestMapping("/warehousedata")
public class WarehouseDataController {
    @Autowired
    AreaService areaService;
    @Autowired
    CarService carService;
    @Autowired
    DeviceService deviceService;
    @Autowired
    LocationService locationService;
    @Autowired
    LocationGroupService locationGroupService;
    @Autowired
    WarehouseService warehouseService;

















    /**
     * 查询所有库位组
     * @param searchDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchLocationGroup")
    public HttpResult<?> searchLocationGroup(@RequestBody SearchDTO searchDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, locationGroupService.searchLocationGroup(searchDTO));
    }

    /**
     * 查询所有库区类型
     * @param searchDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchAreaType")
    public HttpResult<?> searchAreaType(@RequestBody SearchDTO searchDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, areaService.searchAreaType(searchDTO));
    }












    /**
     * 筛选库位组
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectLocationGroup")
    public HttpResult<?> selectLocationGroup(@RequestBody ScreenDTO<LocationGroup> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,locationGroupService.selectLocationGroup(screenDTO));
    }

    /**
     * 筛选库位组数量
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectLocationGroupNum")
    public HttpResult<?> selectLocationGroupNum(@RequestBody ScreenDTO<LocationGroup> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,locationGroupService.selectLocationGroupNum(screenDTO));
    }













    /**
     * 增加库区类型
     * @param warehouseAreaType
     * @return
     */

    @CrossOrigin
    @ResponseBody
    @PostMapping("/addAreaType")
    public HttpResult<?> addAreaType(@RequestBody WarehouseAreaType warehouseAreaType) {
        if (warehouseAreaType != null) {
            return HttpResult.of(HttpResultCodeEnum.SUCCESS, areaService.addAreaType(warehouseAreaType));
        } else return HttpResult.of(HttpResultCodeEnum.SYSTEM_ERROR);
    }



    /**
     * 增加库位组
     * @param locationGroup
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/addLocationGroup")
    public HttpResult<?> addLocationGroup(@RequestBody LocationGroup locationGroup) {
        if (locationGroup != null) {
            return HttpResult.of(HttpResultCodeEnum.SUCCESS, locationGroupService.addLocationGroup(locationGroup));
        } else return HttpResult.of(HttpResultCodeEnum.SYSTEM_ERROR);
    }











    /**
     * 删除库位组
     * @param frontId
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/delLocationGroup")
//    删除库位组同时删除对应库位信息中的库位组信息
    public HttpResult<?> delLocationGroup(@RequestBody FrontId frontId) {
        if (frontId != null) {
            locationService.updByLocationGroup(frontId);
            return HttpResult.of(HttpResultCodeEnum.SUCCESS, locationGroupService.delLocationGroup(frontId));
        } else return HttpResult.of(HttpResultCodeEnum.SYSTEM_ERROR);
    }

    /**
     * 删除库区类型
     * @param frontId
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/delAreaType")
//    删除库区类型同时删除对应库区信息中的库区类型信息

    public HttpResult<?> delAreaType(@RequestBody FrontId frontId) {
        if (frontId != null) {
            areaService.updByAreaType(frontId);
            return HttpResult.of(HttpResultCodeEnum.SUCCESS, areaService.delAreaType(frontId));
        } else return HttpResult.of(HttpResultCodeEnum.SYSTEM_ERROR);
    }




}




