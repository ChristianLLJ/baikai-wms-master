package com.bupt.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class IdAndSearchDTO {
    private Integer id;
    private Integer page;
    private Integer num;
}
