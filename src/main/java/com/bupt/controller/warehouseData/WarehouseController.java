package com.bupt.controller.warehouseData;

import com.bupt.DTO.*;
import com.bupt.controller.BaseController;
import com.bupt.pojo.NullPojo;
import com.bupt.pojo.Warehouse;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.warehouseData.impl.WarehouseServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/warehouse")
public class WarehouseController extends BaseController<WarehouseServiceImpl, Warehouse, NullPojo> {
    /**
     * 查询全部仓库
     * @param searchDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchWarehouse")
    public HttpResult<?> searchWarehouse(@RequestBody SearchDTO searchDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, super.service.searchWarehouse(searchDTO));
    }
    /**
     * 查询仓库名字和代码
     * @param name
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchWarehouseNameAndCode")
    public HttpResult<?> searchAllWarehouseNameAndCode(@RequestBody FrontName name) {
        List<IdAndNameAndCode> warehouses = super.service.searchAllWarehouseNameAndCode(name.getName());
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, warehouses);
    }
    /**
     * 筛选仓库
     * @param warehouseScreenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectWarehouse")
    public HttpResult<?> selectWarehouse(@RequestBody ScreenDTO<Warehouse> warehouseScreenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,super.service.selectWarehouse(warehouseScreenDTO));
    }
    /**
     * 筛选仓库数量
     * @param warehouseScreenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectWarehouseNum")
    public HttpResult<?> selectWarehouseNum(@RequestBody ScreenDTO<Warehouse> warehouseScreenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,super.service.selectWarehouseNum(warehouseScreenDTO));
    }
    /**
     * 增加仓库
     * @param warehouse
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/addWarehouse")
    public HttpResult<?> addWarehouse(@RequestBody Warehouse warehouse) {
        if (warehouse != null) {
            return HttpResult.of(HttpResultCodeEnum.SUCCESS, super.service.addWarehouse(warehouse));
        } else return HttpResult.of(HttpResultCodeEnum.SYSTEM_ERROR);
    }
    /**
     * 删除仓库
     * @param frontId
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/delWarehouse")
    public HttpResult<?> delWarehouse(@RequestBody FrontId frontId) {
        if (frontId != null) {
            return HttpResult.of(HttpResultCodeEnum.SUCCESS, super.service.delWarehouse(frontId));
        } else return HttpResult.of(HttpResultCodeEnum.SYSTEM_ERROR);
    }
}
