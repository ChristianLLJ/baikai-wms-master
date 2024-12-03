package com.bupt.service.baseData.impl;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SearchDTO;
import com.bupt.DTO.wcs.WcsSku;
import com.bupt.mapper.GoodsDAO;
import com.bupt.mapper.GoodsSkuDAO;
import com.bupt.mapper.SkuContainerBatchDAO;
import com.bupt.pojo.Goods;
import com.bupt.pojo.GoodsSku;
import com.bupt.pojo.SkuContainerBatch;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.RevertService;
import com.bupt.service.authority.CodeService;
import com.bupt.service.baseData.GoodsService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.String.format;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsDAO goodsDAO;
    @Autowired
    private GoodsSkuDAO goodsSkuDAO;
    @Autowired
    private SkuContainerBatchDAO skuContainerBatchDAO;
    @Autowired
    private CodeService codeService;
    @Autowired
    RevertService revertService;

    @Override
    public Integer addGoods(Goods goods) {
        goods.setCreateTime(new Date());
        goods.setIsUsable((byte) 0);
        goodsDAO.insertSelective(goods);
        return goods.getId();
    }

    /*
     * 1. 编码 COM+2位品牌号+2位大类号+4位类型号
     * 2. 商品sku编码  SKU+2位品牌号+2位大类号+4位类型号+1位季节号+3位颜色号+2位尺码号
     * */
    @Override
    public String generateGoodsCode(String bType, String type) {
        Integer id = goodsDAO.selectMaxId();
        if (id == null) {
            id = 1;
        }
        StringBuilder s = new StringBuilder("COM");
        String idS=format("%02d", id);
        return s.append(idS).append(bType).append(type).toString();
    }

    @Override
    public HttpResult<?> deleteGoods(Goods goods) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, goodsDAO.deleteByPrimaryKey(goods.getId()));
    }

    @Override
    public HttpResult<?> updateGoods(Goods goods) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, goodsDAO.updateByPrimaryKeySelective(goods));
    }

    @Override
    public List<Goods> screenGoods(ScreenDTO<Goods> screenDTO) {
        SearchDTO searchDTO = screenDTO.getSearchDTO();
        searchDTO.setPage((searchDTO.getPage() - 1) * searchDTO.getNum());
        screenDTO.setSearchDTO(searchDTO);
        List<Goods> goodsList = goodsDAO.screen(screenDTO);
        List<Goods> goodsArrayList = new ArrayList<>();
        for (Goods goods : goodsList) {
            goodsArrayList.add(goodsDAO.selectByPrimaryKey(goods.getId()));
        }
        return goodsArrayList;
    }

    @Override
    public Integer screenGoodsNum(ScreenDTO<Goods> screenDTO) {
        return goodsDAO.numScreen(screenDTO);
    }

    @Override
    public HeadAndDetail<Goods, GoodsSku> searchByBarcode(String barcode) {
        HeadAndDetail<Goods, GoodsSku> headAndDetail=new HeadAndDetail<>();
        GoodsSku goodsSku = goodsSkuDAO.selectByBarcode(barcode);
        List<GoodsSku> goodsSkuList=new ArrayList<>();
        goodsSkuList.add(goodsSku);
        headAndDetail.setHead(goodsDAO.selectByPrimaryKey(goodsSku.getGoodsId()));
        headAndDetail.setDetails(goodsSkuList);
        return headAndDetail;
    }

    @Override
    public Boolean goodsCodeCheck(Goods goods) {
        if (goodsDAO.selectCode(goods.getCode())!=null){
            return false;
        }
        return true;
    }

    @Override
    public Boolean goodsbarcodeCheck(Goods goods) {
        if (goodsDAO.selectBarcode(goods.getBarcode())!=null){
            return false;
        }
        return true;
    }

    @Override
    public Integer addGoodsSku(@NotNull GoodsSku goodsSku) {
        goodsSku.setCreateTime(new Date());
        return goodsSkuDAO.insertSelective(goodsSku);
    }

    @Override
    public Boolean goodsSkuBarcodeCheck(GoodsSku goodsSku) {
        if (goodsSkuDAO.selectBarcode(goodsSku.getSkuBarcode())!=null){
            return false;
        }
        return true;
    }

    @Override
    public Boolean goodsSkuCodeCheck(GoodsSku goodsSku) {
        if (goodsSkuDAO.selectBySkuCode(goodsSku.getSkuCode())!=null){
            return false;
        }
        return true;
    }

    //2. 商品sku编码  SKU+2位品牌号+2位大类号+4位类型号+1位季节号+3位颜色号+2位尺码号
    @Override
    public String generateGoodsSkuCode(String goodsCode, String season, String color, String size) {
        StringBuilder s=new StringBuilder(goodsCode);
        return s.append(season).append(color).append(size).toString();
    }

    @Override
    public HttpResult<?> deleteGoodsAndSku(Goods goods) {
        goodsDAO.deleteByPrimaryKey(goods.getId());
        goodsSkuDAO.selectDetailByPid(goods.getId()).forEach(e->{
            goodsSkuDAO.deleteByPrimaryKey(e.getId());
        });
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public HttpResult<?> updateGoodsSku(GoodsSku goodsSku) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, goodsSkuDAO.updateByPrimaryKeySelective(goodsSku));
    }

    @Override
    public List<GoodsSku> screenGoodsSku(ScreenDTO<GoodsSku> screenDTO) {
        SearchDTO searchDTO = screenDTO.getSearchDTO();
        searchDTO.setPage((searchDTO.getPage() - 1) * searchDTO.getNum());
        screenDTO.setSearchDTO(searchDTO);
        List<GoodsSku> screen = goodsSkuDAO.screen(screenDTO);
        List<GoodsSku> goodsSkuList = new ArrayList<>();
        for (GoodsSku goodsSku : screen) {
            goodsSkuList.add(goodsSkuDAO.selectByPrimaryKey(goodsSku.getId()));
        }
        return goodsSkuList;
    }

    @Override
    public Integer screenGoodsSkuNum(ScreenDTO<GoodsSku> screenDTO) {
        return goodsSkuDAO.numScreen(screenDTO);
    }

    @Override
    public HttpResult<?> addList(List<SkuContainerBatch> skuContainerBatchs) {
        String skb = codeService.code("SKB");
        for (SkuContainerBatch s : skuContainerBatchs) {
            if (s.getSkuBatch() == null)
                s.setSkuBatch(skb);
            s.setArtno(s.getSkuCode());
            skuContainerBatchDAO.insertSelective(s);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public WcsSku baokaiWcsSend(List<SkuContainerBatch> skuContainerBatchs) {
        WcsSku wcsSku = new WcsSku();
        wcsSku.setReqcode("12321321413");
        wcsSku.setSkulist(new ArrayList<>());
        for (int i = 0; i < skuContainerBatchs.size(); i++) {
            SkuContainerBatch tem = skuContainerBatchDAO.selectByPrimaryKey(skuContainerBatchs.get(i).getId());
            wcsSku.getSkulist().add(revertService.RevertSkuContainerBatchToSkulist(tem));
        }
        return wcsSku;
    }
}
