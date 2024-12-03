package com.bupt.mapper;

import com.bupt.DTO.SearchDTO;
import com.bupt.pojo.Department;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DepartmentDAO继承基类
 */
@Mapper
@Repository
public interface DepartmentDAO extends MyBatisBaseDao<Department, Integer> {
    List<Department> selectDepartment(SearchDTO searchDTO);

}
