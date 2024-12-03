package com.bupt.controller.inwarehouse;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.InwarehouseDTO.InventoryMoveAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.wcs.inwarehouse.TInventoryMove;
import com.bupt.DTO.wcs.inwarehouse.TInventoryMoveDetail;
import com.bupt.controller.BaseController;
import com.bupt.mapper.InventoryMoveDAO;
import com.bupt.pojo.InventoryMove;
import com.bupt.pojo.InventoryMoveDetail;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.Inwarehouse.MoveService;
import com.bupt.service.Inwarehouse.impl.MoveServiceImpl;
import com.bupt.service.authority.CodeService;
import com.bupt.service.authority.ShiroService;
import com.bupt.service.util.EasyExcelService;
import com.bupt.service.util.GetMapListService;
import com.bupt.service.util.UtilService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 库存移动相关接口
 */
@RestController
@RequestMapping("/inventoryMove")
public class InventoryMoveController extends BaseController<MoveServiceImpl, InventoryMove, InventoryMoveDetail> {
    @Autowired
    private MoveService moveService;
    @Autowired
    private CodeService codeService;
    @Autowired
    private EasyExcelService easyExcelService;
    @Autowired
    private GetMapListService getMapListService;
    @Autowired
    private ShiroService shiroService;
    @Autowired
    InventoryMoveDAO inventoryMoveDAO;
    @Autowired
    UtilService utilService;

    /**
     * 提交库存移动表头和表细
     *
     * @param inventoryMoveAndDetail
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/submitInventoryMoveAndDetail")
    public HttpResult<?> submitInventoryMoveAndDetail(@RequestBody @NotNull InventoryMoveAndDetail inventoryMoveAndDetail) {
        return moveService.submitInventoryMoveAndDetail(inventoryMoveAndDetail);
    }

    /*
     * 删除
     * */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/deleteInventoryMoveAndDetail")
    public HttpResult<?> deleteInventoryMoveAndDetail(@RequestBody @NotNull InventoryMove inventoryMove) {
        return delTable(inventoryMove);
    }

    /*
     * 修改
     * */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/updateInventoryMoveAndDetail")
    public HttpResult<?> updateInventoryMoveAndDetail(@RequestBody @NotNull HeadAndDetail<InventoryMove, InventoryMoveDetail> headAndDetail) {
        return updTable(headAndDetail);
    }

    /*
     * 根据id查询
     * */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchAllInventoryMoveDetailById")
    public HttpResult<?> searchAllInventoryMoveDetailById(@RequestBody @NotNull InventoryMove inventoryMove) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, moveService.searchInventoryMoveDetailById(inventoryMove));
    }

    /*
     * 查找表头
     * */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchInventoryMoveSelective")
    public HttpResult<?> searchInventoryMoveSelective(@RequestBody @NotNull ScreenDTO<InventoryMove> screenDTO) {
        if (screenDTO.getScreen() != null && screenDTO.getScreen() != "")
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        else screenDTO.setScreen("id");
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, moveService.screenInventoryMove(screenDTO));
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchInventoryMoveNum")
    public HttpResult<?> searchInventoryMoveNum(@RequestBody @NotNull ScreenDTO<InventoryMove> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, moveService.screenInventoryMoveNum(screenDTO));
    }

    /*
     * 查找表细
     * */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchInventoryMoveDetailSelective")
    public HttpResult<?> searchInventoryMoveDetailSelective(@RequestBody @NotNull ScreenDTO<InventoryMoveDetail> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, moveService.screenInventoryMoveDetail(screenDTO));
    }

    /***
     *@Description 查找表细数量
     *@Author lc
     **/
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchInventoryMoveDetailNum")
    public HttpResult<?> searchInventoryMoveDetailNum(@RequestBody @NotNull ScreenDTO<InventoryMoveDetail> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, moveService.screenInventoryMoveDetailNum(screenDTO));
    }

    /**
     * @Description 导出库存移动表头和表细
     * @Author lc
     * @Date 17:53 2022/4/26
     * @Param [screenDTO, response]
     * @Return void
     **/
    @CrossOrigin
    @ResponseBody
    @PostMapping("/exportInventoryMoveAndDetailExcel")
    public void exportInventoryMoveAndDetailExcel(@RequestBody ScreenDTO<InventoryMove> screenDTO, HttpServletResponse response) {
        if (screenDTO.getScreen() != null && screenDTO.getScreen() != "")
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        else screenDTO.setScreen("id");
        List<InventoryMove> inventoryMoveList = moveService.screenInventoryMove(screenDTO);
        List<HeadAndDetail> headAndDetailList = new ArrayList<>();
        for (InventoryMove inventoryMove : inventoryMoveList) {
            HeadAndDetail<InventoryMove, InventoryMoveDetail> headAndDetail = new HeadAndDetail<>();
            headAndDetail.setHead(inventoryMove);
            headAndDetail.setDetails(moveService.searchInventoryMoveDetailById(inventoryMove));
            headAndDetailList.add(headAndDetail);
        }
        easyExcelService.exportExcel("InventoryMoveAndDetail.xlsx", getMapListService.getMap(inventoryMoveList), headAndDetailList, response);
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/audit")
    public HttpResult<?> auditMove(@RequestBody InventoryMove inventoryMove) {
        return moveService.auditMove(inventoryMove);
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/unAudit")
    public HttpResult<?> unAuditMove(@RequestBody InventoryMove inventoryMove) {
        return moveService.unAuditMove(inventoryMove);
    }

    /**
     * rf端获取移动任务
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/applyMove")
    public HttpResult<?> applyMove(@RequestBody ScreenDTO<InventoryMove> screenDTO) {
        return moveService.applyMove(screenDTO);
    }

    /**
     * rf端保存单条移动单表细
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/saveInventoryMoveDetail")
    public HttpResult<?> saveInventoryMoveDetail(@RequestBody InventoryMoveDetail inventoryMoveDetail) {
        return moveService.updateInventoryMoveDetail(inventoryMoveDetail);
    }

    /**
     * rf端提交单条移动单表细
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/submitInventoryMoveDetail")
    public HttpResult<?> submitInventoryMoveDetail(@RequestBody InventoryMoveDetail inventoryMoveDetail) {
        return moveService.updateInventoryMoveDetail(inventoryMoveDetail);
    }

    /**
     * rf端返回还没有移动的表细
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchInventoryMoveDetailByRf")
    public HttpResult<?> searchInventoryMoveDetailByRf(@RequestBody @NotNull ScreenDTO<InventoryMoveDetail> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, moveService.searchInventoryMoveDetailByRf(screenDTO));
    }

    /**
     * rf提交移动表头
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/submitInventoryMove")
    public HttpResult<?> submitInventoryMove(@RequestBody @NotNull InventoryMove inventoryMove) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, moveService.submitInventoryMove(inventoryMove));
    }

    /**
     * 发送给自己的WCS
     *
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/wcsGetInventoryMoveTask")
    public HttpResult<?> wcsGetInventoryMoveTask(@RequestBody ScreenDTO<InventoryMove> screenDTO) {
        return moveService.wcsGetInventoryMoveTask(screenDTO);
    }

    /**
     * 自己wcs移库任务返回
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/wcsInventoryMoveTaskExecute")
    public HttpResult<?> wcsInventoryMoveTaskExecute(@RequestBody HeadAndDetail<TInventoryMove, TInventoryMoveDetail> headAndDetail) {
        return moveService.wcsInventoryMoveTaskExecute(headAndDetail);
    }
}
