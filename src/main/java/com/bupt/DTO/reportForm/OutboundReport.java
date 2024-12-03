package com.bupt.DTO.reportForm;

import com.bupt.DTO.DespatchAndDetailOneToOne;
import com.bupt.DTO.SearchDTO;
import com.bupt.pojo.DespatchDetail;
import com.bupt.pojo.InboundPlanDetail;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class OutboundReport {
    /*1-根据时间段内，该仓库，该客户，该收货人，发运订单进行筛选
      2-如果筛选sku再进行发运订单表细的筛选
      3-统计开始
     */
    //筛选项
    SearchDTO searchDTO;

    Integer customerId;
    String customerName;
    Integer receiverId;
    String receiverName;
    Integer warehouseId;
    String warehouseName;
    Integer skuId;
    String skuName;
    String screen;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date endTime;
    //结果项
    Integer skuOfTotalCnt;//出库sku总数
    Integer skuOfOutCnt;//已出库sku总数,已发运订单表细求和，默认今天，前端传
    Integer skuWaitoutCnt;//待发运订单sku数
    Integer boxesCnt;//总箱数
    Integer totalDesNum;//新生成的发运订单数，默认今天，前端传时间段
    Integer desNumToOut;//已发运订单数，默认今天，前端传时间段
    Integer desNumWaitOut;//待发运订单数，默认今天，前端传时间段
    List<DespatchAndDetailOneToOne> despatchDetailList;
}
