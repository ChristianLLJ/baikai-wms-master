package com.bupt.service.warehouseData;

import com.bupt.DTO.FrontId;
import com.bupt.DTO.ResultsAndNum;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SearchDTO;
import com.bupt.pojo.Device;
import com.bupt.pojo.Warehouse;

import java.util.List;

public interface DeviceService {
    ResultsAndNum<Device> searchDevice(SearchDTO searchDTO);

    Integer addDevice(Device device);

    Integer delDevice(FrontId frontId);

    List<Device> selectDevice(ScreenDTO<Device> screenDTO);

    Integer selectDeviceNum(ScreenDTO<Device> Device);
}
