package com.bupt.mapper;

import com.bupt.pojo.CombineStackPackageDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CombineStackPackageDetailDAO继承基类
 */
@Mapper
@Repository
public interface CombineStackPackageDetailDAO extends MyBatisBaseDao<CombineStackPackageDetail, Integer> {
    Integer deleteByDetailId(Integer id);

    List<CombineStackPackageDetail> checkStacked(Integer id);

    Integer updateByStackBarcodeAndContainerBarcode(CombineStackPackageDetail combineStackPackageDetail);
}
