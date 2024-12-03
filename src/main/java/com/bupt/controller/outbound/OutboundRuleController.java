package com.bupt.controller.outbound;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.mapper.WaveRuleDAO;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.outbound.DespatchService;
import com.bupt.service.outbound.WaveRuleService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/outbound")
public class OutboundRuleController {
    @Autowired
    private WaveRuleService waveRuleService;
    @Autowired
    private DespatchService despatchService;

    //---------------------------------------------WaveRule 操作------------------------------------------------------------

    /**
     * @Name 新增波次规则和时间表
     * @Description 已完成
     * @Author LIWEMY
     * @Date 09:46 2022/6/23
     * @Param [headAndDetail]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/addWaveRuleAndTimers")
    public HttpResult<?> addWaveRuleAndTimers(@RequestBody @NotNull HeadAndDetail<WaveRule, WaveRuleTimer> headAndDetail) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, waveRuleService.addWaveRuleAndTimer(headAndDetail));
    }

    /**
     * @Name 删除波次规则和时间表
     * @Description 已完成
     * @Author LIWEMY
     * @Date 09:46 2022/6/23
     * @Param [waveRuleList]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/deleteWaveRuleAndTimer")
    public HttpResult<?> deleteWaveRuleAndTimer(@RequestBody @NotNull List<WaveRule> waveRuleList) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, waveRuleService.deleteWaveRuleAndTimer(waveRuleList));
    }

    /**
     * @Name 更新波次规则和时间表
     * @Description 已完成
     * @Author LIWEMY
     * @Date 09:46 2022/6/23
     * @Param [waveRuleList]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/updateWaveRuleAndTimer")
    public HttpResult<?> updateWaveRuleAndTimer(@RequestBody @NotNull HeadAndDetail<WaveRule, WaveRuleTimer> headAndDetail) {
        return waveRuleService.updateWaveRuleAndTimer(headAndDetail);
    }

    /**
     * @Name 筛选
     * @Description
     * @Author LIWEMY
     * @Date 21:13 2022/4/18
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/screenWaveRule")
    public HttpResult<?> screenWaveRule(@RequestBody @NotNull ScreenDTO<WaveRule> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, waveRuleService.screenWaveRule(screenDTO));
    }

    /**
     * @Name 筛选总数
     * @Description
     * @Author LIWEMY
     * @Date 21:13 2022/4/18
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/screenWaveRuleSum")
    public HttpResult<?> screenWaveRuleSum(@RequestBody @NotNull ScreenDTO<WaveRule> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, waveRuleService.screenWaveRuleNum(screenDTO));
    }

    @ResponseBody
    @PostMapping("/screenWaveRuleTimer")
    public HttpResult<?> screenWaveRuleTimer(@RequestBody @NotNull ScreenDTO<WaveRuleTimer> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, waveRuleService.screenWaveRuleTimers(screenDTO));
    }

    @ResponseBody
    @PostMapping("/screenWaveRuleTimerSum")
    public HttpResult<?> screenWaveRuleTimerSum(@RequestBody @NotNull ScreenDTO<WaveRuleTimer> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, waveRuleService.screenWaveRuleTimersNum(screenDTO));
    }

    /**
     * @Name 查看WaveRuleTimer
     * @Description 已完成
     * @Author LIWEMY
     * @Date 13:48 2022/6/23
     * @Param [waveRule]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/screenTimer")
    public HttpResult<?> selectTimer(@RequestBody @NotNull ScreenDTO<WaveRuleTimer> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, waveRuleService.screenWaveRuleTimers(screenDTO));
    }

    @ResponseBody
    @PostMapping("/screenTimerSum")
    public HttpResult<?> screenTimerSum(@RequestBody @NotNull ScreenDTO<WaveRuleTimer> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, waveRuleService.screenWaveRuleTimers(screenDTO));
    }

    @ResponseBody
    @PostMapping("/runAllWaveRule")
    public HttpResult<?> runAllWaveRule() throws InterruptedException {
        return waveRuleService.runAllWaveRule();
    }

    @ResponseBody
    @PostMapping("/runWaveRule")
    public HttpResult<?> runWaveRule(@RequestBody @NotNull WaveRule waveRule) throws Exception {
        return waveRuleService.runOneWaveRule(waveRule);
    }

    @ResponseBody
    @PostMapping("/stopAllWaveRule")
    public HttpResult<?> stopAllWaveRule() throws InterruptedException {
        return waveRuleService.stopAllWaveRule();
    }

    @ResponseBody
    @PostMapping("/stopWaveRule")
    public HttpResult<?> stopWaveRule(@RequestBody @NotNull WaveRule waveRule) throws InterruptedException {
        return waveRuleService.stopOneWaveRule(waveRule);
    }

    @ResponseBody
    @PostMapping("/searchWaveByWaveRule")
    public HttpResult<?> searchWaveByWaveRule(@RequestBody @NotNull WaveRule waveRule) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, waveRuleService.selectWavesByWaverule(waveRule));
    }
}
