package com.qianjing.finance.net.response.model;

import java.util.ArrayList;

import com.qianjing.finance.model.assemble.AssembleBase;


/**
 * Title: ResponseAssembleList
 * Description: 返回报文结构
 */
public class ResponseAssembleList extends ResponseBase {

	public ArrayList<AssembleBase> listAssemble = new ArrayList<AssembleBase>();
	
}
