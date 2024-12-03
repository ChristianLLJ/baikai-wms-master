package com.bupt.service.outbound.impl;

import com.bupt.DTO.FrontId;
import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.outBount.*;
import com.bupt.DTO.wcs.Cartonlist;
import com.bupt.DTO.wcs.Lkcktask;
import com.bupt.DTO.wcs.WcsOnshelfSend;
import com.bupt.DTO.wcs.outbound.OutboundTask;
import com.bupt.DTO.wcs.outbound.OutboundDetail;
import com.bupt.DTO.wcs.outbound.OutboundTaskDistribution;
import com.bupt.DTO.wcs.outbound.WcsOutTask;
import com.bupt.DTO.wcs.shuttleTask.Task;
import com.bupt.DTO.wcs.shuttleTask.TaskType;
import com.bupt.DTO.wcs.shuttleTask.upper.ExStoreMsg;
import com.bupt.DTO.wcs.shuttleTask.upper.OutTaskInfo;
import com.bupt.mapper.*;
import com.bupt.pojo.*;
import com.bupt.result.BaokaiHttpResult;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.BaseService;
import com.bupt.service.authority.CodeService;
import com.bupt.service.outbound.PickupBlackService;
import com.bupt.service.util.HttpService;
import com.bupt.statePattern.despatchState.DespatchState;
import com.bupt.statePattern.examineState.ExamineState;
import com.bupt.statePattern.waveState.WaveState;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class PickupBlackServiceImpl extends BaseService<PickTask, PickTaskDetail, PickTaskDAO, PickTaskDetailBlackDAO> implements PickupBlackService {
    @Autowired
    private PickTaskDAO pickTaskDAO;
    @Autowired
    private PickTaskDetailDAO pickTaskDetailDAO;
    @Autowired
    private PickTaskMessageJsonDAO pickTaskMessageJsonDAO;
    @Autowired
    private PickTaskDetailBlackDAO pickTaskDetailBlackDAO;
    @Autowired
    private WaveDAO waveDAO;
    @Autowired
    private DespatchWaveDAO despatchWaveDAO;
    @Autowired
    private InventoryTotalDAO inventoryTotalDAO;
    @Autowired
    private InventoryBalanceDAO inventoryBalanceDAO;
    @Autowired
    private PreDistributionRecordsDAO preDistributionRecordsDAO;
    @Autowired
    private DespatchDAO despatchDAO;
    @Autowired
    private DespatchDetailDAO despatchDetailDAO;
    @Autowired
    private DeviceDAO deviceDAO;
    @Autowired
    private WarehouseLocationDAO warehouseLocationDAO;
    @Autowired
    private CodeService codeService;
    @Autowired
    HttpService httpService;
    @Value("${wcs.shuttleIp}")
    String shuttleIp;
    @Value("${wcs.baokaiIp}")
    String baokaiIp;

    @Override
    public HttpResult<?> updateTaskStatus(PickTask pickTask, Short taskStatus) {
        PickTask rePickTask = new PickTask();
        rePickTask.setId(pickTask.getId());
        rePickTask.setStatus(taskStatus);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public HttpResult<?> updatePickStatus(PickTask pickTask, Short pickStatus) {
        PickTask rePickTask = new PickTask();
        rePickTask.setId(pickTask.getId());
        rePickTask.setStatus(pickStatus);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public HttpResult<?> preDistributionByDespatchId(Despatch despatch) {
        boolean flag = true;
        Despatch despatch1 = despatchDAO.selectByPrimaryKey(despatch.getId());
        int j = despatch1.getVerifyStatus().intValue();
        ExamineState examineState = new ExamineState(j);
        if (!examineState.judge("审核通过", j))
            return HttpResult.of(HttpResultCodeEnum.NOT_EXAMINE_ERROR, false);
        if (despatch1.getIsPreDistributed() == 1) {
            return HttpResult.of(HttpResultCodeEnum.DESPATCH_HAVEBEEN_PRE_DISTRIBUTION, true);
        }
        /*else if (despatch1.getIsPreDistributed() == 2) {
            return HttpResult.of(HttpResultCodeEnum.RE_DESPATCH_PRE_DISTRIBUTION_FAIL, false);
        }*/
        PreDistributionRecords preDistributionRecords = new PreDistributionRecords();
        List<DespatchDetail> despatchDetailList = despatchDetailDAO.selectDetailByPid(despatch.getId());
        for (DespatchDetail despatchDetail : despatchDetailList) {
            Integer sum = inventoryTotalDAO.selectPreDistributionAvailableCountBySkuId(despatch1.getCustomerId(), despatchDetail.getSkuId(), despatch1.getWarehouse());
            if (sum == null)
                throw new RuntimeException("此仓库无此sku:" + despatchDetail.getSkuCode() + despatch.getWarehouse());
            if (despatchDetail.getOrderCnt() > inventoryTotalDAO.selectPreDistributionAvailableCountBySkuId(despatch1.getCustomerId(), despatchDetail.getSkuId(), despatch1.getWarehouse())) {
                flag = false;
            }
        }
        int rowId = 1;
        if (flag) {
            for (DespatchDetail despatchDetail : despatchDetailList) {
                despatch.setIsPreDistributed((short) 1);
                despatchDAO.updateByPrimaryKeySelective(despatch);
                despatchDetail.setStatus("预配成功");
                despatchDetail.setPreDistributionCnt(despatchDetail.getOrderCnt());
                despatchDetailDAO.updateByPrimaryKeySelective(despatchDetail);
                inventoryTotalDAO.increasePreDistributionCountBySkuId(despatch1.getCustomerId(), despatchDetail.getSkuId(), despatchDetail.getOrderCnt(), despatch1.getWarehouse());
                preDistributionRecords = despatchToPreDisRecordsSuccess(despatch1, despatchDetail, "默认规则", rowId++);
                preDistributionRecordsDAO.insertSelective(preDistributionRecords);
            }
        } else {
            for (DespatchDetail despatchDetail : despatchDetailList) {
                Integer haveNum = inventoryTotalDAO.selectPreDistributionAvailableCountBySkuId(despatch1.getCustomerId(), despatchDetail.getSkuId(), despatch1.getWarehouse());
                despatchDetail.setStatus("预配失败");
                despatchDetail.setPreDistributionCnt(0);
                despatchDetailDAO.updateByPrimaryKeySelective(despatchDetail);
                preDistributionRecords = despatchToPreDisRecordsFailed(despatch1, despatchDetail, "默认规则", rowId++);
                if (despatchDetail.getOrderCnt() - haveNum >= 0) {
                    preDistributionRecords.setPreShortageCnt(despatchDetail.getOrderCnt() - haveNum);
                } else preDistributionRecords.setPreShortageCnt(0);
                preDistributionRecordsDAO.insertSelective(preDistributionRecords);
            }
            despatch.setIsPreDistributed((short) 2);
            despatchDAO.updateByPrimaryKeySelective(despatch);
            return HttpResult.of(HttpResultCodeEnum.DESPATCH_PRE_DISTRIBUTION_FAIL, false);
        }
        if (despatch.getIsPreDistributed() == 2) {
            return HttpResult.of(HttpResultCodeEnum.DESPATCH_PART_PRE_DISTRIBUTION_SUCCESS, false);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, true);
    }

    @Override
    public HttpResult<?> preDistributionByDespatchIdList(List<Despatch> despatchs) {
        List<Despatch> despatchList = new ArrayList<>();
        //预配成功标志
        boolean symbol = true;
        int size = despatchs.size();
        for (int i = 0; i < despatchs.size(); i++) {
            Boolean tag = (Boolean) preDistributionByDespatchId(despatchs.get(i)).getData();
            if (!tag) {
                despatchList.add(despatchDAO.selectByPrimaryKey(despatchs.get(i).getId()));
                symbol = false;
            } else size--;
        }
        if (symbol && size == 0) return HttpResult.of(HttpResultCodeEnum.SUCCESS);
        else if (size != 0)
            return HttpResult.of(HttpResultCodeEnum.DESPATCH_PART_PRE_DISTRIBUTION_SUCCESS, despatchList);
        else return HttpResult.of(HttpResultCodeEnum.DESPATCH_PRE_DISTRIBUTION_FAIL, despatchList);
    }

    @Override
    public HttpResult<?> generatePicktaskBlackList(List<PickTaskMessageJson> pickTaskMessageJsons) {
        return generatePicktaskListBlackOrWhite(pickTaskMessageJsons, true);

    }

    @Override
    public HttpResult<?> generatePicktaskBlack(PicktaskMessageDTO picktaskMessageDTO) {

        Wave wave = waveDAO.selectByPrimaryKey(picktaskMessageDTO.getPid());//得到对应的波次
        if (wave.getStatus() == 2)
            return HttpResult.of(HttpResultCodeEnum.DESPATCH_HAVEBEEN_DISTRIBUTION, true);//已经分配成功，无需再次分配
        else if (wave.getStatus() == 22)
            return HttpResult.of(HttpResultCodeEnum.DESPATCH_DISTRIBUTION_NOT_WAREHOUSE, false);//仓库数量不足
        else if (wave.getStatus() == 23)
            return HttpResult.of(HttpResultCodeEnum.DESPATCH_DISTRIBUTION_NOT_AREA, false);//库区数量不足
        List<DespatchWave> despatchWaveList = despatchWaveDAO.selectDespatchIdByWaveId(wave.getId());
        boolean dis = true;
        for (DespatchWave despatchWave : despatchWaveList) {
            Despatch d = new Despatch();
            d.setId(despatchWave.getDespatchId());
            if (!(Boolean) preDistributionByDespatchId(d).getData()) dis = false;
        }
        //todo 状态模式
        WaveState waveState = new WaveState(1);
        if (!dis) {
            wave.setStatus(waveState.getCode("仓库缺货"));//仓库缺货
            waveDAO.updateByPrimaryKeySelective(wave);
            return HttpResult.of(HttpResultCodeEnum.DESPATCH_DISTRIBUTION_FAIL);
        }
        PickTask pickTask = new PickTask();
        pickTask.setPid(wave.getId());
        pickTask.setWaveCode(wave.getWaveId());
        pickTask.setStatus((short) 3);//拣货完成 todo 状态模式
        pickTask.setDistributionTime(new Date());
        pickTask.setTaskId(codeService.code("PIC"));
        pickTask.setStatus((short) 0);
        pickTaskDAO.insertSelective(pickTask);
        Integer rowNo = 1;
        //整箱拣货按比例分配-->按比例对发运订单表头进行分发
        //拆零拣货按比例分配-->按比例对发运订单表头进行分发
        //装箱按照按比例分配-->按比例对发运订单表头进行分发
        int size = despatchWaveList.size(),
                //整箱的订单数
                fullNum = (int) Math.ceil(picktaskMessageDTO.getPicktaskMsgFullBoxList().get(0).getFullPercent() * size),
                //拆零的订单数
                pieceNum = (int) Math.ceil(picktaskMessageDTO.getPicktaskMsgPieceList().get(0).getPiecePercent() * size),
                //装箱的订单数
                fullOrder = 0, packOrder = 0, pieOrder = 0;
        for (int i = 0; i < size; i++) {
            Despatch d = despatchDAO.selectByPrimaryKey(despatchWaveList.get(i).getDespatchId());
            rowNo = generatePickTaskBlackDetail(d,
                    picktaskMessageDTO.getPicktaskMsgWHList().get(0),//todo 库区分配
                    picktaskMessageDTO.getPicktaskMsgFullBoxList().get(fullOrder),
                    picktaskMessageDTO.getPicktaskMsgPieceList().get(pieOrder), rowNo, pickTask.getId());
            d.setStatus((short) 3);//todo 状态模式
            despatchDAO.updateByPrimaryKeySelective(d);
            if (--fullNum == 0 && i != size - 1) {
                fullNum = (int) Math.ceil(picktaskMessageDTO.getPicktaskMsgFullBoxList().get(++fullOrder).getFullPercent() * size);
            }
            if (--pieceNum == 0 && i != size - 1) {
                pieceNum = (int) Math.ceil(picktaskMessageDTO.getPicktaskMsgPieceList().get(++pieOrder).getPiecePercent() * size);
            }
        }
        wave.setStatus(waveState.getCode("已生成分拣任务"));
        waveDAO.updateByPrimaryKeySelective(wave);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, true);
    }

    @Override
    public HttpResult<?> generatePicktaskList(List<PickTaskMessageJson> pickTaskMessageJsons) {
        return generatePicktaskListBlackOrWhite(pickTaskMessageJsons, false);
    }

    @Override
    public HttpResult<?> generatePicktask(PicktaskMessageDTO picktaskMessageDTO) {
        Wave wave = waveDAO.selectByPrimaryKey(picktaskMessageDTO.getPid());//得到对应的波次
        if (wave.getStatus() == 2)
            return HttpResult.of(HttpResultCodeEnum.DESPATCH_HAVEBEEN_DISTRIBUTION, true);//已经分配成功，无需再次分配
        /*else if (wave.getStatus() == 22)
            return HttpResult.of(HttpResultCodeEnum.DESPATCH_DISTRIBUTION_NOT_WAREHOUSE, false);//仓库数量不足
        else if (wave.getStatus() == 23)
            return HttpResult.of(HttpResultCodeEnum.DESPATCH_DISTRIBUTION_NOT_AREA, false);//库区数量不足*/
        List<DespatchWave> despatchWaveList = despatchWaveDAO.selectDespatchIdByWaveId(wave.getId());
        boolean dis = true;
        for (DespatchWave despatchWave : despatchWaveList) {
            Despatch d = new Despatch();
            d.setId(despatchWave.getDespatchId());
            if (!(Boolean) preDistributionByDespatchId(d).getData()) dis = false;
        }
        //todo 状态模式
        WaveState waveState = new WaveState(1);
        if (!dis) {
            wave.setStatus(waveState.getCode("仓库缺货"));//仓库缺货
            waveDAO.updateByPrimaryKeySelective(wave);
            return HttpResult.of(HttpResultCodeEnum.DESPATCH_DISTRIBUTION_FAIL);
        }
        PickTask pickTask = new PickTask();
        pickTask.setPid(wave.getId());
        pickTask.setWaveCode(wave.getWaveId());
        pickTask.setStatus((short) 3);//拣货完成 todo 状态模式
        pickTask.setDistributionTime(new Date());
        pickTask.setTaskId(codeService.code("PIC"));
        pickTask.setStatus((short) 1);//分拣表头状态：已创建
        pickTaskDAO.insertSelective(pickTask);
        List<Despatch> despatchList = new ArrayList<>();
        despatchWaveList.forEach(e -> {
            despatchList.add(despatchDAO.selectByPrimaryKey(e.getDespatchId()));
        });
        Integer rowNo = 1;
        rowNo = fullBoxGeneratePickDetail(despatchList, picktaskMessageDTO, wave, rowNo, pickTask);
        //如果有人工分拣，则人工单独产生分拣任务，每次分拣以一个发运订单为单位，不能合并表细下架
        List<DespatchDetailAndCustomerId> pieceDesList = new ArrayList<>();
        PicktaskMessageDTO newPMD = new PicktaskMessageDTO();
        //检查是否有人工拣货
        boolean tag = manualPickCheck(despatchList, pieceDesList, picktaskMessageDTO, newPMD);
        if (tag) {
            rowNo = pieceGeneratePickDetail(pieceDesList, newPMD,
                    rowNo, pickTask);
            pieceGeneratePickDetail(mergeSkuByCustomer(despatchList), picktaskMessageDTO,
                    rowNo, pickTask);
        }//todo 问题：重复选择一箱，导致还未上架就下架，小车可能找不到货架上的箱子
        else {
            pieceGeneratePickDetail(mergeSkuByCustomer(despatchList), picktaskMessageDTO, rowNo, pickTask);
        }
        wave.setStatus(waveState.getCode("已生成分拣任务"));
        waveDAO.updateByPrimaryKeySelective(wave);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, true);
    }

    @Override
    public HttpResult<?> sendPickTasktoWcs() {
        WaveState ws = new WaveState(2);//todo 状态模式
        List<Wave> waveList = waveDAO.selectByStatus(ws.getCode("已生成分拣任务"));//筛选未发送wcs的表单
        List<WcsOutTask> wcsOutTaskList = new ArrayList<>();
        waveList.forEach(e -> {
            HeadAndDetail<PickTask, PickTaskDetailBlack> headAndDetail = new HeadAndDetail<>();
            PickTask pickTask = pickTaskDAO.selectDetailByPid(e.getId()).get(0);
            List<PickTaskDetail> pickTaskDetailList = pickTaskDetailDAO.selectDetailByPid(pickTask.getId());
            List<Despatch> despatchList = despatchDAO.selectByWaveId(e.getId());
            wcsOutTaskList.add(convertPickTasktoWcs(pickTask, pickTaskDetailList, despatchList));
            e.setStatus(ws.getCode("已发送WCS分拣任务"));
            waveDAO.updateByPrimaryKeySelective(e);//更新表单状态
        });
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, wcsOutTaskList);
    }

    @Override
    public HttpResult<Boolean> sendPickTasktoShuttleWcs(PickTask pickTask) {
        PickTask rePickTask = pickTaskDAO.selectByPrimaryKey(pickTask.getId());
        if (rePickTask.getStatus() > 1) return HttpResult.of(HttpResultCodeEnum.HAVE_BEEN_SENDED);
        List<PickTaskDetail> pickTaskDetails = pickTaskDetailDAO.selectDetailByPid(pickTask.getId());
        List<OutTaskInfo> taskInfos = new ArrayList<>();
        Boolean isFull = true;
        for (PickTaskDetail pd : pickTaskDetails) {
            OutTaskInfo taskInfo = new OutTaskInfo();
            taskInfo.setTaskType(TaskType.OUTBOUND);
            ExStoreMsg exStoreMsg = new ExStoreMsg();
            exStoreMsg.setBid(rePickTask.getWaveCode());
            exStoreMsg.setBoxId(pd.getInventoryBalanceCode());
            exStoreMsg.setId(String.format("%03d", pd.getId()));
            WarehouseLocation loc = warehouseLocationDAO.selectByPrimaryKey(pd.getLocationId());
            exStoreMsg.setLocation(String.format(String.format("%03d", loc.getRows()) + "-"
                    + "%03d", loc.getColumns()) + "-" + String.format("%03d", loc.getLayer()));
            exStoreMsg.setEstablishTime(new Date().toString());
            exStoreMsg.setSkuName(pd.getSkuCode());
            exStoreMsg.setSkuNumber(pd.getInboxCnt().toString());
            taskInfo.setExStoreMsg(exStoreMsg);
            taskInfos.add(taskInfo);//todo
            if (pd.getFullBoxDeviceId() == null) {
                isFull = false;
            }
        }
        pickTask.setStatus((short) 2);
        pickTaskDAO.updateByPrimaryKeySelective(pickTask);
        httpService.httpResponse(shuttleIp + "/api/addTask", taskInfos);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, isFull);
    }

    @Override
    public HttpResult<?> returnToWarehouse(InventoryBalance inventoryBalance) {
        InventoryBalance inv = inventoryBalanceDAO.selectByInventoryCodeType(inventoryBalance.getBalanceCode(), (short) 2);
        //返库的箱type已经置为2
        if (inv == null) return HttpResult.of(HttpResultCodeEnum.WRONG_INV_CODE);
        else if (inv.getType() == 3) return HttpResult.of(HttpResultCodeEnum.HAVE_BEEN_EMPTY);
        inv.setType((short) 1);
        inventoryBalanceDAO.updateByPrimaryKeySelective(inv);
        WcsOnshelfSend wcsOnshelfSend = new WcsOnshelfSend();
        wcsOnshelfSend.setPalletid(inv.getBalanceCode());
        WarehouseLocation loc = warehouseLocationDAO.selectByPrimaryKey(inv.getLocationId());
        wcsOnshelfSend.setOrigination(String.format("%03d", loc.getRows()) + "-" +
                String.format("%03d", loc.getColumns()) + "-" +
                String.format("%03d", loc.getLayer()));
        wcsOnshelfSend.setTarget("FK");
        wcsOnshelfSend.setReqcode("12321321413");
        wcsOnshelfSend.setCartonlist(new ArrayList<>());
        wcsOnshelfSend.getCartonlist().add(new Cartonlist(inv.getTotalCnt(), inv.getSkuCode()));

        Task task = new Task();
        task.setTaskType(TaskType.INBOUND);
        ExStoreMsg exStoreMsg = new ExStoreMsg();
        exStoreMsg.setLocation(inv.getLocationCode());
//        exStoreMsg.setBid(inv.getBalanceCode());
//        exStoreMsg.setBoxId(inv.getBalanceCode());
//        exStoreMsg.setId(inv.getBalanceCode());
//        exStoreMsg.setEstablishTime((new Date()).toString());
//        exStoreMsg.setSkuName(inv.getSkuCode());
//        exStoreMsg.setSkuNumber(inv.getTotalCnt().toString());
        // httpService.httpResponse(shuttleIp + "/api/addTask", task);//todo
        BaokaiHttpResult<?> baokaiHttpResult = httpService.httpResponse(baokaiIp +
                "/api/bkcellar/recroute/asnrgbind", wcsOnshelfSend);
        if (baokaiHttpResult.getErrcode() != 0) return HttpResult.of(baokaiHttpResult.getErrcode(),
                baokaiHttpResult.getErrmsg(), baokaiHttpResult.getData());
        return HttpResult.of();
    }

    @Override
    public HttpResult<?> returnReset(InventoryBalance inventoryBalance) {
        InventoryBalance inv = inventoryBalanceDAO.selectByInventoryCode(inventoryBalance.getBalanceCode());
        if (inv != null) {
            inv.setType((short) 2);
            inventoryBalanceDAO.updateByPrimaryKeySelective(inv);
        } else return HttpResult.of(HttpResultCodeEnum.HAVE_BEEN_EMPTY);
        return HttpResult.of();
    }

    @Override
    public HttpResult<?> boxOut(InventoryBalance inventoryBalance) {
        Lkcktask lkcktask = new Lkcktask();
        lkcktask.setOrigination("000-000-000");
        lkcktask.setTarget("OU");
        lkcktask.setIstotal("1");
        lkcktask.setPalletid(inventoryBalance.getAreaCode());
        lkcktask.setReqcode("12321321413");
        lkcktask.setQty(0);
        BaokaiHttpResult<?> b = httpService.httpResponse(baokaiIp + "/api/bkcellar/recroute/lkcktask", lkcktask);
        if (b.getErrcode() != 0) return HttpResult.of(b.getErrcode(),
                b.getErrmsg(), b.getData());
        return HttpResult.of();
    }

    @Override//收到wcs分拣任务的确认收到信号
    public HttpResult<?> receivePickTaskFromWcs(List<FrontId> waveIds) {
        Boolean tag = true;
        List<Wave> waveList = new ArrayList<>();
        for (FrontId e : waveIds) {
            WaveState w = new WaveState(3);
            Wave wave = new Wave();
            wave.setId(e.getId());
            Wave realWave = waveDAO.selectByPrimaryKey(wave.getId());
            if (realWave.getStatus() == w.getCode("已发送WCS分拣任务")) {
                wave.setStatus(w.getCode("已收到WCS分拣确认"));
                waveDAO.updateByPrimaryKeySelective(wave);
            } else {
                tag = false;
                waveList.add(realWave);
            }
        }
        if (tag) return HttpResult.of(HttpResultCodeEnum.SUCCESS);
        else return HttpResult.of(HttpResultCodeEnum.NOT_RIGHT_STATUS, waveList);
    }

    @Override//wcs发送分拣结果，加减库存
    public HttpResult<?> getPickTaskResult(List<PickTaskDetail> pickTaskDetails) {
        pickTaskDetails.forEach(e -> {
            if (e.getFullTag()) {
                inventoryBalanceDAO.updateAvailableCntAndDistributionCntById(e.getInboxCnt(), e.getInventoryBalanceId());
            } else {
                inventoryBalanceDAO.updateAvailableCntAndDistributionCntById(e.getPieceCnt(), e.getInventoryBalanceId());
            }
            pickTaskDetailDAO.insertSelective(e);
            PickTask pickTask = pickTaskDAO.selectByPrimaryKey(e.getPid());
            pickTask.setStatus((short) 2);
            WaveState waveState = new WaveState(5);
            Wave wave = waveDAO.selectByPrimaryKey(pickTask.getPid());
            if (wave.getStatus() != waveState.getCode("拣货中")) {
                wave.setStatus(waveState.getCode("拣货中"));
                waveDAO.updateByPrimaryKeySelective(wave);
            }
            DespatchState despatchState = new DespatchState(4);
            Despatch despatch = despatchDAO.selectByPrimaryKey(e.getDespatchId());
            if (despatch.getStatus() != (short) despatchState.getCode("拣货中")) {
                despatch.setStatus((short) despatchState.getCode("拣货中"));
                despatchDAO.updateByPrimaryKeySelective(despatch);
            }
        });
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public HttpResult<?> checkAndEndPickTaskResult() {
        System.out.println("分拣检查");
        for (PickTask p : pickTaskDAO.selectTaskByStatus((short) 2)) {
            List<PickTaskDetail> pickTaskDetails = pickTaskDetailDAO.selectDetailByPid(p.getId());//按照despatch_id asc,sku_id asc排序
            List<PickTaskDetailBlack> pickTaskDetailBlacks = pickTaskDetailBlackDAO.selectDetailByPid(p.getId());//按照despatch_id asc,sku_id asc排序
            Boolean pickOverTag = true;
            for (PickTaskDetailBlack pb : pickTaskDetailBlacks) {
                Integer pNums = pickTaskDetailDAO.sumTotalCntBySkuAndPid(pb.getPid(), pb.getSkuId());
                Integer pbNums = pickTaskDetailBlackDAO.sumTotalCntBySkuAndPid(pb.getPid(), pb.getSkuId());
                if (pbNums != pNums) {
                    pickOverTag = false;
                    break;
                }
            }
            //分拣任务单全部分拣完毕，分拣结果数量==分拣任务数量
            if (pickOverTag) {
                p.setStatus((short) 3);//已分拣
                pickTaskDAO.updateByPrimaryKeySelective(p);
                Wave w = new Wave();
                w.setId(p.getPid());
                WaveState ws = new WaveState(6);
                w.setStatus(ws.getCode("已拣货"));
                waveDAO.updateByPrimaryKeySelective(w);
                despatchWaveDAO.selectDetailByPid(p.getPid()).forEach(e -> {
                    Despatch despatch = new Despatch();
                    despatch.setId(e.getDespatchId());
                    DespatchState ds = new DespatchState(5);
                    despatch.setStatus((short) ds.getCode("已拣货"));
                    despatchDAO.updateByPrimaryKeySelective(despatch);
                });
            }
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public HttpResult<?> findDetailInPickingByDesId(Despatch despatch) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, pickTaskDetailDAO.
                selectByDespatchIdAndPieceDevice(despatch.getId()));
    }

    /*@Override
        public HttpResult<?> generatePicktaskBlackByDes(List<Wave> waveList) {
            WaveState waveState = null;
            for (Wave w : waveList) {
                Wave wave = waveDAO.selectByPrimaryKey(w.getId());
                int i = wave.getStatus(), j = wave.getCheckStatus().intValue();
                ExamineState examineState = new ExamineState(j);
                waveState = new WaveState(i);
                if (!examineState.judge("审核通过", j))
                    return HttpResult.of(HttpResultCodeEnum.NOT_EXAMINE_ERROR);
                if (!waveState.judge("已创建", i))
                    return HttpResult.of(HttpResultCodeEnum.NOT_CREARTED_ERROR);
            }
            List<HeadAndDetail<Wave, Despatch>> headAndDetailList = new ArrayList<>();
            List<Despatch> failDespatchList = new ArrayList<>();
            for (Wave e : waveList) {
                List<Despatch> despatchList = new ArrayList<>();
                Wave wave = waveDAO.selectByPrimaryKey(e.getId());
                wave.setStatus(waveState.getCode("已生成分拣任务"));
                HeadAndDetail<Wave, Despatch> headAndDetail = new HeadAndDetail<>();
                headAndDetail.setHead(wave);
                despatchWaveDAO.selectDetailByPid(e.getId()).forEach(k -> {
                    DespatchState d = new DespatchState(1);
                    Despatch despatch = despatchDAO.selectByPrimaryKey(k.getDespatchId());
                    if (despatch.getIsPreDistributed() != 1) {
                        if (!(Boolean) preDistributionByDespatchId(despatch).getData()) {
                            failDespatchList.add(despatch);
                        }
                    }
                    despatch.setStatus((short) d.getCode("已生成分拣任务"));
                    despatchList.add(despatch);
                });
                headAndDetail.setDetails(despatchList);
                headAndDetailList.add(headAndDetail);
            }
            if (failDespatchList.size() > 0)
                return HttpResult.of(HttpResultCodeEnum.FAIL_PICK_DUE_PREFAIL, failDespatchList);
            else {
                headAndDetailList.forEach(e -> {
                    waveDAO.updateByPrimaryKeySelective(e.getHead());
                    e.getDetails().forEach(k -> {
                        despatchDAO.updateByPrimaryKeySelective(k);
                    });
                });
                return HttpResult.of(HttpResultCodeEnum.SUCCESS, headAndDetailList);
            }
        }

        @Override//可重复发送,当收到确认后不可发送
        public HttpResult<?> searchToSendWcs() {
            List<HeadAndDetail<DespatchAndWave, DespatchDetail>> headAndDetailList = new ArrayList<>();
            HeadAndDetail<DespatchAndWave, DespatchDetail> headAndDetail = new HeadAndDetail<>();
            List<Wave> waves = waveDAO.selectByIsSendWcs();
            waves.forEach(e -> {
                despatchWaveDAO.selectDetailByPid(e.getId()).forEach(k -> {
                    DespatchAndWave d = desAddWave(despatchDAO.selectByPrimaryKey(k.getDespatchId()), e);
                    headAndDetail.setHead(d);
                    headAndDetail.setDetails(despatchDetailDAO.selectDetailByPid(d.getDesId()));
                    headAndDetailList.add(headAndDetail);
                });
                WaveState waveState = new WaveState(e.getStatus());
                e.setStatus(waveState.getCode("已发送WCS分拣任务"));
                waveDAO.updateByPrimaryKeySelective(e);
            });
            return HttpResult.of(HttpResultCodeEnum.SUCCESS, headAndDetailList);
        }*/
    //Json转化
    PicktaskMessageDTO jsonToDTO(PickTaskMessageJson pickTaskMessageJson) {
        PicktaskMessageDTO p = new PicktaskMessageDTO();
        JSONArray packJson = JSONArray.fromObject(pickTaskMessageJson.getPackingDeviceJson());
        JSONArray pieceJson = JSONArray.fromObject(pickTaskMessageJson.getPieceDeviceJson());
        JSONArray fullJson = JSONArray.fromObject(pickTaskMessageJson.getFullBoxDeviceJson());
        JSONArray warehouseJson = JSONArray.fromObject(pickTaskMessageJson.getWarehouseAreaJson());
        p.setPid(pickTaskMessageJson.getPid());
        p.setIsFullRepacking(pickTaskMessageJson.getIsFullRepacking());
        p.setIsNotSingleArea(pickTaskMessageJson.getIsNotSingleArea());
        p.setPickTaskRule(pickTaskMessageJson.getPickTaskRule());//todo 空指针？
        p.setCreateTime(new Date());
        p.setWarehouseCode(pickTaskMessageJson.getWarehouseCode());
        p.setWarehouseId(pickTaskMessageJson.getWarehouseId());
        p.setWarehouseName(pickTaskMessageJson.getWarehouseName());
        p.setIsFullRepacking(pickTaskMessageJson.getIsFullRepacking());
        p.setPicktaskMsgFullBoxList((List<PicktaskMsgFullBox>) JSONArray.toCollection(fullJson, PicktaskMsgFullBox.class));
        p.setPicktaskMsgPieceList((List<PicktaskMsgPiece>) JSONArray.toCollection(pieceJson, PicktaskMsgPiece.class));
        p.setPicktaskMsgWHList((List<PicktaskMsgWH>) JSONArray.toCollection(warehouseJson, PicktaskMsgWH.class));
        return p;
    }

    //检查前端传输的比例是否和为1
    boolean checkPercentIsOne(PicktaskMessageDTO picktaskMessageDTO) {
        List<PicktaskMsgFullBox> picktaskMsgFullBoxList = picktaskMessageDTO.getPicktaskMsgFullBoxList();
        List<PicktaskMsgPiece> picktaskMsgPieceList = picktaskMessageDTO.getPicktaskMsgPieceList();
        float full = 0, piece = 0;
        for (PicktaskMsgFullBox picktaskMsgFullBox : picktaskMsgFullBoxList) {
            full += picktaskMsgFullBox.getFullPercent();
        }
        for (PicktaskMsgPiece picktaskMsgPiece : picktaskMsgPieceList) {
            piece += picktaskMsgPiece.getPiecePercent();
        }
        return full == 1 && piece == 1;
    }

    //分拣List操作 白/黑
    public HttpResult<?> generatePicktaskListBlackOrWhite(List<PickTaskMessageJson> pickTaskMessageJsons, Boolean isBlack) {
        //错误信息返回Map
        Map<String, String> map = new HashMap<>();
        Boolean errorTag = false;
        List<PicktaskMessageDTO> picktaskMessageDTOS = new ArrayList<>();
        for (PickTaskMessageJson pt : pickTaskMessageJsons) {
            Wave wave = waveDAO.selectByPrimaryKey(pt.getPid());
            PicktaskMessageDTO w = jsonToDTO(pt);//转json
            picktaskMessageDTOS.add(w);
            if (!checkPercentIsOne(w)) {
                errorTag = true;
                map.put(wave.getWaveId(), "设备分配比例和不等于1");
                continue;
            }
            int i = wave.getStatus(),
                    j = wave.getCheckStatus().intValue();
            ExamineState examineState = new ExamineState(j);
            WaveState waveState = new WaveState(i);
            if (!examineState.judge("审核通过", j)) {
                errorTag = true;
                map.put(wave.getWaveId(), "波次计划未审核");
            }
            if (!(waveState.judge("已创建", i) || i > 21)) {
                errorTag = true;
                map.put(wave.getWaveId(), "波次计划非“已创建”或“缺货”状态，不能进行分拣操作");
            }
        }
        if (errorTag) return HttpResult.of(HttpResultCodeEnum.CANNOT_RUN_ERROR, map);
        pickTaskMessageJsons.forEach(e -> {
            pickTaskMessageJsonDAO.insertSelective(e);
        });
        for (PicktaskMessageDTO e : picktaskMessageDTOS) {
            Wave wave = waveDAO.selectByPrimaryKey(e.getPid());
            HttpResult<?> httpResult = new HttpResult<>(200, null, null);
            if (isBlack) httpResult = generatePicktaskBlack(e);
            else httpResult = generatePicktask(e);
            map.put(wave.getWaveId(), httpResult.getMsg());
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, map);
    }

    //黑箱
    int generatePickTaskBlackDetail(Despatch despatch, PicktaskMsgWH pw,
                                    PicktaskMsgFullBox pf, PicktaskMsgPiece pe, Integer rowNo, Integer pickId) {
        List<DespatchDetail> despatchDetailList = despatchDetailDAO.selectDetailByPid(despatch.getId());
        for (DespatchDetail d : despatchDetailList) {
            InventoryBalance inventoryBalance = inventoryBalanceDAO.selectByAreaCoAndSkuIdAndCusIdLimit
                    (d.getSkuId(), despatch.getCustomerId(), pw.getWarehouseAreaCode());
            Integer inventoryCnt = inventoryBalance.getInventoryCnt();
            Integer boxsNum = d.getOrderCnt() / inventoryCnt;
            Integer pieceNum = d.getOrderCnt() % inventoryCnt;
            PickTaskDetailBlack pickTaskDetailBlack = inventoryBalanceToPickTaskDetailBlack(inventoryBalance, pf, pe);
            pickTaskDetailBlack.setDespatchId(d.getPid());
            pickTaskDetailBlack.setPid(pickId);
            if (boxsNum == 0) pickTaskDetailBlack.setFullTag(false);//一个整箱也没有时，置为false
            else pickTaskDetailBlack.setFullTag(true);
            pickTaskDetailBlack.setBoxesCnt(boxsNum);//整箱数
            pickTaskDetailBlack.setInboxCnt(inventoryCnt);//整箱每箱箱内数量
            pickTaskDetailBlack.setRowNo(rowNo++);
            pickTaskDetailBlack.setPieceCnt(pieceNum);//拆零数
            pickTaskDetailBlack.setReceiverId(despatch.getReceiverId());
            pickTaskDetailBlack.setReceiverName(despatch.getReceiverName());
            pickTaskDetailBlackDAO.insertSelective(pickTaskDetailBlack);
        }
        return rowNo;
    }

    //整箱下架
    int fullBoxGeneratePickDetail(List<Despatch> despatchList, PicktaskMessageDTO picktaskMessageDTO,
                                  Wave wave, int rowNo, PickTask pickTask) {
        int size = despatchList.size(),//ceil:向上取整
                fullNum = (int) Math.ceil(picktaskMessageDTO.getPicktaskMsgFullBoxList().get(0).getFullPercent() * size),
                pieceNum = (int) Math.ceil(picktaskMessageDTO.getPicktaskMsgPieceList().get(0).getPiecePercent() * size),
                fullOrder = 0, pieOrder = 0;
        for (int i = 0; i < size; i++) {
            Despatch d = despatchList.get(i);
            rowNo = generatePickTaskDetailFull(d, picktaskMessageDTO.getPicktaskMsgWHList().get(0),//todo 库区分配
                    picktaskMessageDTO.getPicktaskMsgFullBoxList().get(fullOrder), rowNo, pickTask.getId());
            d.setStatus((short) 3);//todo 状态模式
            despatchDAO.updateByPrimaryKeySelective(d);
            if (--fullNum == 0 && i != size - 1) {
                fullNum = (int) Math.ceil(picktaskMessageDTO.getPicktaskMsgFullBoxList().get(++fullOrder).getFullPercent() * size);
            }
            if (--pieceNum == 0 && i != size - 1) {
                pieceNum = (int) Math.ceil(picktaskMessageDTO.getPicktaskMsgPieceList().get(++pieOrder).getPiecePercent() * size);
            }
        }
        return rowNo;
    }


    //白箱-整箱下架first
    int generatePickTaskDetailFull(Despatch despatch, PicktaskMsgWH pw,
                                   PicktaskMsgFullBox pf, Integer rowNo, Integer pickId) {
        List<DespatchDetail> despatchDetailList = despatchDetailDAO.selectDetailByPid(despatch.getId());
        //对应客户的整箱先找到，并生成分拣明细
        for (DespatchDetail d : despatchDetailList) {
            //找整箱
            List<InventoryBalance> inventoryBalance = inventoryBalanceDAO.selectByAreaCoAndSkuIdAndCusIdFullBoxLimit
                    (d.getSkuId(), despatch.getCustomerId(), pw.getWarehouseAreaCode(), d.getOrderCnt());
            for (InventoryBalance balance : inventoryBalance) {
                PickTaskDetail pickTaskDetail = inventoryBalanceToPickTaskDetail(balance, pf, null);
                pickTaskDetail.setDespatchId(d.getPid());
                pickTaskDetail.setPid(pickId);
                pickTaskDetail.setRowNo(rowNo++);
                pickTaskDetail.setReceiverId(despatch.getReceiverId());
                pickTaskDetail.setReceiverName(despatch.getReceiverName());
                pickTaskDetail.setFullTag(true);//fulltag=1,
                pickTaskDetail.setPickupTag(false);//拆箱标志=0
                pickTaskDetail.setPieceCnt(0);//拆零
                pickTaskDetail.setIsPicked(true);
                pickTaskDetailDAO.insertSelective(pickTaskDetail);
                d.setDistributionCnt(d.getDistributionCnt() + balance.getAvailableCnt());
                //更新库存
                balance.setDistributionCnt(balance.getAvailableCnt());
                balance.setAvailableCnt(0);
                balance.setTotalCnt(0);
                balance.setType((short) 0);
                inventoryBalanceDAO.updateByPrimaryKeySelective(balance);
                WarehouseLocation loc = warehouseLocationDAO.selectByPrimaryKey(balance.getLocationId());
                loc.setIsLocked(false);
                warehouseLocationDAO.updateByPrimaryKeySelective(loc);
            }
            d.setPieceCnt(d.getOrderCnt() - d.getDistributionCnt());
            d.setDistributionCnt(d.getOrderCnt());
            despatchDetailDAO.updateByPrimaryKeySelective(d);
        }
        return rowNo;
    }

    boolean manualPickCheck(List<Despatch> despatchList, List<DespatchDetailAndCustomerId> newDesAndCusIds,
                            PicktaskMessageDTO pd, PicktaskMessageDTO newPd) {
        boolean tag = false;
        int size = despatchList.size();
        //人工分拣的设备列表，一般只有一个，可以有多个
        List<PicktaskMsgPiece> pes = new ArrayList<>();
        float percentSum = 0f, newPercentSum = 0f;
        //人工分拣 检测
        for (PicktaskMsgPiece pe : pd.getPicktaskMsgPieceList()) {
            if (deviceDAO.selectByPrimaryKey(pe.getPieceDeviceId()).getIsManual()) {
                tag = true;
                pes.add(pe);
                newPercentSum += pe.getPiecePercent();
                int n = (int) Math.ceil(size * pe.getPiecePercent());
                for (int i = 0; i < n; i++) {
                    Despatch despatch = despatchList.get(0);
                    for (DespatchDetail dd : despatchDetailDAO.selectDetailByPid(despatch.getId())) {
                        DespatchDetailAndCustomerId ddacd = new DespatchDetailAndCustomerId();
                        ddacd.setDespatchDetail(dd);
                        ddacd.setCustomerId(despatch.getCustomerId());
                        newDesAndCusIds.add(ddacd);
                    }
                    despatchList.remove(0);
                }
            } else
                percentSum += pe.getPiecePercent();
        }
        for (PicktaskMsgPiece pe : pes) {
            pd.getPicktaskMsgPieceList().remove(pe);
        }

        for (int i = 0; i < pes.size(); i++) {
            PicktaskMsgPiece pe = pes.get(i);
            pd.getPicktaskMsgPieceList().remove(pe);
            pes.get(i).setPiecePercent(pe.getPiecePercent() / newPercentSum);
        }
        //分人工拆零分拣的比例重新计算，因为所剩desList已减去人工分拣的list
        for (PicktaskMsgPiece pe : pd.getPicktaskMsgPieceList()) {
            pe.setPiecePercent(pe.getPiecePercent() / percentSum);
        }
        //人工分拣的比例信息
        newPd.setPicktaskMsgPieceList(pes);
        newPd.setPicktaskMsgWHList(pd.getPicktaskMsgWHList());
        newPd.setPicktaskMsgFullBoxList(pd.getPicktaskMsgFullBoxList());
        newPd.setPid(pd.getPid());
        newPd.setWarehouseId(pd.getWarehouseId());
        return tag;
    }

    //拆零任务分设备-白箱-second
    int pieceGeneratePickDetail(List<DespatchDetailAndCustomerId> deDetailCs, PicktaskMessageDTO picktaskMessageDTO,
                                int rowNo, PickTask pickTask) {
        //ceil:向上取整
        int size = deDetailCs.size(), pieOrder = 0, pieceNum = 0;
        if (!(picktaskMessageDTO.getPicktaskMsgPieceList().isEmpty() || picktaskMessageDTO.getPicktaskMsgPieceList() == null)) {
            pieceNum = (int) Math.ceil(picktaskMessageDTO.getPicktaskMsgPieceList().get(0).getPiecePercent() * size);
        }
        for (int i = 0; i < size; i++) {
            DespatchDetailAndCustomerId d = deDetailCs.get(i);
            rowNo = generatePickTaskDetailPiece(d.getCustomerId(), d.getDespatchDetail(),
                    picktaskMessageDTO.getPicktaskMsgWHList().get(0),//todo 库区分配
                    null, picktaskMessageDTO.getPicktaskMsgPieceList().get(pieOrder), rowNo,
                    pickTask.getId());
            if (--pieceNum == 0 && i != size - 1) {
                pieceNum = (int) Math.ceil(picktaskMessageDTO.getPicktaskMsgPieceList().get(++pieOrder).getPiecePercent() * size);
            }
        }
        return rowNo;
    }

    List<DespatchDetailAndCustomerId> mergeSkuByCustomer(List<Despatch> despatchList) {
        List<DespatchDetailAndCustomerId> despatchDetailAndCustomerIds = new ArrayList<>();
        Map<String, DespatchDetailAndCustomerId> map = new HashMap<>();
        for (Despatch despatch : despatchList) {
            for (DespatchDetail dd : despatchDetailDAO.selectDetailByPid(despatch.getId())) {
                dd.setIsMergePiece(true);
                despatchDetailDAO.updateByPrimaryKeySelective(dd);
                if (dd.getPieceCnt() == 0) continue;
                String key = despatch.getCustomerId().toString() + dd.getSkuId().toString();
                DespatchDetailAndCustomerId deC = map.get(key);
                if (deC != null && deC.getCustomerId() == despatch.getCustomerId()) {
                    int pieceCnt = deC.getDespatchDetail().getPieceCnt() + dd.getPieceCnt();
                    map.get(key).getDespatchDetail().setPieceCnt(pieceCnt);
                } else {
                    DespatchDetailAndCustomerId newDedetailC = new DespatchDetailAndCustomerId();
                    newDedetailC.setCustomerId(despatch.getCustomerId());
                    newDedetailC.setDespatchDetail(dd);
                    map.put(key, newDedetailC);
                }
            }
        }
        map.forEach((k, v) -> {
            despatchDetailAndCustomerIds.add(v);
        });
        return despatchDetailAndCustomerIds;
    }

    int generatePickTaskDetailPiece(int customerId, DespatchDetail dd,
                                    PicktaskMsgWH pw, PicktaskMsgFullBox pf,
                                    PicktaskMsgPiece pe, Integer rowNo, Integer pickId) {
        //对拆零件进行整合 分拣任务--分为整箱需拆箱 & 需拆零,despatchDetailList:根据货主和sku整合的detailList
        List<InventoryBalance> inventoryBalance = inventoryBalanceDAO.selectByAreaCoAndSkuIdAndCusIdFullBoxLimit
                (dd.getSkuId(), customerId, pw.getWarehouseAreaCode(), dd.getPieceCnt());
        for (InventoryBalance balance : inventoryBalance) {
            //拆零总和仍然不足一箱，退出循环
            if (inventoryBalance.size() == 1 && inventoryBalance.get(0).getAvailableCnt() >
                    (dd.getPieceCnt())) {
                break;
            }
            PickTaskDetail pickTaskDetail = inventoryBalanceToPickTaskDetail(balance, pf, pe);
            //虽然插入发运订单号码，但是一箱内可能是多个客户的订单，通过发运订单查询各自拆零，
            // wcs先后执行顺序通过rowId排序
            pickTaskDetail.setDespatchId(dd.getPid());
            pickTaskDetail.setPid(pickId);
            pickTaskDetail.setRowNo(rowNo++);
            pickTaskDetail.setFullTag(true);//fulltag=1,
            pickTaskDetail.setPickupTag(true);//拆箱标志=1,混客户整箱，需上分拣机拆箱
            pickTaskDetail.setPieceCnt(pickTaskDetail.getInboxCnt());//拆零
            pickTaskDetailDAO.insertSelective(pickTaskDetail);

            //更新发运订单表细
            dd.setPieceCnt(dd.getPieceCnt() - balance.getAvailableCnt());

            //更新库存
            balance.setDistributionCnt(balance.getAvailableCnt());
            balance.setAvailableCnt(0);
            balance.setTotalCnt(0);
            balance.setType((short) 0);
            inventoryBalanceDAO.updateByPrimaryKeySelective(balance);
        }
        while (dd.getPieceCnt() != 0) {//此时不足一箱，需拆零下架
            InventoryBalance invPiece = inventoryBalanceDAO.selectByAreaCoAndSkuIdAndCusIdLimit(dd.getSkuId(), customerId,
                    pw.getWarehouseAreaCode());
            if (invPiece == null) throw new RuntimeException("无库存");
            //判断本条库存是否够 拆零所需，因为有可能找到的是，已经不足一箱的库存
            int pieceCnt = 0, availableCnt = 0, totalCnt = 0;
            if (invPiece.getAvailableCnt() >= dd.getPieceCnt()) {
                pieceCnt = dd.getPieceCnt();
                availableCnt = invPiece.getAvailableCnt() - pieceCnt;
                invPiece.setAvailableCnt(availableCnt);
                totalCnt = invPiece.getTotalCnt() - pieceCnt;
                invPiece.setDistributionCnt(invPiece.getDistributionCnt() + pieceCnt);
                dd.setPieceCnt(0);
            } else {
                pieceCnt = invPiece.getAvailableCnt();
                dd.setPieceCnt(dd.getPieceCnt() - invPiece.getAvailableCnt());//拆零数量减少
                //加减库存
                totalCnt = invPiece.getTotalCnt() - invPiece.getAvailableCnt();
                availableCnt = 0;
                invPiece.setDistributionCnt(invPiece.getDistributionCnt() + invPiece.getAvailableCnt());
            }
            //分拣明细生成
            PickTaskDetail pickTaskDetail = inventoryBalanceToPickTaskDetail(invPiece, pf, pe);
            pickTaskDetail.setDespatchId(dd.getPid()); //虽然插入发运订单号码，但是一箱内可能是多个客户的订单
            pickTaskDetail.setPid(pickId);
            pickTaskDetail.setRowNo(rowNo++);
            pickTaskDetail.setFullTag(false);//fulltag=0,不是整箱，需要先从整箱中 拆零 出所需数量sku
            pickTaskDetail.setPickupTag(true);//拆箱标志=1,混客户整箱，需上分拣机拆箱
            pickTaskDetail.setPieceCnt(pieceCnt);//拆零数量
            invPiece.setTotalCnt(totalCnt);
            invPiece.setAvailableCnt(availableCnt);
            invPiece.setType((short) 2);//todo 锁定这条数据，重新上架后恢复
            if (invPiece.getAvailableCnt() == 0 && invPiece.getInventoryCnt() == 0) {
                invPiece.setType((short) 3);
            }
            pickTaskDetailDAO.insertSelective(pickTaskDetail);
            if (invPiece.getTotalCnt() == 0) {
                WarehouseLocation loc = warehouseLocationDAO.selectByPrimaryKey(invPiece.getLocationId());
                loc.setIsLocked(false);
                warehouseLocationDAO.updateByPrimaryKeySelective(loc);
            }
            inventoryBalanceDAO.updateByPrimaryKeySelective(invPiece);//更新数据库
        }
        return rowNo;
    }

    PickTaskDetailBlack inventoryBalanceToPickTaskDetailBlack(InventoryBalance in, PicktaskMsgFullBox pf,
                                                              PicktaskMsgPiece pe) {
        PickTaskDetailBlack pickTaskDetail = new PickTaskDetailBlack();
        pickTaskDetail.setCustomerId(in.getCustomId());
        pickTaskDetail.setCustomerName(in.getCustomCode());
        pickTaskDetail.setUnit(in.getUnit());
        pickTaskDetail.setInboxCnt(in.getInventoryCnt());
        pickTaskDetail.setVolume(in.getVolume());
        pickTaskDetail.setWeight(in.getWeight());
        pickTaskDetail.setWarehouseCode(in.getWarehouseCode());
        pickTaskDetail.setContainerId(in.getContainerId());
        pickTaskDetail.setContainerCode(in.getContainerCode());
        pickTaskDetail.setInboxCnt(in.getAvailableCnt());//箱内数量
        pickTaskDetail.setFullBoxDeviceId(pf.getFullBoxDeviceId());
        pickTaskDetail.setFullboxDevice(pf.getFullBoxDevice());
        pickTaskDetail.setPieceDeviceId(pe.getPieceDeviceId());
        pickTaskDetail.setPieceDevice(pe.getPieceDevice());
        pickTaskDetail.setCreateTime(new Date());
        pickTaskDetail.setAreaCode(in.getAreaCode());
        pickTaskDetail.setAreaId(in.getAreaId());
        pickTaskDetail.setSkuId(in.getSkuId());
        pickTaskDetail.setSkuCode(in.getSkuCode());
        pickTaskDetail.setWarehouseId(in.getWarehouseId());
        pickTaskDetail.setWarehouseName(in.getWarehouseName());
        pickTaskDetail.setWarehouseCode(in.getWarehouseCode());
        return pickTaskDetail;
    }

    PickTaskDetail inventoryBalanceToPickTaskDetail(InventoryBalance in,
                                                    PicktaskMsgFullBox pf, PicktaskMsgPiece pe) {
        PickTaskDetail pickTaskDetail = new PickTaskDetail();
        pickTaskDetail.setCustomerId(in.getCustomId());
        pickTaskDetail.setCustomerName(in.getCustomCode());
        pickTaskDetail.setUnit(in.getUnit());
        pickTaskDetail.setVolume(in.getVolume());
        pickTaskDetail.setWeight(in.getWeight());
        pickTaskDetail.setWarehouseCode(in.getWarehouseCode());
        pickTaskDetail.setContainerId(in.getContainerId());
        pickTaskDetail.setContainerCode(in.getContainerCode());
        if (pf != null) {
            pickTaskDetail.setFullBoxDeviceId(pf.getFullBoxDeviceId());
            pickTaskDetail.setFullBoxDevice(pf.getFullBoxDevice());
        }
        if (pe != null) {
            pickTaskDetail.setPieceDeviceId(pe.getPieceDeviceId());
            pickTaskDetail.setPieceDevice(pe.getPieceDevice());
        }
        pickTaskDetail.setCreateTime(new Date());
        pickTaskDetail.setAreaCode(in.getAreaCode());
        pickTaskDetail.setAreaId(in.getAreaId());
        pickTaskDetail.setAreaName(in.getAreaName());
        pickTaskDetail.setSkuId(in.getSkuId());
        pickTaskDetail.setSkuCode(in.getSkuCode());
        pickTaskDetail.setWarehouseId(in.getWarehouseId());
        pickTaskDetail.setWarehouseName(in.getWarehouseName());
        pickTaskDetail.setWarehouseCode(in.getWarehouseCode());
        pickTaskDetail.setLocationId(in.getLocationId());
        pickTaskDetail.setLocationCode(in.getLocationCode());
        pickTaskDetail.setLocationName(in.getLocationName());
        pickTaskDetail.setLocationGroupId(in.getLocationGroupId());
        pickTaskDetail.setLocationGroupName(in.getLocationGroupName());
        pickTaskDetail.setLocationGroupCode(in.getLocationGroupCode());
        pickTaskDetail.setInventoryBalanceId(in.getId());
        pickTaskDetail.setInventoryBalanceCode(in.getBalanceCode());
        pickTaskDetail.setInboxCnt(in.getTotalCnt());
        return pickTaskDetail;
    }


    PreDistributionRecords despatchToPreDisRecordsSuccess(Despatch despatch, DespatchDetail despatchDetail, String rule, Integer rowId) {
        PreDistributionRecords preDistributionRecords = new PreDistributionRecords();
        preDistributionRecords.setPreDistributionRule(rule);
        preDistributionRecords.setPreDistributionCnt(despatchDetail.getOrderCnt());
        preDistributionRecords.setPreDistributionRuleId(1);//暂定
        preDistributionRecords.setChineseDescribe(despatchDetail.getChineseDescribe());
        preDistributionRecords.setEnglishDescribe(despatchDetail.getEnglishDescribe());
        preDistributionRecords.setOtherName(despatchDetail.getOtherName());
        preDistributionRecords.setWarehouseName(despatch.getWarehouseName());
        preDistributionRecords.setWarehouseCode(despatch.getWarehouse());
        preDistributionRecords.setSkuId(despatchDetail.getSkuId());
        preDistributionRecords.setSkuCode(despatchDetail.getSkuCode());
        preDistributionRecords.setIsCompleted(true);
        preDistributionRecords.setPid(despatch.getId());
        preDistributionRecords.setUnit(despatchDetail.getUnit());
        preDistributionRecords.setDespatchCode(despatch.getDespatchId());
        preDistributionRecords.setStatus((short) 1);
        preDistributionRecords.setRowId(rowId);
        preDistributionRecords.setPreDistributionCode(codeService.code("PRD"));
        return preDistributionRecords;
    }

    PreDistributionRecords despatchToPreDisRecordsFailed(Despatch despatch, DespatchDetail despatchDetail, String rule, Integer rowId) {
        PreDistributionRecords preDistributionRecords = new PreDistributionRecords();
        preDistributionRecords.setPreDistributionRule(rule);
        preDistributionRecords.setPreDistributionCnt(despatchDetail.getOrderCnt());
        preDistributionRecords.setPreDistributionRuleId(1);//暂定
        preDistributionRecords.setChineseDescribe(despatchDetail.getChineseDescribe());
        preDistributionRecords.setEnglishDescribe(despatchDetail.getEnglishDescribe());
        preDistributionRecords.setOtherName(despatchDetail.getOtherName());
        preDistributionRecords.setWarehouseName(despatch.getWarehouseName());
        preDistributionRecords.setWarehouseCode(despatch.getWarehouse());
        preDistributionRecords.setSkuId(despatchDetail.getSkuId());
        preDistributionRecords.setSkuCode(despatchDetail.getSkuCode());
        preDistributionRecords.setIsCompleted(true);
        preDistributionRecords.setPid(despatch.getId());
        preDistributionRecords.setUnit(despatchDetail.getUnit());
        preDistributionRecords.setDespatchCode(despatch.getDespatchId());
        preDistributionRecords.setStatus((short) 0);//失败
        preDistributionRecords.setRowId(rowId);
        preDistributionRecords.setPreDistributionCode(codeService.code("PRD"));
        return preDistributionRecords;
    }

    DespatchAndWave desAddWave(Despatch despatch, Wave wave) {
        DespatchAndWave d = new DespatchAndWave();
        d.setDesId(despatch.getId());
        d.setDespatchCode(despatch.getDespatchId());
        d.setType(despatch.getType());
        d.setIsPreDistributed(despatch.getIsPreDistributed());
        d.setStatus(despatch.getStatus());
        d.setCustomerId(despatch.getCustomerId());
        d.setCustomerName(despatch.getCustomerName());
        d.setProvince(despatch.getProvince());
        d.setCity(despatch.getCity());
        d.setSite(despatch.getSite());
        d.setAddress(despatch.getAddress());
        d.setReceiverId(despatch.getReceiverId());
        d.setReceiverName(despatch.getReceiverName());
        d.setCreateTime(despatch.getCreateTime());
        d.setRequestDeliveryTime(despatch.getRequestDeliveryTime());
        d.setExpectSendTime(despatch.getExpectSendTime());
        d.setWarehouseId(despatch.getWarehouseId());
        d.setWarehouseName(despatch.getWarehouseName());
        d.setWarehouse(despatch.getWarehouse());
        d.setVerifyStatus(despatch.getVerifyStatus());
        d.setReviewerId(despatch.getReviewerId());
        d.setReviewerName(despatch.getReviewerName());
        d.setReviewerTime(despatch.getReviewerTime());
        d.setPriority(despatch.getPriority());
        d.setSettlerId(despatch.getSettlerId());
        d.setCarrierId(despatch.getCarrierId());
        d.setOrderId(despatch.getOrderId());
        d.setWaveId(wave.getId());
        d.setWaveCode(wave.getWaveId());
        d.setWaveRuleCode(wave.getWaveRuleCode());
        d.setWaveRuleName(wave.getWaveRuleName());
        return d;
    }

    //json转换接口
    public List<?> packingTojson(String stringJson) {
        JSONArray jsonObject = JSONArray.fromObject(stringJson);
        List<PicktaskMsgPacking> collection = (List<PicktaskMsgPacking>) JSONArray.
                toCollection(jsonObject, PicktaskMsgPacking.class);
        return collection;
    }

    //仓库库区转换
    public List<?> whTojson(String stringJson) {
        JSONArray jsonObject = JSONArray.fromObject(stringJson);
        List<PicktaskMsgWH> collection = (List<PicktaskMsgWH>) JSONArray.
                toCollection(jsonObject, PicktaskMsgWH.class);
        return collection;
    }

    //整箱设备转换
    public List<?> fullboxTojson(String stringJson) {
        JSONArray jsonObject = JSONArray.fromObject(stringJson);
        List<PicktaskMsgFullBox> collection = (List<PicktaskMsgFullBox>) JSONArray.
                toCollection(jsonObject, PicktaskMsgFullBox.class);
        return collection;
    }

    //拆零设备转化
    public List<?> pieceTojson(String stringJson) {
        JSONArray jsonObject = JSONArray.fromObject(stringJson);
        List<PicktaskMsgPiece> collection = (List<PicktaskMsgPiece>) JSONArray.
                toCollection(jsonObject, PicktaskMsgPiece.class);
        return collection;
    }

    //白箱发送wcs任务
    public WcsOutTask convertPickTasktoWcs(PickTask p, List<PickTaskDetail> pds, List<Despatch> deses) {
        WcsOutTask wcsOutTask = new WcsOutTask();
        OutboundTask outboundTask = new OutboundTask();
        wcsOutTask.setOutboundTaskDistributionList(new ArrayList<>());
        wcsOutTask.setOutboundDetailList(new ArrayList<>());
        outboundTask.setTaskId(p.getTaskId());
        outboundTask.setBatchCode(p.getWaveCode());
        outboundTask.setCreateTime(p.getCreateTime());
        outboundTask.setTaskType(1);//暂定1，暂不知何用
        outboundTask.setPriority((short) 2);//2普通，1优先，0紧急
        outboundTask.setTaskState(1);
        wcsOutTask.setOutboundTask(outboundTask);
        for (Despatch des : deses) {
            for (DespatchDetail detail : despatchDetailDAO.selectDetailByPid(des.getId())) {
                OutboundDetail outboundDetail = new OutboundDetail();
                outboundDetail.setStatus("待启动");
                outboundDetail.setTaskCode(p.getTaskId());
                outboundDetail.setDespatchCode(des.getDespatchId());
                outboundDetail.setDespatchId(des.getId());
                outboundDetail.setSkuName(detail.getSkuName());
                outboundDetail.setSkuCode(detail.getSkuCode());
                outboundDetail.setTotalNum(detail.getOrderCnt());
                outboundDetail.setUnit(detail.getUnit());
                wcsOutTask.getOutboundDetailList().add(outboundDetail);
            }
        }
        for (PickTaskDetail pd : pds) {
            OutboundTaskDistribution outboundTaskDistribution = new OutboundTaskDistribution();
            outboundTaskDistribution.setBoxCode(pd.getInventoryBalanceCode());
            outboundTaskDistribution.setDetachNum(pd.getPieceCnt());
            outboundTaskDistribution.setLocationCode(pd.getLocationCode());
            outboundTaskDistribution.setLocationId(pd.getLocationId());
            outboundTaskDistribution.setIsFullLoad((byte) (pd.getFullTag() ? 1 : 0));
            outboundTaskDistribution.setPicktaskDetailId(pd.getId());
            outboundTaskDistribution.setStartTime(new Date());
            wcsOutTask.getOutboundTaskDistributionList().add(outboundTaskDistribution);
        }
        return wcsOutTask;
    }
}
