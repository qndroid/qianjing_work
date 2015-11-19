package com.qianjing.finance.util;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

/**
 * <p>Title: UMengStatics</p>
 * <p>Description: 友盟统计静态方法类</p>
 * @author fangyan
 * @date 2015年1月20日
 */
public class UMengStatics {

	private static void commonEvent(Context mContext, String function) {
		MobclickAgent.onEvent(mContext, function);
	}

	/**
	 * 绑卡-点击提交信息   done
	 */
	public static void onBindPage1Click(Context mContext) {
		commonEvent(mContext, "bind_page1_click");
	}
	
	/**
	 * 绑卡-银联绑卡成功   done
	 */
	public static void onBindPage1Submi(Context mContext) {
		commonEvent(mContext, "bind_page1_submi");
	}
	
	/**
	 * 绑卡-进入提交页面   done
	 */
	public static void onBindPage1View(Context mContext) {
		commonEvent(mContext, "bind_page1_view");
	}
	
	/**
	 * 绑卡-提交快钱验证码  done
	 */
	public static void onBindPage2Click(Context mContext) {
		commonEvent(mContext, "bind_page2_click");
	}
	
	/**
	 * 绑卡-快钱绑卡成功   done
	 */
	public static void onBindPage1Submit(Context mContext) {
		commonEvent(mContext, "bind_page2_submit");
	}
	
	/**
	 * 绑卡-快钱验证码界面  done
	 */
	public static void onBindPage2View(Context mContext) {
		commonEvent(mContext, "bind_page2_view");
	}
	
	/**
	 * 活期宝-活期宝界面   done
	 */
	public static void onCurrentPage0Click(Context mContext) {
		commonEvent(mContext, "current_page0_view");
	}
	
	/**
	 * 活期宝充值-充值界面  done
	 */
	public static void onCurrentBuyPage1View(Context mContext) {
		commonEvent(mContext, "currentbuy_page1_view");
	}
	
	/**
	 * 活期宝充值-弹出密码层     done
	 */
	public static void onCurrentBuyPage2Password(Context mContext) {
		commonEvent(mContext, "currentbuy_page2_password");
	}
	
	/**
	 * 活期宝充值-进入结果页（充值成功）   done
	 */
	public static void onCurrentBuyPage3View(Context mContext) {
		commonEvent(mContext, "currentbuy_page3_view");
	}
	
	/**
	 * 活期宝取现-进入取现界面    done
	 */
	public static void onCurrentRedeemPage1View(Context mContext) {
		commonEvent(mContext, "currentredeem_page1_view");
	}
	
	/**
	 * 活期宝取现-弹出密码层     done
	 */
	public static void onCurrentRedeemPage2View(Context mContext) {
		commonEvent(mContext, "currentredeem_page2_view");
	}
	
	/**
	 * 活期宝取现-进入结果页（取现成功）   done
	 */
	public static void onCurrentRedeemPage3View(Context mContext) {
		commonEvent(mContext, "currentredeem_page3_view");
	}
	
	/**
	 * 尊享定制-进入答1题      done
	 */
	public static void onCustomizedPage1View(Context mContext) {
		commonEvent(mContext, "customized_page1_view");
	}
	
	/**
	 * 尊享定制-进入答2题      done
	 */
	public static void onCustomizedPage2View(Context mContext) {
		commonEvent(mContext, "customized_page2_view");
	}
	
	/**
	 * 尊享定制-进入答3题      done
	 */
	public static void onCustomizedPage3View(Context mContext) {
		commonEvent(mContext, "customized_page3_view");
	}
	
	/**
	 * 尊享定制-进入答4题      done
	 */
	public static void onCustomizedPage4View(Context mContext) {
		commonEvent(mContext, "customized_page4_view");
	}
	
	/**
	 * 尊享定制-进入答5题      done
	 */
	public static void onCustomizedPage5View(Context mContext) {
		commonEvent(mContext, "customized_page5_view");
	}

	/**
	 * 尊享定制-进入配置详情     done
	 */
	public static void onCustomizedPage6View(Context mContext) {
		commonEvent(mContext, "customized_page6_view");
	}
	
	/**
	 * 基金自选-全部     done
	 */
	public static void onFundPage1View(Context mContext) {
		commonEvent(mContext, "fund_page1_view");
	}
	
	/**
	 * 基金自选-股票型        done
	 */
	public static void onFundPage2View(Context mContext) {
		commonEvent(mContext, "fund_page2_view");
	}
	
	/**
	 * 基金自选-债券型        done
	 */
	public static void onFundPage3View(Context mContext) {
		commonEvent(mContext, "fund_page3_view");
	}
	
	/**
	 * 基金自选-货币型        done
	 */
	public static void onFundPage4View(Context mContext) {
		commonEvent(mContext, "fund_page4_view");
	}
	
	/**
	 * 资金自选-混合型        done
	 */
	public static void onFundPage5View(Context mContext) {
		commonEvent(mContext, "fund_page5_view");
	}
	
	/**
	 * 基金自选-ETF    done
	 */
	public static void onFundPage6View(Context mContext) {
		commonEvent(mContext, "fund_page6_view");
	}
	
	/**
	 * 基金自选-QDII   done
	 */
	public static void onFundPage7View(Context mContext) {
		commonEvent(mContext, "fund_page7_view");
	}
	
	/**
	 * 单购基金-进入申购页
	 */
	public static void onOBuyPage1Click(Context mContext) {
		commonEvent(mContext, "obuy_page1_view");
	}
	
	/**
	 * 单购基金-弹出密码层
	 */
	public static void onOBuyPage2Password(Context mContext) {
		commonEvent(mContext, "obuy_page2_password");
	}
	
	/**
	 * 单购基金-进入交易明细
	 */
	public static void onOBuyPage2View(Context mContext) {
		commonEvent(mContext, "obuy_page2_view");
	}
	
	/**
	 * 单购基金-进入结果页
	 */
	public static void onOBuyPage3View(Context mContext) {
		commonEvent(mContext, "obuy_page3_view");
	}
	
	/**
	 * 单基赎回-单基持仓页面
	 */
	public static void onORedeemPage0View(Context mContext) {
		commonEvent(mContext, "oredeem_page0_view");
	}
	
	/**
	 * 单基赎回-进入基金赎回
	 */
	public static void onORedeemPage1View(Context mContext) {
		commonEvent(mContext, "oredeem_page1_view");
	}
	
	/**
	 * 单基赎回-弹出密码层
	 */
	public static void onORedeemPage2Password(Context mContext) {
		commonEvent(mContext, "oredeem_page2_password");
	}
	
	/**
	 * 单基赎回-进入确认赎回
	 */
	public static void onORedeemPage2View(Context mContext) {
		commonEvent(mContext, "oredeem_page2_view");
	}
	
	/**
	 * 单基赎回-进入结果页
	 */
	public static void onORedeemPage3View(Context mContext) {
		commonEvent(mContext, "oredeem_page3_view");
	}
	
	/**
	 * 组合申购-进入申购设置页
	 */
	public static void onPBuyPage1View(Context mContext) {
		commonEvent(mContext, "pbuy_page1_view");
	}
	
	/**
	 * 组合申购-弹出密码层
	 */
	public static void onPBuyPage2Password(Context mContext) {
		commonEvent(mContext, "pbuy_page2_password");
	}
	
	/**
	 * 组合申购-进入申购详情
	 */
	public static void onPBuyPage2View(Context mContext) {
		commonEvent(mContext, "pbuy_page2_view");
	}
	
	/**
	 * 组合申购-进入结果页
	 */
	public static void onPBuyPage3View(Context mContext) {
		commonEvent(mContext, "pbuy_page3_view");
	}
	
	/**
	 * 组合赎回-进入赎回设置
	 */
	public static void onPRedeemPage1View(Context mContext) {
		commonEvent(mContext, "predeem_page1_view");
	}
	
	/**
	 * 组合赎回-弹出密码层
	 */
	public static void onPRedeemPage2Password(Context mContext) {
		commonEvent(mContext, "predeem_page2_password");
	}
	
	/**
	 * 组合赎回-赎回确认页
	 */
	public static void onPRedeemPage2View(Context mContext) {
		commonEvent(mContext, "predeem_page2_view");
	}
	
	/**
	 * 组合赎回-赎回结果页
	 */
	public static void onPRedeemPage3View(Context mContext) {
		commonEvent(mContext, "predeem_page3_view");
	}
	
	/**
	 * 注册-登录界面
	 */
	public static void onRegisterPage0View(Context mContext) {
		commonEvent(mContext, "register_page0_view");
	}
	
	/**
	 * 注册-提交手机号
	 */
	public static void onRegisterPage1Click(Context mContext) {
		commonEvent(mContext, "register_page1_click");
	}
	
	/**
	 * 注册-进入注册界面
	 */
	public static void onRegisterPage1View(Context mContext) {
		commonEvent(mContext, "register_page1_view");
	}
	
	/**
	 * 注册-设置了密码并提交
	 */
	public static void onRegisterPage2Click(Context mContext) {
		commonEvent(mContext, "register_page2_click");
	}
	
	/**
	 * 注册-注册成功
	 */
	public static void onRegisterPage2View(Context mContext) {
		commonEvent(mContext, "register_page2_view");
	}
	
	/**
	 * 虚拟组合申购-申购设置页
	 */
	public static void onSpBuyPage1View(Context mContext) {
		commonEvent(mContext, "spbuy_page1_view");
	}
	
	/**
	 * 虚拟组合申购-弹出密码层
	 */
	public static void onSpBuyPage2Password(Context mContext) {
		commonEvent(mContext, "spbuy_page2_password");
	}
	
	/**
	 * 虚拟组合申购-进入申购详情
	 */
	public static void onSpBuyPage2View(Context mContext) {
		commonEvent(mContext, "spbuy_page2_view");
	}
	
	/**
	 * 虚拟组合申购-进入结果页
	 */
	public static void onSpBuyPage3View(Context mContext) {
		commonEvent(mContext, "spbuy_page3_view");
	}
	
	/**
	 * 虚拟组合赎回-进入赎回设置
	 */
	public static void onSpRedeemPage1View(Context mContext) {
		commonEvent(mContext, "spredeem_page1_view");
	}
	
	/**
	 * 虚拟组合赎回-赎回确认页
	 */
	public static void onSpRedeemPage2View(Context mContext) {
		commonEvent(mContext, "spredeem_page2_view");
	}
	
	/**
	 * 虚拟组合赎回-赎回结果页
	 */
	public static void onSpRedeemPage3View(Context mContext) {
		commonEvent(mContext, "spredeem_page3_view");
	}
	
	/**
	 * 存钱购房-点击
	 */
	public static void onGoufangPage1Click(Context mContext) {
		commonEvent(mContext, "goufang_page1_click");
	}
	
	/**
	 * 存钱购房-提交信息
	 */
	public static void onGoufangPage2Click(Context mContext) {
		commonEvent(mContext, "goufang_page2_click");
	}
	
	/**
	 * 存钱购房-点击“开始投资”或“虚拟试投”
	 */
	public static void onGoufangPage3Click(Context mContext) {
		commonEvent(mContext, "goufang_page3_click");
	}
	
	/**
	 * 存钱购房-进入组合
	 */
	public static void onGoufangPage3View(Context mContext) {
		commonEvent(mContext, "goufang_page3_view");
	}
	
	/**
	 * 存钱结婚-点击
	 */
	public static void onJiehunPage1Click(Context mContext) {
		commonEvent(mContext, "jiehun_page1_click");
	}
	
	/**
	 * 存钱结婚-提交信息
	 */
	public static void onJiehunPage2Click(Context mContext) {
		commonEvent(mContext, "jiehun_page2_click");
	}
	
	/**
	 * 存钱结婚-点击“开始投资”或“虚拟试投”
	 */
	public static void onJiehunPage3Click(Context mContext) {
		commonEvent(mContext, "jiehun_page3_click");
	}
	
	/**
	 * 存钱结婚-进入组合
	 */
	public static void onJiehunPage3View(Context mContext) {
		commonEvent(mContext, "jiehun_page3_view");
	}
	
	/**
	 * 理财增值-点击
	 */
	public static void onLicaiPage1Click(Context mContext) {
		commonEvent(mContext, "licai_page1_click");
	}
	
	/**
	 * 理财增值-提交信息
	 */
	public static void onLicaiPage2Click(Context mContext) {
		commonEvent(mContext, "licai_page2_click");
	}
	
	/**
	 * 理财增值-点“开始投资”或“虚拟试投”
	 */
	public static void onLicaiPage3Click(Context mContext) {
		commonEvent(mContext, "licai_page3_click");
	}
	
	/**
	 * 理财增值-进入组合
	 */
	public static void onLicaiPage3View(Context mContext) {
		commonEvent(mContext, "licai_page3_view");
	}
	
	/**
	 * 梦想基金-点击
	 */
	public static void onDreamPage1Click(Context mContext) {
		commonEvent(mContext, "mengxiang_page1_click");
	}
	
	/**
	 * 梦想基金-提交
	 */
	public static void onDreamPage2Click(Context mContext) {
		commonEvent(mContext, "mengxiang_page2_click");
	}
	
	/**
	 * 梦想基金-点击“开始投资”或“虚拟试投”
	 */
	public static void onDreamPage3Click(Context mContext) {
		commonEvent(mContext, "mengxiang_page3_click");
	}
	
	/**
	 * 梦想基金-进入组合
	 */
	public static void onDreamPage3View(Context mContext) {
		commonEvent(mContext, "mengxiang_page3_view");
	}
	
	/**
	 * 存钱养老-点击
	 */
	public static void onPensionPage1Click(Context mContext) {
		commonEvent(mContext, "yanglao_page1_click");
	}
	
	/**
	 * 存钱养老-提交信息
	 */
	public static void onPensionPage2Click(Context mContext) {
		commonEvent(mContext, "yanglao_page2_click");
	}
	
	/**
	 * 存钱养老-点“开始投资”或“虚拟试投”
	 */
	public static void onPensionPage3Click(Context mContext) {
		commonEvent(mContext, "yanglao_page3_click");
	}
	
	/**
	 * 存钱养老-进入组合
	 */
	public static void onPensionPage3View(Context mContext) {
		commonEvent(mContext, "yanglao_page3_view");
	}
	
	/**
	 * 存钱育儿-点击
	 */
	public static void onChildrenPage1Click(Context mContext) {
		commonEvent(mContext, "yuer_page1_click");
	}
	
	/**
	 * 存钱育儿-提交信息
	 */
	public static void onChildrenPage2Click(Context mContext) {
		commonEvent(mContext, "yuer_page2_click");
	}
	
	/**
	 * 存钱育儿-点击“开始投资”或“虚拟试投”
	 */
	public static void onChildrenPage3Click(Context mContext) {
		commonEvent(mContext, "yuer_page3_click");
	}
	
	/**
	 * 存钱育儿-进入组合
	 */
	public static void onChildrenPage3View(Context mContext) {
		commonEvent(mContext, "yuer_page3_view");
	}
	
}
