package com.bupt.service.authority.impl;

import com.bupt.DTO.*;
import com.bupt.DTO.workGroup.WorkGroupAndFunctionsId;
import com.bupt.DTO.workGroup.WorkGroupAndStaffsId;
import com.bupt.DTO.workGroup.WorkGroupAndWarehousesId;
import com.bupt.mapper.*;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.BaseService;
import com.bupt.service.authority.WorkGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class WorkGroupServiceImpl extends BaseService<WorkGroup,NullPojo,WorkGroupDAO,NullDAO> implements WorkGroupService {
    @Autowired
    WorkGroupUserRelationDAO workGroupUserRelationDAO;
    @Autowired
    WorkGroupDAO workGroupDAO;
    @Autowired
    WorkGroupFunctionRelationDAO workGroupFunctionRelationDAO;
    @Autowired
    FunctionDAO functionDAO;
    @Autowired
    StaffDAO staffDAO;
    @Autowired
    WorkGroupWarehouseRelationDAO workGroupWarehouseRelationDAO;
    @Autowired
    WarehouseDAO warehouseDAO;

    /**
     * 返回员工所在的工作组
     * @param idAndSearchDTO
     * @return
     */
    @Override
    public ResultsAndNum<WorkGroup> info(IdAndSearchDTO idAndSearchDTO){
        ResultsAndNum<WorkGroup> resultsAndNum=new ResultsAndNum<>();
        //通过员工id找到工作组id
        List<Integer> workgroupIds=workGroupUserRelationDAO.selectByStaffId(idAndSearchDTO);
        List<WorkGroup> workGroups= new LinkedList<>();
        //根据id找到工作组
        for(Integer workgroupId :workgroupIds){
            workGroups.add(workGroupDAO.selectByPrimaryKey(workgroupId));
        }
        //获取结果和数量
        resultsAndNum.setResults(workGroups);
        resultsAndNum.setNum(workGroupUserRelationDAO.selectNumByStaffId(idAndSearchDTO.getId()));
        return resultsAndNum;
    }

    /**
     * 分页查询所有的工作组
     * @param searchDTO
     * @return
     */
    @Override
    public ResultsAndNum<WorkGroup> searchWorkgroup(SearchDTO searchDTO){
        //设置limit起始参数
        searchDTO.setPage((searchDTO.getPage()-1)*searchDTO.getNum());
        ResultsAndNum<WorkGroup> resultsAndNum=new ResultsAndNum<>();
        //获取结果和数量
        resultsAndNum.setResults(workGroupDAO.selectWorkgroup(searchDTO));
        resultsAndNum.setNum(workGroupDAO.selectNum());
        return resultsAndNum;
    }

    /**
     * 增加工作组
     * @param workGroup
     * @return
     */
    @Override
    public HttpResult<?> addWorkGroup(WorkGroup workGroup){
        Date now =new Date();
        workGroup.setCreateTime(now);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,workGroupDAO.insert(workGroup));
    }

    /**
     * 删除工作组
     * @param frontId
     * @return
     */
    @Override
    public HttpResult<?> delWorkGroup(FrontId frontId){
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,workGroupDAO.deleteByPrimaryKey(frontId.getId()));
    }

    /**
     * 更新工作组
     * @param workGroup
     * @return
     */
    @Override
    public HttpResult<?> updWorkGroup(WorkGroup workGroup){
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,workGroupDAO.updateByPrimaryKeySelective(workGroup));
    }

    /**
     * 添加用户进工作组
     * @param workGroupAndStaffsId
     * @return
     */
    @Override
    public HttpResult<?> addStaffToWorkGroup(WorkGroupAndStaffsId workGroupAndStaffsId){
        WorkGroupUserRelation workGroupUserRelation=new WorkGroupUserRelation();
        workGroupUserRelation.setWorkGroupId(workGroupAndStaffsId.getWorkGroupId());
        Integer num=0;
        for(Integer staffId:workGroupAndStaffsId.getStaffIds()){
            workGroupUserRelation.setStaffId(staffId);
            workGroupUserRelationDAO.insert(workGroupUserRelation);
            num++;
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,num);
    }

    /**
     * 增加功能进工作组
     * @param workGroupAndFunctionsId
     * @return
     */
    @Override
    public HttpResult<?> addFunctionToWorkGroup(WorkGroupAndFunctionsId workGroupAndFunctionsId){
        WorkGroupFunctionRelation workGroupFunctionRelation=new WorkGroupFunctionRelation();
        workGroupFunctionRelation.setWorkGroupId(workGroupAndFunctionsId.getWorkGroupId());
        Integer num=0;
        for(Integer functionId:workGroupAndFunctionsId.getFunctionsId()){
            workGroupFunctionRelation.setFunctionId(functionId);
            workGroupFunctionRelationDAO.insert(workGroupFunctionRelation);
            num++;
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,num);
    }

    /**
     * 增加仓库进工作组
     * @param workGroupAndWarehousesId
     * @return
     * @throws DataAccessException
     */
    @Override
    public Integer addWarehouseToWorkGroup(WorkGroupAndWarehousesId workGroupAndWarehousesId) throws DataAccessException {
        WorkGroupWarehouseRelation workGroupWarehouseRelation=new WorkGroupWarehouseRelation();
        workGroupWarehouseRelation.setWorkgroupId(workGroupAndWarehousesId.getWorkGroupId());
        Integer num=0;
        for(Integer warehouseId:workGroupAndWarehousesId.getWarehousesId()){
            workGroupWarehouseRelation.setWarehouseId(warehouseId);
            workGroupWarehouseRelationDAO.insert(workGroupWarehouseRelation);
            num++;
        }
        return num;
    }

    /**
     * 查询工作组的功能
     * @param idAndSearchDTO
     * @return
     */
    @Override
    public ResultsAndNum<Function> searchWorkGroupFunction(IdAndSearchDTO idAndSearchDTO){
        ResultsAndNum<Function> resultsAndNum= new ResultsAndNum<>();
        //找到功能id
        List<Integer> functionIds=workGroupFunctionRelationDAO.selectByWorkGroupId(idAndSearchDTO);
        List<Function> functions=new LinkedList<>();
        //通过功能id找到功能
        for(Integer id:functionIds){
            functions.add(functionDAO.selectByPrimaryKey(id));
        }
        //获取结果和数量
        resultsAndNum.setResults(functions);
        resultsAndNum.setNum(workGroupFunctionRelationDAO.selectNum(idAndSearchDTO.getId()));
        return resultsAndNum;
    }

    /**
     * 查询工作组功能（不分页）
     * @param id
     * @return
     */
    @Override
    public List<Function> selectWorkGroupFunction(Integer id) {
        List<Integer> functionIds=workGroupFunctionRelationDAO.selectFunctionId(id);
        List<Function> functions=new LinkedList<>();
        //通过功能id找到功能
        for(Integer functionId:functionIds){
            functions.add(functionDAO.selectByPrimaryKey(functionId));
        }
        return functions;
    }

    /**
     * 查询工作组的仓库
     * @param idAndSearchDTO
     * @return
     */
    @Override
    public ResultsAndNum<Warehouse> searchWorkGroupWarehouse(IdAndSearchDTO idAndSearchDTO) {
        //设置limit起始参数
        idAndSearchDTO.setPage((idAndSearchDTO.getPage()-1)* idAndSearchDTO.getNum());
        ResultsAndNum<Warehouse> resultsAndNum=new ResultsAndNum<>();
        //找到仓库id
        List<WorkGroupWarehouseRelation> warehouseIds=workGroupWarehouseRelationDAO.selectByWorkGroupId(idAndSearchDTO);
        List<Warehouse> warehouses=new LinkedList<>();
        //通过仓库id找到仓库
        for(WorkGroupWarehouseRelation workGroupWarehouseRelation:warehouseIds){
            warehouses.add(warehouseDAO.selectByPrimaryKey(workGroupWarehouseRelation.getWarehouseId()));
        }
        //获取结果和数量
        resultsAndNum.setResults(warehouses);
        resultsAndNum.setNum(workGroupWarehouseRelationDAO.selectNum(idAndSearchDTO.getId()));
        return resultsAndNum;
    }

    /**
     * 查询工作组用户
     * @param idAndSearchDTO
     * @return
     */
    @Override
    public ResultsAndNum<Staff> searchWorkGroupStaff(IdAndSearchDTO idAndSearchDTO) {
        //设置limit起始参数
        idAndSearchDTO.setPage((idAndSearchDTO.getPage() - 1) * idAndSearchDTO.getNum());
        ResultsAndNum<Staff> resultsAndNum=new ResultsAndNum<>();
        //获取用户id
        List<Integer> staffIds = workGroupUserRelationDAO.selectByWorkGroupId(idAndSearchDTO);
        List<Staff> staffs = new LinkedList<>();
        //根据id找到用户
        for (Integer id : staffIds) {
            staffs.add(staffDAO.selectByPrimaryKey(id));
        }
        //获取结果和数量
        resultsAndNum.setResults(staffs);
        resultsAndNum.setNum(workGroupUserRelationDAO.selectNum(idAndSearchDTO.getId()));
        return resultsAndNum;
    }

    /**
     * 删除工作组仓库
     * @param workGroupId
     * @return
     */
    @Override
    public Integer delWorkGroupWarehouseRelation(Integer workGroupId) {
        return workGroupWarehouseRelationDAO.deleteByWorkGroupId(workGroupId);
    }

    /**
     * 删除工作组功能
     * @param workGroupId
     * @return
     */
    @Override
    public Integer delWorkGroupFunctionRelation(Integer workGroupId) {
        return workGroupFunctionRelationDAO.deleteByWorkGroupId(workGroupId);
    }

    /**
     * 删除工作组用户
     * @param workGroupId
     * @return
     */
    @Override
    public Integer delWorkGroupStaffRelation(Integer workGroupId) {
        return workGroupUserRelationDAO.deleteByWorkGroupId(workGroupId);
    }

    /**
     * 分页筛选工作组
     * @param screenDTO
     * @return
     */
    @Override
    public List<WorkGroup> selectWorkGroup(ScreenDTO<WorkGroup> screenDTO) {
        if(screenDTO.getSearchDTO()!=null)
        screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage()-1)* screenDTO.getSearchDTO().getNum());
        return workGroupDAO.screen(screenDTO);
    }

    /**
     * 分页筛选工作组数量
     * @param screenDTO
     * @return
     */
    @Override
    public Integer selectWorkGroupNum(ScreenDTO<WorkGroup> screenDTO) {
        return workGroupDAO.numScreen(screenDTO);
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public List<WorkGroupWarehouseRelation> searchWarehouseId(Integer id) {
        return workGroupWarehouseRelationDAO.selectWarehouseIds(id);
    }

}
