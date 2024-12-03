package com.bupt.mapper;

import com.bupt.pojo.ContainerHardware;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ContainerHardwareDAO继承基类
 */
@Mapper
@Repository
public interface ContainerHardwareDAO extends MyBatisBaseDao<ContainerHardware, Integer> {
    List<ContainerHardware> selectByAreaId(Integer areaId);
}
