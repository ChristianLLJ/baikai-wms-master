package com.bupt.service.authority;

import com.bupt.DTO.*;
import com.bupt.DTO.workGroup.WorkGroupAndFunctionsId;
import com.bupt.DTO.workGroup.WorkGroupAndStaffsId;
import com.bupt.DTO.workGroup.WorkGroupAndWarehousesId;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface WorkGroupService {
    ResultsAndNum<WorkGroup> info(IdAndSearchDTO idAndSearchDTO);

    Integer delWorkGroupWarehouseRelation(Integer workGroupId);
//已废弃
    ResultsAndNum<WorkGroup> searchWorkgroup(SearchDTO searchDTO);

    ResultsAndNum<Function> searchWorkGroupFunction(IdAndSearchDTO idAndSearchDTO);

    ResultsAndNum<Staff> searchWorkGroupStaff(IdAndSearchDTO idAndSearchDTO);
//写接口
    ResultsAndNum<Warehouse> searchWorkGroupWarehouse(IdAndSearchDTO idAndSearchDTO);

    HttpResult<?> addWorkGroup(WorkGroup workGroup);

    HttpResult<?> delWorkGroup(FrontId frontId);

    HttpResult<?> updWorkGroup(WorkGroup workGroup);

    HttpResult<?> addStaffToWorkGroup(WorkGroupAndStaffsId workGroupAndStaffsId);

    HttpResult<?> addFunctionToWorkGroup(WorkGroupAndFunctionsId workGroupAndFunctionsId);

    Integer addWarehouseToWorkGroup(WorkGroupAndWarehousesId workGroupAndWarehousesId) throws DataAccessException;

    Integer delWorkGroupFunctionRelation(Integer workGroupId);

    Integer delWorkGroupStaffRelation(Integer workGroupId);

    List<WorkGroup> selectWorkGroup(ScreenDTO<WorkGroup> screenDTO);

    Integer selectWorkGroupNum(ScreenDTO<WorkGroup> screenDTO);

    List<WorkGroupWarehouseRelation> searchWarehouseId(Integer id);

    List<Function> selectWorkGroupFunction(Integer id);
}
