package com.bupt.service.outbound.impl;

import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SearchDTO;
import com.bupt.mapper.DistributionDAO;
import com.bupt.mapper.OutboundStatisticsDAO;
import com.bupt.mapper.PrePlatformDAO;
import com.bupt.mapper.WcsInterfaceDAO;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.authority.CodeService;
import com.bupt.service.outbound.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service

public class BatchServiceImpl implements BatchService {
    @Autowired
    private PrePlatformDAO prePlatformDAO;
    @Autowired
    private WcsInterfaceDAO wcsInterfaceDAO;
    @Autowired
    private DistributionDAO distributionDAO;
    @Autowired
    private CodeService codeService;
    @Autowired
    private OutboundStatisticsDAO outboundStatisticsDAO;

    @Transactional
    @Override
    public HttpResult<?> addPrePlatform(PrePlatform prePlatform) {
        PrePlatform rePrePlatform = prePlatformDAO.selectByDistributionid(prePlatform.getDistributionId());
        if (!Objects.isNull(rePrePlatform)) return HttpResult.of(HttpResultCodeEnum.PREPLATFORM_HAVEBEEN_ADDED);
        Integer num = prePlatformDAO.selectMaxNumberByPreNum(prePlatform.getPlatformNumber());
        if (num == null) num = 1;
        else num++;
        prePlatform.setNumber(num);
        prePlatform.setPreId(codeService.code("PRE"));
        prePlatformDAO.insertSelective(prePlatform);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Transactional
    @Override
    public HttpResult<?> deletePrePlatform(PrePlatform prePlatform) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, prePlatformDAO.deleteByPrimaryKey(prePlatform.getId()));
    }

    @Transactional
    @Override
    public HttpResult<?> updatePrePlatform(PrePlatform prePlatform) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, prePlatformDAO.updateByPrimaryKeySelective(prePlatform));
    }

    @Transactional
    @Override
    public List<PrePlatform> screenPrePlatform(ScreenDTO<PrePlatform> screenDTO) {
        SearchDTO searchDTO = screenDTO.getSearchDTO();
        searchDTO.setPage((searchDTO.getPage() - 1) * searchDTO.getNum());
        screenDTO.setSearchDTO(searchDTO);
        List<PrePlatform> screen = prePlatformDAO.screen(screenDTO);
        List<PrePlatform> prePlatformList = new ArrayList<>();
        for (PrePlatform prePlatform : screen) {
            prePlatformList.add(prePlatformDAO.selectByPrimaryKey(prePlatform.getId()));
        }
        return prePlatformList;
    }

    @Transactional
    @Override
    public Integer screenPrePlatformNum(ScreenDTO<PrePlatform> screenDTO) {
        return prePlatformDAO.numScreen(screenDTO);
    }

    @Transactional
    @Override
    public Integer addWcsInterface(WcsInterface wcsInterface) {
        wcsInterfaceDAO.insertSelective(wcsInterface);
        return wcsInterface.getId();
    }

    @Transactional
    @Override
    public HttpResult<?> deleteWcsInterface(WcsInterface wcsInterface) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, wcsInterfaceDAO.deleteByPrimaryKey(wcsInterface.getId()));
    }

    @Transactional
    @Override
    public HttpResult<?> updateWcsInterface(WcsInterface wcsInterface) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, wcsInterfaceDAO.updateByPrimaryKeySelective(wcsInterface));
    }

    @Transactional
    @Override
    public List<WcsInterface> screenWcsInterface(ScreenDTO<WcsInterface> screenDTO) {
        SearchDTO searchDTO = screenDTO.getSearchDTO();
        searchDTO.setPage((searchDTO.getPage() - 1) * searchDTO.getNum());
        screenDTO.setSearchDTO(searchDTO);
        List<WcsInterface> screen = wcsInterfaceDAO.screen(screenDTO);
        List<WcsInterface> wcsInterfaceList = new ArrayList<>();
        for (WcsInterface wcsInterface : screen) {
            wcsInterfaceList.add(wcsInterfaceDAO.selectByPrimaryKey(wcsInterface.getId()));
        }
        return wcsInterfaceList;
    }

    @Transactional
    @Override
    public Integer screenWcsInterfaceNum(ScreenDTO<WcsInterface> screenDTO) {
        return wcsInterfaceDAO.numScreen(screenDTO);
    }

    @Transactional
    @Override
    public HttpResult<?> searchOutBoundStatics() {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, outboundStatisticsDAO.selectAll());
    }
}
