package com.bupt.service.warehouseData.impl;

import com.bupt.DTO.*;
import com.bupt.DTO.location.LocationIdAndNull;
import com.bupt.mapper.NullDAO;
import com.bupt.mapper.WarehouseLocationDAO;
import com.bupt.pojo.NullPojo;
import com.bupt.pojo.WarehouseLocation;
import com.bupt.service.BaseService;
import com.bupt.service.authority.CodeService;
import com.bupt.service.warehouseData.LocationService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

@Service
public class LocationServiceImpl extends BaseService<WarehouseLocation, NullPojo,WarehouseLocationDAO, NullDAO> implements LocationService {
    @Autowired
    WarehouseLocationDAO locationDAO;
    @Autowired
    CodeService codeService;
    /**
     * 查询所有库位
     * @param searchDTO
     * @return
     */
    @Override
    public ResultsAndNum<WarehouseLocation> searchLocation(SearchDTO searchDTO){
        searchDTO.setPage((searchDTO.getPage()-1)*searchDTO.getNum());
        ResultsAndNum<WarehouseLocation> resultsAndNum=new ResultsAndNum<>();
        resultsAndNum.setResults(locationDAO.selectWarehouseLocation(searchDTO));
        resultsAndNum.setNum(locationDAO.selectNum());
        return resultsAndNum;
    }

    /**
     * 查询库位名称和代码
     * @param name
     * @return
     */
    @Override
    public List<IdAndNameAndCode> searchLocationNameAndCode(String name) {
        return locationDAO.selectIdAndNameAndCode(name);
    }

    /**
     * 增加库位
     * @param warehouseLocation
     * @return
     */
    @Override
    public Integer addLocation(WarehouseLocation warehouseLocation){
        return locationDAO.insert(warehouseLocation);
    }

    /**
     * 删除库位
     * @param frontId
     * @return
     */
    @Override
    public Integer delLocation(FrontId frontId){
        return locationDAO.deleteByPrimaryKey(frontId.getId());
    }

    /**
     * 更新库位的库位组
     * @param frontId
     * @return
     */
    @Override
    public Integer updByLocationGroup(FrontId frontId){
        LocationIdAndNull locationIdAndNull=new LocationIdAndNull();
        locationIdAndNull.setId(frontId.getId());
        return locationDAO.updateByLocationGroupId(locationIdAndNull);
    }

    /**
     * 分页筛选库位
     * @param screenDTO
     * @return
     */
    @Override
    public List<WarehouseLocation> selectWarehouseLocation(ScreenDTO<WarehouseLocation> screenDTO) {
        if(screenDTO.getSearchDTO()!=null)
        screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage()-1)* screenDTO.getSearchDTO().getNum());
        return locationDAO.screen(screenDTO);
    }

    /**
     * 筛选库位数量
     * @param screenDTO
     * @return
     */
    @Override
    public Integer selectWarehouseLocationNum(ScreenDTO<WarehouseLocation> screenDTO) {
        return locationDAO.numScreen(screenDTO);
    }

    @SneakyThrows
    public Integer importLocation(WarehouseLocation warehouseLocation){
        Integer change =1;
        for(int i =1;i<= warehouseLocation.getLayer();i++){
            for(int j = 1;j<=warehouseLocation.getRows();j++){
                for(int k=1;k<=warehouseLocation.getColumns();k++){
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeObject(warehouseLocation);
                    oos.flush();
                    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
                    WarehouseLocation warehouseLocation1 = (WarehouseLocation) ois.readObject();
                    warehouseLocation1.setIsUsable(true);
                    warehouseLocation1.setIsLocked(false);
                    warehouseLocation1.setCreateTime(new Date());
                    warehouseLocation1.setLayer(i);
                    warehouseLocation1.setRows(j);
                    warehouseLocation1.setColumns(k);
                    warehouseLocation1.setLocationName(warehouseLocation1.getLocationName()+ new DecimalFormat("0000").format(change));
                    warehouseLocation1.setLocationCode(warehouseLocation1.getWarehouseCode()+warehouseLocation1.getAreaCode()+warehouseLocation1.getContainerCode()+ new DecimalFormat("0000").format(change));
                    locationDAO.insert(warehouseLocation1);
                    change++;
                }
            }
        }
        return warehouseLocation.getColumns()*warehouseLocation.getRows()*warehouseLocation.getLayer();
    }
}
