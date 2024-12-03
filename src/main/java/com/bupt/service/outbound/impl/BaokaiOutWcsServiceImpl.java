package com.bupt.service.outbound.impl;

import com.bupt.DTO.wcs.*;
import com.bupt.mapper.*;
import com.bupt.pojo.*;
import com.bupt.result.BaokaiHttpResult;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.authority.CodeService;
import com.bupt.service.outbound.BaokaiOutWcsService;
import com.bupt.service.util.HttpService;
import com.bupt.statePattern.despatchState.DespatchState;
import com.bupt.statePattern.waveState.WaveState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class BaokaiOutWcsServiceImpl implements BaokaiOutWcsService {
    @Autowired
    private WaveDAO waveDAO;
    @Autowired
    private DespatchDAO despatchDAO;
    @Autowired
    private DespatchDetailDAO despatchDetailDAO;
    @Autowired
    private DespatchWaveDAO despatchWaveDAO;
    @Autowired
    private PickTaskDAO pickTaskDAO;
    @Autowired
    private PickTaskDetailDAO pickTaskDetailDAO;
    @Autowired
    private ExPickingDAO exPickingDAO;
    @Autowired
    private ExPickingDetailDAO exPickingDetailDAO;
    @Autowired
    private InventoryBalanceDAO inventoryBalanceDAO;
    @Autowired
    private GoodsSkuDAO goodsSkuDAO;
    @Autowired
    private WarehouseLocationDAO warehouseLocationDAO;
    @Autowired
    private CodeService codeService;
    @Autowired
    private HttpService httpService;
    @Value("${wcs.baokaiIp}")
    String baokaiIp;

    @Override
    public BaokaiHttpResult<?> batchstStatus(WcsBaokaiBatchst wcsBaokaiBatchst) {
        if (wcsBaokaiBatchst.getStatus() == 60) {
            Wave wave = waveDAO.selectByCode(wcsBaokaiBatchst.getBatchid());
            WaveState waveState = new WaveState(4);
            if (wave.getStatus() != waveState.getCode("已发送WCS分拣任务")) {
                return BaokaiHttpResult.of(HttpResultCodeEnum.WAVE_STATUS_WRONG_PICKING);
            }
            //第一次更新状态，第二次不更新
            wave.setStatus(waveState.getCode("拣货中"));
            waveDAO.updateByPrimaryKeySelective(wave);
            DespatchState despatchState = new DespatchState(4);
            despatchWaveDAO.selectDespatchIdByWaveId(wave.getId()).forEach(e -> {
                Despatch despatch = despatchDAO.selectByPrimaryKey(e.getDespatchId());
                despatch.setStatus((short) despatchState.getCode("拣货中"));
                despatchDAO.updateByPrimaryKeySelective(despatch);
            });
            PickTask pickTask = pickTaskDAO.selectDetailByPid(wave.getId()).get(0);
            pickTask.setStatus((short) 2);
            pickTaskDAO.updateByPrimaryKeySelective(pickTask);
        } else if (wcsBaokaiBatchst.getStatus() == 90) {
            Wave wave = waveDAO.selectByCode(wcsBaokaiBatchst.getBatchid());
            WaveState waveState = new WaveState(5);
            //交叉带分拣完，如果整箱和人工也装完怎状态变为 --"已拣货"
            if (wave.getStatus() > waveState.getCode("已拣货")) {
                return BaokaiHttpResult.of(HttpResultCodeEnum.WAVE_STATUS_WRONG_PICKED);
            }
            if (!checkPickingStatus(wave)) {
                return BaokaiHttpResult.of(HttpResultCodeEnum.NOT_PICKED_ENOUGH);
            }
            wave.setStatus(waveState.getCode("已拣货"));
            waveDAO.updateByPrimaryKeySelective(wave);
            DespatchState despatchState = new DespatchState(5);
            despatchWaveDAO.selectDespatchIdByWaveId(wave.getId()).forEach(e -> {
                Despatch despatch = despatchDAO.selectByPrimaryKey(e.getDespatchId());
                despatch.setStatus((short) despatchState.getCode("已拣货"));
                despatchDAO.updateByPrimaryKey(despatch);
            });
            PickTask pickTask = pickTaskDAO.selectDetailByPid(wave.getId()).get(0);
            pickTask.setStatus((short) 3);
            pickTaskDAO.updateByPrimaryKeySelective(pickTask);

        }
        return BaokaiHttpResult.of(new Reqcode("12321321413"));
    }

    @Override
    public BaokaiHttpResult<?> pickresult(PickResult pickResult) {
        Wave wave = waveDAO.selectByCode(pickResult.getBatchid());
        WaveState waveState = new WaveState(6);
        if (wave.getStatus() > waveState.getCode("已拣货")) {
            return BaokaiHttpResult.of(HttpResultCodeEnum.WAVE_STATUS_WRONG_PICKED);
        }
        for (Picklist e : pickResult.getPicklist()) {
            Despatch d = despatchDAO.selectByDespatchId(e.getOrderid());
            for (Detail k : e.getDetail()) {
                DespatchDetail dd = despatchDetailDAO.selectByPidAndSkuCode(d.getId(), k.getSku());
                if (dd == null) {
                    throw new RuntimeException(HttpResultCodeEnum.DESPATCH_SKU_WRONG.getMsg() +
                            "发运订单：" + d.getDespatchId() + "--sku" + k.getSku());
                }
                if ((dd.getTakeCnt() + k.getQty()) > dd.getOrderCnt()) {
                    throw new RuntimeException(HttpResultCodeEnum.DESPATCH_CNT_OVER.getMsg() +
                            "发运订单：" + d.getDespatchId() + "--sku" + k.getSku());
                }
                dd.setTakeCnt(dd.getTakeCnt() + k.getQty());
                despatchDetailDAO.updateByPrimaryKey(dd);
            }
        }
        boolean tag = true;
        PickTask pickTask = pickTaskDAO.selectDetailByPid(wave.getId()).get(0);
        for (PickTaskDetail k : pickTaskDetailDAO.selectDetailByPid(pickTask.getId())) {
            if (!k.getIsPicked()) tag = false;
        }
        if (tag) {
            pickTask.setStatus((short) 3);//已分拣
            pickTaskDAO.updateByPrimaryKeySelective(pickTask);
            wave.setStatus(6);
            waveDAO.updateByPrimaryKeySelective(wave);
            despatchWaveDAO.selectDespatchIdByWaveId(wave.getId()).forEach(dw -> {
                Despatch d = despatchDAO.selectByPrimaryKey(dw.getDespatchId());
                d.setStatus((short) 5);
                despatchDAO.updateByPrimaryKeySelective(d);
            });
        }
        return BaokaiHttpResult.of(0, "成功", new Reqcode("12321321413"));
    }

    @Override
    public HttpResult<?> lkcktask(PickTask pickTask) {
        List<PickTaskDetail> pickTaskDetails = pickTaskDetailDAO.selectDetailByPid(pickTask.getId());
        List<BaokaiHttpResult<?>> baokaiHttpResults = new ArrayList<>();
        for (PickTaskDetail pd : pickTaskDetails) {
            Lkcktask lkcktask = new Lkcktask();
            lkcktask.setOrigination(pd.getLocationCode());
            if (pd.getFullTag() && !pd.getPickupTag()) {//整箱直接运走，同属于一个顾客
                lkcktask.setTarget("CK");
                lkcktask.setIstotal("1");
                lkcktask.setQty(pd.getInboxCnt());
            } else if (pd.getPieceDevice().startsWith("人工")) {
                lkcktask.setTarget("RG");//拆零单，需到人工拆包处，拆完后重新上架
                lkcktask.setIstotal("0");
                lkcktask.setQty(pd.getPieceCnt());
            } else {
                lkcktask.setTarget("FJ");
                lkcktask.setIstotal("1");
                lkcktask.setQty(pd.getPieceCnt());
            }
            lkcktask.setPalletid(pd.getInventoryBalanceCode());
            lkcktask.setReqcode(pd.getId().toString());
            BaokaiHttpResult<?> b = httpService.httpResponse(baokaiIp + "/api/bkcellar/recroute/lkcktask", lkcktask);
            baokaiHttpResults.add(b);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, baokaiHttpResults);
    }

    @Override
    public HttpResult<?> sendWaveToWcs(Wave wave) {
        Wave w = waveDAO.selectByPrimaryKey(wave.getId());
        WaveState ws = new WaveState(3);
        w.setStatus(ws.getCode("已发送WCS分拣任务"));
        waveDAO.updateByPrimaryKeySelective(w);
        Batchst batchst = new Batchst();
        batchst.setBatchid(w.getWaveId());
        batchst.setDevid("JXD02");//todo 待修改商议
        batchst.setBatchtype("0" + w.getDiscribe());//暂定01：B2C; 02：B2B
        batchst.setReqcode("12321321413");//todo 指令号
        batchst.setSign("1");//按订单分拨
        List<Orderlist> orderlists = new ArrayList<>();
        for (DespatchWave dw : despatchWaveDAO.selectDespatchIdByWaveId(wave.getId())) {
            System.out.println(dw);
            Despatch des = despatchDAO.selectByPrimaryKey(dw.getDespatchId());
            System.out.println(des);
            Orderlist o = new Orderlist();
            o.setOrderid(des.getDespatchId());
            if (des.getStatus() > 20) o.setCancelflag("Y");
            else o.setCancelflag("N");//订单取消标志，N
            //orderlist 中的detail数组
            List<Detail> details = new ArrayList<>();
            for (DespatchDetail desd : despatchDetailDAO.selectDetailByPid(des.getId())) {
                if (!desd.getIsMergePiece()) continue;
                Detail detail = new Detail();
                detail.setQty(desd.getPieceCnt());
                detail.setSku(desd.getSkuCode());
                if (!(detail.getQty() == 0 || detail.getQty() == null))
                    details.add(detail);
            }
            if (!details.isEmpty()) {
                o.setDetail(details);
                o.setDmtf(w.getWaveId());
                orderlists.add(o);
            }
            DespatchState ds = new DespatchState(3);
            des.setStatus((short) ds.getCode("已发送WCS分拣任务"));
            despatchDAO.updateByPrimaryKeySelective(des);
        }
        if (!orderlists.isEmpty()) {
            batchst.setOrderlist(orderlists);
            BaokaiHttpResult<?> baokaiHttpResult = httpService.httpResponse(baokaiIp + "/api/bkcellar/recroute/batchst", batchst);
            int errcode = baokaiHttpResult.getErrcode() == 0 ? 200 : baokaiHttpResult.getErrcode();
            return HttpResult.of(errcode, baokaiHttpResult.getErrmsg(), baokaiHttpResult.getData());
        }
        return HttpResult.of(HttpResultCodeEnum.NOT_PICKTASK);
    }

    @Override
    public HttpResult<?> sendBoxToWcs(PickTask pickTask) {
        PickTask pick = pickTaskDAO.selectByPrimaryKey(pickTask.getId());
        Ckpickdetail ckpickdetail = new Ckpickdetail();
        ckpickdetail.setBatchid(pick.getWaveCode());
        ckpickdetail.setReqcode("12321321413");
        List<Detail> detaiList = new ArrayList<>();
        List<BaokaiHttpResult<?>> baokaiHttpResults = new ArrayList<>();
        for (PickTaskDetail pd : pickTaskDetailDAO.selectDetailByPid(pickTask.getId())) {
            if (!(pd.getFullTag() && !pd.getPickupTag())) {
                Detail detail = new Detail();
                InventoryBalance inv = inventoryBalanceDAO.selectByPrimaryKey(pd.getInventoryBalanceId());
                ckpickdetail.setCartonid(inv.getBalanceCode());
                detail.setSku(pd.getSkuCode());
                detail.setQty(pd.getInboxCnt());
                detaiList.add(detail);
                ckpickdetail.setDetail(detaiList);
                BaokaiHttpResult<?> b = httpService.httpResponse(baokaiIp + "/api/bkcellar/recroute/ckpickdetail", ckpickdetail);
                baokaiHttpResults.add(b);
            }
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, baokaiHttpResults);
    }

    //根据wcs发送的信息，生成装箱单表头表细-以发运订单为单位，一个发运订单暂定为一箱
    //留用
    private void getExpicking(Wave wave, Picklist picklist) {
        Despatch des = despatchDAO.selectByDespatchId(picklist.getOrderid());
        ExPicking exPickingOld = exPickingDAO.selectByDespatchId(des.getId());
        ExPicking exPicking = new ExPicking();
        if (exPickingOld != null) {//如果存在，则无需新建表头
            exPicking = exPickingOld;
        } else {
            exPicking.setCustomerId(des.getCustomerId());
            exPicking.setCustomerName(des.getCustomerName());
            exPicking.setContainerId(des.getCarrierId());
            exPicking.setExPickingType((short) 1);//1设备
            exPicking.setReceiverId(des.getReceiverId());
            exPicking.setDespatchCode(des.getDespatchId());
            exPicking.setDespatchId(des.getId());
            exPicking.setWaveNumber(wave.getId());
            exPicking.setExPickingId(codeService.code("EXP"));
            exPickingDAO.insertSelective(exPicking);
        }
        //装箱单表细
        for (Detail e : picklist.getDetail()) {
            GoodsSku sku = goodsSkuDAO.selectBySkuCode(e.getSku());
            ExPickingDetail exPD = exPickingDetailDAO.selectByExPickingIdAndSkuCode(exPicking.getId(), e.getSku());
            ExPickingDetail exPickingDetail = new ExPickingDetail();
            if (exPD != null) {//如果存在，数量相加
                exPD.setSkuCnt(exPD.getSkuCnt() + e.getQty());
                exPickingDetailDAO.updateByPrimaryKeySelective(exPD);
            } else {
                exPickingDetail = new ExPickingDetail(exPicking.getId(), sku.getId(),
                        sku.getSkuCode(), sku.getSkuName(), e.getQty(), sku.getSkuSize(),
                        sku.getSkuColor(), sku.getSkuBarcode());
                exPickingDetailDAO.insertSelective(exPickingDetail);
            }

        }
    }

    private boolean checkPickingStatus(Wave wave) {
        PickTask pickTask = pickTaskDAO.selectDetailByPid(wave.getId()).get(0);
        List<PickTaskDetail> pickTaskDetailList = pickTaskDetailDAO.selectDetailByPid(pickTask.getId());
        for (PickTaskDetail pd : pickTaskDetailList) {
            if (!pd.getIsPicked()) {
                return false;
            }
        }
        return true;
    }

}
