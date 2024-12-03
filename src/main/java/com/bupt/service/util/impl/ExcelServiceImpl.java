package com.bupt.service.util.impl;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.bupt.pojo.InboundPlan;
import com.bupt.pojo.InboundPlanDetail;
import com.bupt.service.util.ExcelService;
import com.bupt.utils.ExcelUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class ExcelServiceImpl implements ExcelService {
    @Override
    public void ExcelOut(List head, List details, String name, HttpServletResponse httpServletResponse) {
        Map<String,Object> headMap=new HashMap<>();
        Map<String,Object> detailMap = new HashMap<>();
        ExportParams headParams = new ExportParams();
        ExportParams detailParams = new ExportParams();
        headParams.setSheetName("表头");
        headMap.put("title",headParams);
        headMap.put("entity",InboundPlan.class);
        headMap.put("data",head);
        detailParams.setSheetName("详细信息");
        detailMap.put("title",detailParams);
        detailMap.put("entity",InboundPlanDetail.class);
        detailMap.put("data",details);
        List<Map<String,Object>> mapList=new LinkedList<>();
        mapList.add(headMap);
        mapList.add(detailMap);
        try {
            ExcelUtils.exportExcel(mapList,name,httpServletResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
