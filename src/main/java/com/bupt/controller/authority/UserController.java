package com.bupt.controller.authority;

import com.bupt.DTO.FrontId;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SearchDTO;
import com.bupt.controller.BaseController;
import com.bupt.pojo.NullPojo;
import com.bupt.pojo.Staff;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.authority.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/staff")
public class UserController extends BaseController<UserServiceImpl, Staff, NullPojo> {
    /**
     * 员工信息全部查询
     * @param searchDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchStaff")
    public HttpResult<?> searchStaff(@RequestBody SearchDTO searchDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,super.service.searchStaff(searchDTO));
    }
    /**
     * 员工信息筛选
     * @param staff
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectStaff")
    public HttpResult<?> selectStaff(@RequestBody ScreenDTO<Staff> staff) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,super.service.selectStaff(staff));
    }
    /**
     * 员工数量返回
     * @param staff
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectStaffNum")
    public HttpResult<?> selectStaffNum(@RequestBody ScreenDTO<Staff> staff) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,super.service.selectStaffNum(staff));
    }
    /**
     * 添加员工
     * @param staff
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("addStaff")
    public HttpResult<?> addStaff(@Validated @RequestBody Staff staff) {
        return super.service.addStaff(staff);
    }
    /**
     * 删除员工
     * @param frontId
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("delStaff")
    public HttpResult<?> delStaff(@RequestBody FrontId frontId) {
        return super.service.delStaff(frontId);
    }
    /**
     * 修改员工
     * @param staff
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("updStaff")
    public HttpResult<?> updStaff(@RequestBody Staff staff) {
        return super.service.updStaff(staff);
    }

}
