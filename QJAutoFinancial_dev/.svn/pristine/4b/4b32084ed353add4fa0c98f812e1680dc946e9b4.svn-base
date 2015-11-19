package com.qianjing.finance.model.history;

import com.qianjing.finance.model.common.BaseModel;
import com.qianjing.finance.net.connection.UrlConst;

public class HistoryInter extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 判断是否是虚拟交易
	 * */
	private boolean isVirtual;
	
	/**
	 * 接口名称
	 * */
	private String interfaceName;
	
	/**
	 * 如果是部分记录需要的sid
	 * */
	
	private String sid;

	public boolean isVirtual() {
		return isVirtual;
	}

	public void setVirtual(boolean isVirtual,String sid) {
		
		if(isVirtual){
			interfaceName = UrlConst.VIRTUAL_TOTAL_PAL;
		}else{
			interfaceName = UrlConst.LIST_OPERATE;
		}
		
		this.sid = sid;
		this.isVirtual = isVirtual;
	}

	public String getSid() {
		return sid;
	}

	
	public String getInterfaceName() {
		return interfaceName;
	}
	
}
