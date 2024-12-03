package com.bupt.mapper;

import com.bupt.DTO.SearchDTO;
import com.bupt.pojo.Function;
import com.bupt.pojo.Functions;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * FunctionsDAO继承基类
 */
@Mapper
@Repository
public interface FunctionsDAO extends MyBatisBaseDao<Functions, Integer> {

}
