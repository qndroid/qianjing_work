package com.qianjing.finance.model.assemble;

import android.os.Parcel;
import android.os.Parcelable;

public class AssembleFixed  implements Parcelable{
    
    private int isFixedExist = 1;
    private String sdid;
    private String uid;
    private String sid;
    private String ctime;
    private String utime;
    private String cworkday;
    private String stop_date;
    private String state;
    private String month_sum;
    private String first_date;
    private String day;
    private String tradeacco;
    private String bank;
    private String card;
    private String reason;
    private String success_ratio;
    private String next_day;
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(isFixedExist);
        dest.writeString(sdid);
        dest.writeString(uid);
        dest.writeString(sid);
        dest.writeString(ctime);
        dest.writeString(utime);
        dest.writeString(cworkday);
        dest.writeString(stop_date);
        dest.writeString(state);
        dest.writeString(month_sum);
        dest.writeString(first_date);
        dest.writeString(day);
        dest.writeString(tradeacco);
        dest.writeString(bank);
        dest.writeString(card);
        dest.writeString(reason);
        dest.writeString(success_ratio);
        dest.writeString(next_day);
    }
    
    
    
    public AssembleFixed() {
    }
    
    
    
    public AssembleFixed(Parcel source) {
        isFixedExist = source.readInt();
        sdid = source.readString();
        uid = source.readString();
        sid = source.readString();
        ctime = source.readString();
        utime = source.readString();
        cworkday = source.readString();
        stop_date = source.readString();
        state = source.readString();
        month_sum = source.readString();
        first_date = source.readString();
        day = source.readString();
        tradeacco = source.readString();
        bank = source.readString();
        card = source.readString();
        reason = source.readString();
        success_ratio = source.readString();
        next_day = source.readString();
    }



    public static final Parcelable.Creator<AssembleFixed> CREATOR = new Creator<AssembleFixed>() {
        
        @Override
        public AssembleFixed createFromParcel(Parcel source) {
            return new AssembleFixed(source);
        }

        @Override
        public AssembleFixed[] newArray(int size) {
            return new AssembleFixed[size];
        }
    };
    
    
    
    public int getIsFixedExist() {
        return isFixedExist;
    }



    public void setIsFixedExist(int isFixedExist) {
        this.isFixedExist = isFixedExist;
    }



    public String getSdid() {
        return sdid;
    }

    public void setSdid(String sdid) {
        this.sdid = sdid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getUtime() {
        return utime;
    }

    public void setUtime(String utime) {
        this.utime = utime;
    }

    public String getCworkday() {
        return cworkday;
    }

    public void setCworkday(String cworkday) {
        this.cworkday = cworkday;
    }

    public String getStop_date() {
        return stop_date;
    }

    public void setStop_date(String stop_date) {
        this.stop_date = stop_date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMonth_sum() {
        return month_sum;
    }

    public void setMonth_sum(String month_sum) {
        this.month_sum = month_sum;
    }

    public String getFirst_date() {
        return first_date;
    }

    public void setFirst_date(String first_date) {
        this.first_date = first_date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTradeacco() {
        return tradeacco;
    }

    public void setTradeacco(String tradeacco) {
        this.tradeacco = tradeacco;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSuccess_ratio() {
        return success_ratio;
    }

    public void setSuccess_ratio(String success_ratio) {
        this.success_ratio = success_ratio;
    }

    public String getNext_day() {
        return next_day;
    }

    public void setNext_day(String next_day) {
        this.next_day = next_day;
    }
    
    
    
    @Override
    public int describeContents() {
        return 0;
    }

    

    
}
