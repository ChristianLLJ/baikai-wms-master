package com.bupt.mapper;

import com.bupt.pojo.SysCode;
import com.bupt.pojo.SysCodeDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * SysCodeDAO继承基类
 */
@Mapper
@Repository
public interface SysCodeDAO extends MyBatisBaseDao<SysCode, Integer> {
     SysCode selectDetailByCode(String code);
}