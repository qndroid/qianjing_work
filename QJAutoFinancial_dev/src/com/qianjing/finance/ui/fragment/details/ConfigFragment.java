
package com.qianjing.finance.ui.fragment.details;

import com.qianjing.finance.model.quickbuy.FundItemDetails;
import com.qianjing.finance.model.quickbuy.QuickBuyDetail;
import com.qianjing.finance.model.quickbuy.QuickBuyListData;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.view.assembledetailview.AssembleNewItemLayout;
import com.qianjing.finance.view.chartview.PieGraph;
import com.qianjing.finance.view.chartview.PieSlice;
import com.qianjing.finance.view.listview.AnimatedExpandableListView;
import com.qianjing.finance.view.listview.AnimatedExpandableListView.AnimatedExpandableListAdapter;
import com.qianjing.finance.view.scrollview.InnerScrollView;
import com.qjautofinancial.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class ConfigFragment extends Fragment {

    private AnimatedExpandableListView aelvAnimList;
    private ScrollView fsvScroll;
    private ViewPager viewPager;
    private ArrayList<QuickBuyListData> listData = new ArrayList<QuickBuyListData>();
    private int groupHeight;
    private int childHeight;
    
    private QuickBuyDetail quickBuyDetail;
    private PieGraph pieGraph;
    private mAnimExpandListAdapter animAdapter;
    private ViewTreeObserver viewTreeObserver;
    private OnGlobalLayoutListener onGlobalLayoutListener;
    /**
     * 配置颜色等级
     */
    private final int[] colors = new int[]
    { R.color.color_5ba7e1, R.color.color_b19ee0, R.color.color_e7a3e5, R.color.color_5a79b7, R.color.color_a6d0e6
            ,R.color.color_a6d0e6 };
    
    
    @Override
    public View onCreateView(LayoutInflater inflater
            , ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_config, null);
        if(quickBuyDetail!=null){
            initView(view);
            updataUI();
        }
        return view;
    }
    
    public void setFundDetails(QuickBuyDetail quickBuyDetail){
        this.quickBuyDetail = quickBuyDetail;
    }
    
    private PieSlice createPieSlice(QuickBuyListData data){

      PieSlice slice = new PieSlice();
      slice.setColor(getActivity().getResources().getColor(data.getIndictorColor()));
      slice.setValue((float) data.getFundRate() * 100);
      return slice;
    }
    
    private void updataUI(){
      
      ArrayList<PieSlice> pieNodes = new ArrayList<PieSlice>();
      for (int i = 0; i < quickBuyDetail.list.size(); i++){
          listData.add(new QuickBuyListData(Double.parseDouble(quickBuyDetail.list.get(i).ratio)
                  ,quickBuyDetail.list.get(i).abbrev
                  ,quickBuyDetail.list.get(i).fdcode
                  ,
                  colors[i]));
          if (listData.get(i).getFundRate() > 0){
              pieNodes.add(createPieSlice(listData.get(i)));
          }
      }
      pieGraph.setDrawText(quickBuyDetail.noStockName, (int)quickBuyDetail.noStockRatio+"%", 
              quickBuyDetail.stockName, (int)quickBuyDetail.stockRatio+"%", pieNodes);
    }
    
    
    @SuppressLint("ClickableViewAccessibility")
    private void initView(View view) {
        
        pieGraph = (PieGraph) view.findViewById(R.id.pie_graph);
        InnerScrollView innerScrollView = (InnerScrollView) view.findViewById(R.id.inner_scroll_view);
        fsvScroll = (ScrollView) getActivity().findViewById(R.id.fsv_scroll);
        
        
        aelvAnimList = (AnimatedExpandableListView) view.findViewById(R.id.aelv_anim_list);
        animAdapter = new mAnimExpandListAdapter(getActivity(),quickBuyDetail.list);
        aelvAnimList.setAdapter(animAdapter);
        
        ViewGroup.LayoutParams params = aelvAnimList.getLayoutParams();
        params.height = (int) TypedValue.applyDimension(TypedValue
                .COMPLEX_UNIT_SP,animAdapter.getGroupCount()*50+150,getActivity().getResources().getDisplayMetrics());
        aelvAnimList.setLayoutParams(params);
        
        aelvAnimList.setGroupIndicator(null);
        aelvAnimList.setOnGroupClickListener(new OnGroupClickListener() {
            
            private int lastPosition = -1;
            
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if(lastPosition!=-1){
                    aelvAnimList.collapseGroupWithAnimation(lastPosition);
                }
                
                if (aelvAnimList.isGroupExpanded(groupPosition)) {
                    aelvAnimList.collapseGroupWithAnimation(groupPosition);
                    lastPosition = -1;
                } else {
                    aelvAnimList.expandGroupWithAnimation(groupPosition);
                    lastPosition = groupPosition;
                }
                return true;
            }
        });
    }
    
    
    private class mAnimExpandListAdapter extends AnimatedExpandableListAdapter{
        
        private Context context;
        private ArrayList<FundItemDetails> list;
        
        
        public mAnimExpandListAdapter(Context context, ArrayList<FundItemDetails> list) {
            super();
            this.context = context;
            this.list = list;
        }

        @Override
        public int getGroupCount() {
            return list.size()+1;
        }
        
        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }
        
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }
        
        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }
        
        @Override
        public boolean hasStableIds() {
            return false;
        }
        
        
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                ViewGroup parent) {
            
            if(groupPosition == list.size()){
                return new View(getActivity());
            }
            
            AssembleNewItemLayout assembleItem = new AssembleNewItemLayout(context);
            ImageView rightArraw = (ImageView) assembleItem.findViewById(R.id.iv_right_arrow);
            if(isExpanded){
                rightArraw.setBackgroundResource(R.drawable.icon_up_arrow2);
            }else{
                rightArraw.setBackgroundResource(R.drawable.icon_down_arrow2);
            }
            if(groupPosition<list.size()){
                assembleItem.initData(colors[groupPosition]
                        , list.get(groupPosition).abbrev
                        , getString(R.string.left_brackets)+list.get(groupPosition).fdcode+getString(R.string.right_brackets)
                        , StringHelper.formatDecimal(String.valueOf(Float.parseFloat(list.get(groupPosition).ratio)*100)));
            }
            
            return assembleItem;
        }
        
        
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
        
        @Override
        public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild,
                View convertView, ViewGroup parent) {
            View view = View.inflate(context, R.layout.item_grade, null);
            TextView rankNum = (TextView)view.findViewById(R.id.tv_rank_num);
            TextView rankTotal = (TextView)view.findViewById(R.id.tv_rank_total);
            TextView reason = (TextView)view.findViewById(R.id.tv_advice_reason);
            RatingBar ratingBar = (RatingBar)view.findViewById(R.id.ratingBar1);
            
            FundItemDetails details = list.get(groupPosition);
            
            rankNum.setText(details.rank);
            rankTotal.setText(details.total_rank);
            reason.setText(details.recomm_reason);
            ratingBar.setRating(Float.parseFloat(details.star));
            
            return view;
        }
        
        @Override
        public int getRealChildrenCount(int groupPosition) {
            return 1;
        }
    }
    
}
