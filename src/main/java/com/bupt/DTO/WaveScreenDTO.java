package com.bupt.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WaveScreenDTO<T> {
    private SearchDTO searchDTO;
    private T pojo;
    private String screen;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date endTime;
    Date startTime1;
    Date endTime1;
    Integer workGroupId, count;
    List<Integer> states = new ArrayList<>(), waveRuleId = new ArrayList<>();
    List<String> warehouseCodes = new ArrayList<>();
}
