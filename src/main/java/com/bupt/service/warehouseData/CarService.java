package com.bupt.service.warehouseData;

import com.bupt.DTO.FrontId;
import com.bupt.DTO.ResultsAndNum;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SearchDTO;
import com.bupt.pojo.Car;
import com.bupt.pojo.Warehouse;

import java.util.List;

import java.util.List;

public interface CarService {
    ResultsAndNum<Car> searchCar(SearchDTO searchDTO);

    Integer addCar(Car car);

    Integer delCar(FrontId frontId);

    List<Car> selectCar(ScreenDTO<Car> screenDTO);

    Integer selectCarNum(ScreenDTO<Car> screenDTO);
}
