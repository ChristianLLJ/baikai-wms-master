package com.bupt.DTO.outBount;

import com.bupt.pojo.Loading;
import com.bupt.pojo.LoadingDetail;
import lombok.Data;

import java.util.List;

@Data
public class LoadAndDetail {
    private Loading loading;

    private List<LoadingDetail> loadingDetailList;
}
