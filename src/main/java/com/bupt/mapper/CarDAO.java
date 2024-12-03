package com.bupt.mapper;

import com.bupt.DTO.SearchDTO;
import com.bupt.pojo.Car;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CarDAO继承基类
 */
@Mapper
@Repository
public interface CarDAO extends MyBatisBaseDao<Car, Integer> {
    List<Car> selectCar(SearchDTO searchDTO);
}
