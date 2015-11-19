
package com.qianjing.finance.model.assemble;

import java.io.Serializable;

import com.qianjing.finance.model.common.BaseModel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <p>
 * Title: AssembleDetail
 * </p>
 * <p>
 * Description: 组合详细信息
 * </p>
 * 
 * @author fangyan
 * @date 2015年5月26日
 */
public class AssembleDetail implements Parcelable {

    private AssembleBase assemble;
    private AssembleConfig config;
    private AssembleAssets assets;
    private AssembleReminder reminder;
    private AssembleFixed fixed;

    public AssembleBase getAssembleBase() {
        return assemble;
    }

    public void setAssembleBase(AssembleBase assemble) {
        this.assemble = assemble;
    }

    public AssembleConfig getAssembleConfig() {
        return config;
    }

    public void setAssembleConfig(AssembleConfig assembly) {
        this.config = assembly;
    }

    public AssembleAssets getAssembleAssets() {
        return assets;
    }

    public void setAssembleAssets(AssembleAssets assets) {
        this.assets = assets;
    }

    public AssembleReminder getAssembleReminder() {
        return reminder;
    }

    public void setAssembleReminder(AssembleReminder investReminder) {
        this.reminder = investReminder;
    }
    
    

    public AssembleFixed getAssembleFixed() {
        return fixed;
    }

    public void setAssembleFixed(AssembleFixed fixed) {
        this.fixed = fixed;
    }

    public AssembleDetail(AssembleBase assemble, AssembleConfig config, AssembleAssets assets,
            AssembleReminder reminder, AssembleFixed fixed) {
        super();
        this.assemble = assemble;
        this.config = config;
        this.assets = assets;
        this.reminder = reminder;
        this.fixed = fixed;
    }

    @Override
    public String toString() {
        return "AssembleDetail [AssembleBase=" + assemble +
                ", AssembleConfig=" + config +
                ", AssembleAssets=" + assets +
                ", AssembleReminder=" + reminder + 
                ", AssembleFixed=" + fixed + "]";
    }

    public AssembleDetail() {
        super();
    }

    public AssembleDetail(Parcel in) {
        // readParcelable方法需要类加载器
        // 类加载器能够从指定类得到
        assemble = in.readParcelable(AssembleBase.class.getClassLoader());
        config = in.readParcelable(AssembleConfig.class.getClassLoader());
        assets = in.readParcelable(AssembleAssets.class.getClassLoader());
        fixed = in.readParcelable(AssembleFixed.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(assemble, flags);
        dest.writeParcelable(config, flags);
        dest.writeParcelable(assets, flags);
        dest.writeParcelable(fixed, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<AssembleDetail> CREATOR = new Creator<AssembleDetail>() {

        @Override
        public AssembleDetail createFromParcel(Parcel source) {
            return new AssembleDetail(source);
        }

        @Override
        public AssembleDetail[] newArray(int size) {
            return new AssembleDetail[size];
        }
    };

}
