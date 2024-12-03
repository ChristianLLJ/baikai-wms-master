package com.bupt.mapper;

import com.bupt.pojo.CombineStack;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CombineStackDAO继承基类
 */
@Mapper
@Repository
public interface CombineStackDAO extends MyBatisBaseDao<CombineStack, Integer> {
    List<CombineStack> selectByCombineCode(String code);
}
