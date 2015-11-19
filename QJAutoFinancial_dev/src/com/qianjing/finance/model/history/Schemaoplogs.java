
package com.qianjing.finance.model.history;

import java.util.ArrayList;

import com.qianjing.finance.model.common.BaseModel;

public class Schemaoplogs extends BaseModel {

    private static final long serialVersionUID = 1L;
    public ArrayList<String> abbrev = new ArrayList<String>();
    public int bid;
    public ArrayList<String> fdcode = new ArrayList<String>();
    public ArrayList<Float> fdshare = new ArrayList<Float>();
    public ArrayList<Integer> fdstate = new ArrayList<Integer>();
    public ArrayList<Float> fdsum = new ArrayList<Float>();
    public String fee;
    public int initSchedule;
    public String opDate;
    public int operate;
    public ArrayList<String> reason = new ArrayList<String>();
    public float remain;
    public String sid;
    public String sname;
    public String sopid;
    public int state;
    public float sum;
    public String uid;
    public String marketValue;

    public String arrive_time;
    public String bank;
    public String card;
    public String confirm_time;
    public String estimate_fee;
    public String estimate_sum;
}
