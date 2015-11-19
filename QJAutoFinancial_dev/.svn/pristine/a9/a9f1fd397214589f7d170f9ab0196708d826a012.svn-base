
package com.qianjing.finance.view.dialog;

import com.qjautofinancial.R;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @description 仿IOS底部弹出对话框
 * @author majinxin
 * @date 2015年8月20日
 */
public class ActionSheet {
    /**
     * common
     */
    private Context mContext;
    private Dialog mDialog;
    private DisplayMetrics dm;
    private LayoutInflater mLayoutInflate;
    /**
     * UI
     */
    private TextView mTitleView;
    private TextView mCancleView;
    private LinearLayout mContentLayout;

    /**
     * data
     */
    private List<SheetItem> sheetItems;;

    public ActionSheet(Context context) {
        this.mContext = context;
        mLayoutInflate = LayoutInflater.from(mContext);
        dm = mContext.getResources().getDisplayMetrics();
    }

    /**
     * 构建基本的Dialog
     * 
     * @return
     */
    public ActionSheet build() {
        View contentView = mLayoutInflate.inflate(R.layout.view_actionsheet,
                null);
        contentView.setMinimumWidth(dm.widthPixels);
        mContentLayout = (LinearLayout) contentView
                .findViewById(R.id.lLayout_content);
        mTitleView = (TextView) contentView.findViewById(R.id.txt_title);
        mCancleView = (TextView) contentView.findViewById(R.id.txt_cancel);
        mCancleView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mDialog = new Dialog(mContext, R.style.SheetDialogStyle);
        mDialog.setContentView(contentView);
        Window dialogWindow = mDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        return this;
    }

    /**
     * 设置Title
     * 
     * @param title
     * @return
     */
    public ActionSheet setTitle(String title) {
        mTitleView.setVisibility(View.VISIBLE);
        mTitleView.setText(title);
        return this;
    }

    public ActionSheet setCancleable(boolean cancle) {
        mDialog.setCancelable(cancle);
        return this;
    }

    public ActionSheet setCancleOnTouchOutside(boolean cancle) {
        mDialog.setCanceledOnTouchOutside(cancle);
        return this;
    }

    public ActionSheet addSheetItem(String item, int color,
            OnSheetItemClickListener listener) {
        if (sheetItems == null) {
            sheetItems = new ArrayList<SheetItem>();
        }
        sheetItems.add(new SheetItem(item, color, listener));
        return this;
    }

    private void initSheetItems() {
        for (int i = 0; i < sheetItems.size(); i++) {
            mContentLayout.addView(createSheetItem(sheetItems.get(i), i));
        }
    }

    private TextView createSheetItem(final SheetItem sheetItem, final int which) {
        TextView textView = new TextView(mContext);
        textView.setText(sheetItem.name);
        textView.setTextSize(18);
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
        textView.setTextColor(sheetItem.color);

        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, dp2px(45)));
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetItem.itemClickListener.onClick(which);
                dismiss();
            }
        });
        return textView;
    }

    public void show() {
        initSheetItems();
        mDialog.show();
    }

    private int dp2px(float dp) {

        float scale = dm.density;
        return (int) (dp * scale + 0.5f);
    }

    public void dismiss() {
        this.sheetItems = null;
        this.mDialog.dismiss();
        this.mDialog = null;
    }

    public interface OnSheetItemClickListener {
        void onClick(int which);
    }

    private class SheetItem {
        String name;
        int color;
        OnSheetItemClickListener itemClickListener;

        public SheetItem(String name, int color,
                OnSheetItemClickListener itemClickListener) {
            this.name = name;
            this.color = color;
            this.itemClickListener = itemClickListener;
        }

    }
}
