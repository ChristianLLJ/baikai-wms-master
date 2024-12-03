package com.bupt.service.authority.impl;

import com.bupt.DTO.*;
import com.bupt.mapper.FunctionDAO;
import com.bupt.mapper.FunctionsDAO;
import com.bupt.mapper.NullDAO;
import com.bupt.mapper.WorkGroupFunctionRelationDAO;
import com.bupt.pojo.Function;
import com.bupt.pojo.Functions;
import com.bupt.pojo.NullPojo;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.BaseService;
import com.bupt.service.authority.FunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class FunctionServiceImpl extends BaseService<Functions, NullPojo, FunctionsDAO, NullDAO>implements FunctionService {
    @Autowired
    WorkGroupFunctionRelationDAO workGroupFunctionRelationDAO;
    @Autowired
    FunctionsDAO functionsDAO;

    /**
     * 返回工作组功能（已废弃）
     * @param idAndSearchDTO
     * @return
     */
    @Override
    public List<Functions> function(IdAndSearchDTO idAndSearchDTO){
        idAndSearchDTO.setPage((idAndSearchDTO.getPage()-1)* idAndSearchDTO.getNum());
        List<Integer> functionIds=workGroupFunctionRelationDAO.selectByWorkGroupId(idAndSearchDTO);
        List<Functions> functions=new LinkedList<>();
        for(Integer id:functionIds){
            functions.add(functionsDAO.selectByPrimaryKey(id));
        }
        return functions;
    }

    /**
     * 分页查询所有功能
     * @param searchDTO
     * @return
     */
    @Override
    public ResultsAndNum<Functions> searchFunction(SearchDTO searchDTO){
        //设置limit起始参数
        searchDTO.setPage((searchDTO.getPage()-1)*searchDTO.getNum());
        ResultsAndNum<Functions> resultsAndNum=new ResultsAndNum<>();
        //获取结果和数量
        // resultsAndNum.setResults(functionsDAO.selectFunction(searchDTO));
        resultsAndNum.setNum(functionsDAO.selectNum());
        return resultsAndNum;
    }

    /**
     * 增加功能
     * @param function
     * @return
     */
    @Override
    public HttpResult<?> addFunction(Functions function){
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,functionsDAO.insert(function));
    }

    /**
     * 删除功能
     * @param frontId
     * @return
     */
    @Override
    public HttpResult<?> delFunction(FrontId frontId){
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,functionsDAO.deleteByPrimaryKey(frontId.getId()));
    }

    /**
     * 更新功能
     * @param function
     * @return
     */
    @Override
    public HttpResult<?> updFunction(Functions function){
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,functionsDAO.updateByPrimaryKeySelective(function));
    }


    /**
     * 筛选功能
     * @param screenDTO
     * @return
     */
    @Override
    public List<Functions> selectFunction(ScreenDTO<Functions> screenDTO) {
        //设置limit起始参数
        if(screenDTO.getSearchDTO()!=null)
        screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage()-1)* screenDTO.getSearchDTO().getNum());
        return functionsDAO.screen(screenDTO);
    }

    /**
     * 筛选功能数量
     * @param screenDTO
     * @return
     */
    @Override
    public Integer selectFunctionNum(ScreenDTO<Functions> screenDTO) {
        return functionsDAO.numScreen(screenDTO);
    }
}
