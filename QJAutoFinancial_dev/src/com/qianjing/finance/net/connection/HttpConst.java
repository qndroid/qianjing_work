package com.qianjing.finance.net.connection;

public class HttpConst {

    /** ok */
	public static final int STATUS_OK = 100;

    /** common error */
	public static final int STATUS_ERROR_COMMON = 1001;
	
    /** param error */
	public static final int STATUS_ERROR_PARAM = 1002;

	/** network error */
	public static final int STATUS_ERROR_NETWORK = 1003;
	
	
    /** IOException (include SocketTimeoutException) */
	public static final int STATUS_EXCEPTION_IO = 1101;
	
    /** MalformedURLException */
	public static final int STATUS_EXCEPTION_MALFORMED_URL = 1102;
	
    /** IllegalResponseStateException */
	public static final int STATUS_EXCEPTION_ILLEGAL_RESPONSE_STATE = 1103;

    /** JSONException */
	public static final int STATUS_EXCEPTION_JSON_PARSE = 1104;

    /** GSON inside error */
    public static final int STATUS_EXCEPTION_GSON = 1105;
	
}
