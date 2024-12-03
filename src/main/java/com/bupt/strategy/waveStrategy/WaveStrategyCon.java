package com.bupt.strategy.waveStrategy;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.pojo.Despatch;
import com.bupt.pojo.DespatchDetail;
import com.bupt.pojo.WaveRule;

import java.util.List;

public class WaveStrategyCon {
    private WaveStrategy waveStrategy;

    public WaveStrategyCon(WaveStrategy waveStrategy) {
        this.waveStrategy = waveStrategy;
    }

    public List<Despatch> generateWaveByStrategy(WaveRule waveRule) {
        return waveStrategy.generateWaveByRule(waveRule);
    }

}