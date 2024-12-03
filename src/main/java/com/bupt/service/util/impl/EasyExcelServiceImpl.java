package com.bupt.service.util.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.bupt.DTO.HeadAndDetail;
import com.bupt.pojo.InventoryBalance;
import com.bupt.service.util.EasyExcelService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@Service
public class EasyExcelServiceImpl implements EasyExcelService {
    @Value("${excelAddress}")
    String excelAddress;
    @Override
    public void exportExcel(String fileName, List<Map<String, Object>> headMapList, List<HeadAndDetail> headAndDetailList, HttpServletResponse response) {
        String templateFileName = excelAddress;
        templateFileName += fileName;
        int sheetNum = headAndDetailList.size();
        try (FileInputStream fileInputStream = new FileInputStream(templateFileName); ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            //原模板只有一个sheet，通过poi复制出需要的sheet个数的模板
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            for (int i = 1; i < sheetNum; i++) {
                //复制模板，得到第i+1个sheet
                workbook.cloneSheet(0, "sheet" + (i + 1));
            }
            //写到流里
            workbook.write(bos);
            byte[] bArray = bos.toByteArray();
            InputStream is = new ByteArrayInputStream(bArray);
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(is).build();
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String s = fileName.replaceAll(".xls", "");
            response.setHeader("Content-disposition", "attachment;filename=" + s + System.currentTimeMillis() + ".xls");
            for (int i = 0; i < sheetNum; i++) {
                WriteSheet writeSheet = EasyExcel.writerSheet(i).build();
                FillConfig fillConfig = FillConfig.builder().build();
                excelWriter.fill(headAndDetailList.get(i).getDetails(), fillConfig, writeSheet);
                excelWriter.fill(headMapList.get(i), writeSheet);
            }
            excelWriter.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> void exportExcelList(String fileName, List<T> list, HttpServletResponse response) {
        String templateFileName = excelAddress;
        templateFileName += fileName;
        try (FileInputStream fileInputStream = new FileInputStream(templateFileName); ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            //原模板只有一个sheet，通过poi复制出需要的sheet个数的模板
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            //写到流里
            workbook.write(bos);
            byte[] bArray = bos.toByteArray();
            InputStream is = new ByteArrayInputStream(bArray);
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(is).build();
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String s = fileName.replaceAll(".xls", "");
            response.setHeader("Content-disposition", "attachment;filename=" + s + System.currentTimeMillis() + ".xls");
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            FillConfig fillConfig = FillConfig.builder().build();
            excelWriter.fill(list, fillConfig, writeSheet);
            excelWriter.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
