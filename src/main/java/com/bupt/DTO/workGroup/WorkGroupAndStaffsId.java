package com.bupt.DTO.workGroup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkGroupAndStaffsId {
    private Integer workGroupId;
    private List<Integer> staffIds;
}
