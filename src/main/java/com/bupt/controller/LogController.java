package com.bupt.controller;

import com.bupt.pojo.DevLog;
import com.bupt.pojo.NullPojo;
import com.bupt.service.DevLogService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/devLog")
public class LogController extends BaseController<DevLogService, DevLog, NullPojo>{
}
