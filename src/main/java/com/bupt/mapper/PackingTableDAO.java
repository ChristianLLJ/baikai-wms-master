package com.bupt.mapper;

import com.bupt.pojo.PackingTable;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * PackingTableDAO继承基类
 */
@Mapper
@Repository
public interface PackingTableDAO extends MyBatisBaseDao<PackingTable, Integer> {
    List<PackingTable> selectByPackingCode(String code);
}
