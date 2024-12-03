package com.bupt.service.util;

import com.bupt.mapper.AuditReasonDAO;
import com.bupt.mapper.NullDAO;
import com.bupt.pojo.AuditReason;
import com.bupt.pojo.NullPojo;
import com.bupt.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class AuditReasonService extends BaseService<AuditReason, NullPojo, AuditReasonDAO, NullDAO> {
    public String unauditReason(AuditReason auditReason){
        return super.tableDAO.selectReason(auditReason);
    }
}
