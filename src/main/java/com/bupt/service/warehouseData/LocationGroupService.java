package com.bupt.service.warehouseData;

import com.bupt.DTO.FrontId;
import com.bupt.DTO.ResultsAndNum;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SearchDTO;
import com.bupt.pojo.LocationGroup;

import java.util.List;

public interface LocationGroupService {
    ResultsAndNum<LocationGroup> searchLocationGroup(SearchDTO searchDTO);

    Integer addLocationGroup(LocationGroup locationGroup);

    Integer delLocationGroup(FrontId frontId);

    List<LocationGroup> selectLocationGroup(ScreenDTO<LocationGroup> screenDTO);

    Integer selectLocationGroupNum(ScreenDTO<LocationGroup> screenDTO);
}
