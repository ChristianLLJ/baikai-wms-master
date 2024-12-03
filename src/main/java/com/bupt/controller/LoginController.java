package com.bupt.controller;

import com.bupt.DTO.staff.StaffIdAndWorkGroup;
import com.bupt.DTO.staff.StaffLogin;
import com.bupt.DTO.IdAndSearchDTO;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.authority.FunctionService;
import com.bupt.service.authority.UserService;
import com.bupt.service.authority.WorkGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登陆相关接口
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    UserService userService;
    @Autowired
    WorkGroupService workGroupService;
    @Autowired
    FunctionService functionService;

    /**
     * 用户登陆功能
     * @param staffLogin
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/getUserId")
    public HttpResult<?> getUserId(@RequestBody @Validated StaffLogin staffLogin) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,userService.selectUserByUsername(staffLogin.getUsername()).getId());
    }
    /**
     * 用户登陆功能
     * @param staffLogin
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/loginUser")
    public HttpResult<?> login(@RequestBody @Validated StaffLogin staffLogin , HttpServletRequest request, HttpServletResponse response) {
        return userService.login(staffLogin,request, response);
    }
    /**
     * 用户登陆功能
     * @param
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/singleTest")
    public HttpResult<?> singleTest() {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }
    /**
     * 返回登陆员工的工作组
     * @param idAndSearchDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody

    @PostMapping("/workGroupInfo")
    public HttpResult<?> workgroupInfo(@RequestBody IdAndSearchDTO idAndSearchDTO){
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,workGroupService.info(idAndSearchDTO));
    }

    /**
     * 确认员工的工作组
     * @param
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/loginWorkGroup")
    public HttpResult<?> loginWorkGroup(@RequestBody StaffIdAndWorkGroup staffIdAndWorkGroup){
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,userService.loginWorkGroup(staffIdAndWorkGroup));
    }
    /**
     * 返回工作组的功能信息
     * @param idAndSearchDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/functionInfo")
    public HttpResult<?> function(@RequestBody IdAndSearchDTO idAndSearchDTO){
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,workGroupService.searchWorkGroupFunction(idAndSearchDTO));
    }
    /**
     *
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/loginOut")
    public HttpResult<?> loginOut() {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,userService.loginOut());
    }
}
