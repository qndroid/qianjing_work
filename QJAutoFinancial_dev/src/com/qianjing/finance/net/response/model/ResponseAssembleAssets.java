package com.qianjing.finance.net.response.model;

import com.qianjing.finance.model.assemble.AssembleAssets;


/**
 * Title: ResponseAssembleAssets
 * Description: 返回报文结构
 */
public class ResponseAssembleAssets extends ResponseBase {
	
	public AssembleAssets assets;
	
	public ResponseAssembleAssets() {
		assets = new AssembleAssets();
	}
	
}
