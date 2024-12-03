package com.bupt.service.outbound;

import com.bupt.DTO.ScreenDTO;
import com.bupt.pojo.PrePlatform;
import com.bupt.pojo.WcsInterface;
import com.bupt.result.HttpResult;

import java.util.List;

public interface BatchService {

    /*
     * PrePlatform 操作
     * */
    HttpResult<?>  addPrePlatform(PrePlatform prePlatform);

    HttpResult<?> deletePrePlatform(PrePlatform prePlatform);

    HttpResult<?> updatePrePlatform(PrePlatform prePlatform);

    List<PrePlatform> screenPrePlatform(ScreenDTO<PrePlatform> screenDTO);

    Integer screenPrePlatformNum(ScreenDTO<PrePlatform> screenDTO);

    /*
     * WcsInterface 操作
     * */
    Integer addWcsInterface(WcsInterface wcsInterface);

    HttpResult<?> deleteWcsInterface(WcsInterface wcsInterface);

    HttpResult<?> updateWcsInterface(WcsInterface wcsInterface);

    List<WcsInterface> screenWcsInterface(ScreenDTO<WcsInterface> screenDTO);

    Integer screenWcsInterfaceNum(ScreenDTO<WcsInterface> screenDTO);

    /**
    * 大屏首页查看
    * */
    HttpResult<?> searchOutBoundStatics();
}
