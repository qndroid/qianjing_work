
package com.qianjing.finance.model.p2p;

import com.google.gson.annotations.SerializedName;
import com.qianjing.finance.model.common.BaseModel;


public class TokenResponse extends BaseModel {

    private static final long serialVersionUID = 1L;

    /*
    {
        ecode: 0,
        emsg: "",
        data: {
            tk: "6b14c23caac866f8265a493943b1b662c6cfea0bed8171cd152e056540e2c9d263e3770da34d0a93ccf51c89818e613bfec29053048a2a4b6c2378c46e641a58dce2a196ff96b3bbe26f1e0d3339255ac75e335a24efa04397207eed63b4ff53c3fceb83d658a22d59b2ccdf656900c63aa7e830a2d028e852b4013636816ffb"
        }
    }
     */
    @SerializedName("data")
    public Token token ;
    
    public static class Token {

        @SerializedName("tk")
        public String token;
    }

}
