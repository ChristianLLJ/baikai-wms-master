package com.bupt.mapper;

import com.bupt.DTO.SearchDTO;
import com.bupt.DTO.staff.StaffIdAndWorkGroup;
import com.bupt.DTO.staff.StaffUpLatestTime;
import com.bupt.pojo.Staff;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * StaffDAO继承基类
 */
@Mapper
@Repository
public interface StaffDAO extends MyBatisBaseDao<Staff, Integer> {
    List<Staff> selectByUsername(String name);

    void updateLatestTime(StaffUpLatestTime now);

    List<Staff> selectStaff(SearchDTO searchDTO);

    Integer updateWorkGroup(StaffIdAndWorkGroup staffIdAndWorkGroup);
}
