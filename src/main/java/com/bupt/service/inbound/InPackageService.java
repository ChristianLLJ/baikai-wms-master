package com.bupt.service.inbound;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SumAndList;
import com.bupt.DTO.inbound.DetailAndCode;
import com.bupt.DTO.inbound.GenerateCOB;
import com.bupt.pojo.PackingDetail;
import com.bupt.pojo.PackingTable;

import java.util.List;

public interface InPackageService {
    List<PackingTable> selectPackingTable(ScreenDTO<PackingTable> screenDTO);

    List<PackingTable> selectPackingTable(List<PackingTable> packingTables);

    Integer selectPackingTableNum(ScreenDTO<PackingTable> screenDTO);

    SumAndList<PackingDetail> selectPackingTableDetail(ScreenDTO<PackingDetail> screenDTO);

    Integer selectPackingTableDetailNum(ScreenDTO<PackingDetail> screenDTO);

    Integer cancel(HeadAndDetail<PackingTable,PackingDetail> headAndDetail);

    Integer close(PackingTable packingTable);

    Integer generateCOB(GenerateCOB generateCOB);

    Integer save(HeadAndDetail<PackingTable,PackingDetail> headAndDetail);

    Integer add(HeadAndDetail<PackingTable,PackingDetail> headAndDetail);

    Boolean packedCheck(PackingTable packingTable);

    Integer packedSet(PackingTable packingTable);

    Integer packedContainer(PackingDetail packingDetail);

    Integer addPackingDetail(DetailAndCode<PackingDetail> detailAndCode);

    Integer setPackingTablePacking(PackingTable packingTable);
}
