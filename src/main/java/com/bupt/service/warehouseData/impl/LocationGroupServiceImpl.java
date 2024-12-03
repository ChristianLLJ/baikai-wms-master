package com.bupt.service.warehouseData.impl;

import com.bupt.DTO.FrontId;
import com.bupt.DTO.ResultsAndNum;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SearchDTO;
import com.bupt.mapper.LocationGroupDAO;
import com.bupt.pojo.LocationGroup;
import com.bupt.service.warehouseData.LocationGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationGroupServiceImpl implements LocationGroupService {
    @Autowired
    LocationGroupDAO locationGroupDAO;

    /**
     * 查询所有库位组
     * @param searchDTO
     * @return
     */
    @Override
    public ResultsAndNum<LocationGroup> searchLocationGroup(SearchDTO searchDTO) {
        searchDTO.setPage((searchDTO.getPage()-1)*searchDTO.getNum());
        ResultsAndNum<LocationGroup> resultsAndNum=new ResultsAndNum<>();
        resultsAndNum.setResults(locationGroupDAO.selectLocationGroup(searchDTO));
        resultsAndNum.setNum(locationGroupDAO.selectNum());
        return resultsAndNum;
    }

    /**
     * 增加库位组
     * @param locationGroup
     * @return
     */
    @Override
    public Integer addLocationGroup(LocationGroup locationGroup){
        return locationGroupDAO.insert(locationGroup);
    }

    /**
     * 删除库位组
     * @param frontId
     * @return
     */
    @Override
    public Integer delLocationGroup(FrontId frontId){
        return locationGroupDAO.deleteByPrimaryKey(frontId.getId());
    }

    /**
     * 筛选库位组
     * @param screenDTO
     * @return
     */
    @Override
    public List<LocationGroup> selectLocationGroup(ScreenDTO<LocationGroup> screenDTO) {
        if(screenDTO.getSearchDTO()!=null)
        screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage()-1)* screenDTO.getSearchDTO().getNum());
        return locationGroupDAO.screen(screenDTO);
    }

    /**
     * 筛选库位组总数
     * @param screenDTO
     * @return
     */
    @Override
    public Integer selectLocationGroupNum(ScreenDTO<LocationGroup> screenDTO) {
        return locationGroupDAO.numScreen(screenDTO);
    }
}
