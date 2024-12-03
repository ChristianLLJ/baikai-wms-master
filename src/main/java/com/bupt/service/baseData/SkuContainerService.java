package com.bupt.service.baseData;

import com.bupt.mapper.NullDAO;
import com.bupt.mapper.SkuContainerBatchDAO;
import com.bupt.pojo.NullPojo;
import com.bupt.pojo.SkuContainerBatch;
import com.bupt.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class SkuContainerService extends BaseService<SkuContainerBatch, NullPojo, SkuContainerBatchDAO, NullDAO> {
}
