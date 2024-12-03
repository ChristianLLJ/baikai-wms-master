package com.bupt.service.warehouseData.impl;

import com.bupt.DTO.FrontId;
import com.bupt.DTO.ResultsAndNum;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SearchDTO;
import com.bupt.controller.BaseController;
import com.bupt.mapper.DeviceDAO;
import com.bupt.mapper.NullDAO;
import com.bupt.mapper.WarehouseDAO;
import com.bupt.pojo.Device;
import com.bupt.pojo.NullPojo;
import com.bupt.pojo.Warehouse;
import com.bupt.service.BaseService;
import com.bupt.service.warehouseData.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceServiceImpl extends BaseService<Device, NullPojo,DeviceDAO, NullDAO> implements DeviceService {
    @Autowired
    DeviceDAO deviceDAO;

    /**
     * 查询所有设备
     * @param searchDTO
     * @return
     */
    public ResultsAndNum<Device> searchDevice(SearchDTO searchDTO){
        searchDTO.setPage((searchDTO.getPage()-1)*searchDTO.getNum());
        ResultsAndNum<Device> resultsAndNum=new ResultsAndNum<>();
        resultsAndNum.setResults(deviceDAO.selectDevice(searchDTO));
        resultsAndNum.setNum(deviceDAO.selectNum());
        return resultsAndNum;
    }

    /**
     * 添加设备
     * @param device
     * @return
     */
    @Override
    public Integer addDevice(Device device){
        return deviceDAO.insert(device);
    }

    /**
     * 删除设备
     * @param frontId
     * @return
     */
    @Override
    public Integer delDevice(FrontId frontId) {
        return deviceDAO.deleteByPrimaryKey(frontId.getId());
    }

    /**
     * 筛选设备
     * @param screenDTO
     * @return
     */
    @Override
    public List<Device> selectDevice(ScreenDTO<Device> screenDTO) {
        if(screenDTO.getSearchDTO()!=null)
        screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage()-1)* screenDTO.getSearchDTO().getNum());
        return deviceDAO.screen(screenDTO);
    }

    /**
     * 筛选设备数量
     * @param screenDTO
     * @return
     */
    @Override
    public Integer selectDeviceNum(ScreenDTO<Device> screenDTO) {
        return deviceDAO.numScreen(screenDTO);
    }
}
