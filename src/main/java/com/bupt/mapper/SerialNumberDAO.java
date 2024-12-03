package com.bupt.mapper;

import com.bupt.pojo.SerialNumber;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * SerialNumberDAO继承基类
 */
@Mapper
@Repository
public interface SerialNumberDAO extends MyBatisBaseDao<SerialNumber, Integer> {
    void update(SerialNumber serialNumber);

    SerialNumber selectByName(String s);

    void updatePlusOne(Integer id);
}
