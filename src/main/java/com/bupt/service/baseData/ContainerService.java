package com.bupt.service.baseData;

import com.bupt.DTO.ResultsAndNum;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SearchDTO;
import com.bupt.pojo.Container;
import com.bupt.result.HttpResult;

import java.util.List;

public interface ContainerService {
    Integer addContainer(Container container);

    //4. 包装编码 PAC+6位日期号+2位包装类型+4位流水号
    String generateContainerCode(String containerType);

    HttpResult<?> deleteContainer(Container container);

    HttpResult<?> updateContainer(Container container);

    List<Container> screenContainer(ScreenDTO<Container> screenDTO);

    Integer screenContainerNum(ScreenDTO<Container> screenDTO);
}
