package com.bupt.service.util;

import com.bupt.DTO.HeadAndDetail;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ExcelService<H,D> {
    void ExcelOut(List<H> head, List<D> details, String name, HttpServletResponse httpServletResponse);
}