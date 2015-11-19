package com.qianjing.finance.net.connection;


/**
 * <p>Title: IllegalResponseStateException</p>
 * <p>Description: 网络请求回复状态异常</p>
 * @author fangyan
 * @date 2015年1月25日
 */
public class IllegalResponseStateException extends Exception {

    /** serialVersionUID*/
	private static final long serialVersionUID = 1L;

	public IllegalResponseStateException() {
        super();
    }

    public IllegalResponseStateException(String message) {
        super(message);
    }

    public IllegalResponseStateException(String message, Throwable cause) {
        super(message);
        initCause(cause);
    }

    @Override
    public String toString() {
        if (getCause() != null) {
            return getLocalizedMessage() + ": " + getCause();
        } else {
            return getLocalizedMessage();
        }
    }
    
}
