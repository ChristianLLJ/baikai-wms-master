package com.bupt.service.outbound;

import com.bupt.DTO.ScreenDTO;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;

import java.util.List;

public interface ExpressService {
    /*
     *ExpressDelivery 快递单 操作
     * */
    //根据配送单生成相关数据信息（收货人，体积重量等）--手动创建过程
    HttpResult<?> generateMsgByDis(Distribution distribution);

    HttpResult<?> addExpress(ExpressDelivery expressDelivery);

    //导出筛选
    List<ExpressDelivery> getHeads(ScreenDTO<ExpressDelivery> screenDTO);

    //导出自选
    List<ExpressDelivery> getHeads(List<ExpressDelivery> expressDeliveries);

}
