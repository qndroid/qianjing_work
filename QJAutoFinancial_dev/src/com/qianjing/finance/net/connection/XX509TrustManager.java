package com.qianjing.finance.net.connection;

import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;


/**
 * <p>Title: XX509TrustManager</p>
 * <p>Description: SSL/TLS加密相关</p>
 * @author fangyan
 * @date 2015年1月25日
 */
class XX509TrustManager implements X509TrustManager {
    
    public void checkClientTrusted(X509Certificate[] chain, String authType) {
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType) {
    }

    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}
