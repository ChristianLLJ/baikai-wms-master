package com.bupt.controller.outbound;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.controller.BaseController;
import com.bupt.pojo.Despatch;
import com.bupt.pojo.DespatchDetail;
import com.bupt.pojo.ExPicking;
import com.bupt.pojo.ExPickingDetail;
import com.bupt.result.HttpResult;
import com.bupt.service.outbound.ExPickingService;
import com.bupt.service.outbound.impl.DespatchImplNewService;
import com.bupt.service.outbound.impl.ExPickingServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/expicking")
public class ExPickingController extends BaseController<ExPickingServiceImpl, ExPicking, ExPickingDetail> {
    @Autowired
    private ExPickingService exPickingService;

    /**
     * @Name wcs传装箱单, 发运订单修改状态
     * @Description todo 测试
     * @Author LIWEMY
     * @Date 13:41 2022/11/26
     * @Param [headAndDetail]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/getExpickingAndDetail")
    public HttpResult<?> getExpickingAndDetail(@RequestBody List<HeadAndDetail<ExPicking, ExPickingDetail>> headAndDetails) {
        return exPickingService.getExPickingAndDetail(headAndDetails);
    }

    /**
     * @Name 检测装箱是否完毕，sku 数量是否与发运订单需求相等,定时/手动触发
     * @Description todo 测试
     * @Author LIWEMY
     * @Date 14:34 2022/11/26
     * @Param [headAndDetails]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/checkExpForDes")
    public HttpResult<?> checkExpForDes() {
        return exPickingService.checkExPickingForDespatch();
    }

    /**
     * @Name 名下发运订单全部装箱完毕, 更改wave状态为 “已装箱”
     * @Description todo 测试
     * @Author LIWEMY
     * @Date 14:45 2022/11/26
     * @Param []
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/checkExpForWav")
    public HttpResult<?> checkExpForWav() {
        return exPickingService.checkExPickingForWave();
    }

    /**
     * @Name 根据DespatchId 查看装箱位置，若未进行合单，返回空
     * @Description todo 测试
     * @Author LIWEMY
     * @Date 14:46 2022/11/26
     * @Param [exPicking]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/searchMergePosition")
    public HttpResult<?> searchMergePosition(@RequestBody ExPicking exPicking) {
        return exPickingService.searchMergePosition(exPicking);
    }

    /**
     * @Name 设置合单位置，非必要操作
     * @Description rf? todo 测试
     * @Author LIWEMY
     * @Date 14:47 2022/11/26
     * @Param [exPicking]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/setMergePosition")
    public HttpResult<?> setMergePosition(@RequestBody ExPicking exPicking) {
        return exPickingService.setMergePosition(exPicking);
    }

    /**
     * @Name 重置合单位置
     * @Description rf?todo 测试
     * @Author LIWEMY
     * @Date 14:47 2022/11/26
     * @Param [exPicking]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/resetMergePosition")
    public HttpResult<?> resetMergePosition(@RequestBody ExPicking exPicking) {
        return exPickingService.resetMergePosition(exPicking);
    }

    /**
     * @Name 合单, 挪到合单位置后，更新合单状态
     * @Description rf?todo 测试
     * @Author LIWEMY
     * @Date 14:47 2022/11/26
     * @Param [exPickings]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/mergeExpicking")
    public HttpResult<?> mergeExpicking(@RequestBody List<ExPicking> exPickings) {
        return exPickingService.mergeExpicking(exPickings);
    }

}
