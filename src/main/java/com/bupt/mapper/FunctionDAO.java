package com.bupt.mapper;

import com.bupt.DTO.SearchDTO;
import com.bupt.pojo.Function;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * FunctionDAO继承基类
 */
@Mapper
@Repository
public interface FunctionDAO extends MyBatisBaseDao<Function, Integer> {
    List<Function> selectFunction(SearchDTO searchDTO);
}
