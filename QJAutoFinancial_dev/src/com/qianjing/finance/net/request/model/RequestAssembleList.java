
package com.qianjing.finance.net.request.model;

import com.qianjing.finance.net.connection.UrlConst;

/**
 * Title: RequestAssembleList Description: 组合列表请求
 */
public class RequestAssembleList extends RequestBase {

    public static final int TYPE_ASSETS = 1;
    public static final int TYPE_NO_ASSETS = 2;
    public static final int TYPE_REBALANCE = 3;

    /** 开始位置字段 */
    private final String PARAM_START_NUMBER = "of";
    /** 页数字段 */
    private final String PARAM_PAGE_NUMBER = "nm";
    /** 列表类型字段 */
    private final String PARAM_PAGE_HAS_ASSETS = "type";
    /** 开始位置 */
    private int startNumber;
    /** 页数 */
    private int pageNumber;
    /** 类型 */
    private int type;

    public RequestAssembleList(int startNumber, int pageNumber, int type) {
        this.startNumber = startNumber;
        this.pageNumber = pageNumber;
        this.type = type;

        create();
    }

    /**
     * 初始化请求
     * 
     * @return
     */
    public void create() {
        if (type == TYPE_ASSETS || type == TYPE_NO_ASSETS)
            url = UrlConst.ASSEMBLE_LIST;
        else if (type == TYPE_REBALANCE)
            url = UrlConst.REBALANCE_ASSEMBLE_LIST;

        properties.put(PARAM_START_NUMBER, startNumber);
        properties.put(PARAM_PAGE_NUMBER, pageNumber);
        if (type == TYPE_ASSETS)
            properties.put(PARAM_PAGE_HAS_ASSETS, 1);
        else if (type == TYPE_NO_ASSETS)
            properties.put(PARAM_PAGE_HAS_ASSETS, 2);
    }

}
