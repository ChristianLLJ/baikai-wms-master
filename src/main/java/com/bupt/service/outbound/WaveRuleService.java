package com.bupt.service.outbound;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.pojo.Wave;
import com.bupt.pojo.WaveRule;
import com.bupt.pojo.WaveRuleTimer;
import com.bupt.result.HttpResult;

import java.util.List;

public interface WaveRuleService {
    Integer addWaveRuleAndTimer(HeadAndDetail<WaveRule, WaveRuleTimer> headAndDetail);

    Integer addWaveRule(WaveRule waveRule);

    String deleteWaveRuleAndTimer(List<WaveRule> waveRuleList);

    HttpResult<?> updateWaveRuleAndTimer(HeadAndDetail<WaveRule, WaveRuleTimer> headAndDetail);

    List<WaveRule> screenWaveRule(ScreenDTO<WaveRule> screenDTO);

    Integer screenWaveRuleNum(ScreenDTO<WaveRule> screenDTO);

    Integer addWaveRuleTimer(WaveRuleTimer waveRuleTimer);

    List<WaveRuleTimer> screenWaveRuleTimers(ScreenDTO<WaveRuleTimer> screenDTO);

    Integer screenWaveRuleTimersNum(ScreenDTO<WaveRuleTimer> screenDTO);

    List<Wave> selectWavesByWaverule(WaveRule waveRule);

    HttpResult<?> runAllWaveRule();

    HttpResult<?> stopAllWaveRule();

    HttpResult<?> runOneWaveRule(WaveRule waveRule) throws Exception;

    HttpResult<?> stopOneWaveRule(WaveRule inWaveRule);

}
