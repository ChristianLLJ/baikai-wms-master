package com.bupt.service.outbound.impl;

import com.bupt.DTO.ScreenDTO;
import com.bupt.mapper.*;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.BaseService;
import com.bupt.service.authority.impl.CodeServiceImpl;
import com.bupt.service.outbound.ExpressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExpressServiceImpl extends BaseService<ExpressDelivery, NullPojo, ExpressDeliveryDAO, NullDAO> implements ExpressService {
    @Autowired
    private ExpressDeliveryDAO expressDeliveryDAO;
    @Autowired
    private ExpressDeliveryDetailDAO expressDeliveryDetailDAO;
    @Autowired
    private DespatchDAO despatchDAO;
    @Autowired
    private DespatchDetailDAO despatchDetailDAO;
    @Autowired
    private CustomDAO customDAO;
    @Autowired
    private GoodsSkuDAO goodsSkuDAO;
    @Autowired
    private DesDistributionsDAO desDistributionsDAO;
    @Autowired
    private InventoryBalanceDAO inventoryBalanceDAO;
    @Autowired
    private PickTaskDetailDAO pickTaskDetailDAO;
    @Autowired
    private ExPickingDAO exPickingDAO;
    @Autowired
    private DistributionDAO distributionDAO;
    @Autowired
    private ExPickingDetailDAO exPickingDetailDAO;
    @Autowired
    private CodeServiceImpl codeService;

    @Override
    public HttpResult<?> generateMsgByDis(Distribution distribution) {
        Distribution d = distributionDAO.selectByPrimaryKey(distribution.getId());
        ExpressDelivery e = new ExpressDelivery();
        e.setReceiverName(d.getReceiver());
        e.setWeight(d.getWeight());
        e.setHight(d.getHight());
        e.setLength(d.getLength());
        e.setVolume(d.getVolume());
        e.setWide(d.getWide());
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, e);
    }

    @Override
    public HttpResult<?> addExpress(ExpressDelivery expressDelivery) {
        if (expressDelivery.getExpressDeliveryId()==null) {
            expressDelivery.setExpressDeliveryId(codeService.code("EXP"));
        }
        expressDeliveryDAO.insertSelective(expressDelivery);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public List<ExpressDelivery> getHeads(ScreenDTO<ExpressDelivery> screenDTO) {
        if (screenDTO.getSearchDTO() != null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage() - 1) * screenDTO.getSearchDTO().getNum());
        return expressDeliveryDAO.screen(screenDTO);
    }

    @Override
    public List<ExpressDelivery> getHeads(List<ExpressDelivery> expressDeliveries) {
        List<ExpressDelivery> expressDeliveryList =new ArrayList<>();
        expressDeliveries.forEach(e->{
            expressDeliveryList.add(expressDeliveryDAO.selectByPrimaryKey(e.getId()));
        });
        return expressDeliveryList;
    }
}