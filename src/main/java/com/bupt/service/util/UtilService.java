package com.bupt.service.util;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UtilService {
    /**
     * @param str
     * @return java.lang.String
     * @author Howe
     * @Description 将驼峰转为下划线
     * @Date 2022/4/22 13:11
     * @since 1.0.0
     */
    public  String xX2x_x(String str) {
        Pattern compile = Pattern.compile("[A-Z]");
        Matcher matcher = compile.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
