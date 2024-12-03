package com.bupt.DTO.workGroup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@Data
@AllArgsConstructor
public class WorkGroupAndFunctionsId {
    private Integer workGroupId;
    private List<Integer> functionsId;
}
