package com.bupt.strategy.waveStrategy;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SearchDTO;
import com.bupt.mapper.DespatchDAO;
import com.bupt.mapper.DespatchDetailDAO;
import com.bupt.mapper.WaveRuleDAO;
import com.bupt.pojo.Despatch;
import com.bupt.pojo.DespatchDetail;
import com.bupt.pojo.WaveRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
@Component
public class TimedTaskStrategy implements WaveStrategy {
    @Autowired
    private DespatchDAO despatchDAO;
    @Autowired
    private DespatchDetailDAO despatchDetailDAO;
    @Autowired
    private WaveRuleDAO waveRuleDAO;
    private static TimedTaskStrategy timedTaskStrategy;

    @PostConstruct
    private void init() {
        timedTaskStrategy = this;
        timedTaskStrategy.despatchDAO = this.despatchDAO;
        timedTaskStrategy.despatchDetailDAO=this.despatchDetailDAO;
        timedTaskStrategy.waveRuleDAO=this.waveRuleDAO;
    }

    @Override
    public List<Despatch> generateWaveByRule(WaveRule waveRule) {
        WaveRule reWaveRule = timedTaskStrategy.waveRuleDAO.selectByPrimaryKey(waveRule.getId());
        ScreenDTO<Despatch> screenDTO = new ScreenDTO<>();
        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setPage(0);
        searchDTO.setNum(100);
        screenDTO.setSearchDTO(searchDTO);
        Despatch despatch = waveRuleToDespatch(reWaveRule);
        screenDTO.setPojo(despatch);
        List<Despatch> despatchList = timedTaskStrategy.despatchDAO.screen(screenDTO);
        return despatchList;
    }


    public Despatch waveRuleToDespatch(WaveRule waveRule) {
        Despatch despatch = new Despatch();
        if (waveRule.getOrderStatus() != null) {
            despatch.setStatus(waveRule.getOrderStatus());
        }
        if (waveRule.getOrderType() != null) {
            despatch.setType(waveRule.getOrderType());
        }
        if (waveRule.getClientId() != null) {
            despatch.setCustomerId(waveRule.getClientId());
        }
        if (waveRule.getCarrierId() != null) {
            despatch.setCarrierId(waveRule.getCarrierId());
        }
        if (waveRule.getWarehouseCode() != null) {
            despatch.setWarehouse(waveRule.getWarehouseCode());
        }
        despatch.setStatus((short) 1);
        return despatch;
    }
}
