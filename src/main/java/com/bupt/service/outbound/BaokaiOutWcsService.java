package com.bupt.service.outbound;

import com.bupt.DTO.wcs.PickResult;
import com.bupt.DTO.wcs.WcsBaokaiBatchst;
import com.bupt.pojo.InventoryBalance;
import com.bupt.pojo.PickTask;
import com.bupt.pojo.Wave;
import com.bupt.result.BaokaiHttpResult;
import com.bupt.result.HttpResult;

public interface BaokaiOutWcsService {
    //分拨设备-波次分拨状态回传接口
    BaokaiHttpResult<?> batchstStatus(WcsBaokaiBatchst wcsBaokaiBatchst);

    //分拨设备-订单分拨结果回传接口
    BaokaiHttpResult<?> pickresult(PickResult pickResult);

    //密集库出库任务推送接口
    HttpResult<?> lkcktask(PickTask pickTask);

    HttpResult<?> sendWaveToWcs(Wave wave);

    //分拨箱‘箱明细’接口
    HttpResult<?> sendBoxToWcs(PickTask pickTask);

}
