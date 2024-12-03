package com.bupt.service.baseData.impl;

import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SearchDTO;
import com.bupt.mapper.CustomDAO;
import com.bupt.pojo.Custom;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.baseData.CustomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.String.format;

@Service
public class CustomServiceImpl implements CustomService {
    @Autowired
    private CustomDAO customDAO;

    @Override
    public Integer addCustom(Custom custom) {
        custom.setCreateTime(new Date());
        customDAO.insertSelective(custom);
        return custom.getId();
    }

    //客户编码 COO+2位客户类型+1位客户国内外区分+4位地区号+4位流水号
    @Override
    public String generateCustomCode(String customType, String isForeign, String regionNum) {
        StringBuilder s = new StringBuilder("COO");
        String format = format("%04d", customDAO.selectMaxId());
        return s.append(customType).append(isForeign).append(regionNum).append(format).toString();
    }

    @Override
    public HttpResult<?> deleteCustom(Custom custom) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, customDAO.deleteByPrimaryKey(custom.getId()));

    }

    @Override
    public HttpResult<?> updateCustom(Custom custom) {
        
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, customDAO.updateByPrimaryKeySelective(custom));
    }

    @Override
    public List<Custom> screenCustom(ScreenDTO<Custom> screenDTO) {
        SearchDTO searchDTO = screenDTO.getSearchDTO();
        searchDTO.setPage((searchDTO.getPage() - 1) * searchDTO.getNum());
        screenDTO.setSearchDTO(searchDTO);
        List<Custom> screen = customDAO.screen(screenDTO);
        List<Custom> customList = new ArrayList<>();
        for (Custom custom : screen) {
            customList.add(customDAO.selectByPrimaryKey(custom.getId()));
        }
        return customList;
    }

    @Override
    public Integer screenCustomNum(ScreenDTO<Custom> screenDTO) {
        return customDAO.numScreen(screenDTO);

    }
}
