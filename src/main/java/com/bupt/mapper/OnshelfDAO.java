package com.bupt.mapper;

import com.bupt.pojo.Onshelf;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * OnshelfDAO继承基类
 */
@Mapper
@Repository
public interface OnshelfDAO extends MyBatisBaseDao<Onshelf, Integer> {

    List<Onshelf> selectByOnshelfCode(String code);

    List<Onshelf> selectStateAfterCreated( );
}
