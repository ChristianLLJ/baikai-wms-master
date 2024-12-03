package com.bupt.DTO.BaseDataDTO;

import com.bupt.pojo.SysCode;
import com.bupt.pojo.SysCodeDetail;
import lombok.Data;

import java.util.List;

@Data
public class SysCodeAndDetail {
    SysCode sysCode;

    List<SysCodeDetail> sysCodeDetailList;

    Integer sumNum;
}
