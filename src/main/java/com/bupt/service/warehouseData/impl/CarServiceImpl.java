package com.bupt.service.warehouseData.impl;

import com.bupt.DTO.FrontId;
import com.bupt.DTO.ResultsAndNum;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SearchDTO;
import com.bupt.mapper.CarDAO;
import com.bupt.mapper.NullDAO;
import com.bupt.mapper.WarehouseDAO;
import com.bupt.pojo.Car;
import com.bupt.pojo.NullPojo;
import com.bupt.pojo.Warehouse;
import com.bupt.service.BaseService;
import com.bupt.service.warehouseData.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarServiceImpl extends BaseService<Car, NullPojo,CarDAO, NullDAO> implements CarService {
    @Autowired
    CarDAO carDAO;

    /**
     * 查询所有车辆
     * @param searchDTO
     * @return
     */
    @Override
    public ResultsAndNum<Car> searchCar(SearchDTO searchDTO){
        //设置limit参数
        searchDTO.setPage((searchDTO.getPage()-1)*searchDTO.getNum());
        ResultsAndNum<Car> resultsAndNum=new ResultsAndNum<>();
        //获取结果和数量
        resultsAndNum.setResults(carDAO.selectCar(searchDTO));
        resultsAndNum.setNum(carDAO.selectNum());
        return resultsAndNum;
    }

    /**
     * 添加车辆
     * @param car
     * @return
     */
    @Override
    public Integer addCar(Car car){
        return carDAO.insert(car);
    }

    /**
     * 删除车辆
     * @param frontId
     * @return
     */
    @Override
    public Integer delCar(FrontId frontId) {
        return carDAO.deleteByPrimaryKey(frontId.getId());
    }

    /**
     * 筛选车辆
     * @param screenDTO
     * @return
     */
    @Override
    public List<Car> selectCar(ScreenDTO<Car> screenDTO) {
        if(screenDTO.getSearchDTO()!=null)
        screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage()-1)* screenDTO.getSearchDTO().getNum());
        return carDAO.screen(screenDTO);
    }

    /**
     * 筛选车辆数量
     * @param screenDTO
     * @return
     */
    @Override
    public Integer selectCarNum(ScreenDTO<Car> screenDTO) {
        return carDAO.numScreen(screenDTO);
    }
}
