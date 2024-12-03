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
public class QuantitativeTaskStrategy implements WaveStrategy {
    @Autowired
    private DespatchDAO despatchDAO;
    @Autowired
    private DespatchDetailDAO despatchDetailDAO;
    @Autowired
    private WaveRuleDAO waveRuleDAO;

    private static QuantitativeTaskStrategy quantitativeTaskStrategy;

    @PostConstruct
    private void init() {
        quantitativeTaskStrategy = this;
        quantitativeTaskStrategy.despatchDAO = this.despatchDAO;
        quantitativeTaskStrategy.despatchDetailDAO=this.despatchDetailDAO;
        quantitativeTaskStrategy.waveRuleDAO=this.waveRuleDAO;
    }

    @Override
    public List<Despatch> generateWaveByRule(WaveRule waveRule) {
        List<Despatch> despatchesGoal=new ArrayList<>();
        ScreenDTO<Despatch> screenDTO = new ScreenDTO<>();
        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setPage(0);
        searchDTO.setNum(100);
        screenDTO.setSearchDTO(searchDTO);
        Despatch despatch = waveRuleToDespatch(waveRule);
        screenDTO.setPojo(despatch);
        List<Despatch> despatchList = quantitativeTaskStrategy.despatchDAO.screen(screenDTO);
        int size = despatchList.size();
        Integer upLimit = waveRule.getOrderNumUpperLimit();
        Integer lowLimit = waveRule.getOrderNumLowerLimit();
        //订单量>上限
        if (size >= upLimit) {
            for (int i = 0; i < upLimit; i++) {
                Despatch reDespatch = despatchList.get(i);
                despatchesGoal.add(reDespatch);
            }
            return despatchesGoal;
        } else if (size <= upLimit && size >= lowLimit) {
            for (int i = 0; i < size; i++) {
                Despatch reDespatch = despatchList.get(i);
                despatchesGoal.add(reDespatch);
            }
            return despatchesGoal;
        } else return null;
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
