package com.bupt.service.outbound.impl;

import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SearchDTO;
import com.bupt.DTO.WaveScreenDTO;
import com.bupt.mapper.*;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.BaseService;
import com.bupt.service.authority.impl.CodeServiceImpl;
import com.bupt.service.outbound.DespatchService;
import com.bupt.statePattern.despatchState.DespatchState;
import com.bupt.statePattern.examineState.ExamineState;
import com.bupt.strategy.waveStrategy.WaveStrategy;
import com.bupt.strategy.waveStrategy.WaveStrategyCon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DespatchServiceImpl extends BaseService<Despatch, DespatchDetail, DespatchDAO, DespatchDetailDAO> implements DespatchService {
    @Autowired
    private DespatchDAO despatchDAO;
    @Autowired
    private DespatchDetailDAO despatchDetailDAO;
    @Autowired
    private DespatchWaveDAO despatchWaveDAO;
    @Autowired
    private ReplenishDAO replenishDAO;
    @Autowired
    private InventoryBalanceDAO inventoryBalanceDAO;
    @Autowired
    private WaveDAO waveDAO;
    @Autowired
    private WarehouseLocationDAO warehouseLocationDAO;
    @Autowired
    private PickTaskShortageDAO pickTaskShortageDAO;
    @Autowired
    private WaveRuleDAO waveRuleDAO;
    @Autowired
    private StaffDAO staffDAO;
    @Autowired
    private CodeServiceImpl codeService;

    @Override
    public List<Despatch> serachDespatchList(List<Despatch> despatches) {
        List<Despatch> despatchList = new ArrayList<>();
        despatches.forEach(e -> {
            despatchList.add(despatchDAO.selectByPrimaryKey(e.getId()));
        });
        return despatchList;
    }

    @Override
    public List<Despatch> screenDespatch(ScreenDTO<Despatch> screenDTO) {
        SearchDTO searchDTO = screenDTO.getSearchDTO();
        if (searchDTO != null) {
            searchDTO.setPage((searchDTO.getPage() - 1) * searchDTO.getNum());
            screenDTO.setSearchDTO(searchDTO);
        }
        if (screenDTO.getScreen() != null) screenDTO.setScreen(xX2x_x(screenDTO.getScreen()));
        List<Despatch> screen = despatchDAO.screen(screenDTO);
        List<Despatch> despatchList = new ArrayList<>();
        screen.forEach(e -> despatchList.add(despatchDAO.selectByPrimaryKey(e.getId())));
        return despatchList;
    }

    @Override
    public Integer screenDespatchNum(ScreenDTO<Despatch> screenDTO) {
        return despatchDAO.numScreen(screenDTO);

    }

    /*
     * 发运订单合并波次，同种 sku 统计 数量 , 未释放订单不能
     * */
    @Override
    public List<DespatchDetail> merageDespatch(List<Despatch> despatchList) {
        List<DespatchDetail> despatchDetailList = new ArrayList<>();
        if (despatchList.size() <= 1) {
            if (despatchList.size() == 1) {
                despatchDetailList.addAll(despatchDetailDAO.selectDetailByPid(despatchList.get(0).getId()));
            }
            return despatchDetailList;
        }
        for (Despatch despatch : despatchList) {
            /*if (despatchDAO.selectByPrimaryKey(despatch.getId()).getStatus() != (short) 2) {
                return null;
            }*/
            despatchDetailList.addAll(despatchDetailDAO.selectDetailByPid(despatch.getId()));
        }
        for (Despatch despatch : despatchList) {
            despatch.setStatus((short) 1);
            despatchDAO.updateByPrimaryKeySelective(despatch);
        }
        despatchDetailList.sort(Comparator.comparing(DespatchDetail::getSkuCode));
        List<DespatchDetail> despatchDetails = new ArrayList<>();
        int size = despatchDetailList.size();
        for (int i = 0; i < size; i++) {
            DespatchDetail despatchDetail = despatchDetailList.get(i);
            for (int j = i + 1; j < size; j++) {
                DespatchDetail despatchDetail1 = despatchDetailList.get(j);
                if (despatchDetail.getSkuCode().equals(despatchDetail1.getSkuCode())) {
                    despatchDetail.setOrderCnt(despatchDetail.getOrderCnt() + despatchDetail1.getOrderCnt());
                    despatchDetail.setWeight(despatchDetail.getWeight() + despatchDetail1.getWeight());
                    despatchDetail.setVolume(despatchDetail1.getVolume() + despatchDetail.getVolume());
                } else {
                    i = j - 1;
                    break;
                }
            }
            System.out.println(i + "--------------");
            if (i != (size - 1) || !despatchDetailList.get(i).getSkuCode().equals(despatchDetailList.get(i - 1).getSkuCode())) {
                despatchDetails.add(despatchDetail);
            }
        }
        return despatchDetails;
    }

    @Override
    public List<Despatch> searchAllDespatchByWaveId(ScreenDTO<DespatchWave> screenDTO) {
        Integer num = screenDTO.getSearchDTO().getNum();
        Integer page = screenDTO.getSearchDTO().getPage();
        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setPage(page);
        searchDTO.setNum(100);
        screenDTO.setSearchDTO(searchDTO);
        List<DespatchWave> screen = screenDespatchWave(screenDTO);
        List<Despatch> despatches = new ArrayList<>();
        screen.forEach(e -> despatches.add(despatchDAO.selectByPrimaryKey(e.getDespatchId())));
        List<Despatch> despatchList = new ArrayList<>();
        num = num < despatches.size() ? num : despatches.size();
        for (int i = 0; i < num; i++) {
            despatchList.add(despatches.get((page - 1) * num + i));
        }
        return despatchList;
    }

    @Override
    public Integer searchAllDespatchByWaveIdNum(ScreenDTO<DespatchWave> screenDTO) {
        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setPage(screenDTO.getSearchDTO().getPage());
        searchDTO.setNum(100);
        screenDTO.setSearchDTO(searchDTO);
        List<DespatchWave> screen = screenDespatchWave(screenDTO);
        return screen.size();
    }

    @Override
    public HttpResult<?> deleteDespatchDetail(DespatchDetail despatchDetail) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, despatchDetailDAO.deleteByPrimaryKey(despatchDetail.getId()));
    }

    @Override
    public HttpResult<?> updateDespatchDetail(DespatchDetail despatchDetail) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, despatchDetailDAO.updateByPrimaryKeySelective(despatchDetail));
    }

    @Override
    public List<DespatchDetail> selectDetailByDespatchId(Integer despatchId) {
        return despatchDetailDAO.selectDetailByPid(despatchId);
    }

    @Override
    public List<DespatchDetail> screenDespatchDetail(ScreenDTO<DespatchDetail> screenDTO) {
        SearchDTO searchDTO = screenDTO.getSearchDTO();
        searchDTO.setPage((searchDTO.getPage() - 1) * searchDTO.getNum());
        screenDTO.setSearchDTO(searchDTO);
        List<DespatchDetail> screen = despatchDetailDAO.screen(screenDTO);
        return screen;
    }

    @Override
    public Integer screenDespatchDetailNum(ScreenDTO<DespatchDetail> screenDTO) {
        return despatchDetailDAO.numScreen(screenDTO);
    }


    @Override
    public Integer addWave(Wave wave) {
        wave.setWaveId(codeService.code("WAV"));
        wave.setCreateTime(new Date());
        waveDAO.insertSelective(wave);
        return wave.getId();
    }

    @Override
    public WaveRule newWaveByDespatchList() {
        WaveRule waveRule = new WaveRule();
        waveRule.setId(9999);
        waveRule.setWaveRuleName("人工分波次");
        waveRule.setWaveRuleCode("ArtificialWaveStrategy");
        return waveRule;
    }

    @Override
    public WaveRule newWaveByDespatch() {
        WaveRule waveRule = new WaveRule();
        waveRule.setId(9999);
        waveRule.setWaveRuleName("人工分波次");
        waveRule.setWaveRuleCode("ArtificialWaveStrategy");
        return waveRule;
    }

    @Override
    public HttpResult<?> generateWave(List<Despatch> despatchList, WaveRule waveRule) {
        if (despatchList == null || despatchList.isEmpty()) {
            return HttpResult.of(HttpResultCodeEnum.DESPATCH_CANTBEEN_FINDED);
        }
        for (Despatch despatch : despatchList) {
            Despatch d = despatchDAO.selectByPrimaryKey(despatch.getId());
            int i = d.getStatus().intValue(), j = d.getVerifyStatus().intValue();
            DespatchState despatchState = new DespatchState(i);
            ExamineState examineState = new ExamineState(j);
            if (!examineState.judge("审核通过", j)) return HttpResult.of(HttpResultCodeEnum.NOT_EXAMINE_ERROR);
            if (!despatchState.judge("已创建", i)) return HttpResult.of(HttpResultCodeEnum.DESPATCH_CANTBEEN_WAVE);
        }
        List<DespatchDetail> despatchDetails = merageDespatch(despatchList);
        if (despatchDetails.isEmpty()) {
            return HttpResult.of(HttpResultCodeEnum.DESPATCH_HAVENOTBEEN_RELASE);
        }
        Despatch des = despatchDAO.selectByPrimaryKey(despatchList.get(0).getId());
        //1：创建成功
        Wave wave = new Wave();
        wave.setVolume(0.0);
        wave.setWeight(0.0);
        wave.setStatus(1);//已创建
        wave.setWaveRuleId(waveRule.getId());
        wave.setWaveRuleCode(waveRule.getWaveRuleCode());
        wave.setWaveRuleName(waveRule.getWaveRuleName());
        wave.setWarehouse(des.getWarehouse());
        wave.setWarehouseName(des.getWarehouseName());
        despatchDetails.forEach(e -> {
            wave.setVolume(wave.getVolume() + e.getVolume());
            wave.setWeight(wave.getWeight() + e.getWeight());
        });
        Integer waveId = addWave(wave);
        addDespatchWave(despatchList, waveId);
        despatchList.forEach(e -> {
            e.setStatus((short) 2);
            despatchDAO.updateByPrimaryKeySelective(e);
        });
        System.out.println("合成波次成功");
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, waveId);
    }

    @Override
    public HttpResult<?> oneDespatchWaveManual(List<Despatch> despatchList, WaveRule waveRule) {
        if (despatchList == null || despatchList.isEmpty()) {
            return HttpResult.of(HttpResultCodeEnum.DESPATCH_CANTBEEN_FINDED);
        }
        List<Despatch> despatches = new ArrayList<>();
        for (Despatch despatch : despatchList) {
            Despatch d = despatchDAO.selectByPrimaryKey(despatch.getId());
            int i = d.getStatus().intValue(), j = d.getVerifyStatus().intValue();
            DespatchState despatchState = new DespatchState(i);
            ExamineState examineState = new ExamineState(j);
            if (!examineState.judge("审核通过", j)) return HttpResult.of(HttpResultCodeEnum.NOT_EXAMINE_ERROR);
            if (!despatchState.judge("已创建", i)) return HttpResult.of(HttpResultCodeEnum.DESPATCH_CANTBEEN_WAVE);
            despatches.add(d);
        }
        List<Integer> waveIds = new ArrayList<>();
        List<DespatchDetail> despatchDetails = new ArrayList<>();
        for (Despatch despatch : despatches) {
            despatchDetails.addAll(despatchDetailDAO.selectDetailByPid(despatch.getId()));
            if (despatchDetails.isEmpty()) {
                return HttpResult.of(HttpResultCodeEnum.DESPATCH_HAVENOTBEEN_RELASE);
            }
            //1：创建成功
            Wave wave = new Wave();
            wave.setVolume(0.0);
            wave.setWeight(0.0);
            wave.setStatus(1);
            wave.setWaveRuleId(waveRule.getId());
            wave.setWaveRuleCode(waveRule.getWaveRuleCode());
            wave.setWaveRuleName(waveRule.getWaveRuleName());
            wave.setWarehouse(despatch.getWarehouse());
            wave.setWarehouseName(despatch.getWarehouseName());
            despatchDetails.forEach(e -> {
                wave.setVolume(wave.getVolume() + e.getVolume());
                wave.setWeight(wave.getWeight() + e.getWeight());
            });
            Integer waveId = addWave(wave);
            List<Despatch> despatches1 = new ArrayList<>();
            despatches1.add(despatch);
            addDespatchWave(despatches1, waveId);
            despatch.setStatus((short) 2);
            despatchDAO.updateByPrimaryKeySelective(despatch);
            despatchDetails.clear();
            waveIds.add(waveId);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, waveIds);
    }

    @Transactional
    @Override
    public Wave generateWaveByStrategy(WaveRule waveRule) {
        WaveRule reWaveRule = waveRuleDAO.selectByPrimaryKey(waveRule.getId());
        try {
            Class c = Class.forName("com.bupt.strategy.waveStrategy." + reWaveRule.getWaveRuleCode());
            WaveStrategyCon waveStrategyCon = new WaveStrategyCon((WaveStrategy) c.newInstance());
            waveStrategyCon.generateWaveByStrategy(reWaveRule);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public HttpResult<?> deleteWave(Wave wave) {
        List<DespatchWave> despatchWaves = despatchWaveDAO.selectDetailByPid(wave.getId());
        for (DespatchWave e : despatchWaves) {
            // TODO: 2022/7/4 波次删除暂时定为未分配时才能操作，后续添加取消分配等操作
            Despatch despatch = despatchDAO.selectByPrimaryKey(e.getDespatchId());
            if (despatch.getStatus() > 2 || despatch.getIsPreDistributed() != 0) {
                return HttpResult.of(HttpResultCodeEnum.WAVE_CANTBEEN_DELETED);
            }
        }
        waveDAO.deleteByPrimaryKey(wave.getId());
        despatchWaves.forEach(e -> {
            Despatch despatch = new Despatch();
            despatch.setId(e.getDespatchId());
            despatch.setStatus((short) 1);
            despatchDAO.updateByPrimaryKeySelective(despatch);
            deleteDespatchWave(e);
        });
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public HttpResult<?> examineWave(List<Wave> waves) {
        List<Wave> waveList = new ArrayList<>();
        boolean tag = false;
        for (Wave e : waves) {
            Wave wave = waveDAO.selectByPrimaryKey(e.getId());
            if (wave.getCheckStatus() > 1) {
                waveList.add(wave);
                tag = true;
            } else {
                wave.setId(e.getId());
                wave.setCheckStatus((short) 2);//todo 状态模式
                wave.setReviewerId(e.getReviewerId());
                wave.setReviewerName(staffDAO.selectByPrimaryKey(e.getReviewerId()).getStaffName());
                wave.setReviewerTime(new Date());
                waveDAO.updateByPrimaryKeySelective(wave);
            }
        }
        return !tag ? HttpResult.of(HttpResultCodeEnum.SUCCESS) : HttpResult.of(HttpResultCodeEnum.FAIL_CHECK_ALL, waveList);
    }

    @Override
    public HttpResult<?> examineWaveReject(List<Wave> waves) {
        List<Wave> waveList = new ArrayList<>();
        boolean tag = false;
        for (Wave e : waves) {
            Wave wave = waveDAO.selectByPrimaryKey(e.getId());
            if (wave.getCheckStatus() > 1) {
                waveList.add(wave);
                tag = true;
            } else {
                wave.setId(e.getId());
                wave.setCheckStatus((short) 11);//todo 状态模式
                wave.setReviewerId(e.getReviewerId());
                wave.setReviewerName(staffDAO.selectByPrimaryKey(e.getReviewerId()).getStaffName());
                wave.setReviewerTime(new Date());
                waveDAO.updateByPrimaryKeySelective(wave);
            }
        }
        return !tag ? HttpResult.of(HttpResultCodeEnum.SUCCESS) : HttpResult.of(HttpResultCodeEnum.FAIL_CHECK_ALL, waveList);
    }

    @Override
    public HttpResult<?> RejectDespatch(List<Wave> waves) {
        for (Wave e : waves) {
            despatchWaveDAO.selectDespatchIdByWaveId(e.getId()).forEach(k -> {
                Despatch d = new Despatch();
                d.setId(k.getId());
                d.setStatus((short) 1);//回退，更改状态为"已创建"
            });
            despatchWaveDAO.deleteByWaveId(e.getId());//删除波次与发运订单关联表
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public HttpResult<?> updateWaveAndDespatch(Wave wave, List<Despatch> despatchList) {
        wave.setVolume(0.0);
        wave.setWeight(0.0);
        for (Despatch e : despatchList) {
            // 波次删除暂时定为未分配时,并且未审核 才能操作，后续添加取消分配等操作
            Despatch despatch = despatchDAO.selectByPrimaryKey(e.getId());
            if (despatch.getStatus() > 2) {
                return HttpResult.of(HttpResultCodeEnum.WAVE_CANTBEEN_UPDATE);
            }
        }
        despatchWaveDAO.selectDespatchIdByWaveId(wave.getId()).forEach(e -> {
            Despatch despatch = despatchDAO.selectByPrimaryKey(e.getDespatchId());
            despatchDetailDAO.selectDetailByPid(e.getId()).forEach(k -> {
                wave.setVolume(wave.getVolume() + k.getVolume());
                wave.setWeight(wave.getWeight() + k.getWeight());
            });
        });
        despatchList.forEach(e -> {
            DespatchWave despatchWave = new DespatchWave();
            despatchWave.setWaveId(wave.getId());
            despatchWave.setDespatchId(e.getId());
            despatchWaveDAO.insertSelective(despatchWave);
            Despatch despatch = new Despatch();
            despatch.setId(e.getId());
            despatch.setStatus((short) 2);
            despatchDAO.updateByPrimaryKeySelective(despatch);
            despatchDetailDAO.selectDetailByPid(e.getId()).forEach(k -> {
                wave.setVolume(wave.getVolume() + k.getVolume());
                wave.setWeight(wave.getWeight() + k.getWeight());
            });
        });
        waveDAO.updateByPrimaryKeySelective(wave);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public HttpResult<?> deleteDespatchInWave(List<Despatch> despatchList) {
        for (Despatch d : despatchList) {
            despatchWaveDAO.deleteByDespatchId(d.getId());
            d.setStatus((short) 1);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public List<Wave> screenWave(WaveScreenDTO<Wave> screenDTO) {
        screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage() - 1) * screenDTO.getSearchDTO().getNum());
        if (screenDTO.getScreen() != null) screenDTO.setScreen(xX2x_x(screenDTO.getScreen()));
        List<Wave> screen = waveDAO.screen(screenDTO);
        List<Wave> waveList = new ArrayList<>();
        screen.forEach(e -> waveList.add(waveDAO.selectByPrimaryKey(e.getId())));
        return waveList;
    }

    @Override
    public Integer screenWaveNum(WaveScreenDTO<Wave> screenDTO) {
        return waveDAO.numScreen(screenDTO);
    }

    @Override
    public Wave selectById(Integer waveId) {
        return waveDAO.selectByPrimaryKey(waveId);
    }


    @Override
    public Integer addDespatchWave(List<Despatch> despatchList, Integer waveId) {
        for (Despatch despatch : despatchList) {
            DespatchWave despatchWave = new DespatchWave();
            despatchWave.setDespatchId(despatch.getId());
            despatchWave.setWaveId(waveId);
            despatchWaveDAO.insertSelective(despatchWave);
        }
        return null;
    }

    @Override
    public HttpResult<?> deleteDespatchWave(DespatchWave despatchWave) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, despatchWaveDAO.deleteByPrimaryKey(despatchWave.getId()));
    }

    @Override
    public HttpResult<?> updateDespatchWave(DespatchWave despatchWave) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, despatchWaveDAO.updateByPrimaryKeySelective(despatchWave));
    }

    @Override
    public List<DespatchWave> screenDespatchWave(ScreenDTO<DespatchWave> screenDTO) {
        SearchDTO searchDTO = screenDTO.getSearchDTO();
        searchDTO.setPage((searchDTO.getPage() - 1) * searchDTO.getNum());
        screenDTO.setSearchDTO(searchDTO);
        List<DespatchWave> screen = despatchWaveDAO.screen(screenDTO);
        List<DespatchWave> despatchWaveList = new ArrayList<>();
        screen.forEach(e -> despatchWaveList.add(despatchWaveDAO.selectByPrimaryKey(e.getId())));
        return despatchWaveList;
    }

    @Override
    public Integer screenDespatchWaveNum(ScreenDTO<DespatchWave> screenDTO) {
        return despatchWaveDAO.numScreen(screenDTO);

    }

    @Override
    public List<Replenish> generateReplenish(PickTaskShortage pickTaskShortage, Replenish replenish) {
        Replenish replenishBus = pickTaskShortageToReplenish(pickTaskShortage);
        List<Replenish> replenishListNoLoc = new ArrayList<>();
        while (pickTaskShortage.getShortageCnt() > 0) {
            Replenish reReplenish = replenishBus;
            //找来源库位
            InventoryBalance inventoryBalances = inventoryBalanceDAO.selectByAreaCoAndWareCoAndSkuIdAndCusIdLimit(pickTaskShortage.getSkuId(), pickTaskShortage.getCustomerId(), replenish.getSourceAreaCode(), pickTaskShortage.getWarehouseCode());
            //找目标库位
            WarehouseLocation warehouseLocation = null;
            for (WarehouseLocation w : warehouseLocationDAO.selectByAreaId(replenish.getSourceAreaId())) {
                if (inventoryBalanceDAO.selectByLocationId(warehouseLocation.getId()).size() == 0) {
                    warehouseLocation = w;
                    System.out.println(w);//todo 删除
                    break;
                }
            }
            reReplenish.setSourceLocationId(inventoryBalances.getLocationId());
            reReplenish.setSourceLocationCode(inventoryBalances.getLocationCode());
            reReplenish.setSourceLocationName(warehouseLocationDAO.selectByPrimaryKey(inventoryBalances.getLocationId()).getLocationName());
            pickTaskShortage.setShortageCnt(pickTaskShortage.getShortageCnt() - inventoryBalances.getAvailableCnt());
            reReplenish.setReplenishCnt(inventoryBalances.getAvailableCnt());
            if (warehouseLocation == null) {
                reReplenish.setReplenishStatus((short) 11);//11:本库区无库位存放，手动解决
                inventoryBalanceDAO.updatePreDistributionById(inventoryBalances.getAvailableCnt(), inventoryBalances.getId());
                replenishDAO.insertSelective(replenish);
                replenishListNoLoc.add(reReplenish);
            } else {
                reReplenish.setTargetLocationId(warehouseLocation.getId());
                reReplenish.setTargetLocationCode(warehouseLocation.getLocationCode());
                reReplenish.setTargetLocationName(warehouseLocation.getLocationName());
                replenishDAO.insertSelective(replenish);
            }
        }
        return replenishListNoLoc;
    }

    @Override
    public HttpResult<?> generateReplenishByWave(Wave wave, Replenish replenish) {
        Wave wave1 = waveDAO.selectByPrimaryKey(wave.getId());
        List<PickTaskShortage> pickTaskShortages = pickTaskShortageDAO.selectDetailByPid(wave.getId());
        if (pickTaskShortages.size() == 0) return HttpResult.of(HttpResultCodeEnum.NOT_FIND_SHORETAGE);
        List<PickTaskShortage> pickTaskShortages1 = new ArrayList<>();
        pickTaskShortages.forEach(e -> {
            e.getShortageCnt();
            Integer sum = inventoryBalanceDAO.selectAvailableCntByAreaCodeAndWarehouseCodeAndSkuIdAndCustomId(e.getSkuId(), e.getCustomerId(), replenish.getSourceAreaCode(), e.getWarehouseCode());
            if (sum == null) sum = 0;
            if (e.getShortageCnt() > sum) {
                e.setShortageCnt(e.getShortageCnt() - sum);
                pickTaskShortages1.add(e);
            }
        });
        if (pickTaskShortages1.size() > 0)
            return HttpResult.of(HttpResultCodeEnum.REPLENISH_INVENTORY_SHORETAGE, pickTaskShortages1);
        if (wave1.getStatus() != 13) {
            return HttpResult.of(HttpResultCodeEnum.NOT_AREA_SHORTAGE_WAVE);
        }
        List<Replenish> replenishList = new ArrayList<>();
        pickTaskShortages.forEach(e -> {
            replenishList.addAll(generateReplenish(e, replenish));
        });
        if (replenishList.size() > 0) return HttpResult.of(HttpResultCodeEnum.CANNOT_FIND_EMPTY_LOCATION);
        else return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    private Replenish pickTaskShortageToReplenish(PickTaskShortage pickTaskShortage) {
        Replenish replenish = new Replenish();
        replenish.setReplenishId(codeService.code("REP"));
        replenish.setPid(pickTaskShortage.getWaveId());
        replenish.setReplenishStatus((short) 1);//已创建
        replenish.setShortageAreaId(pickTaskShortage.getShortageAreaId());
        replenish.setShortageAreaCode(pickTaskShortage.getShortageAreaCode());
        replenish.setShortageAreaName(pickTaskShortage.getShortageAreaName());
        replenish.setProposalCnt(pickTaskShortage.getShortageCnt());
        replenish.setCustomerId(pickTaskShortage.getCustomerId());
        replenish.setCustomerName(pickTaskShortage.getCustomerName());
        replenish.setSkuBarCode(pickTaskShortage.getSkuBarcode());
        replenish.setWarehouseId(pickTaskShortage.getWarehouseId());
        replenish.setWarehouseCode(pickTaskShortage.getWarehouseCode());
        replenish.setWarehouseName(pickTaskShortage.getWarehouseName());
        return replenish;
    }

    @Override
    public HttpResult<?> deleteReplenish(Replenish replenish) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, replenishDAO.deleteByPrimaryKey(replenish.getId()));
    }

    @Override
    public HttpResult<?> judgeInventoryBalanceByArea(Wave wave, Replenish replenish) {
        List<PickTaskShortage> pickTaskShortages = new ArrayList<>();
        List<PickTaskShortage> rePickTaskShortages = pickTaskShortageDAO.selectDetailByPid(wave.getId());
        if (rePickTaskShortages.size() == 0) return HttpResult.of(HttpResultCodeEnum.NOT_FIND_SHORETAGE);
        rePickTaskShortages.forEach(e -> {
            e.getShortageCnt();
            Integer sum = inventoryBalanceDAO.selectAvailableCntByAreaCodeAndWarehouseCodeAndSkuIdAndCustomId(e.getSkuId(), e.getCustomerId(), replenish.getSourceAreaCode(), e.getWarehouseCode());
            if (sum == null) sum = 0;
            if (e.getShortageCnt() > sum) {
                e.setShortageCnt(e.getShortageCnt() - sum);
                pickTaskShortages.add(e);
            }
        });
        if (pickTaskShortages.size() > 0)
            return HttpResult.of(HttpResultCodeEnum.REPLENISH_INVENTORY_SHORETAGE, pickTaskShortages);
        else return HttpResult.of(HttpResultCodeEnum.SUCCESS, "库存充足");
    }

    @Override
    public HttpResult<?> updateReplenish(Replenish replenish) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, replenishDAO.updateByPrimaryKeySelective(replenish));
    }


    @Override
    public List<Replenish> screenReplenish(ScreenDTO<Replenish> screenDTO) {
        SearchDTO searchDTO = screenDTO.getSearchDTO();
        searchDTO.setPage((searchDTO.getPage() - 1) * searchDTO.getNum());
        screenDTO.setSearchDTO(searchDTO);
        List<Replenish> screen = replenishDAO.screen(screenDTO);
        List<Replenish> replenishList = new ArrayList<>();
        screen.forEach(e -> replenishList.add(replenishDAO.selectByPrimaryKey(e.getId())));
        return replenishList;
    }

    @Override
    public Integer screenReplenishNum(ScreenDTO<Replenish> screenDTO) {
        return replenishDAO.numScreen(screenDTO);

    }

    public String xX2x_x(String str) {
        Pattern compile = Pattern.compile("[A-Z]");
        Matcher matcher = compile.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }


}
