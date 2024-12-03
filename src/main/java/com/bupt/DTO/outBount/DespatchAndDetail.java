package com.bupt.DTO.outBount;

import com.bupt.pojo.Despatch;
import com.bupt.pojo.DespatchDetail;
import lombok.Data;

import java.util.List;

@Data
public class DespatchAndDetail {
    private Despatch despatch;
    private List<DespatchDetail> despatchDetailList;
}
