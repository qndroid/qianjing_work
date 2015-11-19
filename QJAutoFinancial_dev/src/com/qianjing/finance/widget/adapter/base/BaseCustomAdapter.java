package com.qianjing.finance.widget.adapter.base;

import java.io.Serializable;
import java.util.List;

import android.util.SparseArray;
import android.view.View;

public interface BaseCustomAdapter {

	public List<SparseArray<Serializable>> getDetailData();
	
	public void setCustomItemClick(View view,
			int position);
	
}
