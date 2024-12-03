package com.bupt.service.outbound.impl;

import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SearchDTO;
import com.bupt.mapper.DeliverGoodsDAO;
import com.bupt.mapper.DeliverGoodsDetailDAO;
import com.bupt.mapper.DespatchDAO;
import com.bupt.mapper.DespatchDetailDAO;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.outbound.DeliverGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DeliverGoodsServiceImpl implements DeliverGoodsService {
    @Autowired
    private DeliverGoodsDAO deliverGoodsDAO;
    @Autowired
    private DeliverGoodsDetailDAO deliverGoodsDetailDAO;
    @Autowired
    private DespatchDAO despatchDAO;
    @Autowired
    private DespatchDetailDAO despatchDetailDAO;

    @Transactional
    @Override
    public Integer addDeliverGoods(DeliverGoods deliverGoods) {
        deliverGoodsDAO.insertSelective(deliverGoods);
        return deliverGoods.getId();
    }

    @Transactional
    @Override
    public HttpResult<?> generateDeliverGoodsAndDetail(Despatch despatch, String deliverGoodsCode) {
        DeliverGoods deliverGoods = despatchToDeliverGoods(despatchDAO.selectByPrimaryKey(despatch.getId()));
        deliverGoods.setDeliverGoodsId(deliverGoodsCode);
        List<DespatchDetail> despatchDetails = despatchDetailDAO.selectDetailByPid(despatch.getId());
        Integer deliverGoodsId = addDeliverGoods(deliverGoods);
        for (DespatchDetail despatchDetail : despatchDetails) {
            deliverGoodsDetailDAO.insertSelective(despatchDetailToDeliverGoodsDetail(despatchDetail, deliverGoodsId));
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Transactional
    @Override
    public HttpResult<?> deleteDeliverGoods(DeliverGoods deliverGoods) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, deliverGoodsDAO.deleteByPrimaryKey(deliverGoods.getId()));

    }

    @Transactional
    @Override
    public HttpResult<?> updateDeliverGoods(DeliverGoods deliverGoods) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, deliverGoodsDAO.updateByPrimaryKeySelective(deliverGoods));

    }


    @Transactional
    @Override
    public List<DeliverGoods> screenDeliverGoods(ScreenDTO<DeliverGoods> screenDTO) {
        SearchDTO searchDTO = screenDTO.getSearchDTO();
        searchDTO.setPage((searchDTO.getPage() - 1) * searchDTO.getNum());
        screenDTO.setSearchDTO(searchDTO);
        List<DeliverGoods> screen = deliverGoodsDAO.screen(screenDTO);
        List<DeliverGoods> deliverGoodsList = new ArrayList<>();
        for (DeliverGoods deliverGoods : screen) {
            deliverGoodsList.add(deliverGoodsDAO.selectByPrimaryKey(deliverGoods.getId()));
        }
        return deliverGoodsList;
    }

    @Transactional
    @Override
    public Integer screenDeliverGoodsNum(ScreenDTO<DeliverGoods> screenDTO) {
        return deliverGoodsDAO.numScreen(screenDTO);
    }

    @Transactional
    @Override
    public Integer addDeliverGoodsDetail(DeliverGoodsDetail deliverGoodsDetail) {
        deliverGoodsDetailDAO.insertSelective(deliverGoodsDetail);
        return deliverGoodsDetail.getId();
    }

    @Transactional
    @Override
    public HttpResult<?> deleteDeliverGoodsDetail(DeliverGoodsDetail deliverGoodsDetail) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, deliverGoodsDetailDAO.deleteByPrimaryKey(deliverGoodsDetail.getId()));

    }

    @Transactional
    @Override
    public HttpResult<?> updateDeliverGoodsDetail(DeliverGoodsDetail deliverGoodsDetail) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, deliverGoodsDetailDAO.updateByPrimaryKeySelective(deliverGoodsDetail));
    }


    @Transactional
    @Override
    public List<DeliverGoodsDetail> screenDeliverGoodsDetail(ScreenDTO<DeliverGoodsDetail> screenDTO) {
        SearchDTO searchDTO = screenDTO.getSearchDTO();
        searchDTO.setPage((searchDTO.getPage() - 1) * searchDTO.getNum());
        screenDTO.setSearchDTO(searchDTO);
        List<DeliverGoodsDetail> screen = deliverGoodsDetailDAO.screen(screenDTO);
        List<DeliverGoodsDetail> deliverGoodsDetailList = new ArrayList<>();
        for (DeliverGoodsDetail deliverGoodsDetail : screen) {
            deliverGoodsDetailList.add(deliverGoodsDetailDAO.selectByPrimaryKey(deliverGoodsDetail.getId()));
        }
        return deliverGoodsDetailList;
    }

    @Transactional
    @Override
    public Integer screenDeliverGoodsDetailNum(ScreenDTO<DeliverGoodsDetail> screenDTO) {
        return deliverGoodsDetailDAO.numScreen(screenDTO);
    }

    public DeliverGoods despatchToDeliverGoods(Despatch despatch) {
        DeliverGoods deliverGoods = new DeliverGoods();
        deliverGoods.setType(despatch.getType());
        deliverGoods.setStatus(despatch.getStatus());
        deliverGoods.setCustomerId(despatch.getCustomerId());
        deliverGoods.setCustomerName(despatch.getCustomerName());
        deliverGoods.setReceiverId(despatch.getReceiverId());
        deliverGoods.setCreateTime(new Date());
        deliverGoods.setRequestDeliveryTime(despatch.getRequestDeliveryTime());
        deliverGoods.setExpectSendTime(despatch.getExpectSendTime());
        deliverGoods.setWarehouse(despatch.getWarehouse());
        deliverGoods.setVerifyStatus(despatch.getVerifyStatus());
        deliverGoods.setPriority(despatch.getPriority());
        deliverGoods.setSettlerId(despatch.getSettlerId());
        deliverGoods.setCarrierId(despatch.getCarrierId());
        deliverGoods.setOrderId(despatch.getOrderId());
        return deliverGoods;
    }

    public DeliverGoodsDetail despatchDetailToDeliverGoodsDetail(DespatchDetail despatchDetail, Integer deliverGoodsId) {
        DeliverGoodsDetail deliverGoodsDetail = new DeliverGoodsDetail();
        deliverGoodsDetail.setPid(deliverGoodsId);
        deliverGoodsDetail.setRowId(despatchDetail.getRowId());
        deliverGoodsDetail.setStatus(despatchDetail.getStatus());
        deliverGoodsDetail.setSkuCode(despatchDetail.getSkuCode());
        deliverGoodsDetail.setChineseDescribe(despatchDetail.getChineseDescribe());
        deliverGoodsDetail.setEnglishDescribe(despatchDetail.getEnglishDescribe());
        deliverGoodsDetail.setOtherName(despatchDetail.getOtherName());
        deliverGoodsDetail.setUnit(despatchDetail.getUnit());
        deliverGoodsDetail.setOrderCnt(despatchDetail.getOrderCnt());
        deliverGoodsDetail.setVolume(despatchDetail.getVolume());
        deliverGoodsDetail.setWeight(despatchDetail.getWeight());
        deliverGoodsDetail.setNetWeight(despatchDetail.getNetWeight());
        deliverGoodsDetail.setMoney(despatchDetail.getMoney());
        return deliverGoodsDetail;
    }
}
