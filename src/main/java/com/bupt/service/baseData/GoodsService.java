package com.bupt.service.baseData;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.wcs.WcsSku;
import com.bupt.pojo.Goods;
import com.bupt.pojo.GoodsSku;
import com.bupt.pojo.SkuContainerBatch;
import com.bupt.result.HttpResult;

import java.util.List;

public interface GoodsService {
    /*
     * Goods表操作
     * */
    Integer addGoods(Goods goods);

    String generateGoodsCode(String bType, String type);

    HttpResult<?> deleteGoods(Goods goods);

    HttpResult<?> updateGoods(Goods goods);

    List<Goods> screenGoods(ScreenDTO<Goods> screenDTO);

    Integer screenGoodsNum(ScreenDTO<Goods> screenDTO);

    HeadAndDetail<Goods, GoodsSku> searchByBarcode(String barcode);

    Boolean goodsCodeCheck(Goods goods);

    Boolean goodsbarcodeCheck(Goods goods);

    /*
     * GoodsSku表操作
     * */
    Integer addGoodsSku(GoodsSku goodsSku);

    Boolean goodsSkuBarcodeCheck(GoodsSku goodsSku);

    Boolean goodsSkuCodeCheck(GoodsSku goodsSku);

    //2. 商品sku编码  SKU+2位品牌号+2位大类号+4位类型号+1位季节号+3位颜色号+2位尺码号
    String generateGoodsSkuCode(String goodsCode, String season, String color, String size);

    HttpResult<?> deleteGoodsAndSku(Goods goods);

    HttpResult<?> updateGoodsSku(GoodsSku goodsSku);

    List<GoodsSku> screenGoodsSku(ScreenDTO<GoodsSku> screenDTO);

    Integer screenGoodsSkuNum(ScreenDTO<GoodsSku> screenDTO);

    HttpResult<?> addList(List<SkuContainerBatch> skuContainerBatchs);

    WcsSku baokaiWcsSend(List<SkuContainerBatch> skuContainerBatchs);

}
