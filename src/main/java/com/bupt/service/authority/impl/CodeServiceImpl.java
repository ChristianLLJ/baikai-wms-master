package com.bupt.service.authority.impl;

import com.bupt.mapper.SerialNumberDAO;
import com.bupt.pojo.SerialNumber;
import com.bupt.service.authority.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class CodeServiceImpl implements CodeService {
    @Autowired
    SerialNumberDAO serialNumberDAO;
    @Scheduled(cron = "0 0 0 * * ?")
    /**
     * 更新流水号（每日更新）
     */
    @Override
    public void changeSerialNumber(){
        SerialNumber serialNumber=new SerialNumber();
        String[] strNow1 = new SimpleDateFormat("yy-MM-dd").format(new Date()).toString().split("-");
        serialNumber.setDateNum(strNow1[0]+strNow1[1]+strNow1[2]);
        serialNumber.setNum(1);
        serialNumberDAO.update(serialNumber);
    }

    /**
     * 根据单号类型生成代码
     * @param s
     * @return
     */
    @Override
    public String code(String s) {
        SerialNumber serialNumber=new SerialNumber();
        //获取流水号
        serialNumber=serialNumberDAO.selectByName(s);
        //根据流水号生成代码
        String newString=s+serialNumber.getDateNum()+ new DecimalFormat("0000").format(serialNumber.getNum());
        //流水号加一
        serialNumberDAO.updatePlusOne(serialNumber.getId());
        return newString;
    }
}
