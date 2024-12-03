package com.bupt.service.warehouseData;

import com.bupt.mapper.ContainerHardwareDAO;
import com.bupt.mapper.NullDAO;
import com.bupt.pojo.ContainerHardware;
import com.bupt.pojo.NullPojo;
import com.bupt.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class ShelfService extends BaseService<ContainerHardware, NullPojo, ContainerHardwareDAO, NullDAO> {
}
