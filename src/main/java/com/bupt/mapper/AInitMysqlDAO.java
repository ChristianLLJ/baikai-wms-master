package com.bupt.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AInitMysqlDAO {
    void init();
    void initTwo();
    void initThree();
    void initFour();
    void initDispaly();
}
