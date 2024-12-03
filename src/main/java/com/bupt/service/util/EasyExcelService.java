package com.bupt.service.util;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.pojo.InventoryBalance;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface EasyExcelService {
    void exportExcel(String fileName, List<Map<String, Object>> headMapList, List<HeadAndDetail> headAndDetailList,
                     HttpServletResponse response);

    //只有表细只有一个sheet
    <T> void exportExcelList(String fileName, List<T> list, HttpServletResponse response);

}
