package com.bupt.strategy.waveStrategy;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SearchDTO;
import com.bupt.mapper.DespatchDAO;
import com.bupt.mapper.DespatchDetailDAO;
import com.bupt.mapper.InventoryBalanceDAO;
import com.bupt.mapper.WaveRuleDAO;
import com.bupt.pojo.Despatch;
import com.bupt.pojo.DespatchDetail;
import com.bupt.pojo.WaveRule;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class InWarehouseTimeStrategy implements WaveStrategy {
    @Autowired
    private DespatchDAO despatchDAO;
    @Autowired
    private DespatchDetailDAO despatchDetailDAO;
    @Autowired
    private WaveRuleDAO waveRuleDAO;
    @Autowired
    private InventoryBalanceDAO inventoryBalanceDAO;

    @Override
    public List<Despatch> generateWaveByRule(WaveRule waveRule) {
        List<Despatch> despatchesGoal=new ArrayList<>();
        WaveRule reWaveRule = waveRuleDAO.selectByPrimaryKey(waveRule.getId());
        ScreenDTO<Despatch> screenDTO = new ScreenDTO<>();
        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setPage(0);
        searchDTO.setNum(100);
        screenDTO.setSearchDTO(searchDTO);
        Despatch despatch = waveRuleToDespatch(reWaveRule);
        screenDTO.setPojo(despatch);
        int soNum = 0;
        for (Despatch testDespatch : despatchDAO.screen(screenDTO)) {
            Double volum = 0.0, weight = 0.0;
            Integer sum = 0, minNum = 0;
            boolean tag = false;
            List<DespatchDetail> despatchDetails = despatchDetailDAO.selectDetailByPid(despatch.getId());
            for (DespatchDetail despatchDetail : despatchDetails) {
                volum += despatchDetail.getVolume();
                weight += despatchDetail.getWeight();
                sum += despatchDetail.getOrderCnt();
                minNum = Math.min(minNum, despatchDetail.getOrderCnt());
                if (inventoryBalanceDAO.selectBySkuIdOrderByInBoundTimeLimit(despatchDetail.getSkuId(),
                        despatch.getWarehouse()).getInBoundTime().before(reWaveRule.getInBoundTime())) {
                    tag = true;
                }
            }
            if (volum < reWaveRule.getVolumeLimit() && weight < reWaveRule.getWeightLimit() && sum < reWaveRule.getTotalNum() &&
                    minNum > reWaveRule.getMinimumSkuNum()&&tag) {
                despatchesGoal.add(testDespatch);
                soNum++;
            }
            if (soNum == reWaveRule.getOrderNumUpperLimit()) break;
        }
        if (soNum < reWaveRule.getOrderNumLowerLimit()) return null;
        return despatchesGoal;
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
