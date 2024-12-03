package com.bupt.service.authority;

import com.bupt.DTO.FrontId;
import com.bupt.DTO.ResultsAndNum;
import com.bupt.DTO.SearchDTO;
import com.bupt.DTO.ScreenDTO;
import com.bupt.pojo.Company;
import com.bupt.pojo.Department;
import com.bupt.result.HttpResult;

import java.util.List;

public interface CompanyService {
    //已废弃
    ResultsAndNum<Company> searchCompany(SearchDTO searchDTO);
    //已废弃
    ResultsAndNum<Department> searchDepartment(SearchDTO searchDTO);

    HttpResult<?> addCompany(Company company);

    HttpResult<?> addDepartment(Department department);

    HttpResult<?> delCompany(FrontId frontId);

    HttpResult<?> delDepartment(FrontId frontId);

    HttpResult<?> updCompany(Company company);

    HttpResult<?> updDepartment(Department department);

    List<Company> selectCompany(ScreenDTO<Company> companyScreenDTO);

    Integer selectCompanyNum(ScreenDTO<Company> companyScreenDTO);

    List<Department> selectDepartment(ScreenDTO<Department> departmentScreenDTO);

    Integer selectDepartmentNum(ScreenDTO<Department> departmentScreenDTO);
}