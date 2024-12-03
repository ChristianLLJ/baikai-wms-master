package com.bupt.service.util;

import java.util.List;
import java.util.Map;

public interface GetMapListService<T> {
    List<Map<String, Object>> getMap(List<T> objectList);

}
