package com.bupt.controller.authority;

import com.bupt.DTO.FrontId;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SearchDTO;
import com.bupt.controller.BaseController;
import com.bupt.pojo.Function;
import com.bupt.pojo.Functions;
import com.bupt.pojo.NullPojo;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.authority.impl.FunctionServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/function")
public class FunctionController extends BaseController<FunctionServiceImpl, Functions, NullPojo> {
    /**
     * 功能信息全部查询
     * @param searchDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchFunction")
    public HttpResult<?> searchFunction(@RequestBody SearchDTO searchDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,super.service.searchFunction(searchDTO));
    }
    /**
     * 功能筛选
     * @param function
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectFunction")
    public HttpResult<?> selectFunction(@RequestBody ScreenDTO<Functions> function) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,super.service.selectFunction(function));
    }

    /**
     * 功能数量返回
     * @param function
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectFunctionNum")
    public HttpResult<?> selectFunctionNum(@RequestBody ScreenDTO<Functions> function) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,super.service.selectFunctionNum(function));
    }
    /**
     * 添加功能
     * @param function
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("addFunction")
    public HttpResult<?> addFunction(@RequestBody @Valid Functions function) {
        return super.service.addFunction(function);
    }



    /**
     * 删除功能
     * @param frontId
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("delFunction")
    public HttpResult<?> delFunction(@RequestBody FrontId frontId) {
        return super.service.delFunction(frontId);
    }
    /**
     * 修改功能
     * @param function
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("updFunction")
    public HttpResult<?> updFunction(@RequestBody @Valid Functions function) {
        return super.service.updFunction(function);
    }
}
