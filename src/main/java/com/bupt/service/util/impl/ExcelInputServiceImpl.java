package com.bupt.service.util.impl;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.service.util.ExcelInputService;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@Service
public class ExcelInputServiceImpl implements ExcelInputService {
    /*@Override
    public List<HeadAndDetail> excelInput(String filePath) throws IOException, BiffException {

        FileInputStream fileIn = new FileInputStream(filePath);
        Workbook wb = Workbook.getWorkbook(fileIn);
        List<HeadAndDetail> headAndDetails=new ArrayList<>();
        HeadAndDetail<List<Cell>,List<List<Cell>>> headAndDetail=new HeadAndDetail<>();
        List<List<Cell>> cells=new ArrayList<>();

        for (Sheet sheet:wb.getSheets()){
            headAndDetail.setHead(Arrays.asList(sheet.getRow(1)));
            for (int i = 3; i < sheet.getRows(); i++) {
                cells.add(Arrays.asList(sheet.getRow(i)));
            }
            headAndDetail.setDetails(Collections.singletonList(cells));
            headAndDetails.add(headAndDetail);
        }
        fileIn.close();
        return headAndDetails;
    }*/

    @Override
    public void exportExcel(String fileName, Map<String, Object>[] headMapList, List<HeadAndDetail> headAndDetailList, HttpServletResponse response) {

    }
}
