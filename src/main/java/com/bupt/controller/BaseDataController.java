package com.bupt.controller;

import com.bupt.DTO.BaseDataDTO.GoodsAndSku;
import com.bupt.DTO.BaseDataDTO.SysCodeAndDetail;
import com.bupt.DTO.FrontName;
import com.bupt.DTO.ScreenDTO;
import com.bupt.mapper.GoodsSkuDAO;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.authority.CodeService;
import com.bupt.service.baseData.*;


import com.bupt.service.util.HttpService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 基础数据相关接口
 */
@CrossOrigin
@RestController
@RequestMapping("/baseData")
public class BaseDataController {
    @Autowired
    private SystemCodeService systemCodeService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private ContainerService containerService;
    @Autowired
    private CustomService customService;
    @Autowired
    private CodeService codeService;
    @Autowired
    HttpService httpService;
    @Autowired
    GoodsSkuDAO goodsSkuDAO;

    @ResponseBody
    @PostMapping("/getCode")
    public HttpResult<?> getCode(@RequestBody @Validated FrontName code) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, codeService.code(code.getName()));
    }

    //-------------------------------------------SysCode操作--------------------------------------------------------------
    /*
     * 新增系统代码--over
     * */

    /**
     * 新增系统代码
     *
     * @return com.bupt.result.HttpResult<?>
     * @Description
     * @Author LIWEMY
     * @Date 18:43 2022/4/5
     * @Param [sysCodeAndDetail]
     **/

    @ResponseBody
    @PostMapping("/addSysCodeAndDetail")
    public HttpResult<?> addSysCodeAndDetail(@RequestBody @Validated SysCodeAndDetail sysCodeAndDetail) {
        return systemCodeService.addSysCodeAndDetail(sysCodeAndDetail.getSysCode(), sysCodeAndDetail.getSysCodeDetailList());
    }

    /**
     * 修改系统代码
     *
     * @return com.bupt.result.HttpResult<?>
     * @Description over
     * @Author LIWEMY
     * @Date 18:43 2022/4/5
     * @Param [sysCodeAndDetail]
     **/
    @ResponseBody
    @PostMapping("/upgradeSysCodeAndDetail")
    public HttpResult<?> upgradeSysCodeAndDetail(@RequestBody @NotNull SysCodeAndDetail sysCodeAndDetail) {
        return systemCodeService.updateSysCodeAndDetail(sysCodeAndDetail);
    }

    /**
     * @Name 删除系统代码
     * @Description
     * @Author LIWEMY
     * @Date 15:44 2022/9/15
     * @Param [sysCode]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/deleteSysCodeAndDetail")
    public HttpResult<?> deleteSysCodeAndDetail(@RequestBody @NotNull SysCode sysCode) {
        return systemCodeService.deleteSysCodeAndDetail(sysCode);
    }

    /**
     * @Name 筛选 代码表头
     * @Description
     * @Author LIWEMY
     * @Date 15:44 2022/9/15
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/screenSysCode")
    public HttpResult<?> screenSysCode(@RequestBody @NotNull ScreenDTO<SysCode> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, systemCodeService.screenSysCode(screenDTO));
    }

    /**
     * 获取 代码表头 总数
     *
     * @return com.bupt.result.HttpResult<?>
     * @Description
     * @Author LIWEMY
     * @Date 18:44 2022/4/5
     * @Param [screenDTO]
     **/

    @ResponseBody
    @PostMapping("/screenSysCodeSum")
    public HttpResult<?> screenSysCodeSum(@RequestBody @NotNull ScreenDTO<SysCode> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, systemCodeService.screenSysCodeNum(screenDTO));
    }

    /**
     * 筛选 系统代码 表细
     *
     * @return com.bupt.result.HttpResult<?>
     * @Description over
     * @Author LIWEMY
     * @Date 18:44 2022/4/5
     * @Param [screenDTO]
     **/
    @ResponseBody
    @PostMapping("/screenSysCodeDetail")
    public HttpResult<?> screenSysCodeDetail(@RequestBody @NotNull ScreenDTO<SysCodeDetail> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, systemCodeService.screenSysCodeDetail(screenDTO));
    }

    /**
     * 获取 系统代码 总数
     *
     * @return com.bupt.result.HttpResult<?>
     * @Description
     * @Author LIWEMY
     * @Date 18:44 2022/4/5
     * @Param [screenDTO]
     **/

    @ResponseBody
    @PostMapping("/screenSysCodeDetailSum")
    public HttpResult<?> screenSysCodeDetailSum(@RequestBody @NotNull ScreenDTO<SysCodeDetail> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, systemCodeService.screenSysCodeDetailNum(screenDTO));
    }

    /**
     * @Name  通过系统代码表头code获取表细
     * @Description
     * @Author LIWEMY
     * @Date 15:32 2023/5/10
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/selectDetailByCode")
    public HttpResult<?> selectDetailByCode(@RequestBody SysCode sysCode) {
        return systemCodeService.selectDetailByCode(sysCode);
    }
    //-------------------------------------------Goods操作--------------------------------------------------------------

    /**
     * 增加商品
     *
     * @return com.bupt.result.HttpResult<?>
     * @Description
     * @Author LIWEMY
     * @Date 18:45 2022/4/5
     * @Param [goodsAndSku]
     **/

    @ResponseBody
    @PostMapping("/addGoodsAndSku")
    public HttpResult<?> addGoodsAndSku(@RequestBody @NotNull GoodsAndSku goodsAndSku) {
        Integer goodsId = goodsService.addGoods(goodsAndSku.getGoods());
        for (GoodsSku goodsSku : goodsAndSku.getGoodsSkuList()) {
            goodsSku.setGoodsId(goodsId);
            goodsService.addGoodsSku(goodsSku);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    /*
     * 删除系统代码--over
     * */
    @ResponseBody
    @PostMapping("/deleteGoodsAndSku")
    public HttpResult<?> deleteGoodsAndSku(@RequestBody @NotNull Goods goods) {
        return goodsService.deleteGoodsAndSku(goods);
    }

    /**
     * 修改系统代码
     *
     * @return com.bupt.result.HttpResult<?>
     * @Description
     * @Author LIWEMY
     * @Date 18:45 2022/4/5
     * @Param [goodsAndSku]
     **/

    @ResponseBody
    @PostMapping("/upgradeGoodsAndSku")
    public HttpResult<?> upgradeGoodsAndSku(@RequestBody @NotNull GoodsAndSku goodsAndSku) {
        goodsService.updateGoods(goodsAndSku.getGoods());
        for (GoodsSku goodsSku : goodsAndSku.getGoodsSkuList()) {
            goodsService.updateGoodsSku(goodsSku);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    /**
     * 筛选 商品 表头信息
     *
     * @return com.bupt.result.HttpResult<?>
     * @Description
     * @Author LIWEMY
     * @Date 18:46 2022/4/5
     * @Param [screenDTO]
     **/

    @ResponseBody
    @PostMapping("/screenGoods")
    public HttpResult<?> screenGoods(@RequestBody @NotNull ScreenDTO<Goods> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, goodsService.screenGoods(screenDTO));
    }

    /**
     * 获取 商品表头 总数
     *
     * @return com.bupt.result.HttpResult<?>
     * @Description
     * @Author LIWEMY
     * @Date 18:46 2022/4/5
     * @Param [screenDTO]
     **/

    @ResponseBody
    @PostMapping("/screenGoodsSum")
    public HttpResult<?> screenGoodsSum(@RequestBody @NotNull ScreenDTO<Goods> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, goodsService.screenGoodsNum(screenDTO));
    }

    /**
     * 筛选 商品SKU
     *
     * @return com.bupt.result.HttpResult<?>
     * @Description
     * @Author LIWEMY
     * @Date 18:46 2022/4/5
     * @Param [screenDTO]
     **/

    @ResponseBody
    @PostMapping("/screenGoodsSku")
    public HttpResult<?> screenGoodsSku(@RequestBody @NotNull ScreenDTO<GoodsSku> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, goodsService.screenGoodsSku(screenDTO));
    }

    /**
     * 筛选 商品SKU 总数
     *
     * @return com.bupt.result.HttpResult<?>
     * @Description over
     * @Author LIWEMY
     * @Date 18:46 2022/4/5
     * @Param [screenDTO]
     **/

    @ResponseBody
    @PostMapping("/screenGoodsSkuSum")
    public HttpResult<?> screenGoodsSkuSum(@RequestBody @NotNull ScreenDTO<GoodsSku> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, goodsService.screenGoodsSkuNum(screenDTO));
    }

    /**
     * @Name 通过skuBarcode查找sku信息
     * @Description
     * @Author LIWEMY
     * @Date 10:44 2022/7/20
     * @Param [goodsSku]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/searchByBarcode")
    public HttpResult<?> searchByBarcode(@RequestBody @NotNull GoodsSku goodsSku) {
        GoodsSku goodsSku1 = goodsSkuDAO.selectByBarcode(goodsSku.getSkuBarcode());
        if (goodsSku1 == null) return HttpResult.of(HttpResultCodeEnum.SYSTEM_NULL, "该sku不存在");
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, goodsService.searchByBarcode(goodsSku.getSkuBarcode()));
    }

    /**
     * @Name 商品代码重复性检查
     * @Description
     * @Author LIWEMY
     * @Date 10:52 2022/7/20
     * @Param [goods]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/checkGoodsCode")
    public HttpResult<?> checkGoodsCode(@RequestBody @NotNull Goods goods) {
        if (!goodsService.goodsCodeCheck(goods)) return HttpResult.of(HttpResultCodeEnum.GOODS_CODE_DUPLICATED);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    /**
     * @Name 商品条码重复性检查
     * @Description
     * @Author LIWEMY
     * @Date 10:52 2022/7/20
     * @Param [goods]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/checkGoodsBarcode")
    public HttpResult<?> checkGoodsBarcode(@RequestBody @NotNull Goods goods) {
        if (!goodsService.goodsbarcodeCheck(goods)) return HttpResult.of(HttpResultCodeEnum.GOODS_CODE_DUPLICATED);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    /**
     * @Name 商品Sku代码重复性检查
     * @Description
     * @Author LIWEMY
     * @Date 10:52 2022/7/20
     * @Param [goods]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/checkGoodsSkuCode")
    public HttpResult<?> checkGoodsSkuCode(@RequestBody @NotNull GoodsSku goodsSku) {
        if (!goodsService.goodsSkuCodeCheck(goodsSku)) return HttpResult.of(HttpResultCodeEnum.GOODS_CODE_DUPLICATED);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    /**
     * @Name 商品Sku条码重复性检查
     * @Description
     * @Author LIWEMY
     * @Date 10:52 2022/7/20
     * @Param [goods]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/checkGoodsSkuBarcode")
    public HttpResult<?> checkGoodsBcheckGoodsSkuBarcodearcode(@RequestBody @NotNull GoodsSku goodsSku) {
        if (!goodsService.goodsSkuBarcodeCheck(goodsSku))
            return HttpResult.of(HttpResultCodeEnum.GOODS_CODE_DUPLICATED);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }
    //------------------------------------------Container操作------------------------------------------------------------

    /**
     * @Name 新增 Container
     * @Description 包装类型：1：盒、2：箱、3：托盘、4：袋、5：桶
     * @Author LIWEMY
     * @Date 14:40 2022/7/6
     * @Param [container]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/addContainer")
    public HttpResult<?> addContainer(@RequestBody @NotNull Container container) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, containerService.addContainer(container));
    }

    /*
     * 删除 Container
     * */

    @ResponseBody
    @PostMapping("/deleteContainer")
    public HttpResult<?> deleteContainer(@RequestBody @NotNull Container container) {
        return containerService.deleteContainer(container);
    }

    /**
     * 更新Container
     *
     * @return com.bupt.result.HttpResult<?>
     * @Description
     * @Author LIWEMY
     * @Date 18:47 2022/4/5
     * @Param [container]
     **/

    @ResponseBody
    @PostMapping("/updateContainer")
    public HttpResult<?> update(@RequestBody @NotNull Container container) {
        return containerService.updateContainer(container);
    }

    /**
     * 筛选 Container
     *
     * @return com.bupt.result.HttpResult<?>
     * @Description
     * @Author LIWEMY
     * @Date 18:47 2022/4/5
     * @Param [screenDTO]
     **/

    @ResponseBody
    @PostMapping("/screenContainer")
    public HttpResult<?> screenContainer(@RequestBody @NotNull ScreenDTO<Container> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, containerService.screenContainer(screenDTO));
    }

    /**
     * 筛选 Container 总数
     *
     * @return com.bupt.result.HttpResult<?>
     * @Description
     * @Author LIWEMY
     * @Date 18:47 2022/4/5
     * @Param [screenDTO]
     **/

    @ResponseBody
    @PostMapping("/screenContainerSum")
    public HttpResult<?> screenContainerSum(@RequestBody @NotNull ScreenDTO<Container> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, containerService.screenContainerNum(screenDTO));
    }
    //---------------------------------------------Custom操作------------------------------------------------------------

    /**
     * 新增 Custom
     *
     * @return com.bupt.result.HttpResult<?>
     * @Description 客户状态 1：货主2：收货人3：供应商4：承运人5：结算人6：仓库7：下单方8：其他
     * @Author LIWEMY
     * @Date 18:48 2022/4/5
     * @Param [custom]
     **/

    @ResponseBody
    @PostMapping("/addCustom")
    public HttpResult<?> addCustom(@RequestBody @NotNull Custom custom) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, customService.addCustom(custom));
    }

    /*
     * 删除
     * */

    @ResponseBody
    @PostMapping("/deleteCustom")
    public HttpResult<?> deleteCustom(@RequestBody @NotNull Custom custom) {
        return customService.deleteCustom(custom);
    }

    /**
     * 更新 Custom
     *
     * @return com.bupt.result.HttpResult<?>
     * @Description
     * @Author LIWEMY
     * @Date 18:48 2022/4/5
     * @Param [custom]
     **/

    @ResponseBody
    @PostMapping("/updateCustom")
    public HttpResult<?> updateCustom(@RequestBody @NotNull Custom custom) {
        return customService.updateCustom(custom);
    }

    /**
     * 筛选
     *
     * @return com.bupt.result.HttpResult<?>
     * @Description
     * @Author LIWEMY
     * @Date 18:48 2022/4/5
     * @Param [screenDTO]
     **/

    @ResponseBody
    @PostMapping("/screenCustom")
    public HttpResult<?> screenCustom(@RequestBody @NotNull ScreenDTO<Custom> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, customService.screenCustom(screenDTO));
    }

    /**
     * 筛选总数
     *
     * @return com.bupt.result.HttpResult<?>
     * @Description
     * @Author LIWEMY
     * @Date 18:48 2022/4/5
     * @Param [screenDTO]
     **/

    @ResponseBody
    @PostMapping("/screenCustomSum")
    public HttpResult<?> screenCustomSum(@RequestBody @NotNull ScreenDTO<Custom> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, customService.screenCustomNum(screenDTO));
    }
}
