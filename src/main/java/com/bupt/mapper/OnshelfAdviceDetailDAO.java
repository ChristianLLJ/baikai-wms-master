package com.bupt.mapper;

import com.bupt.pojo.OnshelfAdviceDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * OnshelfAdviceDetailDAO继承基类
 */
@Mapper
@Repository
public interface OnshelfAdviceDetailDAO extends MyBatisBaseDao<OnshelfAdviceDetail, Integer> {
}