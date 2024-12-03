package com.bupt.controller.api;

import com.bupt.DTO.wcs.WcsOnshelfReturn;
import com.bupt.result.BaokaiHttpResult;
import com.bupt.result.HttpResult;
import com.bupt.service.inbound.OnshelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class apiController {
    @Autowired
    OnshelfService onshelfService;
    @CrossOrigin
    @ResponseBody
    @PostMapping("/bkcellar/retroute/asnlkfsh")
    public BaokaiHttpResult<?> baokaiWcsTaskExecute(@RequestBody WcsOnshelfReturn wcsOnshelfReturn){
        return onshelfService.baokaiWcsTaskExecute(wcsOnshelfReturn);
    }
}
