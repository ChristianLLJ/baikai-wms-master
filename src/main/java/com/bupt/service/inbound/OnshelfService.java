package com.bupt.service.inbound;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SumAndList;
import com.bupt.DTO.inbound.DetailAndCode;
import com.bupt.DTO.inbound.OnshelfStrategyDTO;
import com.bupt.DTO.wcs.WcsOnshelfReturn;
import com.bupt.pojo.*;
import com.bupt.result.BaokaiHttpResult;
import com.bupt.result.HttpResult;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.util.List;

public interface OnshelfService {
    List<OnshelfAdvice> selectOnshelfAdvice(ScreenDTO<OnshelfAdvice> screenDTO);

    List<OnshelfAdvice> selectOnshelfAdvice(List<OnshelfAdvice> onshelfAdvices);

    Integer selectOnshelfAdviceNum(ScreenDTO<OnshelfAdvice> screenDTO);

    List<OnshelfAdviceDetail> selectOnshelfAdviceDetail(ScreenDTO<OnshelfAdviceDetail> screenDTO);

    Integer selectOnshelfAdviceDetailNum(ScreenDTO<OnshelfAdviceDetail> screenDTO);

    List<Onshelf> selectOnshelf(ScreenDTO<Onshelf> screenDTO);


    Integer selectOnshelfNum(ScreenDTO<Onshelf> screenDTO);

    SumAndList<OnshelfDetail> selectOnshelfDetail(ScreenDTO<OnshelfDetail> screenDTO);

    Integer selectOnshelfDetailNum(ScreenDTO<OnshelfDetail> screenDTO);

    List<OnshelfStrategy> selectOnshelfStrategy(ScreenDTO<OnshelfStrategy> screenDTO);

    Integer selectOnshelfStrategyNum(ScreenDTO<OnshelfStrategy> screenDTO);

    List<OnshelfStrategyDetail> selectOnshelfStrategyDetail(ScreenDTO<OnshelfStrategyDetail> screenDTO);

    Integer selectOnshelfStrategyDetailNum(ScreenDTO<OnshelfStrategyDetail> screenDTO);

    Integer addOnshelfStrategy(OnshelfStrategyDTO onshelfStrategyDTO);

    HeadAndDetail<Onshelf,OnshelfDetail> ONPtoONF(HeadAndDetail<OnshelfAdvice,OnshelfAdviceDetail> onshelfAdviceDetailHeadAndDetail);

    Integer updateInventoryByONF(Onshelf onshelf);

    Integer add(HeadAndDetail<Onshelf,OnshelfDetail> headAndDetail);

    Integer save(HeadAndDetail<Onshelf,OnshelfDetail> headAndDetail);

    Integer packageOnshelf(OnshelfDetail onshelfDetail);

    Integer packageOnshelfSet(Onshelf onshelf);

    Boolean packageOnshelfCheck(Onshelf onshelf);

    Integer addOnshelfDetail(DetailAndCode<OnshelfDetail> detailAndCode);

    Integer setOnshelfOnshelfing(Onshelf onshelf);

    HttpResult<?> wcsTaskExecute(List<String> packageBarcodes);

    BaokaiHttpResult<?> baokaiWcsTaskExecute(WcsOnshelfReturn wcsOnshelfReturn);

    HttpResult<?> reportForm(HeadAndDetail<Onshelf,OnshelfDetail> headAndDetail);
}
