
package com.qianjing.finance.model.assemble;

import android.os.Parcel;
import android.os.Parcelable;

public class InvestPlan implements Parcelable {

    private int index;
    private float except_max;
    private float except_min;
    private float except_profit;
    private float one_invest;
    private float month_fixed;
    private float month_minsum;
    private int invest_year;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public float getExcept_max() {
        return except_max;
    }

    public void setExcept_max(float except_max) {
        this.except_max = except_max;
    }

    public float getExcept_min() {
        return except_min;
    }

    public void setExcept_min(float except_min) {
        this.except_min = except_min;
    }

    public float getExcept_profit() {
        return except_profit;
    }

    public void setExcept_profit(float except_profit) {
        this.except_profit = except_profit;
    }

    public float getOne_invest() {
        return one_invest;
    }

    public void setOne_invest(float one_invest) {
        this.one_invest = one_invest;
    }

    public float getMonth_fixed() {
        return month_fixed;
    }

    public void setMonth_fixed(float month_fixed) {
        this.month_fixed = month_fixed;
    }

    public float getMonth_minsum() {
        return month_minsum;
    }

    public void setMonth_minsum(float month_minsum) {
        this.month_minsum = month_minsum;
    }

    public int getInvest_year() {
        return invest_year;
    }

    public void setInvest_year(int invest_year) {
        this.invest_year = invest_year;
    }

    public InvestPlan() {
    }

    public InvestPlan(Parcel source) {
        index = source.readInt();
        except_max = source.readFloat();
        except_min = source.readFloat();
        except_profit = source.readFloat();
        one_invest = source.readFloat();
        month_fixed = source.readFloat();
        month_minsum = source.readFloat();
        invest_year = source.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(index);
        dest.writeFloat(except_max);
        dest.writeFloat(except_min);
        dest.writeFloat(except_profit);
        dest.writeFloat(one_invest);
        dest.writeFloat(month_fixed);
        dest.writeFloat(month_minsum);
        dest.writeInt(invest_year);
    }

    public static final Parcelable.Creator<InvestPlan> CREATOR = new Creator<InvestPlan>() {

        @Override
        public InvestPlan createFromParcel(Parcel source) {
            return new InvestPlan(source);
        }

        @Override
        public InvestPlan[] newArray(int size) {
            return new InvestPlan[size];
        }

    };

}
