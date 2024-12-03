package com.bupt.baokai_wms;

import com.bupt.pojo.Despatch;
import com.bupt.service.authority.CodeService;
import com.bupt.service.outbound.DeliverGoodsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class BaokaiWmsApplicationTests {
    @Autowired
    private DeliverGoodsService deliverGoodsService;
    @Autowired
    private CodeService codeService;

    @Test
    void contextLoads() {
    }

    @Test
    void returnCode() {
        // 
    }

    @Test
    public void changeSerialNumber() {
        /*SerialNumber serialNumber=new SerialNumber();
        String[] strNow1 = new SimpleDateFormat("yy-MM-dd").format(new Date()).toString().split("-");
        serialNumber.setDateNum(strNow1[0]+strNow1[1]+strNow1[2]);
        serialNumber.setNum(0);
        serialNumberDAO.update(serialNumber);*/

    }
}
