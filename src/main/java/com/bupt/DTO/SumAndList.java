package com.bupt.DTO;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class SumAndList <T> {
    List<T> list;
    HashMap<String,Double> sums;
}
