package com.bupt.controller.warehouseData;

import com.bupt.DTO.FrontId;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SearchDTO;
import com.bupt.controller.BaseController;
import com.bupt.pojo.Device;
import com.bupt.pojo.NullPojo;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.warehouseData.impl.DeviceServiceImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/device")
public class DeviceController extends BaseController<DeviceServiceImpl, Device, NullPojo> {
    /**
     * 查询所有设备
     * @param searchDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchDevice")
    public HttpResult<?> searchDevice(@RequestBody SearchDTO searchDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, super.service.searchDevice(searchDTO));
    }
    /**
     * 筛选设备
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectDevice")
    public HttpResult<?> selectDevice(@RequestBody ScreenDTO<Device> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,super.service.selectDevice(screenDTO));
    }
    /**
     * 筛选设备数量
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectDeviceNum")
    public HttpResult<?> selectDeviceNum(@RequestBody ScreenDTO<Device> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,super.service.selectDeviceNum(screenDTO));
    }
    /**
     * 增加设备
     * @param device
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/addDevice")
    public HttpResult<?> addDevice(@RequestBody Device device) {
        if (device != null) {
            return HttpResult.of(HttpResultCodeEnum.SUCCESS, super.service.addDevice(device));
        } else return HttpResult.of(HttpResultCodeEnum.SYSTEM_ERROR);
    }
    /**
     * 删除设备
     * @param frontId
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/delDevice")
    public HttpResult<?> delDevice(@RequestBody FrontId frontId) {
        if (frontId != null) {
            return HttpResult.of(HttpResultCodeEnum.SUCCESS, super.service.delDevice(frontId));
        } else return HttpResult.of(HttpResultCodeEnum.SYSTEM_ERROR);
    }
}
