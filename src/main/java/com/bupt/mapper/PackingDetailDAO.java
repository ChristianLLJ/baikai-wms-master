package com.bupt.mapper;

import com.bupt.pojo.PackingDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * PackingDetailDAO继承基类
 */
@Mapper
@Repository
public interface PackingDetailDAO extends MyBatisBaseDao<PackingDetail, Integer> {
    Integer deleteByPackingId(Integer id );

    List<PackingDetail> checkPacked(Integer id);

    Integer updateIsPackedByPackageCodeAndSkuCode(PackingDetail packingDetail);
}
