package com.qianjing.finance.model.p2p;

import com.google.gson.annotations.SerializedName;
import com.qianjing.finance.model.common.BaseModel;

public class P2PSteadyStateResponse extends BaseModel {

    private static final long serialVersionUID = 1L;

    /*
    {
    "data": {
        "state": 1
    },
    "ecode": 0,
    "emsg": ""
    }
     */
    @SerializedName("data")
    public SteadyState state;

    public static class SteadyState {

        @SerializedName("status")
        public int state;
    }

}
