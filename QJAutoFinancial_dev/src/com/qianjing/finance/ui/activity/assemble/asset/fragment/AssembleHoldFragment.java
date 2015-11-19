
package com.qianjing.finance.ui.activity.assemble.asset.fragment;

import android.os.Bundle;

/**
 * @description 组合有持仓Fragment
 * @author majinxin
 * @date 2015年9月8日
 */
public class AssembleHoldFragment extends AssembleHoldBaseFragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestAssembleList(getType(), true);
        // requestP2pDetail(true);
    }

    @Override
    protected int getType()
    {
        return 1;
    }
}
