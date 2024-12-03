package com.bupt.service.util;

import com.bupt.DTO.HeadAndDetail;


import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ExcelInputService {
    //List<HeadAndDetail> excelInput(String filePath) throws IOException, BiffException;
    void exportExcel(String fileName, Map<String, Object>[] headMapList, List<HeadAndDetail> headAndDetailList, HttpServletResponse response) ;

}
