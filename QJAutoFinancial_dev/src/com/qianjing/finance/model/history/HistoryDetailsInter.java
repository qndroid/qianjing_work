package com.qianjing.finance.model.history;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.util.SparseArray;

import com.qianjing.finance.model.common.BaseModel;

public class HistoryDetailsInter extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3760025004505081055L;
	
	public String dateStr;
	public String operation;
	public String optState;
	
	public List<HashMap<String,Serializable>> historyDetails;
	public ArrayList<String> reasons;
}
