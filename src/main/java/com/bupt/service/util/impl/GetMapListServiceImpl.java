package com.bupt.service.util.impl;

import com.bupt.service.util.GetMapListService;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;
@Service
public class GetMapListServiceImpl implements GetMapListService {
    @Override
    public List<Map<String, Object>> getMap(List objectList) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        objectList.forEach(e -> {
            Map<String, Object> map = new HashMap<>();
            List<String> allComponentsName = getAllComponentsName(e);
            allComponentsName.forEach(a -> {
                map.put(a, getAllComponentsValue(e, a));
            });
            mapList.add(map);
        });
        return mapList;
    }

    public static List<String> getAllComponentsName(Object f) {
        Field[] fields = f.getClass().getDeclaredFields();
        List<String> nameList = new ArrayList<>();
        Arrays.stream(fields).forEach(e -> nameList.add(e.getName()));
        return nameList;
    }

    public static Object getAllComponentsValue(Object f, String name) {
        Object obj = null;
        // 获取f对象对应类中的所有属性域
        Field[] fields = f.getClass().getDeclaredFields();
        for (int i = 0, len = fields.length; i < len; i++) {
            // 对于每个属性，获取属性名
            String varName = fields[i].getName();
            if (varName.equals(name)) {
                try {
                    // 获取原来的访问控制权限
                    boolean accessFlag = fields[i].isAccessible();
                    // 修改访问控制权限
                    fields[i].setAccessible(true);
                    // 获取在对象f中属性fields[i]对应的对象中的变量
                    obj = fields[i].get(f);
                    // 恢复访问控制权限
                    fields[i].setAccessible(accessFlag);
                } catch (IllegalArgumentException ex) {
                    ex.printStackTrace();
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return obj;
    }
}
