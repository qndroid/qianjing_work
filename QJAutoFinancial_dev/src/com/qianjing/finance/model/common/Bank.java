package com.qianjing.finance.model.common;

public class Bank extends BaseModel {
    private static final long serialVersionUID = 1L;
    private int id;
	private int B_ISSHOW;
	private String B_NAME;
	private int B_QUICK;
	private int B_FLAG;
	private String B_CODE;
	
	public Bank(int id, int b_ISSHOW, String b_NAME, int b_QUICK,
			int b_FLAG, String b_CODE) {
		super();
		this.id = id;
		B_ISSHOW = b_ISSHOW;
		B_NAME = b_NAME;
		B_QUICK = b_QUICK;
		B_FLAG = b_FLAG;
		B_CODE = b_CODE;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getB_ISSHOW() {
		return B_ISSHOW;
	}
	public void setB_ISSHOW(int b_ISSHOW) {
		B_ISSHOW = b_ISSHOW;
	}
	public String getB_NAME() {
		return B_NAME;
	}
	public void setB_NAME(String b_NAME) {
		B_NAME = b_NAME;
	}
	public int getB_QUICK() {
		return B_QUICK;
	}
	public void setB_QUICK(int b_QUICK) {
		B_QUICK = b_QUICK;
	}
	public int getB_FLAG() {
		return B_FLAG;
	}
	public void setB_FLAG(int b_FLAG) {
		B_FLAG = b_FLAG;
	}
	public String getB_CODE() {
		return B_CODE;
	}
	public void setB_CODE(String b_CODE) {
		B_CODE = b_CODE;
	}
	@Override
	public String toString() {
		return "Bank [id=" + id + ", B_ISSHOW=" + B_ISSHOW + ", B_NAME="
				+ B_NAME + ", B_QUICK=" + B_QUICK + ", B_FLAG=" + B_FLAG
				+ ", B_CODE=" + B_CODE + "]";
	}
	
 	
}
