package com.bupt.controller.authority;

import com.bupt.DTO.FrontId;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SearchDTO;
import com.bupt.controller.BaseController;
import com.bupt.pojo.Company;
import com.bupt.pojo.Department;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.authority.impl.CompanyServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/company")
public class CompanyController extends BaseController<CompanyServiceImpl, Company, Department> {
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchCompany")
    public HttpResult<?> searchCompany(@RequestBody SearchDTO searchDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,super.service.searchCompany(searchDTO));
    }
    /**
     * 部门信息全部查询
     * @param searchDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchDepartment")
    public HttpResult<?> searchDepartment(@RequestBody SearchDTO searchDTO) { ;
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,super.service.searchDepartment(searchDTO));
    }
    /**
     * 公司信息筛选
     * @param company
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectCompany")
    public HttpResult<?> selectCompany(@RequestBody ScreenDTO<Company> company) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,super.service.selectCompany(company));
    }
    /**
     * 公司数量返回
     * @param company
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectCompanyNum")
    public HttpResult<?> selectCompanyNum(@RequestBody ScreenDTO<Company> company) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,super.service.selectCompanyNum(company));
    }
    /**
     * 部门数量筛选
     * @param department
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectDepartment")
    public HttpResult<?> selectDepartment(@RequestBody ScreenDTO<Department> department) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,super.service.selectDepartment(department));
    }
    /**
     * 部门数量返回
     * @param department
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectDepartmentNum")
    public HttpResult<?> selectDepartmentNum(@RequestBody ScreenDTO<Department> department) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,super.service.selectDepartmentNum(department));
    }
    /**
     * 添加公司
     * @param company
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("addCompany")
    public HttpResult<?> addCompany(@RequestBody @Valid Company company) {
        return super.service.addCompany(company);
    }

    /**
     * 添加部门
     * @param department
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("addDepartment")
    public HttpResult<?> addDepartment(@RequestBody @Valid Department department) {
        return super.service.addDepartment(department);
    }
    /**
     * 删除公司
     * @param frontId
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("delCompany")
    public HttpResult<?> delCompany(@RequestBody FrontId frontId) {
        return super.service
                .delCompany(frontId);
    }

    /**
     * 删除部门
     * @param frontId
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("delDepartment")
    public HttpResult<?> delDepartment(@RequestBody FrontId frontId) {
        return super.service.delDepartment(frontId);
    }
    /**
     * 修改公司
     * @param company
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("updCompany")
    public HttpResult<?> updCompany(@RequestBody @Valid Company company) {
        return super.service.updCompany(company);
    }

    /**
     * 修改部门
     * @param department
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("updDepartment")
    public HttpResult<?> updDepartment(@RequestBody @Valid Department department) {
        return super.service.updDepartment(department);
    }
}
