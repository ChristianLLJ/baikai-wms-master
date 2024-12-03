package com.bupt.DTO.area;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AreaTypeIdAndNull {
    private Integer id;
    private Integer cid=null;
    private String name=null;
}
