package com.bupt.mapper;

import com.bupt.pojo.PickTask;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * PickTaskDAO继承基类
 */
@Mapper
@Repository
public interface PickTaskDAO extends MyBatisBaseDao<PickTask, Integer> {
    List<PickTask> selectTaskByStatus(Short status);
    List<PickTask> selectTaskStatusAfterCreated();
}