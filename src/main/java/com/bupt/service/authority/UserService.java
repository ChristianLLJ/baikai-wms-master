package com.bupt.service.authority;

import com.bupt.DTO.FrontId;
import com.bupt.DTO.ResultsAndNum;
import com.bupt.DTO.SearchDTO;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.staff.StaffIdAndWorkGroup;
import com.bupt.DTO.staff.StaffLogin;
import com.bupt.pojo.Staff;
import com.bupt.result.HttpResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface UserService {
    HttpResult<?> login(StaffLogin staffLogin, HttpServletRequest request, HttpServletResponse response);
//已废弃
    ResultsAndNum<Staff> searchStaff(SearchDTO searchDTO);

    HttpResult<?> addStaff(Staff staff);

    HttpResult<?> delStaff(FrontId frontId);

    HttpResult<?> updStaff(Staff staff);

    List<Staff> selectStaff(ScreenDTO<Staff> staffScreenDTO);

    Integer selectStaffNum(ScreenDTO<Staff> staffScreenDTO);

    Staff selectUserByUsername(String username);

    Integer loginWorkGroup(StaffIdAndWorkGroup staffIdAndWorkGroup);

    Integer loginOut();
}
