package com.bupt.mapper;

import com.bupt.DTO.SearchDTO;
import com.bupt.pojo.Device;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DeviceDAO继承基类
 */
@Mapper
@Repository
public interface DeviceDAO extends MyBatisBaseDao<Device, Integer> {
    List<Device> selectDevice(SearchDTO searchDTO);
}