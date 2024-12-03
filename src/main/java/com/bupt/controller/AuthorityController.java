package com.bupt.controller;

import com.bupt.DTO.*;
import com.bupt.DTO.workGroup.WorkGroupAndFunctionsId;
import com.bupt.DTO.workGroup.WorkGroupAndStaffsId;
import com.bupt.DTO.workGroup.WorkGroupAndWarehousesId;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.authority.CompanyService;
import com.bupt.service.authority.FunctionService;
import com.bupt.service.authority.UserService;
import com.bupt.service.authority.WorkGroupService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 权限管理相关接口
 */
@RestController
@RequestMapping("/authority")
public class AuthorityController {
    @Autowired
    UserService userService;
    @Autowired
    CompanyService companyService;
    @Autowired
    FunctionService functionService;
    @Autowired
    WorkGroupService workGroupService;

    /**
     * 公司信息全部查询
     * @param
     * @return
     */

















































}


