package com.qianjing.finance.model.purchase;

import com.qianjing.finance.model.common.BaseModel;
import com.qianjing.finance.net.connection.UrlConst;

public class PurchaseModel extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 是否是虚拟申购
	 * */
	private boolean isVirtual;
	
	/**
	 * 最小申购金额
	 * */
	private float minPurchase;
	
	/**
	 * 组合名称
	 * */
	private String schemaName;
	/**
	 * 收费方式
	 * */
	private String feeWay;
	/**
	 * 支付方式
	 * */
	private String payWay;
	
	/**
	 * 组合的sid
	 * */
	private String sid;
	
	/**
	 * 当前可用金额
	 * */
	private float usableMonay;
	
	/**
	 * 接口选择
	 * */
	private String holdDetailInterface;
	
	
	
	public String getHoldDetailInterface() {
		return holdDetailInterface;
	}

	public void setVirtual(boolean isVirtual) {
		
		if(isVirtual){
			
			feeWay = "虚拟收费";
			payWay = "虚拟交易";
			
			/**
			 * 为虚拟组合购买
			 * */
			holdDetailInterface = UrlConst.VIRTUAL_POSITION_DETAILS;
			minPurchase = (float)1;
			
		}else{
			holdDetailInterface = UrlConst.LIST_ASSETS;
		}
		
		this.isVirtual = isVirtual;
	}
	
	
	
	public float getMinPurchase() {
		return minPurchase;
	}
	public void setMinPurchase(float minPurchase) {
		this.minPurchase = minPurchase;
	}
	public String getSchemaName() {
		return schemaName;
	}
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	public String getFeeWay() {
		return feeWay;
	}
	public String getPayWay() {
		return payWay;
	}
	
	public boolean isVirtual() {
		return isVirtual;
	}


	public void setFeeWay(String feeWay) {
		this.feeWay = feeWay;
	}


	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}


	public String getSid() {
		return sid;
	}


	public void setSid(String sid) {
		this.sid = sid;
	}


	public float getUsableMonay() {
		return usableMonay;
	}


	public void setUsableMonay(float usableMonay) {
		this.usableMonay = usableMonay;
	}
}
