package com.bupt.service.authority.impl;

import com.bupt.DTO.*;
import com.bupt.DTO.staff.StaffIdAndWorkGroup;
import com.bupt.DTO.staff.StaffLogin;
import com.bupt.DTO.staff.StaffUpLatestTime;
import com.bupt.mapper.NullDAO;
import com.bupt.mapper.SerialNumberDAO;
import com.bupt.mapper.StaffDAO;
import com.bupt.pojo.NullPojo;
import com.bupt.pojo.Staff;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.BaseService;
import com.bupt.service.authority.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl extends BaseService<Staff, NullPojo,StaffDAO, NullDAO>implements UserService {
    @Autowired
    StaffDAO staffDAO;
    @Autowired
    SerialNumberDAO serialNumberDAO;

    /**
     * 登陆功能
     * @param staffLogin
     * @return
     */
    @Override
    public HttpResult<?> login(StaffLogin staffLogin, HttpServletRequest request, HttpServletResponse response) {
        //获取shiro Subject
        Subject subject = SecurityUtils.getSubject();
        System.out.println("登陆中");
        //得到登陆token
        UsernamePasswordToken token = new UsernamePasswordToken(staffLogin.getUsername(),staffLogin.getPassword());
        try {
            //shiro登陆
            subject.login(token);
            Staff staff = staffDAO.selectByUsername(staffLogin.getUsername()).get(0);
            IdAndNameAndCode idAndNameAndCode = new IdAndNameAndCode();
            idAndNameAndCode.setId(staff.getId());
            idAndNameAndCode.setName(staff.getStaffName());
            return  HttpResult.of(HttpResultCodeEnum.SUCCESS,idAndNameAndCode);
        }catch (UnknownAccountException uae){
            return HttpResult.of(HttpResultCodeEnum.LOGIN_UNKNOWN_USERNAME);
        }catch (IncorrectCredentialsException ice){
            return HttpResult.of(HttpResultCodeEnum.LOGIN_INCORRECT_PASSWORD);
        }
        /*
        //获取登陆用户名
        String name = staffLogin.getUsername();
        //通过用户名找到数据库用户
        Staff restStaff = new Staff();
        restStaff=staffDAO.selectByUsername(name);
        //如果用户不存在，登陆失败
        if(restStaff==null){
            return HttpResult.of(HttpResultCodeEnum.LOGIN_FAIL);
        }
        //判断密码是否正确
        if (!Objects.equals(restStaff.getPassword(), staffLogin.getPassword())) {
            return HttpResult.of(HttpResultCodeEnum.LOGIN_FAIL);
        }
        //判断是否启用
        if (restStaff.getIsUsable() == 0) {
            return HttpResult.of(HttpResultCodeEnum.LOGIN_FAIL_NOT_USABLE);
        }
        restStaff.setPassword("******");
        //更新最近一次登陆时间
        Date now=new Date();
        restStaff.setLatestTime(now);
        StaffUpLatestTime staffUpLatestTime=new StaffUpLatestTime(restStaff.getId(),now);
        staffDAO.updateLatestTime(staffUpLatestTime);
        return HttpResult.of(HttpResultCodeEnum.LOGIN_SUCCESS,restStaff);

         */
    }

    /**
     * 设置登陆用户所属工作组
     * @param
     * @return
     */
    @Override
    public Integer loginWorkGroup(StaffIdAndWorkGroup staffIdAndWorkGroup) {
        return staffDAO.updateWorkGroup(staffIdAndWorkGroup);
    }

    /**
     * 分页查询所有用户
     * @param searchDTO
     * @return
     */
    @Override
    public ResultsAndNum<Staff> searchStaff(SearchDTO searchDTO){
        //设置limit起始参数
        searchDTO.setPage((searchDTO.getPage()-1)*searchDTO.getNum());
        ResultsAndNum<Staff> resultsAndNum=new ResultsAndNum<>();
        //获取结果
        resultsAndNum.setResults(staffDAO.selectStaff(searchDTO));
        resultsAndNum.setNum(staffDAO.selectNum());
        return resultsAndNum;
    }

    /**
     * 添加用户
     * @param staff
     * @return
     */
    @Override
    public HttpResult<?> addStaff(Staff staff){
        Date now =new Date();
        staff.setCreateTime(now);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,staffDAO.insert(staff));
    }

    /**
     * 删除用户
     * @param frontId
     * @return
     */
    @Override
    public HttpResult<?> delStaff(FrontId frontId){
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,staffDAO.deleteByPrimaryKey(frontId.getId()));
    }

    /**
     * 更新用户
     * @param staff
     * @return
     */
    @Override
    public HttpResult<?> updStaff(Staff staff){
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,staffDAO.updateByPrimaryKeySelective(staff));
    }

    /**
     * 筛选用户
     * @param staffScreenDTO
     * @return
     */
    @Override
    public List<Staff> selectStaff(ScreenDTO<Staff> staffScreenDTO) {
        staffScreenDTO.getSearchDTO().setPage((staffScreenDTO.getSearchDTO().getPage()-1)* staffScreenDTO.getSearchDTO().getNum());
        return staffDAO.screen(staffScreenDTO);
    }

    /**
     * 筛选用户总数
     * @param staffScreenDTO
     * @return
     */
    @Override
    public Integer selectStaffNum(ScreenDTO<Staff> staffScreenDTO) {
        return staffDAO.numScreen(staffScreenDTO);
    }

    @Override
    public Staff selectUserByUsername(String username) {
        List<Staff> staffs = staffDAO.selectByUsername(username);
        if(staffs.size() ==0)
            return null;
        return staffs.get(0);
    }

    @Override
    public Integer loginOut() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return 1;
    }
}
