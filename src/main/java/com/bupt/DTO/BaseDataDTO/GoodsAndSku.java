package com.bupt.DTO.BaseDataDTO;

import com.bupt.pojo.Goods;
import com.bupt.pojo.GoodsSku;
import lombok.Data;

import java.util.List;

@Data
public class GoodsAndSku {
    private Goods goods;
    private List<GoodsSku> goodsSkuList;
    Integer sumNum;

}
