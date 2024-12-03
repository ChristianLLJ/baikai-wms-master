package com.bupt.service.outbound.impl;

import com.bupt.DTO.AclassAndBclass;
import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SearchDTO;
import com.bupt.DTO.wcs.outbound.SkuDTO;
import com.bupt.mapper.*;
import com.bupt.pojo.*;
import com.bupt.result.BaokaiHttpResult;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.authority.CodeService;
import com.bupt.service.outbound.OutPackageService;
import com.bupt.service.util.HttpService;
import com.bupt.statePattern.despatchState.DespatchState;
import com.bupt.statePattern.waveState.WaveState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class OutpackageServiceImpl implements OutPackageService {
    @Autowired
    private ExPickingDetailDAO exPickingDetailDAO;
    @Autowired
    private ExPickingDAO exPickingDAO;
    @Autowired
    private PickTaskDAO pickTaskDAO;
    @Autowired
    private PickTaskDetailDAO pickTaskDetailDAO;
    @Autowired
    private DespatchDAO despatchDAO;
    @Autowired
    private DespatchDetailDAO despatchDetailDAO;
    @Autowired
    private DespatchWaveDAO despatchWaveDAO;
    @Autowired
    private InventoryBalanceDAO inventoryBalanceDAO;
    @Autowired
    private WaveDAO waveDAO;
    @Autowired
    private ContainerDAO containerDAO;
    @Autowired
    private GoodsSkuDAO goodsSkuDAO;
    @Autowired
    private ManualSortSeqDAO manualSortSeqDAO;
    @Autowired
    private CodeService codeService;
    @Autowired
    private HttpService httpService;
    @Value("${wcs.baokaiIp}")
    String baokaiIp;


    @Override
    public Integer addExPicking(ExPicking exPicking) {
        exPickingDAO.insertSelective(exPicking);
        return exPicking.getId();
    }

    @Override//查看已装箱信息--整+零
    public HttpResult<?> searchExpickingByDespatch(Despatch despatch) {
        AclassAndBclass<List<PickTaskDetail>, List<ExPicking>> aclassAndBclass = new AclassAndBclass<>();
        List<ExPicking> exPickings = exPickingDAO.selectByDespatchIdAndType(despatch.getId(), (short) 2);
        List<PickTaskDetail> pickTaskDetails = pickTaskDetailDAO.selectByDespatchId(despatch.getId());
        aclassAndBclass.setAclass(pickTaskDetails);
        aclassAndBclass.setBclass(exPickings);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, aclassAndBclass);
    }

    @Override
    public HttpResult<?> searchFullExpickingByDespatch(Despatch despatch) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, pickTaskDetailDAO.selectByDespatchId(despatch.getId()));
    }

    @Override
    public HttpResult<?> searchPieceExpickingByDespatch(Despatch despatch, Short type) {
        List<ExPicking> exPickings = exPickingDAO.selectByDespatchIdAndType(despatch.getId(), type);
        if (exPickings.size() == 0) {
            return HttpResult.of(HttpResultCodeEnum.EXPICKING_NOTHAVE_BEEN_ASSIGNED);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, exPickings);
    }

    @Override
    public HttpResult<?> addDetailAndSetExPicking(List<ExPickingDetail> exPickingDetailList, Despatch despatch) {
        Despatch reDespatch = despatchDAO.selectByPrimaryKey(despatch.getId());
        if (reDespatch.getStatus() != 4) {
            return HttpResult.of(HttpResultCodeEnum.EXPICKING_FAILED);
        }
        ExPicking exPicking = despatchToExPicking(reDespatch);
        exPicking.setExPickingId(codeService.code("OUP"));
        exPicking.setExPickingType((short) 1);
        exPicking.setWorkType((short) 0);//暂定
        exPicking.setWaveNumber(despatchWaveDAO.selectWaveIdByDespatchId(reDespatch.getId()));
        exPickingDAO.insertSelective(exPicking);
        int id = exPicking.getId();
        int k = 1;
        for (ExPickingDetail e : exPickingDetailList) {
            e.setExPickingDetailId(k++);
            e.setExPickingId(id);
            exPickingDetailDAO.insertSelective(e);
        }
        reDespatch.setStatus((short) 5);//装箱后状态吗更新为：装箱中，复核后改已装箱8
        despatchDAO.updateByPrimaryKeySelective(reDespatch);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, id);
    }

    @Override
    public HttpResult<?> checkSkuNum(HeadAndDetail<Despatch, DespatchDetail> e) {
        HeadAndDetail<Despatch, DespatchDetail> headAndDetail = new HeadAndDetail<>();
        List<DespatchDetail> despatchDetails = new ArrayList<>();
        List<DespatchDetail> despatchDetailList = despatchDetailDAO.selectDetailByPid(e.getHead().getId());
        for (DespatchDetail k : e.getDetails()) {
            Integer skuId = k.getSkuId();
            Integer pieceCnt = k.getPieceCnt();
            Boolean tag = false;
            for (int i = 0; i < despatchDetailList.size(); i++) {
                DespatchDetail despatchDetail = despatchDetailList.get(i);
                if (despatchDetail.getSkuId() == skuId) {
                    tag = true;
                    if (despatchDetail.getPieceCnt() != pieceCnt) {
                        k.setSkuCode(despatchDetail.getSkuCode());
                        int cnt = pieceCnt - despatchDetail.getPieceCnt();
                        k.setPieceCnt(cnt);
                        if (k.getPieceCnt() > 0) k.setChineseDescribe("SKU代码为" + despatchDetail.getSkuCode() +
                                "的商品 多 装了：" + cnt + "件（个）");
                        else k.setChineseDescribe("SKU代码为" + despatchDetail.getSkuCode() +
                                "的商品 少 装了：" + cnt + "件（个）");
                        despatchDetails.add(k);
                        despatchDetailList.remove(despatchDetail);
                        System.out.println(despatchDetailList);
                        break;
                    }
                }
            }
            if (tag) tag = false;
            else {
                k.setChineseDescribe("该订单中没有这个产品");
                despatchDetails.add(k);
            }
        }
        int size = despatchDetailList.size();
        for (int i = 0; i < size; i++) {
            DespatchDetail a = despatchDetailList.get(i);
            a.setChineseDescribe("装箱过程中未装入此产品");
            despatchDetails.add(a);
        }
        if (!despatchDetails.isEmpty()) {
            headAndDetail.setDetails(despatchDetails);
            headAndDetail.setHead(despatchDAO.selectByPrimaryKey(e.getHead().getId()));
        }
        if (despatchDetails.isEmpty()) {
            Despatch despatch = new Despatch();
            despatch.setId(e.getHead().getId());
            despatch.setStatus((short) 6);//已装箱
            despatchDAO.updateByPrimaryKeySelective(despatch);
        }
        if (headAndDetail.getHead() != null) return HttpResult.of(HttpResultCodeEnum.BOXEDNUM_WRONG, headAndDetail);
        else return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public HttpResult<?> changeType(List<ExPicking> exPickings, Short workType, Short expickingType) {
        for (ExPicking exPicking : exPickings) {
            exPicking.setExPickingType(expickingType);
            exPicking.setWorkType(workType);
            exPickingDAO.updateByPrimaryKeySelective(exPicking);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    //无拆零波次数量
    static Integer pieceNum = 0;
    static boolean piecetag = false;

    @Override
    public HttpResult<?> generateExPicking(HeadAndDetail<Container, Wave> headAndDetail) {
        while (piecetag) ;
        pieceNum = 0;
        for (Wave w : headAndDetail.getDetails()) {
            Wave wave = waveDAO.selectByPrimaryKey(w.getId());
            for (DespatchWave d : despatchWaveDAO.selectDetailByPid(w.getId())) {
                Despatch despatch = despatchDAO.selectByPrimaryKey(d.getDespatchId());
                if (despatch.getStatus() != 4 && wave.getStatus() != 2) {//4:已分拣
                    return HttpResult.of(HttpResultCodeEnum.EXPICKING_FAILED);
                }
            }
        }
        piecetag = true;
        for (Wave w : headAndDetail.getDetails()) {
            generateOneExPicking(w, headAndDetail.getHead());
        }
        piecetag = false;
        if (pieceNum != 0) return HttpResult.of(HttpResultCodeEnum.NOT_NEED_BE_BOXED,
                "共有" + pieceNum + "条波次计划单无需拆零，故无需装箱");
        else return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }


    public Integer generateOneExPicking(Wave w, Container c) {
        Wave wave = waveDAO.selectByPrimaryKey(w.getId());
        Container container = containerDAO.selectByPrimaryKey(c.getId());
        Float weight = container.getWeight();
        Float volumn = container.getVolumn();
        List<Despatch> despatchs = new ArrayList<>();
        //是否装箱标志
        Boolean tag = false;
        for (DespatchWave d : despatchWaveDAO.selectDetailByPid(wave.getId())) {
            Despatch despatch = despatchDAO.selectByPrimaryKey(d.getDespatchId());
            despatchs.add(despatch);
        }
        for (Despatch e : despatchs) {
            Container reContainer = new Container();
            reContainer.setWeight(weight);
            reContainer.setVolumn(volumn);
            Despatch despatch = new Despatch();
            despatch.setId(e.getId());
            despatch.setStatus((short) 5);
            //装一箱未完成标志
            Boolean symbol = true;
            List<DespatchDetail> despatchDetailList = despatchDetailDAO.selectDetailByPidAndPieceCnt(e.getId());
            while (true) {
                if (!symbol) {
                    reContainer.setWeight(weight);
                    reContainer.setVolumn(volumn);
                    symbol = true;
                }
                if (despatchDetailList.size() == 0) break;
                tag = true;
                ExPicking exPicking = despatchToExPicking(e);
                exPicking.setWaveNumber(wave.getId());
                exPicking.setExPickingId(codeService.code("OUP"));
                exPicking.setExPickingType((short) 2);//待装箱
                exPicking.setWorkType((short) 0);//0：未装箱，只产生装箱计划
                exPicking.setDespatchCode(e.getDespatchId());
                exPicking.setDespatchId(despatch.getId());
                exPicking.setWarehouseCode(e.getWarehouse());
                exPicking.setCargoOwnerCode(e.getCustomerName());
                Integer exPickingId = addExPicking(exPicking);
                int k = 1, limitCnt;
                ExPickingDetail exPickingDetail = new ExPickingDetail();
                Integer deleteNum = 0;
                for (int i = 0; i < despatchDetailList.size(); i++) {
                    DespatchDetail d = despatchDetailList.get(i);
                    int weightLimit = (int) Math.round(reContainer.getWeight() / d.getWeight());
                    int volumnLimit = (int) Math.round(reContainer.getVolumn() / d.getVolume());
                    limitCnt = weightLimit > volumnLimit ? volumnLimit : weightLimit;
                    int pieceCnt = d.getPieceCnt();
                    GoodsSku goodsSku = goodsSkuDAO.selectByPrimaryKey(d.getSkuId());
                    if (limitCnt <= pieceCnt && symbol) {
                        exPickingDetail.setSkuCode(d.getSkuCode());
                        exPickingDetail.setExPickingId(exPickingId);
                        exPickingDetail.setSkuCnt(limitCnt);
                        exPickingDetail.setGoodsColor(goodsSku.getSkuColor());
                        exPickingDetail.setGoodsSize(goodsSku.getSkuSize());
                        exPickingDetail.setSkuBarCode(goodsSku.getSkuBarcode());
                        exPickingDetail.setExPickingDetailId(k++);
                        exPickingDetailDAO.insertSelective(exPickingDetail);
                        symbol = false;
                        if (limitCnt == pieceCnt) deleteNum++;
                        d.setPieceCnt(pieceCnt - limitCnt);
                        despatchDetailList.set(i, d);
                    } else {
                        exPickingDetail.setSkuCode(d.getSkuCode());
                        exPickingDetail.setExPickingId(exPickingId);
                        exPickingDetail.setSkuCnt(pieceCnt);
                        exPickingDetail.setGoodsColor(goodsSku.getSkuColor());
                        exPickingDetail.setGoodsSize(goodsSku.getSkuSize());
                        exPickingDetail.setSkuBarCode(goodsSku.getSkuBarcode());
                        exPickingDetail.setExPickingDetailId(k++);
                        exPickingDetailDAO.insertSelective(exPickingDetail);
                        deleteNum++;
                        reContainer.setWeight(reContainer.getWeight() - (float) (pieceCnt * d.getWeight()));
                        reContainer.setVolumn(reContainer.getVolumn() - (float) (pieceCnt * d.getVolume()));
                    }
                }
                for (int i = 0; i < deleteNum; i++) {
                    despatchDetailList.remove(0);
                }
            }
            despatchDAO.updateByPrimaryKeySelective(despatch);
        }
        if (!tag) pieceNum++;
        w.setStatus(3);//已装箱
        waveDAO.updateByPrimaryKeySelective(w);
        return pieceNum;
    }

    @Override
    public HttpResult<?> deleteExPicking(ExPicking exPicking) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, exPickingDAO.deleteByPrimaryKey(exPicking.getId()));
    }

    @Override
    public HttpResult<?> updateExPicking(ExPicking exPicking) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, exPickingDAO.updateByPrimaryKeySelective(exPicking));
    }

    @Override
    public List<ExPicking> screenExPicking(ScreenDTO<ExPicking> screenDTO) {
        SearchDTO searchDTO = screenDTO.getSearchDTO();
        searchDTO.setPage((searchDTO.getPage() - 1) * searchDTO.getNum());
        screenDTO.setSearchDTO(searchDTO);
        List<ExPicking> screen = exPickingDAO.screen(screenDTO);
        List<ExPicking> exPickingList = new ArrayList<>();
        for (ExPicking exPicking : screen) {
            exPickingList.add(exPickingDAO.selectByPrimaryKey(exPicking.getId()));
        }
        return exPickingList;
    }

    @Override
    public Integer screenExPickingNum(ScreenDTO<ExPicking> screenDTO) {
        return exPickingDAO.numScreen(screenDTO);
    }

    @Override
    public HttpResult<?> searchByBalanceCode(PickTaskDetail pickTaskDetail) {
        //找出manualsortseq中的执行订单：需要仓库库区
        InventoryBalance inv = inventoryBalanceDAO.selectByInventoryCodeType(pickTaskDetail.getInventoryBalanceCode(), (short) 2);

        if (inv == null) return HttpResult.of(HttpResultCodeEnum.WRONG_INV_CODE);
        //逻辑上只能筛选出一条，但是还是用list防止出错 todo 拉式作业 待修改
        //List<ManualSortSeq> manualSortSeqList = manualSortSeqDAO.selectByAreaIAndStatus(inv.getAreaId(), (byte) 2);
//        if (manualSortSeqList == null || manualSortSeqList.isEmpty()) {
//            return HttpResult.of(HttpResultCodeEnum.WRONG_INV_CODE);
//        }
//        List<PickTaskDetail> p = pickTaskDetailDAO.selectDetailByBalanceCodeAndDesId
//                (pickTaskDetail.getInventoryBalanceCode(), manualSortSeqList.get(0).getDespatchId());//todo
        List<PickTaskDetail> p = pickTaskDetailDAO.selectDetailByBalanceCodeManual
                (pickTaskDetail.getInventoryBalanceCode());
        if (p == null || p.isEmpty()) return HttpResult.of(HttpResultCodeEnum.WRONG_INV_CODE);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, p.get(0));
    }

    @Override
    public HttpResult<?> searchByBalanceCodeDevice(PickTaskDetail pickTaskDetail) {
        List<PickTaskDetail> p = pickTaskDetailDAO.selectDetailByBalanceCode
                (pickTaskDetail.getInventoryBalanceCode());
        if (p == null || p.isEmpty()) return HttpResult.of(HttpResultCodeEnum.WRONG_INV_CODE);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, p.get(0));
    }

    @Override
    public HttpResult<?> endSelect(PickTaskDetail pickTaskDetail) {
        PickTaskDetail realPD = pickTaskDetailDAO.selectByPrimaryKey(pickTaskDetail.getId());
        if ((!realPD.getIsPicked()) && pickTaskDetail.getSkuCode().equals(realPD.getSkuCode())
                && pickTaskDetail.getPieceCnt() == realPD.getPieceCnt()) {
            realPD.setIsPicked(true);
            pickTaskDetailDAO.updateByPrimaryKeySelective(realPD);
            boolean tag=true;
            for (PickTaskDetail k : pickTaskDetailDAO.selectDetailByPid(realPD.getPid())) {
                if (!k.getIsPicked()) tag=false;
            }
            if (tag){
                PickTask p=pickTaskDAO.selectByPrimaryKey(realPD.getPid());
                p.setStatus((short) 3);//已分拣
                pickTaskDAO.updateByPrimaryKeySelective(p);
                Wave wave = waveDAO.selectByPrimaryKey(p.getPid());
                WaveState ws=new WaveState(6);
                wave.setStatus(ws.getCode("已拣货"));
                waveDAO.updateByPrimaryKeySelective(wave);
                despatchWaveDAO.selectDespatchIdByWaveId(wave.getId()).forEach(dw->{
                    DespatchState ds=new DespatchState(7);
                    Despatch d = despatchDAO.selectByPrimaryKey(dw.getDespatchId());
                    d.setStatus((short) ds.getCode("已拣货"));
                    despatchDAO.updateByPrimaryKeySelective(d);
                });
            }
            return HttpResult.of(HttpResultCodeEnum.SUCCESS);
        }
        return HttpResult.of(HttpResultCodeEnum.WRONG_PICK_NUM);
    }

    @Override
    public HttpResult<?> pieceSelectToEnd(PickTaskDetail pickTaskDetail) {
        PickTaskDetail realPD = pickTaskDetailDAO.selectByPrimaryKey(pickTaskDetail.getId());
        realPD.setDevicePickNum(realPD.getDevicePickNum() + 1);
        System.out.println(pickTaskDetail.getSkuCode().equals(realPD.getSkuCode()));
        if ((!realPD.getIsPicked()) && pickTaskDetail.getSkuCode().equals(realPD.getSkuCode())) {
            SkuDTO skuDTO = new SkuDTO(realPD.getSkuCode());
            if (realPD.getDevicePickNum() == realPD.getPieceCnt()) {
                realPD.setIsPicked(true);
                pickTaskDetailDAO.updateByPrimaryKeySelective(realPD);
                BaokaiHttpResult<?> b = httpService.httpResponse(baokaiIp + "/api/bkcellar/recroute/GetSku", skuDTO);
                if (b.getErrcode() != 0) throw new RuntimeException("通信异常,wcs接收问题" + b.getErrmsg());
                return HttpResult.of(HttpResultCodeEnum.HAVE_BEEN_PICK_ENOUGH, realPD);
            } else if (realPD.getDevicePickNum() < realPD.getPieceCnt()) {
                pickTaskDetailDAO.updateByPrimaryKeySelective(realPD);
                BaokaiHttpResult<?> b = httpService.httpResponse(baokaiIp + "/api/bkcellar/recroute/GetSku", skuDTO);
                if (b.getErrcode() != 0) throw new RuntimeException("通信异常,wcs接收问题" + b.getErrmsg());
                return HttpResult.of(HttpResultCodeEnum.SUCCESS, realPD);
            }
        }
        return HttpResult.of(HttpResultCodeEnum.WRONG_PICK_NUM);
    }

    @Override
    public Integer addExPickingDetail(ExPickingDetail exPickingDetail) {
        exPickingDetailDAO.insertSelective(exPickingDetail);
        return exPickingDetail.getId();
    }

    @Override
    public HttpResult<?> deleteExPickingDetail(ExPickingDetail exPickingDetail) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, exPickingDetailDAO.deleteByPrimaryKey(exPickingDetail.getId()));
    }

    @Override
    public HttpResult<?> updateExPickingDetail(ExPickingDetail exPickingDetail) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, exPickingDetailDAO.updateByPrimaryKeySelective(exPickingDetail));
    }

    @Override
    public List<ExPickingDetail> screenExPickingDetail(ScreenDTO<ExPickingDetail> screenDTO) {
        SearchDTO searchDTO = screenDTO.getSearchDTO();
        searchDTO.setPage((searchDTO.getPage() - 1) * searchDTO.getNum());
        screenDTO.setSearchDTO(searchDTO);
        List<ExPickingDetail> screen = exPickingDetailDAO.screen(screenDTO);
        List<ExPickingDetail> exPickingDetailList = new ArrayList<>();
        for (ExPickingDetail exPickingDetail : screen) {
            exPickingDetailList.add(exPickingDetailDAO.selectByPrimaryKey(exPickingDetail.getId()));
        }
        return exPickingDetailList;
    }

    @Override
    public Integer screenExPickingDetailNum(ScreenDTO<ExPickingDetail> screenDTO) {
        return exPickingDetailDAO.numScreen(screenDTO);
    }

    public ExPicking despatchToExPicking(Despatch despatch) {
        ExPicking exPicking = new ExPicking();
        if (despatch.getCreateTime() != null)
            exPicking.setBiilDate(despatch.getCreateTime());
        exPicking.setDespatchCode(despatch.getDespatchId());
        exPicking.setDespatchId(despatch.getId());
        exPicking.setCustomerId(despatch.getCustomerId());
        exPicking.setCustomerCode(despatch.getCustomerName());
        exPicking.setCustomerName(despatch.getCustomerName());
        exPicking.setReceiverId(despatch.getReceiverId());
        exPicking.setBiilDate(new Date());
        return exPicking;
    }

    public ExPickingDetail inventoryBalanceToExPickingDetail(Integer exPickingId, InventoryBalance inventoryBalance) {
        ExPickingDetail exPickingDetail = new ExPickingDetail();
        exPickingDetail.setExPickingId(exPickingId);
        exPickingDetail.setSkuCode(inventoryBalance.getSkuCode());
        exPickingDetail.setSkuCnt(inventoryBalance.getInventoryCnt());
        //生产批次
        exPickingDetail.setProductBatch(inventoryBalance.getProductBatch());
        exPickingDetail.setProductCompany(inventoryBalance.getProductCompany());
        exPickingDetail.setProductTime(inventoryBalance.getProductTime());
        return exPickingDetail;
    }
}

