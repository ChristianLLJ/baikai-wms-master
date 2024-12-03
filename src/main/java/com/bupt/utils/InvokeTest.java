package com.bupt.utils;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.JindieERPPurchaseOrder;
import com.bupt.mapper.PurchaseOrderDAO;
import com.bupt.mapper.PurchaseOrderDetailDAO;
import com.bupt.mapper.StaffDAO;
import com.bupt.pojo.PurchaseOrder;
import com.bupt.pojo.PurchaseOrderDetail;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
@Service
public class InvokeTest {
	@Autowired
	static StaffDAO staffDAO;
	@Autowired
	static PurchaseOrderDAO purchaseOrderDAO;
	@Autowired
	static PurchaseOrderDetailDAO purchaseOrderDetailDAO;
	public static void main(String[] args) throws Exception {
		InvokeHelper.POST_K3CloudURL = "http://baokai.ik3cloud.com/k3cloud/";
		String dbId = "20180507125438";
		String uid = "苏志远";
		String pwd = "bk-123456";
		int lang = 2052;
		List<JindieERPPurchaseOrder> jindieERPPurchaseOrders = new LinkedList<>();
		if (InvokeHelper.Login(dbId, uid, pwd, lang)) {
			// ���۶����������
			// ҵ�����Id
			//��Ҫ���������
			// �����ֶο�����Ҫ�����Լ�ʵ��ֵ���޸�
			// FCustId FSalerId FMaterialId FUnitID
			Date current = new Date();
			Date back = DateUtils.addDays(current,-1);
			SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd");
			String date = matter1.format(back);
			String sContent = "{\"FormId\":\"PUR_PurchaseOrder\",\"FieldKeys\":\"FBillNo,FDocumentStatus,FBillTypeID.FName,FSupplierId.FName,FDate,FPurchaseOrgId.FName,FCloseStatus,FMaterialId,FMaterialName,FUnitId.Fname,FQty,FEntryAmount,FDeliveryDate,FMRPCloseStatus,FModifyDate\",\"FilterString\":[{\"Left\":\"(\",\"FieldName\":\"FModifyDate\",\"Compare\":\">\",\"Value\":\""+date+"\",\"Right\":\")\",\"Logic\":\"AND\"}],\"OrderString\":\"\",\"TopRowCount\":0,\"StartRow\":0,\"Limit\":10000,\"SubSystemId\":\"\"}";
			String result = InvokeHelper.ExecuteBillQuery(sContent);
			result = result.substring(2,result.length()-2);
			String[] strings = result.split("],\\[");
			for(String s:strings){
				String format = "yyyy-MM-dd'T'HH:mm:ss";
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
				s=s.replaceAll("\"","");
				String[] val = s.split(",");
				JindieERPPurchaseOrder jindieERPPurchaseOrder = new JindieERPPurchaseOrder();
				jindieERPPurchaseOrder.setFBillNo(val[0]);
				jindieERPPurchaseOrder.setFDocumentStatus(val[1]);
				jindieERPPurchaseOrder.setFBillTypeID(val[2]);
				jindieERPPurchaseOrder.setFSupplierId(val[3]);
				jindieERPPurchaseOrder.setFDate(simpleDateFormat.parse(val[4]));
				jindieERPPurchaseOrder.setFPurchaseOrgId(val[5]);
				jindieERPPurchaseOrder.setFCloseStatus(val[6]);
				jindieERPPurchaseOrder.setFMaterialId(Integer.valueOf(val[7]));
				jindieERPPurchaseOrder.setFMaterialName(val[8]);
				jindieERPPurchaseOrder.setFUnitId(val[9]);
				try {
					jindieERPPurchaseOrder.setFQty(Double.valueOf(val[10]));
				}
				catch (Exception e){
					continue;
				}
				jindieERPPurchaseOrder.setFEntryAmount(Double.valueOf(val[11]));
				jindieERPPurchaseOrder.setFDeliveryDate(simpleDateFormat.parse(val[4]));
				jindieERPPurchaseOrder.setFMRPCloseStatus(val[13]);
				jindieERPPurchaseOrders.add(jindieERPPurchaseOrder);
			}
		}
		HashMap<String,Integer> heads = new HashMap<>();
		List<HeadAndDetail<PurchaseOrder, PurchaseOrderDetail>> headAndDetailList = new LinkedList<>();
		List<HeadAndDetail<PurchaseOrder,PurchaseOrderDetail>> result = new LinkedList<>();
		int order=0;
		for(JindieERPPurchaseOrder jindieERPPurchaseOrder:jindieERPPurchaseOrders){
			if(!heads.containsKey(jindieERPPurchaseOrder.getFBillNo())){
				heads.put(jindieERPPurchaseOrder.getFBillNo(),order);
				order++;
				HeadAndDetail<PurchaseOrder,PurchaseOrderDetail> headAndDetail = new HeadAndDetail<>();
				headAndDetail.setHead(new PurchaseOrder());
				headAndDetail.setDetails(new LinkedList<>());
				headAndDetail.getHead().setOrderId(jindieERPPurchaseOrder.getFBillNo());
				headAndDetail.getHead().setOrderState(1);
				//headAndDetail.getHead().setOrderType();
				headAndDetail.getHead().setSupplierName(jindieERPPurchaseOrder.getFSupplierId());
				headAndDetail.getHead().setWarehouseName(jindieERPPurchaseOrder.getFPurchaseOrgId());
				headAndDetailList.add(headAndDetail);
			}
			HeadAndDetail<PurchaseOrder,PurchaseOrderDetail> headAndDetail = headAndDetailList.get(heads.get(jindieERPPurchaseOrder.getFBillNo()));
			PurchaseOrderDetail purchaseOrderDetail = new PurchaseOrderDetail();
			purchaseOrderDetail.setSkuName(jindieERPPurchaseOrder.getFMaterialName());
			purchaseOrderDetail.setUnit(jindieERPPurchaseOrder.getFUnitId());
			purchaseOrderDetail.setPredictNum(jindieERPPurchaseOrder.getFQty());
			purchaseOrderDetail.setTotalMoney(jindieERPPurchaseOrder.getFEntryAmount());
			purchaseOrderDetail.setOrderId(heads.get(jindieERPPurchaseOrder.getFBillNo()));
			headAndDetail.getDetails().add(purchaseOrderDetail);
		}
		for(int i = 0;i<headAndDetailList.size();i++){
			System.out.println(headAndDetailList.get(i));
			PurchaseOrder purchaseOrder = purchaseOrderDAO.selectPurchaseCode(headAndDetailList.get(i).getHead());
			if(purchaseOrder==null){
				HeadAndDetail<PurchaseOrder,PurchaseOrderDetail> headAndDetail = headAndDetailList.get(i);
				headAndDetail.getHead().setCreateTime(new Date());
				headAndDetail.getHead().setAddPersonName(staffDAO.selectByPrimaryKey(headAndDetail.getHead().getAddPersonId()).getStaffName());
				Integer id = purchaseOrderDAO.insert(headAndDetail.getHead());
				for(int j =0;j<headAndDetail.getDetails().size();j++){
					PurchaseOrderDetail purchaseOrderDetail = headAndDetail.getDetails().get(j);
					purchaseOrderDetail.setOrderId(id);
					purchaseOrderDetailDAO.insert(purchaseOrderDetail);
				}
			}
			else {
				purchaseOrderDAO.updateByPrimaryKeySelective(headAndDetailList.get(i).getHead());
				for(int j = 0;j<headAndDetailList.get(i).getDetails().size();j++){
					headAndDetailList.get(i).getDetails().get(j).setOrderId(purchaseOrder.getId());
					headAndDetailList.get(i).getDetails().get(j).setId(purchaseOrderDetailDAO.selectByOrderId(headAndDetailList.get(i).getDetails().get(j)));
					purchaseOrderDetailDAO.updateByPrimaryKeySelective(headAndDetailList.get(i).getDetails().get(j));
				}
			}
		}
	}
}
