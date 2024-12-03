package com.bupt.controller.warehouseData;

import com.bupt.controller.BaseController;
import com.bupt.pojo.ContainerHardware;
import com.bupt.pojo.NullPojo;
import com.bupt.service.warehouseData.ShelfService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shelf")
public class ShelfController extends BaseController<ShelfService, ContainerHardware, NullPojo> {

}
