package com.bupt.mapper;

import com.bupt.DTO.SearchDTO;
import com.bupt.pojo.Company;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CompanyDAO继承基类
 */
@Mapper
@Repository
public interface CompanyDAO extends MyBatisBaseDao<Company, Integer> {
    List<Company> selectCompany(SearchDTO searchDTO);
}
