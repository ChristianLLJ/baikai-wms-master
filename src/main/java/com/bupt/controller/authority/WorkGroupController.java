package com.bupt.controller.authority;

import com.bupt.DTO.FrontId;
import com.bupt.DTO.IdAndSearchDTO;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SearchDTO;
import com.bupt.DTO.workGroup.WorkGroupAndFunctionsId;
import com.bupt.DTO.workGroup.WorkGroupAndStaffsId;
import com.bupt.DTO.workGroup.WorkGroupAndWarehousesId;
import com.bupt.controller.BaseController;
import com.bupt.pojo.NullPojo;
import com.bupt.pojo.WorkGroup;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.authority.impl.WorkGroupServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/workGroup")
public class WorkGroupController extends BaseController<WorkGroupServiceImpl, WorkGroup, NullPojo> {
    /**
     * 工作组信息全部查询
     * @param searchDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchWorkgroup")
    public HttpResult<?> searchWorkGroup(@RequestBody SearchDTO searchDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,super.service.searchWorkgroup(searchDTO));
    }
    /**
     * 查询工作组的功能
     * @param idAndSearchDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchWorkGroupFunction")
    public HttpResult<?> searchWorkGroupFunction(@RequestBody IdAndSearchDTO idAndSearchDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,super.service.searchWorkGroupFunction(idAndSearchDTO));
    }
    /**
     * 查询工作组的员工
     * @param idAndSearchDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchWorkGroupStaff")
    public HttpResult<?> searchWorkGroupStaff(@RequestBody IdAndSearchDTO idAndSearchDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,super.service.searchWorkGroupStaff(idAndSearchDTO));
    }
    /**
     * 查询工作组的仓库
     * @param idAndSearchDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchWorkGroupWarehouse")
    public HttpResult<?> searchWorkGroupWarehouse(@RequestBody IdAndSearchDTO idAndSearchDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,super.service.searchWorkGroupWarehouse(idAndSearchDTO));
    }
    /**
     * 工作组筛选
     * @param workGroup
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectWorkGroup")
    public HttpResult<?> selectWorkGroup(@RequestBody ScreenDTO<WorkGroup> workGroup) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,super.service.selectWorkGroup(workGroup));
    }

    /**
     * 工作组数量返回
     * @param workGroup
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectWorkGroupNum")
    public HttpResult<?> selectWorkGroupNum(@RequestBody ScreenDTO<WorkGroup> workGroup) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,super.service.selectWorkGroupNum(workGroup));
    }
    /**
     * 添加工作组
     * @param workGroup
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("addWorkGroup")
    public HttpResult<?> addWorkGroup(@RequestBody @Valid WorkGroup workGroup) {
        return super.service.addWorkGroup(workGroup);
    }
    /**
     * 删除工作组
     * @param frontId
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("delWorkgroup")
    public HttpResult<?> delWorkGroup(@RequestBody FrontId frontId) {
        return super.service.delWorkGroup(frontId);
    }
    /**
     * 修改工作组
     * @param workGroup
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("updWorkGroup")
    public HttpResult<?> updWorkGroup(@RequestBody @Valid WorkGroup workGroup) {
        return super.service.updWorkGroup(workGroup);
    }
    /**
     * 添加员工到工作组中
     * @param workGroupAndStaffsId
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("addStaffToWorkGroup")
    public HttpResult<?> addStaffToWorkGroup(@RequestBody WorkGroupAndStaffsId workGroupAndStaffsId) {
        super.service.delWorkGroupStaffRelation(workGroupAndStaffsId.getWorkGroupId());
        return super.service.addStaffToWorkGroup(workGroupAndStaffsId);
    }
    /**
     * 添加功能到工作组中
     * @param workGroupAndFunctionsId
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("addFunctionToWorkGroup")
    public HttpResult<?> addFunctionToWorkGroup(@RequestBody WorkGroupAndFunctionsId workGroupAndFunctionsId) {
        super.service.delWorkGroupFunctionRelation(workGroupAndFunctionsId.getWorkGroupId());
        return super.service.addFunctionToWorkGroup(workGroupAndFunctionsId);
    }

    /**
     * 添加仓库到工作组中
     * @param workGroupAndWarehousesId
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("addWarehouseToWorkGroup")
    public HttpResult<?> addWarehouseToWorkGroup(@RequestBody WorkGroupAndWarehousesId workGroupAndWarehousesId) {
        try {
            super.service.delWorkGroupWarehouseRelation(workGroupAndWarehousesId.getWorkGroupId());
            Integer num=super.service.addWarehouseToWorkGroup(workGroupAndWarehousesId);
            return HttpResult.of(HttpResultCodeEnum.SUCCESS,num);
        }
        catch (Exception e){
            return HttpResult.of(HttpResultCodeEnum.SYSTEM_ERROR);
        }
    }
}
