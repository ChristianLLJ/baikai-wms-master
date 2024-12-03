package com.bupt.service;

import com.bupt.mapper.DevLogDAO;
import com.bupt.mapper.NullDAO;
import com.bupt.pojo.DevLog;
import com.bupt.pojo.NullPojo;
import org.springframework.stereotype.Service;

@Service
public class DevLogService extends BaseService<DevLog, NullPojo, DevLogDAO,NullDAO>{
}
