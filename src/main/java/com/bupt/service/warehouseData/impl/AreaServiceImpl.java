package com.bupt.service.warehouseData.impl;

import com.bupt.DTO.*;
import com.bupt.DTO.area.AreaTypeIdAndNull;
import com.bupt.mapper.NullDAO;
import com.bupt.mapper.WarehouseAreaDAO;
import com.bupt.mapper.WarehouseAreaTypeDAO;
import com.bupt.pojo.NullPojo;
import com.bupt.pojo.WarehouseArea;
import com.bupt.pojo.WarehouseAreaType;
import com.bupt.service.BaseService;
import com.bupt.service.warehouseData.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaServiceImpl extends BaseService<WarehouseArea, NullPojo,WarehouseAreaDAO, NullDAO> implements AreaService {
    @Autowired
    WarehouseAreaDAO warehouseAreaDAO;
    @Autowired
    WarehouseAreaTypeDAO warehouseAreaTypeDAO;

    /**
     * 查询全部库区
     * @param searchDTO
     * @return
     */
    @Override
    public ResultsAndNum<WarehouseArea> searchWarehouseArea(SearchDTO searchDTO){
        //设置limit起始参数
        searchDTO.setPage((searchDTO.getPage()-1)*searchDTO.getNum());
        ResultsAndNum<WarehouseArea> resultsAndNum=new ResultsAndNum<>();
        //获取结果和数量
        resultsAndNum.setResults(warehouseAreaDAO.selectWarehouseArea(searchDTO));
        resultsAndNum.setNum(warehouseAreaDAO.selectNum());
        return resultsAndNum;
    }

    /**
     * 查询库区名称和代码
     * @param name
     * @return
     */
    @Override
    public List<IdAndNameAndCode> searchAreaNameAndCode(String name) {
        return warehouseAreaDAO.selectIdAndNameAndCode(name);
    }

    /**
     * 增加库区
     * @param warehouseArea
     * @return
     */
    @Override
    public Integer addArea(WarehouseArea warehouseArea){
        return warehouseAreaDAO.insert(warehouseArea);
    }

    /**
     * 查询库区类型
     * @param searchDTO
     * @return
     */
    @Override
    public ResultsAndNum<WarehouseAreaType> searchAreaType(SearchDTO searchDTO){
        //设置limit起始参数
        searchDTO.setPage((searchDTO.getPage()-1)*searchDTO.getNum());
        ResultsAndNum<WarehouseAreaType> resultsAndNum=new ResultsAndNum<>();
        //获取结果和数量
        resultsAndNum.setResults(warehouseAreaTypeDAO.selectAreaType(searchDTO));
        resultsAndNum.setNum(warehouseAreaTypeDAO.selectNum());
        return resultsAndNum;
    }

    /**
     * 增加库区类型
     * @param warehouseAreaType
     * @return
     */
    @Override
    public Integer addAreaType(WarehouseAreaType warehouseAreaType){
        return warehouseAreaTypeDAO.insert(warehouseAreaType);
    }

    /**
     * 删除库区
     * @param frontId
     * @return
     */
    @Override
    public Integer delArea(FrontId frontId){
        return warehouseAreaDAO.deleteByPrimaryKey(frontId.getId());
    }

    /**
     * 更新库区类型
     * @param frontId
     * @return
     */
    @Override
    public Integer updByAreaType(FrontId frontId){
        AreaTypeIdAndNull areaTypeIdAndNull=new AreaTypeIdAndNull();
        areaTypeIdAndNull.setId(frontId.getId());
        return warehouseAreaDAO.updByAreaType(areaTypeIdAndNull);
    }

    /**
     * 删除库区类型
     * @param frontId
     * @return
     */
    @Override
    public Integer delAreaType(FrontId frontId) {
        return warehouseAreaTypeDAO.deleteByPrimaryKey(frontId.getId());
    }

    /**
     * 分页筛选库区类型
     * @param screenDTO
     * @return
     */
    @Override
    public List<WarehouseArea> selectWarehouseArea(ScreenDTO<WarehouseArea> screenDTO) {
        //设置limit起始参数
        if(screenDTO.getSearchDTO()!=null)
        screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage()-1)* screenDTO.getSearchDTO().getNum());
        return warehouseAreaDAO.screen(screenDTO);
    }

    /**
     * 筛选库区数量
     * @param screenDTO
     * @return
     */
    @Override
    public Integer selectWarehouseAreaNum(ScreenDTO<WarehouseArea> screenDTO) {
        return warehouseAreaDAO.numScreen(screenDTO);
    }
}
