package com.bupt.mapper;

import com.bupt.DTO.ScreenDTO;
import com.bupt.pojo.InboundPlanDetail;
import com.bupt.pojo.Onshelf;
import com.bupt.pojo.OnshelfDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * OnshelfDetailDAO继承基类
 */
@Mapper
@Repository
public interface OnshelfDetailDAO extends MyBatisBaseDao<OnshelfDetail, Integer> {
    List<OnshelfDetail> selectByOnshelfId(Onshelf onshelf);

    Integer deleteByOnshelfId(Integer id);

    List<OnshelfDetail> checkOnshelf(Integer id);

    List<OnshelfDetail> selectByContainerBarcode(OnshelfDetail onshelfDetail);

    List<OnshelfDetail> selectByOnshelfIdAndState(Integer id);

    Integer SumOnshelfNum(ScreenDTO<InboundPlanDetail> screenDTO);
}
