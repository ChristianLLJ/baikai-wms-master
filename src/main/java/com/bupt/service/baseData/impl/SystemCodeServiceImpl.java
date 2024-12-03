package com.bupt.service.baseData.impl;

import com.bupt.DTO.BaseDataDTO.SysCodeAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SearchDTO;
import com.bupt.mapper.SysCodeDAO;
import com.bupt.mapper.SysCodeDetailDAO;
import com.bupt.pojo.SysCode;
import com.bupt.pojo.SysCodeDetail;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.baseData.SystemCodeService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SystemCodeServiceImpl implements SystemCodeService {
    @Autowired
    private SysCodeDAO sysCodeDAO;
    @Autowired
    private SysCodeDetailDAO sysCodeDetailDAO;
    /*@Autowired
    private*/

    @Override
    public Integer addSysCode(SysCode sysCode) {
        sysCodeDAO.insertSelective(sysCode);
        return sysCode.getId();
    }

    @Override
    public HttpResult<?> addSysCodeAndDetail(SysCode sysCode, List<SysCodeDetail> sysCodeDetailList) {
        sysCodeDAO.insertSelective(sysCode);
        int k=1;
        for (SysCodeDetail e:sysCodeDetailList){
            e.setSystemId(sysCode.getId());
            e.setTypeNumber(k++);
            sysCodeDetailDAO.insertSelective(e);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public HttpResult<?> deleteSysCodeAndDetail(@NotNull SysCode sysCode) {
        if (sysCodeDAO.selectByPrimaryKey(sysCode.getId()).getIsmodify().equals("0")) {
            return HttpResult.of(HttpResultCodeEnum.SYS_CODE_CANNOT_MODIFY);
        }
        sysCodeDAO.deleteByPrimaryKey(sysCode.getId());
        sysCodeDetailDAO.selectDetailByPid(sysCode.getId()).forEach(e -> {
            sysCodeDetailDAO.deleteByPrimaryKey(e.getId());
        });
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public HttpResult<?> updateSysCodeAndDetail(SysCodeAndDetail sysCodeAndDetail) {
        SysCode sysCode = sysCodeAndDetail.getSysCode();
        if (sysCodeDAO.selectByPrimaryKey(sysCode.getId()).getIsmodify().equals("0")) {
            return HttpResult.of(HttpResultCodeEnum.SYS_CODE_CANNOT_MODIFY);
        }
        sysCodeDAO.updateByPrimaryKeySelective(sysCode);
        sysCodeDetailDAO.selectDetailByPid(sysCodeAndDetail.getSysCode().getId()).forEach(e -> {
            sysCodeDetailDAO.deleteByPrimaryKey(e.getId());
        });
        List<SysCodeDetail> sysCodeDetailList = sysCodeAndDetail.getSysCodeDetailList();
        for (int i = 0; i < sysCodeDetailList.size(); i++) {
            SysCodeDetail e=sysCodeDetailList.get(i);
            e.setId(null);
            e.setSystemId(sysCode.getId());
            e.setTypeNumber(i+1);
            sysCodeDetailDAO.insertSelective(e);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public List<SysCode> screenSysCode(ScreenDTO<SysCode> screenDTO) {
        SearchDTO searchDTO = screenDTO.getSearchDTO();
        searchDTO.setPage((searchDTO.getPage() - 1) * searchDTO.getNum());
        screenDTO.setSearchDTO(searchDTO);
        List<SysCode> screen = sysCodeDAO.screen(screenDTO);
        List<SysCode> sysCodeList = new ArrayList<>();
        for (SysCode sysCode : screen) {
            sysCodeList.add(sysCodeDAO.selectByPrimaryKey(sysCode.getId()));
        }
        return sysCodeList;
    }

    @Override
    public Integer screenSysCodeNum(ScreenDTO<SysCode> screenDTO) {
        return sysCodeDAO.numScreen(screenDTO);
    }

    @Override
    public void addSysCodeDetail(SysCodeDetail sysCodeDetail) {
        sysCodeDetailDAO.insertSelective(sysCodeDetail);
    }

    @Override
    public List<SysCodeDetail> screenSysCodeDetail(ScreenDTO<SysCodeDetail> screenDTO) {
        SearchDTO searchDTO = screenDTO.getSearchDTO();
        searchDTO.setPage((searchDTO.getPage() - 1) * searchDTO.getNum());
        screenDTO.setSearchDTO(searchDTO);
        List<SysCodeDetail> screen = sysCodeDetailDAO.screen(screenDTO);
        List<SysCodeDetail> sysCodeDetailList = new ArrayList<>();
        for (SysCodeDetail sysCodeDetail : screen) {
            sysCodeDetailList.add(sysCodeDetailDAO.selectByPrimaryKey(sysCodeDetail.getId()));
        }
        return sysCodeDetailList;
    }

    @Override
    public Integer screenSysCodeDetailNum(ScreenDTO<SysCodeDetail> screenDTO) {
        return sysCodeDetailDAO.numScreen(screenDTO);
    }

    @Override
    public HttpResult<?> selectDetailByCode(SysCode sysCode) {
        SysCode goal = sysCodeDAO.selectDetailByCode(sysCode.getCode());
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,sysCodeDetailDAO.selectDetailByPid(goal.getId()));
    }
}
