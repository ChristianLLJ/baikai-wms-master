package com.bupt.controller.warehouseData;

import com.bupt.DTO.FrontId;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SearchDTO;
import com.bupt.controller.BaseController;
import com.bupt.pojo.Car;
import com.bupt.pojo.NullPojo;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.warehouseData.impl.CarServiceImpl;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/car")
@RestController
public class CarController extends BaseController<CarServiceImpl, Car, NullPojo> {
    /**
     * 查询所有车辆
     * @param searchDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchCar")
    public HttpResult<?> searchCar(@RequestBody SearchDTO searchDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, super.service.searchCar(searchDTO));
    }
    /**
     * 筛选车辆
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectCar")
    public HttpResult<?> selectCar(@RequestBody ScreenDTO<Car> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,super.service.selectCar(screenDTO));
    }
    /**
     * 筛选车辆数量
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectCarNum")
    public HttpResult<?> selectCarNum(@RequestBody ScreenDTO<Car> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,super.service.selectCarNum(screenDTO));
    }
    /**
     * 增加车辆
     * @param car
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/addCar")
    public HttpResult<?> addCar(@RequestBody Car car) {
        if (car != null) {
            return HttpResult.of(HttpResultCodeEnum.SUCCESS, super.service.addCar(car));
        } else return HttpResult.of(HttpResultCodeEnum.SYSTEM_ERROR);
    }
    /**
     * 删除车辆
     * @param frontId
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/delCar")
    public HttpResult<?> delCar(@RequestBody FrontId frontId) {
        if (frontId != null) {
            return HttpResult.of(HttpResultCodeEnum.SUCCESS, super.service.delCar(frontId));
        } else return HttpResult.of(HttpResultCodeEnum.SYSTEM_ERROR);
    }

}
