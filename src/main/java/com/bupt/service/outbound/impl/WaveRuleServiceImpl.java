package com.bupt.service.outbound.impl;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SearchDTO;
import com.bupt.mapper.WaveDAO;
import com.bupt.mapper.WaveRuleDAO;
import com.bupt.mapper.WaveRuleRunnerDAO;
import com.bupt.mapper.WaveRuleTimerDAO;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.outbound.WaveRuleService;
import com.bupt.strategy.waveStrategy.WaveStrategy;
import com.bupt.strategy.waveStrategy.WaveStrategyCon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class WaveRuleServiceImpl implements WaveRuleService {
    @Autowired
    private WaveRuleDAO waveRuleDAO;
    @Autowired
    private WaveRuleTimerDAO waveRuleTimerDAO;
    @Autowired
    private WaveDAO waveDAO;
    @Autowired
    private WaveRuleRunnerDAO waveRuleRunnerDAO;
    @Autowired
    private DespatchServiceImpl despatchService;
    private static List<Boolean> interruptedTags = new ArrayList<>();
    private static List<Long> periods = new ArrayList<>();
    private static List<Integer> is = new ArrayList<>();
    private static int kk = 0;
    private static Boolean shutdown = false, firstPool = true;
    private static Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
    private static ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(10);

    @Override
    public Integer addWaveRuleAndTimer(HeadAndDetail<WaveRule, WaveRuleTimer> headAndDetail) {
        List<WaveRuleTimer> waveRuleTimers = headAndDetail.getDetails();
        WaveRule waveRule = headAndDetail.getHead();
        Integer id = addWaveRule(waveRule);
        waveRuleTimers.forEach(e -> {
            e.setWaveRuleId(id);
            addWaveRuleTimer(e);
        });
        return 1;
    }

    @Override
    public Integer addWaveRule(WaveRule waveRule) {
        waveRuleDAO.insertSelective(waveRule);
        return waveRule.getId();
    }

    @Override
    public String deleteWaveRuleAndTimer(List<WaveRule> waveRuleList) {
        waveRuleList.forEach(e -> {
            waveRuleDAO.deleteByPrimaryKey(e.getId());
            waveRuleTimerDAO.deleteByWaveRuleId(e.getId());
        });
        return "共删除" + waveRuleList.size() + "条数据";
    }

    @Override
    public HttpResult<?> updateWaveRuleAndTimer(HeadAndDetail<WaveRule, WaveRuleTimer> headAndDetail) {
        WaveRule reWaveRule = waveRuleDAO.selectByPrimaryKey(headAndDetail.getHead().getId());
        if (!reWaveRule.getIsModified())
            return HttpResult.of(HttpResultCodeEnum.WAVERULE_BENOT_MODIFIED);
        waveRuleDAO.updateByPrimaryKeySelective(headAndDetail.getHead());
        waveRuleTimerDAO.deleteByWaveRuleId(reWaveRule.getId());
        headAndDetail.getDetails().forEach(e->{
            e.setWaveRuleId(reWaveRule.getId());
            waveRuleTimerDAO.insertSelective(e);
        });
        return HttpResult.of(HttpResultCodeEnum.WAVERULE_UPDATE_SUCCESS);
    }

    @Override
    public List<WaveRule> screenWaveRule(ScreenDTO<WaveRule> screenDTO) {
        SearchDTO searchDTO = screenDTO.getSearchDTO();
        searchDTO.setPage((searchDTO.getPage() - 1) * searchDTO.getNum());
        screenDTO.setSearchDTO(searchDTO);
        return waveRuleDAO.screen(screenDTO);
    }

    @Override
    public Integer screenWaveRuleNum(ScreenDTO<WaveRule> screenDTO) {
        SearchDTO searchDTO = screenDTO.getSearchDTO();
        searchDTO.setPage((searchDTO.getPage() - 1) * searchDTO.getNum());
        screenDTO.setSearchDTO(searchDTO);
        return waveRuleDAO.numScreen(screenDTO);
    }

    @Override
    public Integer addWaveRuleTimer(WaveRuleTimer waveRuleTimer) {
        waveRuleTimerDAO.insertSelective(waveRuleTimer);
        return waveRuleTimer.getId();
    }

    @Override
    public List<WaveRuleTimer> screenWaveRuleTimers(ScreenDTO<WaveRuleTimer> screenDTO) {
        SearchDTO searchDTO = screenDTO.getSearchDTO();
        searchDTO.setPage((searchDTO.getPage() - 1) * searchDTO.getNum());
        screenDTO.setSearchDTO(searchDTO);
        return waveRuleTimerDAO.screen(screenDTO);
    }

    @Override
    public Integer screenWaveRuleTimersNum(ScreenDTO<WaveRuleTimer> screenDTO) {
        SearchDTO searchDTO = screenDTO.getSearchDTO();
        searchDTO.setPage((searchDTO.getPage() - 1) * searchDTO.getNum());
        screenDTO.setSearchDTO(searchDTO);
        return waveRuleTimerDAO.numScreen(screenDTO);
    }

    @Override
    public List<Wave> selectWavesByWaverule(WaveRule waveRule) {
        return waveDAO.selectByWaveRuleId(waveRule.getId());
    }

    @Override
    public HttpResult<?> runAllWaveRule() {
        if (!firstPool) return null;
        firstPool = false;
        for (int i = 0; i < interruptedTags.size(); i++) {
            interruptedTags.set(i, false);
        }
        System.out.println("点击全部运行，执行所有非运行激活状态的波次规则" + LocalDateTime.now());
        List<WaveRule> waveRules = waveRuleDAO.selectAllToRun();
        waveRules.forEach(e -> runOneWaveRule(e));
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public HttpResult<?> runOneWaveRule(WaveRule waveRule) {
        WaveRule reWaveRule = waveRuleDAO.selectByPrimaryKey(waveRule.getId());
        int k = 0;
        if (reWaveRule.getWaveRuleCode().equals("QuantitativeTaskStrategy")) {
            if (reWaveRule.getIsRunning()) return HttpResult.of(HttpResultCodeEnum.WAVERULE_HAVEBEEN_RUNNING);
            else {
                reWaveRule.setIsRunning(true);
                waveRuleDAO.updateByPrimaryKeySelective(reWaveRule);
            }
            k = kk++;
            interruptedTags.add(false);
            periods.add(3L);
            is.add(0);
            quantitativeTask(reWaveRule, k);
        } else if (reWaveRule.getWaveRuleCode().equals("TimedTaskStrategy")) {
            if (timedTaskSizeCheck(reWaveRule)==1){
                if (reWaveRule.getIsRunning()) return HttpResult.of(HttpResultCodeEnum.WAVERULE_HAVEBEEN_RUNNING);
                else {
                    reWaveRule.setIsRunning(true);
                    waveRuleDAO.updateByPrimaryKeySelective(reWaveRule);
                }
                k = kk++;
                interruptedTags.add(false);
                periods.add(3L);
                is.add(0);
                timedTask(reWaveRule, k);
            }else return HttpResult.of(HttpResultCodeEnum.TIMEDWAVERULE_HAVEBEEN_OVERTIME);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }
    @Override
    public HttpResult<?> stopAllWaveRule() {
        for (int i = 0; i < interruptedTags.size(); i++) {
            interruptedTags.set(i, true);
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        waveRuleDAO.selectAllToStop().forEach(e -> stopOneWaveRule(e));
        interruptedTags.clear();
        periods.clear();
        firstPool = true;
        kk = 0;
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }


    @Override
    public HttpResult<?> stopOneWaveRule(WaveRule inWaveRule) {
        WaveRule waveRule = waveRuleDAO.selectByPrimaryKey(inWaveRule.getId());
        if (!waveRule.getIsRunning()) return HttpResult.of(HttpResultCodeEnum.WAVERULE_NOTBE_RUNNING);
        WaveRuleRunner waveRuleRunner = waveRuleRunnerDAO.searchByWaveRuleId(inWaveRule.getId());
        interruptedTags.set(waveRuleRunner.getRunningOrder(), true);
        waveRuleRunnerStop(waveRuleRunner);
        inWaveRule.setIsRunning(false);
        waveRuleDAO.updateByPrimaryKeySelective(inWaveRule);
        System.out.println("第" + (waveRuleRunner.getRunningOrder() + 1) + "个规则(定量）终止运行");
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, inWaveRule.getId());
    }

    private void quantitativeTask(WaveRule waveRule, int k) {
        WaveRuleTimer waveRuleTimer = waveRuleTimerDAO.selectDetailByPid(waveRule.getId()).get(0);
        waveRuleRunnerInsert(waveRule.getId(), k);
        Time now = new Time(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND));
        long initialDelay = 3;
        if (waveRuleTimer.getStartTime().after(now))
            initialDelay = (waveRuleTimer.getStartTime().getTime() - now.getTime()) / 1000;
        //开始时间未到等待
        threadPool.scheduleAtFixedRate
                (new Runnable() {
                     @Override
                     public void run() {
                         System.out.println("新增执行 第" + (k + 1) + "个规则(定量）：" + LocalDateTime.now());
                         try {
                             while (interruptedTags.get(k)) {
                                 Thread.interrupted();
                             }
                             Class c = Class.forName("com.bupt.strategy.waveStrategy."
                                     + waveRule.getWaveRuleCode());
                             WaveStrategyCon waveStrategyCon = new WaveStrategyCon((WaveStrategy)
                                     c.newInstance());
                             for (int i = 0; i < waveRule.getWaveNum(); i++) {
                                 List<Despatch> despatches = waveStrategyCon.generateWaveByStrategy(waveRule);
                                 if (despatches == null) break;
                                 despatchService.generateWave(despatches, waveRule);
                                 despatches.clear();
                             }
                             //结束时间到，停止运行
                             if (now.after(waveRuleTimer.getEndTime())) {
                                 interruptedTags.set(waveRule.getId(), true);
                                 waveRule.setIsRunning(false);
                             }
                             System.out.println("----------------第" + (k + 1) + "个规则(定量）执行一次--------------------");
                         } catch (ClassNotFoundException e) {
                             throw new RuntimeException(e);
                         } catch (InstantiationException e) {
                             throw new RuntimeException(e);
                         } catch (IllegalAccessException e) {
                             throw new RuntimeException(e);
                         }
                     }
                 },
                        initialDelay, // 初始延迟时间
                        (waveRuleTimer.getFrequencyMinute() * 60), // 第二次之后的执行间隔
                        //11L,
                        TimeUnit.SECONDS);
    }

    private void timedTask(WaveRule waveRule, int k) {
        List<WaveRuleTimer> waveRuleTimerList = waveRuleTimerDAO.selectDetailByPid(waveRule.getId());
        int size = waveRuleTimerList.size();
        Time now = new Time(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND));
        long initialDelay = 3;
        int symbol = 0;
        for (int i = 0; i < size; i++) {
            if (now.after(waveRuleTimerList.get(i).getFixedTime()) && i != size) {
                continue;
            }
            if (now.before(waveRuleTimerList.get(i).getFixedTime())) {
                symbol = i + 1;
                initialDelay = (waveRuleTimerList.get(i).getFixedTime().getTime() - now.getTime()) / 1000;
                break;
            }
        }
        waveRuleRunnerInsert(waveRule.getId(), k);
        is.set(k, symbol);
        threadPool.scheduleAtFixedRate
                (new Runnable() {
                     @Override
                     public void run() {
                         System.out.println("新增执行 第" + (k + 1) + "个规则（定时）：" + LocalDateTime.now());
                         try {
                             if (is.get(k) < size) {
                                 System.out.println("is.get(k) < size:成功");
                                 periods.set(k, waveRuleTimerList.get(is.get(k)).getFixedTime().getTime() -
                                         waveRuleTimerList.get(is.get(k) - 1).getFixedTime().getTime());
                             } else {
                                 System.out.println("is.get(k) > size:停止");
                                 interruptedTags.set(waveRuleRunnerDAO.searchByWaveRuleId
                                         (waveRule.getId()).getId(), true);
                                 waveRule.setIsRunning(false);
                             }
                             is.set(k, (is.get(k) + 1));
                             while (interruptedTags.get(k)) Thread.interrupted();
                             Class c = Class.forName("com.bupt.strategy.waveStrategy."
                                     + waveRule.getWaveRuleCode());
                             WaveStrategyCon waveStrategyCon = new WaveStrategyCon((WaveStrategy)
                                     c.newInstance());
                             List<Despatch> despatches = waveStrategyCon.generateWaveByStrategy
                                     (waveRule);
                             despatchService.generateWave(despatches,
                                     waveRule);
                             //结束时间到，停止运行
                             if (now.after(waveRuleTimerList.get(size - 1).getFixedTime())) {
                                 interruptedTags.set(waveRuleRunnerDAO.searchByWaveRuleId
                                         (waveRule.getId()).getId(), true);
                                 waveRule.setIsRunning(false);
                             }
                             Thread.sleep(periods.get(k));
                             System.out.println("-------------------第" + (k + 1) + "个规则(定时）执行一次------------------");
                         } catch (ClassNotFoundException e) {
                             throw new RuntimeException(e);
                         } catch (InstantiationException e) {
                             throw new RuntimeException(e);
                         } catch (IllegalAccessException e) {
                             throw new RuntimeException(e);
                         } catch (InterruptedException e) {
                             throw new RuntimeException(e);
                         }
                     }
                 },
                        initialDelay, // 初始时间间隔
                        //2,
                        3L, // 第二次定时任务的执行间隔
                        TimeUnit.SECONDS);
    }

    private void waveRuleRunnerInsert(Integer waveRuleId, Integer order) {
        WaveRuleRunner waveRuleRunner = new WaveRuleRunner();
        waveRuleRunner.setWaveRuleId(waveRuleId);
        waveRuleRunner.setRunningOrder(order);
        waveRuleRunner.setRunningTag(true);
        waveRuleRunnerDAO.insertSelective(waveRuleRunner);
    }

    private void waveRuleRunnerStop(WaveRuleRunner waveRuleRunner) {
        waveRuleRunner.setRunningTag(false);
        waveRuleRunnerDAO.updateByPrimaryKeySelective(waveRuleRunner);
    }


    private int timedTaskSizeCheck(WaveRule waveRule) {
        List<WaveRuleTimer> waveRuleTimerList = waveRuleTimerDAO.selectDetailByPid(waveRule.getId());
        int size = waveRuleTimerList.size();
        Time now = new Time(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND));
        for (int i = 0; i < size; i++) {
            if (now.after(waveRuleTimerList.get(i).getFixedTime()) && i != size) {
                if (i == (size - 1)) {
                    return 0;
                }
                continue;
            }
            if (now.before(waveRuleTimerList.get(i).getFixedTime())) {
                break;
            }
        }
        return 1;
    }
}
