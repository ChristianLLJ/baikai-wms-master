package com.bupt.service.authority.impl;

import com.bupt.DTO.FrontId;
import com.bupt.DTO.ResultsAndNum;
import com.bupt.DTO.SearchDTO;
import com.bupt.DTO.ScreenDTO;
import com.bupt.mapper.CompanyDAO;
import com.bupt.mapper.DepartmentDAO;
import com.bupt.pojo.Company;
import com.bupt.pojo.Department;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.BaseService;
import com.bupt.service.authority.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CompanyServiceImpl extends BaseService<Company,Department,CompanyDAO,DepartmentDAO> implements CompanyService {
    @Autowired
    private CompanyDAO companyDAO;
    @Autowired
    DepartmentDAO departmentDAO;

    /**
     * 分页查找所有的公司
     * @param searchDTO
     * @return 结果和数量
     */
    @Override
    public ResultsAndNum<Company> searchCompany(SearchDTO searchDTO){
        //设置起始页
        searchDTO.setPage((searchDTO.getPage()-1)*searchDTO.getNum());
        ResultsAndNum<Company> resultsAndNum=new ResultsAndNum<>();
        //获取结果和数量
        resultsAndNum.setResults(companyDAO.selectCompany(searchDTO));
        resultsAndNum.setNum(companyDAO.selectNum());
        return resultsAndNum;
    }

    /**
     * 分页查询所有部门
     * @param searchDTO
     * @return 结果和数量
     */
    @Override
    public ResultsAndNum<Department> searchDepartment(SearchDTO searchDTO){
        //设置起始页
        searchDTO.setPage((searchDTO.getPage()-1)*searchDTO.getNum());
        ResultsAndNum<Department> resultsAndNum=new ResultsAndNum<>();
        //获取结果和数量
        resultsAndNum.setResults(departmentDAO.selectDepartment(searchDTO));
        resultsAndNum.setNum(departmentDAO.selectNum());
        return resultsAndNum;
    }

    /**
     * 添加公司
     * @param company
     * @return
     */
    @Override
    public HttpResult<?> addCompany(Company company){
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,companyDAO.insertSelective(company));
    }

    /**
     * 添加部门
     * @param department
     * @return
     */
    @Override
    public HttpResult<?> addDepartment(Department department){
        Date now =new Date();
        department.setCreateTime(now);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,departmentDAO.insert(department));
    }

    /**
     * 删除公司
     * @param frontId
     * @return
     */
    @Override
    public HttpResult<?> delCompany(FrontId frontId){
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,companyDAO.deleteByPrimaryKey(frontId.getId()));
    }

    /**
     * 删除部门
     * @param frontId
     * @return
     */
    @Override
    public HttpResult<?> delDepartment(FrontId frontId){
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,departmentDAO.deleteByPrimaryKey(frontId.getId()));
    }

    /**
     * 公司更新
     * @param company
     * @return
     */
    @Override
    public HttpResult<?> updCompany(Company company){
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,companyDAO.updateByPrimaryKeySelective(company));
    }

    /**
     * 部门更新
     * @param department
     * @return
     */
    @Override
    public HttpResult<?> updDepartment(Department department){
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,departmentDAO.updateByPrimaryKeySelective(department));
    }

    /**
     * 分页筛选公司
     * @param companyScreenDTO
     * @return
     */
    @Override
    public List<Company> selectCompany(ScreenDTO<Company> companyScreenDTO) {
        //设置limit起始位置
        companyScreenDTO.getSearchDTO().setPage((companyScreenDTO.getSearchDTO().getPage()-1)* companyScreenDTO.getSearchDTO().getNum());
        return companyDAO.screen(companyScreenDTO);
    }

    /**
     * 分页筛选公司总数
     * @param companyScreenDTO
     * @return
     */
    @Override
    public Integer selectCompanyNum(ScreenDTO<Company> companyScreenDTO) {
        return companyDAO.numScreen(companyScreenDTO);
    }

    /**
     * 分页筛选部门数量
     * @param departmentScreenDTO
     * @return
     */
    @Override
    public Integer selectDepartmentNum(ScreenDTO<Department> departmentScreenDTO) {
        return departmentDAO.numScreen(departmentScreenDTO);
    }

    /**
     * 分页筛选部门
     * @param departmentScreenDTO
     * @return
     */
    @Override
    public List<Department> selectDepartment(ScreenDTO<Department> departmentScreenDTO) {
        //设置limit起始位置
        departmentScreenDTO.getSearchDTO().setPage((departmentScreenDTO.getSearchDTO().getPage()-1)* departmentScreenDTO.getSearchDTO().getNum());
        return departmentDAO.screen(departmentScreenDTO);
    }
}
