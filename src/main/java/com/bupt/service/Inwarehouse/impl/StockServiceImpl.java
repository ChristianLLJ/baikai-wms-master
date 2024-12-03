package com.bupt.service.Inwarehouse.impl;

import com.bupt.DTO.ResultsAndNum;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.reportForm.InwarehouseReportVO;
import com.bupt.DTO.reportForm.ReportSum;
import com.bupt.mapper.InventoryBalanceDAO;
import com.bupt.pojo.InventoryBalance;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.Inwarehouse.StockService;
import com.bupt.service.authority.CodeService;
import com.bupt.service.util.EasyExcelService;
import com.bupt.service.util.GetMapListService;
import com.bupt.service.util.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@EnableScheduling
@Service
public class StockServiceImpl implements StockService {
    @Autowired
    private InventoryBalanceDAO inventoryBalanceDAO;
    @Autowired
    private CodeService codeService;
    @Autowired
    private EasyExcelService easyExcelService;
    @Autowired
    private GetMapListService getMapListService;
    @Autowired
    UtilService utilService;

    @Override
    @Transactional
    public Integer addInventoryBalance(InventoryBalance inventoryBalance) {
        inventoryBalance.setType((short) 1);
        inventoryBalance.setBalanceCode(codeService.code("BAL"));
        inventoryBalanceDAO.insertSelective(inventoryBalance);
        return inventoryBalance.getId();
    }

    @Override
    @Transactional
    public HttpResult<?> deleteInventoryBalance(InventoryBalance inventoryBalance) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, inventoryBalanceDAO.deleteByPrimaryKey(inventoryBalance.getId()));
    }

    @Override
    @Transactional
    public HttpResult<?> updateInventoryBalance(InventoryBalance inventoryBalance) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, inventoryBalanceDAO.updateByPrimaryKeySelective(inventoryBalance));
    }

    @Override
    public List<InventoryBalance> screenInventoryBalance(ScreenDTO<InventoryBalance> screenDTO) {
        if (screenDTO.getScreen() != null && screenDTO.getScreen() != "")//排序字段
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        else screenDTO.setScreen("id");
        if (screenDTO.getSearchDTO() != null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage() - 1) * screenDTO.getSearchDTO().getNum());
        return inventoryBalanceDAO.screen(screenDTO);
    }

    @Override
    public List<InventoryBalance> fuzzyQueryInventoryBalance(ScreenDTO<InventoryBalance> screenDTO) {
        if (screenDTO.getScreen() != null && screenDTO.getScreen() != "")
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));//排序字段
        else screenDTO.setScreen("id");
        if (screenDTO.getWhere() != null && screenDTO.getWhere() != "")
            screenDTO.setWhere(utilService.xX2x_x(screenDTO.getWhere()));//输入特定条件
        if (screenDTO.getSearchDTO() != null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage() - 1) * screenDTO.getSearchDTO().getNum());
        return inventoryBalanceDAO.fuzzyQuery(screenDTO);
    }

    @Override
    public ResultsAndNum<InventoryBalance> selectGroupBySelective(ScreenDTO<InventoryBalance> screenDTO) {
        if (screenDTO.getScreen() != null && screenDTO.getScreen() != "")
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));//排序字段
        else screenDTO.setScreen("id");
        if (screenDTO.getGroup() != null && screenDTO.getGroup() != "")
            screenDTO.setGroup(utilService.xX2x_x(screenDTO.getGroup()));//分组字段
        else screenDTO.setGroup("id");
        if (screenDTO.getWhere() != null && screenDTO.getWhere() != "")
            screenDTO.setWhere(utilService.xX2x_x(screenDTO.getWhere()));//输入where特定条件
        if (screenDTO.getHaving() != null && screenDTO.getHaving() != "")
            screenDTO.setHaving(utilService.xX2x_x(screenDTO.getHaving()));//输入having特定条件
        if (screenDTO.getSearchDTO() != null)//分页
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage() - 1) * screenDTO.getSearchDTO().getNum());
        List<InventoryBalance> inventoryBalanceList = inventoryBalanceDAO.selectGroupBySelective(screenDTO);
        ResultsAndNum<InventoryBalance> resultsAndNum = new ResultsAndNum<>();
        resultsAndNum.setResults(inventoryBalanceList);
        resultsAndNum.setNum(inventoryBalanceList.size());
        return resultsAndNum;
    }

    @Override
    public Integer screenInventoryBalanceNum(ScreenDTO<InventoryBalance> screenDTO) {
        return inventoryBalanceDAO.numScreen(screenDTO);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Override
    public List<InventoryBalance> alarmByValidDate() throws Exception {
        List<InventoryBalance> inventoryBalanceList = inventoryBalanceDAO.selectAll();
        Date date = new Date();
        for (InventoryBalance inventoryBalance : inventoryBalanceList) {
            inventoryBalance.setExpirationTime(addSpecificDate(inventoryBalance.getProductTime(), inventoryBalance.getQualityGuaranteePeriod()));
            int bets = daysBetween(inventoryBalance.getProductTime(), date);//生产日期和当前日期的间隔天数
            if (bets + inventoryBalance.getValidityLeadDay() >= inventoryBalance.getQualityGuaranteePeriod()) {
                inventoryBalance.setIsQualityAlarm((short) 1);
                inventoryBalanceDAO.updateByPrimaryKeySelective(inventoryBalance);
                inventoryBalance.setRetentionDay(bets - inventoryBalance.getQualityGuaranteePeriod());//用来保存过期的天数
            }
        }
        return inventoryBalanceList;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Override
    public List<InventoryBalance> alarmByRetentionDay() throws ParseException {
        List<InventoryBalance> inventoryBalanceList = inventoryBalanceDAO.selectAll();
        Date date = new Date();
        for (InventoryBalance inventoryBalance : inventoryBalanceList) {
            int bets = daysBetween(inventoryBalance.getInBoundTime(), date);//入库日期和当前日期的间隔天数
            if (bets >= inventoryBalance.getRetentionLeadDay()) {
                inventoryBalance.setIsRetentionAlarm((short) 1);
                inventoryBalanceDAO.updateByPrimaryKeySelective(inventoryBalance);
                inventoryBalance.setAlarmCount(bets - inventoryBalance.getRetentionLeadDay());//用来保存超过滞留期的天数
            }
        }
        return inventoryBalanceList;
    }

    @Override
    public InwarehouseReportVO sumInventoryBalance(ScreenDTO<InventoryBalance> screenDTO) {
        ReportSum reportSum = inventoryBalanceDAO.sumInventoryBalanceNum(screenDTO);
        InwarehouseReportVO inwarehouseReportVO = new InwarehouseReportVO();
        inwarehouseReportVO.setInventoryBalanceList(inventoryBalanceDAO.screen(screenDTO));
        inwarehouseReportVO.setAvailableSum(reportSum.getAvailableSum());
        inwarehouseReportVO.setFreezeSum(reportSum.getFreezeSum());
        inwarehouseReportVO.setDistributionSum(reportSum.getDistributionSum());
        inwarehouseReportVO.setTotalSum(reportSum.getTotalSum());
        return inwarehouseReportVO;
    }

    @Override
    public List<com.bupt.DTO.wcs.inwarehouse.InventoryBalance> wcsGetInventoryBalance(ScreenDTO<InventoryBalance> screenDTO) {
        screenDTO.getPojo().setType((short) 1);//查找还没有发送给WCS的库存数据
        List<InventoryBalance> inventoryBalanceList = inventoryBalanceDAO.screen(screenDTO);
        List<com.bupt.DTO.wcs.inwarehouse.InventoryBalance> wcsInventoryBalanceList = new ArrayList<>();
        if (inventoryBalanceList.size() == 0) return wcsInventoryBalanceList;
        for (InventoryBalance inventoryBalance : inventoryBalanceList) {
            inventoryBalance.setType((short) 2);//已经发送给WCS
            inventoryBalanceDAO.updateByPrimaryKeySelective(inventoryBalance);
            wcsInventoryBalanceList.add(inventoryBalanceToWcsInventoryBalance(inventoryBalance));
        }
        return wcsInventoryBalanceList;
    }

    com.bupt.DTO.wcs.inwarehouse.InventoryBalance inventoryBalanceToWcsInventoryBalance(InventoryBalance inventoryBalance) {
        com.bupt.DTO.wcs.inwarehouse.InventoryBalance wcsInventoryBalance = new com.bupt.DTO.wcs.inwarehouse.InventoryBalance();
        wcsInventoryBalance.setType(inventoryBalance.getType());
        wcsInventoryBalance.setCountAlarmEnabled(inventoryBalance.getCountAlarmEnabled());
        wcsInventoryBalance.setValidityAlarmEnabled(inventoryBalance.getValidityAlarmEnabled());
        wcsInventoryBalance.setRetentionAlarmEnabled(inventoryBalance.getRetentionAlarmEnabled());
        wcsInventoryBalance.setCustomId(inventoryBalance.getCustomId());
        wcsInventoryBalance.setCustomCode(inventoryBalance.getCustomCode());
        wcsInventoryBalance.setCustomName(inventoryBalance.getCustomName());
        wcsInventoryBalance.setCargoType(inventoryBalance.getCargoType());
        wcsInventoryBalance.setCommodityId(inventoryBalance.getCommodityId());
        wcsInventoryBalance.setCommodityCode(inventoryBalance.getCommodityCode());
        wcsInventoryBalance.setCommodityName(inventoryBalance.getCommodityName());
        wcsInventoryBalance.setWarehouseId(inventoryBalance.getWarehouseId());
        wcsInventoryBalance.setWarehouseCode(inventoryBalance.getWarehouseCode());
        wcsInventoryBalance.setWarehouseName(inventoryBalance.getWarehouseName());
        wcsInventoryBalance.setAreaId(inventoryBalance.getAreaId());
        wcsInventoryBalance.setAreaCode(inventoryBalance.getAreaCode());
        wcsInventoryBalance.setAreaName(inventoryBalance.getAreaName());
        wcsInventoryBalance.setLocationGroupId(inventoryBalance.getLocationGroupId());
        wcsInventoryBalance.setLocationGroupCode(inventoryBalance.getLocationGroupCode());
        wcsInventoryBalance.setLocationGroupName(inventoryBalance.getLocationGroupName());
        wcsInventoryBalance.setLocationId(inventoryBalance.getLocationId());
        wcsInventoryBalance.setLocationCode(inventoryBalance.getLocationCode());
        wcsInventoryBalance.setLocationName(inventoryBalance.getLocationName());
        wcsInventoryBalance.setContainerId(inventoryBalance.getContainerId());
        wcsInventoryBalance.setContainerCode(inventoryBalance.getContainerCode());
        wcsInventoryBalance.setContainerName(inventoryBalance.getContainerName());
        wcsInventoryBalance.setSkuId(inventoryBalance.getSkuId());
        wcsInventoryBalance.setSkuCode(inventoryBalance.getSkuCode());
        wcsInventoryBalance.setSkuName(inventoryBalance.getSkuName());
        wcsInventoryBalance.setUnit(inventoryBalance.getUnit());
        wcsInventoryBalance.setVolume(inventoryBalance.getVolume());
        wcsInventoryBalance.setWeight(inventoryBalance.getWeight());
        wcsInventoryBalance.setPrice(inventoryBalance.getPrice());
        wcsInventoryBalance.setProductCompany(inventoryBalance.getProductCompany());
        wcsInventoryBalance.setProductTime(inventoryBalance.getProductTime());
        wcsInventoryBalance.setProductBatch(inventoryBalance.getProductBatch());
        wcsInventoryBalance.setTraceCode(inventoryBalance.getTraceCode());
        wcsInventoryBalance.setPreDistributionCnt(inventoryBalance.getPreDistributionCnt());
        wcsInventoryBalance.setDistributionCnt(inventoryBalance.getDistributionCnt());
        wcsInventoryBalance.setFreezeCnt(inventoryBalance.getFreezeCnt());
        wcsInventoryBalance.setAvailableCnt(inventoryBalance.getAvailableCnt());
        wcsInventoryBalance.setTotalCnt(inventoryBalance.getTotalCnt());
        wcsInventoryBalance.setInventoryCnt(inventoryBalance.getInventoryCnt());
        wcsInventoryBalance.setUpdateTime(inventoryBalance.getUpdateTime());
        wcsInventoryBalance.setInBoundTime(inventoryBalance.getInBoundTime());
        wcsInventoryBalance.setIsCountAlarm(inventoryBalance.getIsCountAlarm());
        wcsInventoryBalance.setAlarmCount(inventoryBalance.getAlarmCount());
        wcsInventoryBalance.setIsQualityAlarm(inventoryBalance.getIsQualityAlarm());
        wcsInventoryBalance.setQualityGuaranteePeriod(inventoryBalance.getQualityGuaranteePeriod());
        wcsInventoryBalance.setExpirationTime(inventoryBalance.getExpirationTime());
        wcsInventoryBalance.setValidityLeadDay(inventoryBalance.getValidityLeadDay());
        wcsInventoryBalance.setIsRetentionAlarm(inventoryBalance.getIsRetentionAlarm());
        wcsInventoryBalance.setRetentionDay(inventoryBalance.getRetentionDay());
        wcsInventoryBalance.setRetentionLeadDay(inventoryBalance.getRetentionLeadDay());
        wcsInventoryBalance.setWmsBalanceCode(inventoryBalance.getBalanceCode());
        return wcsInventoryBalance;
    }

    /***
     *@Description 计算两个日期之间相差的天数
     *@Author lc
     *@Date 18:51 2022/10/12
     *@Param [smdate较小的时间, bdate较大的时间]
     *@Return int
     **/
    public int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 一个日期加上固定的天数
     *
     * @param date
     * @param specificDay
     * @return
     * @throws Exception
     */
    public Date addSpecificDate(Date date, Integer specificDay) throws Exception {
        //将传入的时间换算成毫秒数
        long dateTime = date.getTime();
        //将需要加的特定的时间天数也换算成毫秒数
        specificDay = specificDay * 24 * 60 * 60 * 1000;
        //将传入的时间和特定的时间进行相加,得到新的毫秒数
        dateTime += specificDay; // 相加
        //将新的毫毫秒数转换成日期返回
        return new Date(dateTime);
    }

    @Scheduled(cron = "0 0/60 * * * ?")
    @Override
    public List<InventoryBalance> alarmByInventoryCount() {
        List<InventoryBalance> inventoryBalanceList = inventoryBalanceDAO.selectAll();
        for (InventoryBalance inventoryBalance : inventoryBalanceList) {
            if (inventoryBalance.getAvailableCnt() == null) inventoryBalance.setAvailableCnt(0);
            if (inventoryBalance.getAlarmCount() == null) inventoryBalance.setAlarmCount(0);
            if (inventoryBalance.getAvailableCnt() < inventoryBalance.getAlarmCount()) {
                inventoryBalance.setIsCountAlarm((short) 1);
                inventoryBalanceDAO.updateByPrimaryKeySelective(inventoryBalance);
                inventoryBalance.setRetentionDay(inventoryBalance.getAlarmCount() - inventoryBalance.getAvailableCnt());//用来保存缺少的数量
            }
        }
        return inventoryBalanceList;
    }
}
