package com.bupt.mapper;

import com.bupt.pojo.CombineStackDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CombineStackDetailDAO继承基类
 */
@Mapper
@Repository
public interface CombineStackDetailDAO extends MyBatisBaseDao<CombineStackDetail, Integer> {
    Integer deleteByStackId(Integer id);

    List<CombineStackDetail> selectByPackageBarcode(String code);

    List<CombineStackDetail> checkStacked(Integer id);
}
