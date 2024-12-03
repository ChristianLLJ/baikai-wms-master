package com.bupt.service.outbound;

import com.bupt.DTO.ScreenDTO;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;

import java.util.List;

public interface DeliverGoodsService {
    /*
     * DeliverGoods操作
     * */
    Integer addDeliverGoods(DeliverGoods deliverGoods);

    HttpResult<?> generateDeliverGoodsAndDetail(Despatch despatch, String deliverGoodsCode);

    HttpResult<?> deleteDeliverGoods(DeliverGoods deliverGoods);

    HttpResult<?> updateDeliverGoods(DeliverGoods deliverGoods);

    List<DeliverGoods> screenDeliverGoods(ScreenDTO<DeliverGoods> screenDTO);

    Integer screenDeliverGoodsNum(ScreenDTO<DeliverGoods> screenDTO);

    /*
     * DeliverGoodsDetail 操作
     * */
    Integer addDeliverGoodsDetail(DeliverGoodsDetail deliverGoodsDetail);

    HttpResult<?> deleteDeliverGoodsDetail(DeliverGoodsDetail deliverGoodsDetail);

    HttpResult<?> updateDeliverGoodsDetail(DeliverGoodsDetail deliverGoodsDetail);

    List<DeliverGoodsDetail> screenDeliverGoodsDetail(ScreenDTO<DeliverGoodsDetail> screenDTO);

    Integer screenDeliverGoodsDetailNum(ScreenDTO<DeliverGoodsDetail> screenDTO);

}
