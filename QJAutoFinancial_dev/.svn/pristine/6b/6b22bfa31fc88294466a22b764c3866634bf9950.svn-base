
package com.qianjing.finance.widget.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qianjing.finance.database.PrefManager;
import com.qjautofinancial.R;

public class InformAdapter extends BaseAdapter {
    private Context mConext;
    private LayoutInflater inflater;
    private List<Map<String, String>> listInform;

    public InformAdapter(Context context, List<Map<String, String>> listInform) {
        mConext = context;
        this.listInform = listInform;
        inflater = (LayoutInflater) mConext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listInform.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_inform, null);
            holder = new ViewHolder();
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_info_title);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_info_time);
            holder.tvContent = (TextView) convertView.findViewById(R.id.tv_info_content);
            holder.ivUnread = (ImageView) convertView.findViewById(R.id.iv_unread);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Map<String, String> mapInform = listInform.get(position);
        String title = mapInform.get("title");
        String time = mapInform.get("time");
        String content = mapInform.get("content");
        String id = mapInform.get("id");
        holder.tvTitle.setText(title);
        holder.tvTime.setText(time);
        holder.tvContent.setText(content);

        PrefManager spManager = PrefManager.getInstance();
        if (spManager.getInt("inform_" + id, -1) == 1) {
            holder.ivUnread.setVisibility(View.VISIBLE);
        }
        else {
            holder.ivUnread.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    public class ViewHolder {
        TextView tvTitle;
        TextView tvTime;
        TextView tvContent;
        ImageView ivUnread;
    }

}
