package com.bupt.service;

import com.bupt.DTO.wcs.Skulist;
import com.bupt.mapper.ContainerDAO;
import com.bupt.mapper.GoodsSkuDAO;
import com.bupt.pojo.Container;
import com.bupt.pojo.GoodsSku;
import com.bupt.pojo.SkuContainerBatch;
import com.bupt.service.authority.CodeService;
import com.bupt.service.baseData.ContainerService;
import com.bupt.service.baseData.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RevertService {
    @Autowired
    GoodsSkuDAO goodsSkuDAO;
    @Autowired
    ContainerDAO containerDAO;
    @Autowired
    ContainerService containerService;
    @Autowired
    CodeService codeService;
    public Skulist RevertSkuContainerBatchToSkulist(SkuContainerBatch skuContainerBatch){
        Skulist skulist = new Skulist();
        GoodsSku goodsSku = goodsSkuDAO.selectByPrimaryKey(skuContainerBatch.getSkuId());
        Container container = containerDAO.selectByPrimaryKey(skuContainerBatch.getContainerId());
        skulist.setSku(goodsSku.getSkuCode());
        skulist.setArtno(goodsSku.getSkuCode());
        skulist.setBvol(container.getVolumn());
        skulist.setColor(goodsSku.getSkuColor());
        skulist.setCtn(skuContainerBatch.getCtn());
        skulist.setMatkl(goodsSku.getSkuBrand());
        skulist.setSeason(goodsSku.getSkuSeason());
        skulist.setName(goodsSku.getSkuName());
        skulist.setSvol(goodsSku.getVolume());
        skulist.setYeas(goodsSku.getYear());
        return skulist;
    }
}
