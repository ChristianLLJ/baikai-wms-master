package com.bupt.service.inbound;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SumAndList;
import com.bupt.DTO.inbound.GenerateIPLDTO;
import com.bupt.pojo.PurchaseOrder;
import com.bupt.pojo.PurchaseOrderDetail;

import java.util.List;

public interface PurchaseOrderService {
    List<PurchaseOrder> selectPurchaseOrder(ScreenDTO<PurchaseOrder> screenDTO);

    List<PurchaseOrder> selectPurchaseOrder(List<PurchaseOrder> purchaseOrders);

    Integer selectPurchaseOrderNum(ScreenDTO<PurchaseOrder> screenDTO);

    SumAndList<PurchaseOrderDetail> selectPurchaseOrderDetail(ScreenDTO<PurchaseOrderDetail> screenDTO);

    Integer selectPurchaseOrderDetailNum(ScreenDTO<PurchaseOrderDetail> screenDTO);

    Integer save(HeadAndDetail<PurchaseOrder,PurchaseOrderDetail> headAndDetail);

    Integer add(HeadAndDetail<PurchaseOrder,PurchaseOrderDetail> headAndDetail);

    Integer cancel(HeadAndDetail<PurchaseOrder,PurchaseOrderDetail> headAndDetail);

    Integer close(PurchaseOrder purchaseOrder);

    Integer generate(GenerateIPLDTO generateIPLDTO);
}
