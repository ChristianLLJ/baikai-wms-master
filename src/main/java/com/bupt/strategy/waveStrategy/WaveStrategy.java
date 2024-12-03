package com.bupt.strategy.waveStrategy;

import com.bupt.pojo.Despatch;
import com.bupt.pojo.WaveRule;

import java.util.List;

public interface WaveStrategy {
    List<Despatch> generateWaveByRule(WaveRule waveRule);
}
