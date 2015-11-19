package com.qianjing.finance.ui.fragment.details;

import com.qianjing.finance.util.helper.StringHelper;
import com.qjautofinancial.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AdviceFragment extends Fragment {

    private String common;
    
    @Override
    public View onCreateView(LayoutInflater inflater
            , ViewGroup container, Bundle savedInstanceState) {
        
        View view = View.inflate(getActivity(), R.layout.fragment_advice, null);
        TextView tvAdvice = (TextView)view.findViewById(R.id.tv_advice);
        if(!StringHelper.isBlank(common)){
            tvAdvice.setText(common);
        }
        
        return view;
        
    }
    
    public void setAdviceText(String common){
        this.common = common;
    }
    
    
    
}
