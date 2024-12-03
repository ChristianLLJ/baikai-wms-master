package com.bupt.service.baseData;

import com.bupt.DTO.ScreenDTO;
import com.bupt.pojo.Custom;
import com.bupt.result.HttpResult;

import java.util.List;

public interface CustomService {
    Integer addCustom(Custom custom);

    //客户编码 COO+2位客户类型+1位客户国内外区分+4位地区号+4位流水号
    String generateCustomCode(String customType,String isForeign,String regionNum);

    HttpResult<?> deleteCustom(Custom custom);

    HttpResult<?> updateCustom(Custom custom);

    List<Custom> screenCustom(ScreenDTO<Custom> screenDTO);

    Integer screenCustomNum(ScreenDTO<Custom> screenDTO);
}
