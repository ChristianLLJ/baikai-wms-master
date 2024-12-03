package com.bupt.controller.authority;

import com.bupt.DTO.FrontId;
import com.bupt.DTO.wcs.Cartonlist;
import com.bupt.DTO.wcs.WcsOnshelfSend;
import com.bupt.controller.BaseController;
import com.bupt.mapper.*;
import com.bupt.pojo.*;
import com.bupt.result.BaokaiHttpResult;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.authority.CodeService;
import com.bupt.service.inbound.impl.OnshelfServiceImpl;
import com.bupt.service.outbound.BaokaiOutWcsService;
import com.bupt.service.outbound.PickupBlackService;
import com.bupt.service.util.HttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RestController
public class InitController extends BaseController<OnshelfServiceImpl, Onshelf, OnshelfDetail> {
    @Autowired
    private AInitMysqlDAO aInitMysqlDAO;
    @Autowired
    private PickTaskDAO pickTaskDAO;
    @Autowired
    private OnshelfDAO onshelfDAO;
    @Autowired
    private BaokaiOutWcsService baokaiOutWcsService;
    @Autowired
    private HttpService httpService;
    @Autowired
    private OnshelfDetailDAO onshelfDetailDAO;
    @Value("${wcs.baokaiIp}")
    private String baokaiIp;
    @Autowired
    private WarehouseLocationDAO warehouseLocationDAO;
    @Autowired
    private PickupBlackService pickupBlackService;
    @Autowired
    private WaveDAO waveDAO;
    @Autowired
    private CodeService codeService;
    @CrossOrigin
    @ResponseBody
    @Transactional
    @PostMapping("/initSystem")
    public HttpResult<?> initSystem(@RequestBody List<FrontId> layers) {
        if (layers.isEmpty() || layers == null) return HttpResult.of(HttpResultCodeEnum.LAYERS_NULL);
        List<PickTask> pickTasks = pickTaskDAO.selectTaskStatusAfterCreated();
        List<Onshelf> onshelves = onshelfDAO.selectStateAfterCreated();
        if (!(pickTasks.isEmpty() || pickTasks == null) || !(onshelves.isEmpty() || onshelves == null)) {
            return HttpResult.of(HttpResultCodeEnum.NOT_FINISHED);
        }
        aInitMysqlDAO.init();
        for (FrontId layer : layers) {
            switch (layer.getId()) {
                case 2: {
                    aInitMysqlDAO.initTwo();
                    Wave w2 = new Wave();
                    w2.setId(2);
                    w2.setWaveId(codeService.code("WAV"));
                    waveDAO.updateByPrimaryKeySelective(w2);
                    PickTask p2 = new PickTask();
                    p2.setId(2);
                    p2.setWaveCode(w2.getWaveId());
                    pickTaskDAO.updateByPrimaryKeySelective(p2);
                    baokaiOutWcsService.sendWaveToWcs(w2);
                    baokaiOutWcsService.lkcktask(p2);
                    pickupBlackService.sendPickTasktoShuttleWcs(p2);
                    pickupBlackService.checkAndEndPickTaskResult();
                    Onshelf onshelf=new Onshelf();
                    onshelf.setId(2);
                    baokaiWcsGetInboundTask(onshelfDetailDAO.selectByOnshelfId(onshelf));
                    break;
                }
                case 3: {
                    aInitMysqlDAO.initThree();
                    Wave w3 = new Wave();
                    w3.setId(3);
                    w3.setWaveId(codeService.code("WAV"));
                    waveDAO.updateByPrimaryKeySelective(w3);
                    PickTask p3 = new PickTask();
                    p3.setId(3);
                    p3.setWaveCode(w3.getWaveId());
                    pickTaskDAO.updateByPrimaryKeySelective(p3);
                    baokaiOutWcsService.sendWaveToWcs(w3);
                    baokaiOutWcsService.lkcktask(p3);
                    pickupBlackService.sendPickTasktoShuttleWcs(p3);
                    break;
                }
                case 4: {
                    aInitMysqlDAO.initFour();
                    Wave w4 = new Wave();
                    w4.setId(4);
                    w4.setWaveId(codeService.code("WAV"));
                    waveDAO.updateByPrimaryKeySelective(w4);
                    PickTask p4 = new PickTask();
                    p4.setId(4);
                    p4.setWaveCode(w4.getWaveId());
                    pickTaskDAO.updateByPrimaryKeySelective(p4);
                    baokaiOutWcsService.sendWaveToWcs(w4);
                    baokaiOutWcsService.lkcktask(p4);
                    pickupBlackService.sendPickTasktoShuttleWcs(p4);
                    break;
                }
            }
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    public HttpResult<?> baokaiWcsGetInboundTask( List<OnshelfDetail> onshelfDetails) {
        for (int i = 0; i < onshelfDetails.size(); i++) {
            onshelfDetails.set(i, onshelfDetailDAO.selectByPrimaryKey(onshelfDetails.get(i).getId()));
            if (onshelfDetails.get(i).getOnshelfState() == null || onshelfDetails.get(i).getOnshelfState() != 1)
                return HttpResult.of(HttpResultCodeEnum.ONSHELF_DETAIL_NOT_IN_UNSEND);
        }
        List<BaokaiHttpResult> baokaiHttpResults = new ArrayList<>();
        for (int i = 0; i < onshelfDetails.size(); i++) {
            onshelfDetails.get(i).setOnshelfState(2);
            onshelfDetails.get(i).setOnshelfTaskExecuteDevice("wcs");
            List<OnshelfDetail> onshelfDetails1 = new LinkedList<>();
            onshelfDetails1.add(onshelfDetails.get(i));
            super.service.updDetail(onshelfDetails1);
            WcsOnshelfSend wcsOnshelfSend = new WcsOnshelfSend();
            wcsOnshelfSend.setPalletid(onshelfDetails.get(i).getContainerBarcode());
            WarehouseLocation loc = warehouseLocationDAO.selectByPrimaryKey(onshelfDetails.get(i).getFactLocationId());
            wcsOnshelfSend.setOrigination(String.format("%03d", loc.getRows()) + "-" +
                    String.format("%03d", loc.getColumns()) + "-" +
                    String.format("%03d", loc.getLayer()));
            wcsOnshelfSend.setTarget("LK");
            wcsOnshelfSend.setReqcode(onshelfDetails.get(i).getTraceCode());//todo 入库跟踪号wcs对接
            wcsOnshelfSend.setCartonlist(new ArrayList<>());
            wcsOnshelfSend.getCartonlist().add(new Cartonlist(onshelfDetails.get(i).getSkuNum().intValue(),
                    onshelfDetails.get(i).getSkuCode()));
            BaokaiHttpResult<?> bk = httpService.httpResponse(baokaiIp + "/api/bkcellar/recroute/asnrgbind", wcsOnshelfSend);
            baokaiHttpResults.add(bk);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, baokaiHttpResults);
    }
}
