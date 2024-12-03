package com.bupt.service.baseData.impl;

import com.bupt.DTO.ResultsAndNum;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SearchDTO;
import com.bupt.mapper.ContainerDAO;
import com.bupt.pojo.Container;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.authority.CodeService;
import com.bupt.service.baseData.ContainerService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.String.format;

@Service
public class ContainerServiceImpl implements ContainerService {
    @Autowired
    private ContainerDAO containerDAO;

    @Override
    public Integer addContainer(Container container) {
        container.setCreateTime(new Date());
        containerDAO.insertSelective(container);
        container.setCode("CON"+String.format("%0" + 3 + "d",container.getId()));
        containerDAO.updateByPrimaryKeySelective(container);
        return container.getId();
    }

    //4. 包装编码 PAC+6位日期号+2位包装类型+4位流水号
    @Override
    public String generateContainerCode(String containerType) {
        StringBuilder s=new StringBuilder("PAC");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        String format = format("%04d", containerDAO.selectMaxId());
        return s.append(sdf.format(new Date())).append(containerType).append(format).toString();
    }

    @Override
    public HttpResult<?> deleteContainer(Container container) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, containerDAO.deleteByPrimaryKey(container.getId()));
    }

    @Override
    public HttpResult<?> updateContainer(Container container) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, containerDAO.updateByPrimaryKeySelective(container));
    }

    @Override
    public List<Container> screenContainer(ScreenDTO<Container> screenDTO) {
        SearchDTO searchDTO = screenDTO.getSearchDTO();
        searchDTO.setPage((searchDTO.getPage() - 1) * searchDTO.getNum());
        screenDTO.setSearchDTO(searchDTO);
        List<Container> screen = containerDAO.screen(screenDTO);
        List<Container> containerList = new ArrayList<>();
        for (Container container : screen) {
            containerList.add(containerDAO.selectByPrimaryKey(container.getId()));
        }
        return containerList;
    }

    @Override
    public Integer screenContainerNum(ScreenDTO<Container> screenDTO) {
        return containerDAO.numScreen(screenDTO);
    }
}
