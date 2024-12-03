package com.bupt.DTO.inbound;

import com.bupt.pojo.OnshelfStrategy;
import com.bupt.pojo.OnshelfStrategyDetail;
import lombok.Data;

import java.util.List;

@Data
public class OnshelfStrategyDTO {
    OnshelfStrategy onshelfStrategy;
    List<OnshelfStrategyDetail> onshelfStrategyDetails;
    List<List<Integer>> limitLocationIds;
    List<List<Integer>> limitSpaceIds;
}
