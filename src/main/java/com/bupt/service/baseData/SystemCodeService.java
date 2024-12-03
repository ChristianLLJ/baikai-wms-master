package com.bupt.service.baseData;

import com.bupt.DTO.BaseDataDTO.SysCodeAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.pojo.SysCode;
import com.bupt.pojo.SysCodeDetail;
import com.bupt.result.HttpResult;

import java.util.List;

public interface SystemCodeService {
    /*
     * SysCode
     * */
    Integer addSysCode(SysCode sysCode);

    HttpResult<?> addSysCodeAndDetail(SysCode sysCode,List<SysCodeDetail> sysCodeDetailList);

    HttpResult<?> deleteSysCodeAndDetail(SysCode sysCode);
    HttpResult<?> updateSysCodeAndDetail(SysCodeAndDetail sysCodeAndDetail);

    List<SysCode> screenSysCode(ScreenDTO<SysCode> screenDTO);

    Integer screenSysCodeNum(ScreenDTO<SysCode> screenDTO);

    /*
     * SysCodeDetail
     * */
    void addSysCodeDetail(SysCodeDetail sysCodeDetail);

    List<SysCodeDetail> screenSysCodeDetail(ScreenDTO<SysCodeDetail> screenDTO);

    Integer screenSysCodeDetailNum(ScreenDTO<SysCodeDetail> screenDTO);

    HttpResult<?> selectDetailByCode(SysCode sysCode);

}
