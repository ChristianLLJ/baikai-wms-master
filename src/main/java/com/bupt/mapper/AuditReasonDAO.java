package com.bupt.mapper;

import com.bupt.pojo.AuditReason;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * AuditReasonDAO继承基类
 */
@Mapper
@Repository
public interface AuditReasonDAO extends MyBatisBaseDao<AuditReason, Integer> {
    String selectReason(AuditReason auditReason);
}
