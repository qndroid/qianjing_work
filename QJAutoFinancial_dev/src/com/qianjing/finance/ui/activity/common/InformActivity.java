
package com.qianjing.finance.ui.activity.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.qianjing.finance.database.PrefManager;
import com.qianjing.finance.model.common.BaseModel;
import com.qianjing.finance.model.common.InformList;
import com.qianjing.finance.model.common.InformList.InformListKey.Information;
import com.qianjing.finance.net.RequestManager;
import com.qianjing.finance.net.connection.XCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.util.CommonUtil;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.DateFormatHelper.DateFormat;
import com.qianjing.finance.widget.adapter.InformAdapter;
import com.qjautofinancial.R;

/**
 * @description 消息通知列表页面
 * @author fangyan
 * @date 2015年8月12日
 */
public class InformActivity extends BaseActivity {

    private List<Map<String, String>> mlist = new ArrayList<Map<String, String>>();
    private InformAdapter adapter;
    private List<Information> listInform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {

        setContentView(R.layout.activity_information);

        setTitleBack();

        setTitleWithId(R.string.title_message_manager);

        adapter = new InformAdapter(this, mlist);
        ListView lv_listview = (ListView) findViewById(R.id.lv_listview);
        lv_listview.setAdapter(adapter);
        lv_listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                Map<String, String> map = mlist.get(position);
                bundle.putString("msg_type", map.get("msg_type"));
                bundle.putString("contenturl", map.get("contenturl"));
                bundle.putString("contentid", map.get("contentid"));
                bundle.putString("id", map.get("id"));
                openActivity(InformDetailActivity.class, bundle);
            }
        });

        requestInform();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (adapter != null)
            adapter.notifyDataSetChanged();

        // 没有未读通知则更新总状态
        if (listInform != null && !CommonUtil.hasUnread(listInform))
            PrefManager.getInstance().putBoolean(PrefManager.KEY_UNREAD_INFORM, false);
    }

    public void requestInform() {

        RequestManager.request(this, UrlConst.PUSH_LIST, null, InformList.class,
                new XCallBack() {
                    @Override
                    public void success(BaseModel model) {
                        if (model != null) {
                            InformList informList = (InformList) model;
                            if (informList.keyList != null && informList.keyList.listInform != null) {

                                listInform = informList.keyList.listInform;
                                for (Information inform : listInform) {
                                    Map<String, String> map = new HashMap<String, String>();
                                    map.put("title", inform.title);
                                    map.put("time", DateFormatHelper.formatDate(
                                            inform.logTime.concat("000"), DateFormat.DATE_5));
                                    map.put("content", inform.content);
                                    map.put("id", inform.id);
                                    map.put("msg_type", inform.msgType);
                                    map.put("contentid", inform.contentId);
                                    map.put("contenturl", inform.contentUrl);
                                    mlist.add(map);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void fail() {
                    }
                });
    }

}
