package com.bupt.service.outbound.impl;

import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SearchDTO;
import com.bupt.mapper.*;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.BaseService;
import com.bupt.service.authority.impl.CodeServiceImpl;
import com.bupt.service.outbound.DistributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class DistributionServiceImpl extends BaseService<Distribution, NullPojo, DistributionDAO, NullDAO> implements DistributionService {
    @Autowired
    private DistributionDAO distributionDAO;
    @Autowired
    private DistributionDetailDAO distributionDetailDAO;
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
    private ExPickingDetailDAO exPickingDetailDAO;
    @Autowired
    private CodeServiceImpl codeService;

    @Override
    public HttpResult<?> addDistribution(List<Distribution> distributions) {

        distributions.forEach(e -> {
            ExPicking exPicking = exPickingDAO.selectByPrimaryKey(e.getExPickingId());
            List<ExPickingDetail> exPickingDetails = exPickingDetailDAO.selectDetailByPid(exPicking.getId());
            Despatch despatch = despatchDAO.selectByPrimaryKey(exPicking.getDespatchId());
            if (e.getDistributionId() == null) {
                e.setDistributionId(codeService.code("DIS"));
            }
            e.setStatus((short) 1);//已创建 todo --状态模式
            e.setExPickingCode(exPicking.getExPickingId());
            e.setDespatchId(exPicking.getDespatchId());
            e.setDespatchCode(exPicking.getDespatchCode());
            e.setReceiver(despatch.getReceiverName());
            e.setReceiverId(despatch.getReceiverId());
            e.setReceiverProvince(despatch.getProvince());
            e.setReceiverCity(despatch.getCity());
            e.setReceiverSite(despatch.getSite());
            e.setReceiverAddress(despatch.getAddress());
            distributionDAO.insertSelective(e);
        });
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public List<Distribution> getHeads(ScreenDTO<Distribution> screenDTO) {
        if (screenDTO.getSearchDTO() != null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage() - 1) * screenDTO.getSearchDTO().getNum());
        return distributionDAO.screen(screenDTO);
    }

    @Override
    public List<Distribution> getHeads(List<Distribution> distributions) {
        List<Distribution> distributionList=new ArrayList<>();
        distributions.forEach(e->{
            distributionList.add(distributionDAO.selectByPrimaryKey(e.getId()));
        });
        return distributionList;
    }
}
