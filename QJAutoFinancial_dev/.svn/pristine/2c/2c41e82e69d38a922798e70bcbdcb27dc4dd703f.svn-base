/**
 * Project Name:QJAutoFinancial_dev
 * File Name:AnimorUtils.java
 * Package Name:com.qianjing.finance.util
 * Date:2015年9月29日上午11:27:51
 * Copyright (c) 2015, www.qianjing.com All Rights Reserved.
 *
*/

package com.qianjing.finance.util.helper;

import android.animation.ObjectAnimator;
import android.view.View;

/**
 * ClassName:AnimorUtils <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2015年9月29日 上午11:27:51 <br/>
 * @author   liuchen
 * @version  
 * @see 	 
 */
public class AnimHelper {
    
    public enum AnimType{
        TRANS_X("x"),TRANS_Y("y");
        
        private String type;
        AnimType(String type){
            this.type = type;
        }
        private String getAnimType(){
            return type;
        }
    }
    
    /** 
    * @description 
    * @author liuchen
    * @param view          执行动画的View
    * @param animType      动画类型
    * @param target        动画终点位置
    */ 
    public static void loadPropertyTransAnim(View view,AnimType animType,int target){
        ObjectAnimator translationRight = ObjectAnimator.ofFloat(view, animType.getAnimType(),
                target);
        translationRight.setDuration(300);
        translationRight.start();
    }

}

