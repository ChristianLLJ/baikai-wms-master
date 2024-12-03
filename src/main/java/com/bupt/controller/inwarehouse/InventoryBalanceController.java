package com.bupt.controller.inwarehouse;

import com.bupt.DTO.ScreenDTO;
import com.bupt.mapper.InventoryBalanceDAO;
import com.bupt.pojo.InventoryBalance;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.Inwarehouse.InventoryService;
import com.bupt.service.Inwarehouse.StockService;
import com.bupt.service.authority.CodeService;
import com.bupt.service.authority.ShiroService;
import com.bupt.service.util.EasyExcelService;
import com.bupt.service.util.GetMapListService;
import com.bupt.service.util.UtilService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;

/**
 * 库存余量相关接口
 */
@RestController
@RequestMapping("/inventoryBalance")
public class InventoryBalanceController {
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private StockService stockService;
    @Autowired
    private CodeService codeService;
    @Autowired
    private EasyExcelService easyExcelService;
    @Autowired
    private GetMapListService getMapListService;
    @Autowired
    private ShiroService shiroService;
    @Autowired
    private InventoryBalanceDAO inventoryBalanceDAO;
    @Autowired
    UtilService utilService;

    /*
     * 新增
     * */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/submitInventoryBalance")
    public HttpResult<?> submitInventoryBalance(@RequestBody @NotNull InventoryBalance inventoryBalance) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, stockService.addInventoryBalance(inventoryBalance));
    }

    /*
     * 删除
     * */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/deleteInventoryBalance")
    public HttpResult<?> deleteInventoryBalance(@RequestBody @NotNull InventoryBalance inventoryBalance) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, stockService.deleteInventoryBalance(inventoryBalance));
    }

    /*
     * 修改
     * */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/updateInventoryBalance")
    public HttpResult<?> updateInventoryBalance(@RequestBody @NotNull InventoryBalance inventoryBalance) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, stockService.updateInventoryBalance(inventoryBalance));
    }

    /*
     * 精确查询
     * */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchInventoryBalanceSelective")
    public HttpResult<?> searchInventoryBalanceSelective(@RequestBody @NotNull ScreenDTO<InventoryBalance> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, stockService.screenInventoryBalance(screenDTO));
    }

    /**
     * 模糊查询
     *
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/fuzzyQueryInventoryBalance")
    public HttpResult<?> fuzzyQueryInventoryBalance(@RequestBody @NotNull ScreenDTO<InventoryBalance> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, stockService.fuzzyQueryInventoryBalance(screenDTO));
    }

    /**
     * 库内分组查询
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectGroupBySelective")
    public HttpResult<?> selectGroupBySelective(@RequestBody @NotNull ScreenDTO<InventoryBalance> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, stockService.selectGroupBySelective(screenDTO));
    }

    /**
     * @Description 查找数量
     * @Author lc
     * @Date 18:16 2022/4/18
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchInventoryBalanceNum")
    public HttpResult<?> searchInventoryBalanceNum(@RequestBody @NotNull ScreenDTO<InventoryBalance> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, stockService.screenInventoryBalanceNum(screenDTO));
    }

    /***
     *@Description 有效期报警
     *@Author lc
     *@Date 21:30 2022/10/12
     **/
    @CrossOrigin
    @ResponseBody
    @PostMapping("/alarmByValidDate")
    public HttpResult<?> alarmByValidDate() throws Exception {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, stockService.alarmByValidDate());
    }

    /***
     *@Description 导出有效期报警表单
     *@Author lc
     *@Date 16:16 2022/11/23
     *@Param [response]
     *@Return com.bupt.result.HttpResult<?>
     **/
    @CrossOrigin
    @ResponseBody
    @PostMapping("/exportValidAlarmExcel")
    public void exportValidAlarmExcel(HttpServletResponse response) {
        easyExcelService.exportExcelList("ValidDateAlarm.xlsx", inventoryBalanceDAO.selectByValidAlarm(), response);
    }

    /***
     *@Description 库存数量报警
     *@Author lc
     *@Date 21:31 2022/10/12
     **/
    @CrossOrigin
    @ResponseBody
    @PostMapping("/alarmByInventoryCount")
    public HttpResult<?> alarmByInventoryCount() {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, stockService.alarmByInventoryCount());
    }

    /***
     *@Description 导出数量报警表单
     *@Author lc
     *@Date 17:38 2022/11/23
     *@Param [response]
     *@Return com.bupt.result.HttpResult<?>
     **/
    @CrossOrigin
    @ResponseBody
    @PostMapping("/exportCountAlarmExcel")
    public void exportCountAlarmExcel(HttpServletResponse response) {
        easyExcelService.exportExcelList("CountAlarm.xlsx", inventoryBalanceDAO.selectByCountAlarm(), response);
    }

    /***
     *@Description 滞留期报警
     *@Author lc
     *@Date 21:32 2022/10/12
     **/
    @CrossOrigin
    @ResponseBody
    @PostMapping("/alarmByRetentionDay")
    public HttpResult<?> alarmByRetentionDay() throws ParseException {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, stockService.alarmByRetentionDay());
    }

    /***
     *@Description 导出滞留期报警表单
     *@Author lc
     *@Date 17:36 2022/11/23
     *@Param [response]
     *@Return com.bupt.result.HttpResult<?>
     **/
    @CrossOrigin
    @ResponseBody
    @PostMapping("/exportRetentionAlarmExcel")
    public void exportRetentionAlarmExcel(HttpServletResponse response) {
        easyExcelService.exportExcelList("RetentionAlarm.xlsx", inventoryBalanceDAO.selectByRetentionAlarm(), response);
    }

    /***
     *@Description 库存数据发送给自己的WCS
     *@Author lc
     **/
    @CrossOrigin
    @ResponseBody
    @PostMapping("/wcsGetInventoryBalance")
    public HttpResult<?> wcsGetInventoryBalance(@RequestBody ScreenDTO<InventoryBalance> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, stockService.wcsGetInventoryBalance(screenDTO));
    }
}
