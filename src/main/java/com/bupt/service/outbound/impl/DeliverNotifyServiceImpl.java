package com.bupt.service.outbound.impl;

import com.bupt.DTO.FrontId;
import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SearchDTO;
import com.bupt.mapper.*;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.authority.CodeService;
import com.bupt.service.outbound.DeliverNotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DeliverNotifyServiceImpl implements DeliverNotifyService {
    @Autowired
    private TobDeliverNotifyDAO tobDeliverNotifyDAO;
    @Autowired
    private TobDeliverNotifyDetailDAO tobDeliverNotifyDetailDAO;
    @Autowired
    private TobNotifyDespatchDAO tobNotifyDespatchDAO;
    @Autowired
    private TocDeliverNotifyDAO tocDeliverNotifyDAO;
    @Autowired
    private TocDeliverNotifyDetailDAO tocDeliverNotifyDetailDAO;
    @Autowired
    private TocNotifyDespatchDAO tocNotifyDespatchDAO;
    @Autowired
    private DespatchDAO despatchDAO;
    @Autowired
    private DespatchDetailDAO despatchDetailDAO;
    @Autowired
    private WarehouseDAO warehouseDAO;
    @Autowired
    private GoodsSkuDAO goodsSkuDAO;
    @Autowired
    private CodeService codeService;

    @Transactional
    @Override
    public HttpResult<?> toBAdd(HeadAndDetail<TobDeliverNotify, TobDeliverNotifyDetail> headAndDetail) {
        TobDeliverNotify head = headAndDetail.getHead();
        head.setDeliverId(codeService.code("TOB"));
        head.setCreateTime(new Date());
        head.setExpectSendTime(new Date());
        head.setPlaceTime(new Date());
        tobDeliverNotifyDAO.insertSelective(head);
        headAndDetail.getDetails().forEach(e -> {
            e.setDeliverNotifyId(head.getId());
            e.setRemainNum(e.getOrderCnt());
            e.setDespatchNum(0);
            tobDeliverNotifyDetailDAO.insertSelective(e);
        });
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Transactional
    @Override
    public HttpResult<?> toBDelHeadAndDetail(Integer id) {
        tobDeliverNotifyDAO.deleteByPrimaryKey(id);
        tobDeliverNotifyDetailDAO.selectDetailByPid(id).forEach(e -> {
            tobDeliverNotifyDetailDAO.deleteByPrimaryKey(e.getId());
        });
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Transactional
    @Override
    public HttpResult<?> toBDelDetail(List<TobDeliverNotifyDetail> tobds) {
        for (TobDeliverNotifyDetail tobDeliverNotifyDetail : tobds) {
            tobDeliverNotifyDetailDAO.deleteByPrimaryKey(tobDeliverNotifyDetail.getId());
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Transactional
    @Override
    public HttpResult<?> toBUpd(HeadAndDetail<TobDeliverNotify, TobDeliverNotifyDetail> headAndDetail) {
        TobDeliverNotify head = headAndDetail.getHead();
        List<TobDeliverNotifyDetail> details = headAndDetail.getDetails();
        if (head != null) {
            tobDeliverNotifyDAO.updateByPrimaryKeySelective(head);
        }
        if (details.size() != 0) {
            details.forEach(e -> {
                e.setRemainNum(e.getOrderCnt());
                e.setDespatchNum(0);
                tobDeliverNotifyDetailDAO.updateByPrimaryKeySelective(e);
            });
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public HttpResult<?> toBAddDetail(List<TobDeliverNotifyDetail> tobDeliverNotifyDetails) {
        for (TobDeliverNotifyDetail tobDeliverNotifyDetail : tobDeliverNotifyDetails) {
            if (tobDeliverNotifyDetail.getId() != null) {
                tobDeliverNotifyDetail.setId(null);
            }
            tobDeliverNotifyDetail.setRemainNum(tobDeliverNotifyDetail.getOrderCnt());
            tobDeliverNotifyDetail.setDespatchNum(0);
            tobDeliverNotifyDetailDAO.insertSelective(tobDeliverNotifyDetail);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Transactional
    @Override
    public HttpResult<?> toBScreenNum(ScreenDTO<TobDeliverNotify> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, tobDeliverNotifyDAO.numScreen(screenDTO));
    }

    @Transactional
    @Override
    public HttpResult<?> toBScreen(ScreenDTO<TobDeliverNotify> screenDTO) {
        SearchDTO searchDTO = screenDTO.getSearchDTO();
        searchDTO.setPage((searchDTO.getPage() - 1) * searchDTO.getNum());
        screenDTO.setSearchDTO(searchDTO);
        if (screenDTO.getScreen() != null) screenDTO.setScreen(xX2x_x(screenDTO.getScreen()));
        List<TobDeliverNotify> screen = tobDeliverNotifyDAO.screen(screenDTO);
        List<TobDeliverNotify> tobDeliverNotifies = new ArrayList<>();
        for (TobDeliverNotify e : screen) {
            tobDeliverNotifies.add(tobDeliverNotifyDAO.selectByPrimaryKey(e.getId()));
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, tobDeliverNotifies);
    }

    @Transactional
    @Override
    public HttpResult<?> toBDetailScreenNum(ScreenDTO<TobDeliverNotifyDetail> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, tobDeliverNotifyDetailDAO.numScreen(screenDTO));

    }

    @Transactional
    @Override
    public HttpResult<?> toBDetailScreen(ScreenDTO<TobDeliverNotifyDetail> screenDTO) {
        SearchDTO searchDTO = screenDTO.getSearchDTO();
        searchDTO.setPage((searchDTO.getPage() - 1) * searchDTO.getNum());
        screenDTO.setSearchDTO(searchDTO);
        if (screenDTO.getScreen() != null) screenDTO.setScreen(xX2x_x(screenDTO.getScreen()));
        List<TobDeliverNotifyDetail> screen = tobDeliverNotifyDetailDAO.screen(screenDTO);
        List<TobDeliverNotifyDetail> tobDeliverNotifyDetails = new ArrayList<>();
        for (TobDeliverNotifyDetail e : screen) {
            tobDeliverNotifyDetails.add(tobDeliverNotifyDetailDAO.selectByPrimaryKey(e.getId()));
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, tobDeliverNotifyDetails);
    }

    @Transactional
    @Override
    public HttpResult<?> toBSplit(HeadAndDetail<Despatch, DespatchDetail> headAndDetail) {
        Despatch head = headAndDetail.getHead();
        List<DespatchDetail> details = headAndDetail.getDetails();
        //默认发运订单表头id为客户发货单ID
        TobDeliverNotify tobDeliverNotify = tobDeliverNotifyDAO.selectByPrimaryKey(head.getId());
        //todo 状态模式
        if (tobDeliverNotify.getStatus() == 3) return HttpResult.of(HttpResultCodeEnum.TOB_CONVERT_ERROR);
        List<TobDeliverNotifyDetail> tobDeliverNotifyDetails = tobDeliverNotifyDetailDAO.selectDetailByPid(tobDeliverNotify.getId());
        //key:skuid value：rowid
        Map<Integer, Integer> skuId = new HashMap<>();
        for (TobDeliverNotifyDetail e : tobDeliverNotifyDetails) {
            skuId.put(e.getSkuId(), e.getRowId());
        }
        for (DespatchDetail e : details) {
            Integer tobId = skuId.get(e.getSkuId()) - 1;
            TobDeliverNotifyDetail goalTob = tobDeliverNotifyDetails.get(tobId);
            Integer remainNum = goalTob.getRemainNum();
            //存在明细中不包含的sku 或者 数量超过剩余数量报错
            if (!(skuId.containsKey(e.getSkuId()) && remainNum >= e.getOrderCnt())) {
                return HttpResult.of(HttpResultCodeEnum.TOB_CONVERT_ERROR, e);
            }
            tobDeliverNotifyDetails.get(tobId).setRemainNum(remainNum - e.getOrderCnt());
            tobDeliverNotifyDetails.get(tobId).setDespatchNum(goalTob.getDespatchNum() + e.getOrderCnt());
        }
        head.setId(null);
        head.setDespatchId(codeService.code("DES"));
        head.setType((short) 2);
        despatchDAO.insertSelective(head);
        tobDeliverNotify.setStatus((short) 2);
        tobDeliverNotifyDAO.updateByPrimaryKey(tobDeliverNotify);
        TobNotifyDespatch tobNotifyDespatch = new TobNotifyDespatch();
        tobNotifyDespatch.setDespatchId(head.getId());
        tobNotifyDespatch.setTobId(tobDeliverNotify.getId());
        tobNotifyDespatchDAO.insertSelective(tobNotifyDespatch);
        int k = 1;
        for (DespatchDetail e : details) {
            e.setRowId(k++);
            GoodsSku goodsSku = goodsSkuDAO.selectByPrimaryKey(e.getSkuId());
            e.setMoney(goodsSku.getMoney());
            e.setWeight(goodsSku.getWeight());
            e.setVolume(goodsSku.getVolume());
            e.setPid(head.getId());
            despatchDetailDAO.insertSelective(e);
            Integer tobId = skuId.get(e.getSkuId()) - 1;
            tobDeliverNotifyDetailDAO.updateByPrimaryKeySelective(tobDeliverNotifyDetails.get(tobId));
        }
        Boolean tobEndTag = true;
        for (TobDeliverNotifyDetail e : tobDeliverNotifyDetails) {
            System.out.println(e.getRemainNum());
            if (tobDeliverNotifyDetailDAO.selectByPrimaryKey(e.getId()).getRemainNum() != 0) {
                tobEndTag = false;
                break;
            }
        }
        if (tobEndTag) {
            tobDeliverNotify.setStatus((short) 3);//如果全部转成发运订单，则变更状态为"全部转发运"
            int i = tobDeliverNotifyDAO.updateByPrimaryKeySelective(tobDeliverNotify);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, tobDeliverNotifyDetails);
    }

    @Override
    public HttpResult<?> endToBSplit(List<FrontId> tobIds) {
        List<TobDeliverNotify> tobDeliverNotifies = new ArrayList<>();
        Boolean tag = false;
        for (FrontId e : tobIds) {
            TobDeliverNotify tobDeliverNotify = tobDeliverNotifyDAO.selectByPrimaryKey(e.getId());
            if (tobDeliverNotify.getStatus() == 3) {
                tobDeliverNotifies.add(tobDeliverNotify);
                continue;
            }
            //todo 状态模式
            tobDeliverNotify.setStatus((short) 3);
            for (TobDeliverNotifyDetail tob : tobDeliverNotifyDetailDAO.selectDetailByPid(e.getId())) {
                if (tob.getRemainNum() != 0) {
                    tobDeliverNotifies.add(tobDeliverNotify);
                    tag = true;
                    break;
                }
            }
            if (tag) {
                tag = false;
            } else tobDeliverNotifyDAO.updateByPrimaryKey(tobDeliverNotify);
        }
        if (tobDeliverNotifies.size() != 0)
            return HttpResult.of(HttpResultCodeEnum.END_TOB_PART_FAIL, tobDeliverNotifies);
        else return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public HttpResult<?> toBGenerate(HeadAndDetail<FrontId, FrontId> warehouseAndIds) {
        List<FrontId> details = warehouseAndIds.getDetails();
        Integer tobId = warehouseAndIds.getHead().getId();
        Warehouse warehouse = warehouseDAO.selectByPrimaryKey(tobId);
        List<TobDeliverNotify> tobDeliverNotifyList = new ArrayList<>();
        for (FrontId e : details) {
            TobDeliverNotify tobDeliverNotify = tobDeliverNotifyDAO.selectByPrimaryKey(e.getId());
            if (tobDeliverNotify.getStatus() != 1) {
                tobDeliverNotifyList.add(tobDeliverNotify);
                continue;
            }
            Despatch despatch = toBToDespatch(tobDeliverNotify);
            despatch.setWarehouse(warehouse.getWarehouseCode());
            despatch.setWarehouseId(warehouse.getId());
            despatch.setWarehouseName(warehouse.getWarehouseName());
            despatch.setDespatchId(codeService.code("DES"));
            despatch.setType((short) 2);
            despatchDAO.insertSelective(despatch);
            TobNotifyDespatch tobNotifyDespatch = new TobNotifyDespatch();
            tobNotifyDespatch.setDespatchId(despatch.getId());
            tobNotifyDespatch.setTobId(e.getId());
            tobNotifyDespatchDAO.insertSelective(tobNotifyDespatch);
            int i = 1;
            for (TobDeliverNotifyDetail k : tobDeliverNotifyDetailDAO.selectDetailByPid(e.getId())) {
                DespatchDetail despatchDetail = toBDetailToDespatchDetail(k);
                despatchDetail.setPid(despatch.getId());
                despatchDetail.setRowId(i++);
                GoodsSku goodsSku=goodsSkuDAO.selectByPrimaryKey(k.getSkuId());
                despatchDetail.setWeight(goodsSku.getWeight());
                despatchDetail.setVolume(goodsSku.getVolume());
                despatchDetailDAO.insertSelective(despatchDetail);
            }
            tobDeliverNotify.setStatus((short) 3);//已生成发运订单
            tobDeliverNotifyDAO.updateByPrimaryKeySelective(tobDeliverNotify);
        }
        if (tobDeliverNotifyList.size() != 0)
            return HttpResult.of(HttpResultCodeEnum.DELIVER_CONVERT_PART_FAIL, tobDeliverNotifyList);
        else return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public HttpResult<?> findTobByDes(FrontId desId) {
        List<TobNotifyDespatch> tobDesList = tobNotifyDespatchDAO.findByDesId(desId.getId());
        List<TobDeliverNotify> tobDeliverNotifies = new ArrayList<>();
        tobDesList.forEach(e -> tobDeliverNotifies.add(tobDeliverNotifyDAO.selectByPrimaryKey(e.getTobId())));
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, tobDeliverNotifies);
    }

    @Override
    public HttpResult<?> findDesByTob(FrontId tobId) {
        List<TobNotifyDespatch> tobDesList = tobNotifyDespatchDAO.findByTobId(tobId.getId());
        List<Despatch> despatchList = new ArrayList<>();
        tobDesList.forEach(e -> despatchList.add(despatchDAO.selectByPrimaryKey(e.getDespatchId())));
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, despatchList);
    }

    @Transactional
    @Override
    public HttpResult<?> toCAdd(HeadAndDetail<TocDeliverNotify, TocDeliverNotifyDetail> headAndDetail) {
        TocDeliverNotify head = headAndDetail.getHead();
        head.setDeliverId(codeService.code("TOC"));
        head.setCreateTime(new Date());
        head.setExpectSendTime(new Date());
        head.setPlaceTime(new Date());
        tocDeliverNotifyDAO.insertSelective(head);
        headAndDetail.getDetails().forEach(e -> {
            e.setDeliverNotifyId(head.getId());
            tocDeliverNotifyDetailDAO.insertSelective(e);
        });
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Transactional
    @Override
    public HttpResult<?> toCDelHeadAndDetail(Integer id) {
        tocDeliverNotifyDAO.deleteByPrimaryKey(id);
        tocDeliverNotifyDetailDAO.selectDetailByPid(id).forEach(e -> {
            tocDeliverNotifyDetailDAO.deleteByPrimaryKey(e.getId());
        });
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Transactional
    @Override
    public HttpResult<?> toCDelDetail(List<TocDeliverNotifyDetail> tocs) {
        for (TocDeliverNotifyDetail tocDeliverNotifyDetail : tocs) {
            tocDeliverNotifyDetailDAO.deleteByPrimaryKey(tocDeliverNotifyDetail.getId());
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Transactional
    @Override
    public HttpResult<?> toCUpd(HeadAndDetail<TocDeliverNotify, TocDeliverNotifyDetail> headAndDetail) {
        TocDeliverNotify head = headAndDetail.getHead();
        List<TocDeliverNotifyDetail> details = headAndDetail.getDetails();
        if (head != null) {
            tocDeliverNotifyDAO.updateByPrimaryKeySelective(head);
        }
        if (details.size() != 0) {
            details.forEach(e -> {
                tocDeliverNotifyDetailDAO.updateByPrimaryKeySelective(e);
            });
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public HttpResult<?> toCAddDetail(List<TocDeliverNotifyDetail> tocDeliverNotifyDetails) {
        for (TocDeliverNotifyDetail tocDeliverNotifyDetail : tocDeliverNotifyDetails) {
            if (tocDeliverNotifyDetail.getId() != null) {
                tocDeliverNotifyDetail.setId(null);
            }
            tocDeliverNotifyDetailDAO.insertSelective(tocDeliverNotifyDetail);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Transactional
    @Override
    public HttpResult<?> toCScreenNum(ScreenDTO<TocDeliverNotify> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, tocDeliverNotifyDAO.numScreen(screenDTO));
    }

    @Transactional
    @Override
    public HttpResult<?> toCScreen(ScreenDTO<TocDeliverNotify> screenDTO) {
        SearchDTO searchDTO = screenDTO.getSearchDTO();
        searchDTO.setPage((searchDTO.getPage() - 1) * searchDTO.getNum());
        screenDTO.setSearchDTO(searchDTO);
        if (screenDTO.getScreen() != null) screenDTO.setScreen(xX2x_x(screenDTO.getScreen()));
        List<TocDeliverNotify> tocDeliverNotifies = new ArrayList<>();
        List<TocDeliverNotify> screen = tocDeliverNotifyDAO.screen(screenDTO);
        for (TocDeliverNotify e : screen) {
            tocDeliverNotifies.add(tocDeliverNotifyDAO.selectByPrimaryKey(e.getId()));
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, tocDeliverNotifies);
    }

    @Transactional
    @Override
    public HttpResult<?> toCDetailScreenNum(ScreenDTO<TocDeliverNotifyDetail> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, tocDeliverNotifyDetailDAO.numScreen(screenDTO));
    }

    @Transactional
    @Override
    public HttpResult<?> toCDetailScreen(ScreenDTO<TocDeliverNotifyDetail> screenDTO) {
        SearchDTO searchDTO = screenDTO.getSearchDTO();
        searchDTO.setPage((searchDTO.getPage() - 1) * searchDTO.getNum());
        screenDTO.setSearchDTO(searchDTO);
        if (screenDTO.getScreen() != null) screenDTO.setScreen(xX2x_x(screenDTO.getScreen()));
        List<TocDeliverNotifyDetail> screen = tocDeliverNotifyDetailDAO.screen(screenDTO);
        List<TocDeliverNotifyDetail> tocDeliverNotifyDetails = new ArrayList<>();
        for (TocDeliverNotifyDetail e : screen) {
            tocDeliverNotifyDetails.add(tocDeliverNotifyDetailDAO.selectByPrimaryKey(e.getId()));
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, tocDeliverNotifyDetails);
    }

    @Transactional
    @Override
    public HttpResult<?> toCGenerate(HeadAndDetail<FrontId, FrontId> warehouseAndIds) {
        List<FrontId> details = warehouseAndIds.getDetails();
        Integer tocId = warehouseAndIds.getHead().getId();
        Warehouse warehouse = warehouseDAO.selectByPrimaryKey(tocId);
        List<TocDeliverNotify> tocDeliverNotifyList = new ArrayList<>();
        for (FrontId e : details) {
            TocDeliverNotify tocDeliverNotify = tocDeliverNotifyDAO.selectByPrimaryKey(e.getId());
            if (tocDeliverNotify.getStatus() != 1) {
                tocDeliverNotifyList.add(tocDeliverNotify);
                continue;
            }
            Despatch despatch = toCToDespatch(tocDeliverNotify);
            despatch.setWarehouse(warehouse.getWarehouseCode());
            despatch.setWarehouseName(warehouse.getWarehouseName());
            despatch.setWarehouseId(warehouse.getId());
            despatch.setType((short) 3);
            despatchDAO.insertSelective(despatch);
            TocNotifyDespatch tocNotifyDespatch = new TocNotifyDespatch();
            tocNotifyDespatch.setDespatchId(despatch.getId());
            tocNotifyDespatch.setTocId(e.getId());
            tocNotifyDespatch.setUpdateDate(new Date());
            tocNotifyDespatchDAO.insertSelective(tocNotifyDespatch);
            int i = 1;
            for (TocDeliverNotifyDetail k : tocDeliverNotifyDetailDAO.selectDetailByPid(e.getId())) {
                DespatchDetail despatchDetail = toCDetailToDespatchDetail(k);
                despatchDetail.setPid(despatch.getId());
                despatchDetail.setRowId(i++);
                GoodsSku goodsSku=goodsSkuDAO.selectByPrimaryKey(k.getSkuId());
                despatchDetail.setWeight(goodsSku.getWeight());
                despatchDetail.setVolume(goodsSku.getVolume());
                despatchDetail.setMoney(goodsSku.getMoney());
                despatchDetail.setNetWeight(goodsSku.getWeight());
                despatchDetail.setUnit("件/个");
                despatchDetailDAO.insertSelective(despatchDetail);
            }
            tocDeliverNotify.setStatus((short) 3);//已全部生成发运订单
            tocDeliverNotifyDAO.updateByPrimaryKeySelective(tocDeliverNotify);
        }
        if (tocDeliverNotifyList.size() != 0)
            return HttpResult.of(HttpResultCodeEnum.DELIVER_CONVERT_PART_FAIL, tocDeliverNotifyList);
        else return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public HttpResult<?> findToCByDes(FrontId desId) {
        List<TocNotifyDespatch> tocDesList = tocNotifyDespatchDAO.findByDesId(desId.getId());
        List<TocDeliverNotify> tocDeliverNotifies = new ArrayList<>();
        tocDesList.forEach(e -> tocDeliverNotifies.add(tocDeliverNotifyDAO.selectByPrimaryKey(e.getTocId())));
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, tocDeliverNotifies);
    }

    @Override
    public HttpResult<?> findDesByToC(FrontId tocId) {
        List<TocNotifyDespatch> tocDesList = tocNotifyDespatchDAO.findByTocId(tocId.getId());
        List<Despatch> despatchList = new ArrayList<>();
        tocDesList.forEach(e -> despatchList.add(despatchDAO.selectByPrimaryKey(e.getDespatchId())));
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, despatchList);
    }

    private Despatch toBToDespatch(TobDeliverNotify toB) {
        Despatch despatch = new Despatch();
        despatch.setDespatchId(codeService.code("DES"));
        despatch.setCustomerId(toB.getCustomerId());
        despatch.setCustomerName(toB.getCustomerName());
        despatch.setAddress(toB.getAddress());
        despatch.setProvince(toB.getProvince());
        despatch.setCity(toB.getCity());
        despatch.setSite(toB.getSite());
        despatch.setExpectSendTime(toB.getExpectSendTime());
        despatch.setRequestDeliveryTime(toB.getRequestDeliveryTime());
        despatch.setReceiverId(toB.getReceiverId());
        despatch.setReceiverName(toB.getReceiverName());
        return despatch;
    }

    private Despatch toCToDespatch(TocDeliverNotify toB) {
        Despatch despatch = new Despatch();
        despatch.setDespatchId(codeService.code("DES"));
        despatch.setCustomerId(toB.getCustomerId());
        despatch.setCustomerName(toB.getCustomerName());
        despatch.setAddress(toB.getAddress());
        despatch.setProvince(toB.getProvince());
        despatch.setCity(toB.getCity());
        despatch.setSite(toB.getSite());
        despatch.setExpectSendTime(toB.getExpectSendTime());
        despatch.setRequestDeliveryTime(toB.getRequestDeliveryTime());
        despatch.setReceiverId(toB.getReceiverId());
        despatch.setReceiverName(toB.getReceiverName());
        return despatch;
    }

    private DespatchDetail toBDetailToDespatchDetail(TobDeliverNotifyDetail toBDetail) {
        DespatchDetail despatchDetail = new DespatchDetail();
        despatchDetail.setSkuId(toBDetail.getSkuId());
        despatchDetail.setSkuBarcode(toBDetail.getSkuBarcode());
        despatchDetail.setSkuCode(toBDetail.getSkuCode());
        despatchDetail.setOrderCnt(toBDetail.getOrderCnt());
        return despatchDetail;
    }

    private DespatchDetail toCDetailToDespatchDetail(TocDeliverNotifyDetail toCDetail) {
        DespatchDetail despatchDetail = new DespatchDetail();
        despatchDetail.setSkuId(toCDetail.getSkuId());
        despatchDetail.setSkuBarcode(toCDetail.getSkuBarcode());
        despatchDetail.setSkuCode(toCDetail.getSkuCode());
        despatchDetail.setOrderCnt(toCDetail.getOrderCnt());
        despatchDetail.setSkuName(toCDetail.getSkuName());
        return despatchDetail;
    }

    public String xX2x_x(String str) {
        Pattern compile = Pattern.compile("[A-Z]");
        Matcher matcher = compile.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
