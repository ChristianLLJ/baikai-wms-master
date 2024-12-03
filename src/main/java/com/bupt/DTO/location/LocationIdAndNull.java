package com.bupt.DTO.location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LocationIdAndNull {
    private Integer id;
    private Integer cid=null;
    private String name=null;
    private String code=null;
}
