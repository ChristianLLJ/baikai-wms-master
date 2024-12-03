package com.bupt.service.authority;

import com.bupt.DTO.*;
import com.bupt.pojo.Function;
import com.bupt.pojo.Functions;
import com.bupt.result.HttpResult;

import java.util.List;

public interface FunctionService {
    //已废弃
    List<Functions> function(IdAndSearchDTO idAndSearchDTO);
//已废弃
    ResultsAndNum<Functions> searchFunction(SearchDTO searchDTO);

    HttpResult<?> addFunction(Functions function);

    HttpResult<?> delFunction(FrontId frontId);

    HttpResult<?> updFunction(Functions function);

    List<Functions> selectFunction(ScreenDTO<Functions> screenDTO);

    Integer selectFunctionNum(ScreenDTO<Functions> screenDTO);
}
