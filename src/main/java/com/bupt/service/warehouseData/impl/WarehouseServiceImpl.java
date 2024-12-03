package com.bupt.service.warehouseData.impl;

import com.bupt.DTO.*;
import com.bupt.mapper.NullDAO;
import com.bupt.mapper.WarehouseDAO;
import com.bupt.pojo.NullPojo;
import com.bupt.pojo.Warehouse;
import com.bupt.service.BaseService;
import com.bupt.service.warehouseData.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseServiceImpl extends BaseService<Warehouse, NullPojo,WarehouseDAO, NullDAO> implements WarehouseService {
    @Autowired
    WarehouseDAO warehouseDAO;

    /**
     * 查询所有仓库
     * @param searchDTO
     * @return
     */
    @Override
    public ResultsAndNum<Warehouse> searchWarehouse(SearchDTO searchDTO){
        searchDTO.setPage((searchDTO.getPage()-1)*searchDTO.getNum());
        ResultsAndNum<Warehouse> resultsAndNum=new ResultsAndNum<>();
        resultsAndNum.setResults(warehouseDAO.selectWarehouse(searchDTO));
        resultsAndNum.setNum(warehouseDAO.selectNum());
        return resultsAndNum;
    }

    /**
     * 增加仓库
     * @param warehouse
     * @return
     */
    @Override
    public Integer addWarehouse(Warehouse warehouse){
        return warehouseDAO.insert(warehouse);
    }

    /**
     * 删除仓库
     * @param frontId
     * @return
     */
    @Override
    public Integer delWarehouse(FrontId frontId){
        return warehouseDAO.deleteByPrimaryKey(frontId.getId());
    }

    /**
     * 查询仓库名称和代码
     * @param name
     * @return
     */
    @Override
    public List<IdAndNameAndCode> searchAllWarehouseNameAndCode(String name) {
        return warehouseDAO.selectIdAndNameAndCode(name);
    }

    /**
     * 分页筛选仓库
     * @param screenDTO
     * @return
     */
    @Override
    public List<Warehouse> selectWarehouse(ScreenDTO<Warehouse> screenDTO) {
        if(screenDTO.getSearchDTO()!=null)
        screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage()-1)* screenDTO.getSearchDTO().getNum());
        return warehouseDAO.screen(screenDTO);
    }

    /**
     * 分页筛选仓库数量
     * @param screenDTO
     * @return
     */
    @Override
    public Integer selectWarehouseNum(ScreenDTO<Warehouse> screenDTO) {
        return warehouseDAO.numScreen(screenDTO);
    }
}

