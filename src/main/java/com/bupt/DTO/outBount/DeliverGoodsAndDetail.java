package com.bupt.DTO.outBount;

import com.bupt.pojo.DeliverGoods;
import com.bupt.pojo.DeliverGoodsDetail;
import lombok.Data;

import java.util.List;

@Data
public class DeliverGoodsAndDetail {
    private DeliverGoods deliverGoods;
    private List<DeliverGoodsDetail> deliverGoodsDetailList;
}
