
package com.qianjing.finance.net.connection;

/**
 * Title: UrlConst Description: 网络请求地址
 */
public class UrlConst {

	/**
	 * root
	 */

	// public static final String ROOT_URL = "https://i.qianjing.com/";
	//	public static final String ROOT_URL = "http://xianjinbao.qianjing.com/";
	public static final String ROOT_URL = "http://qjtest.qianjing.com/";

	public static final String ROOT_URL_TEST = "http://xianjinbao.qianjing.com/";

	public static final String URL_ADS = "http://i.qianjing.com/activ/ad.php?adsType=2";
	// public static final String URL_ADS = "http://test.qianjing.com/activ/ad.php?adsType=2";

	/**
	 * common
	 */

	/** 应用激活 */
	public static final String APP_ACTIVE = "tool/adupdata/api.php";

	/** 注册中请求短信验证码 参数: mb=手机号 */
	public static final String REGISTER_SMS = "user/regist_phone.php";

	/** 注册 参数: vc=验证码 mb=手机号 pwd=登录密码 */
	public static final String REGISTER = "user/activ_phone.php";

	/** 忘记密码后重置密码 参数: vc=验证码 mb=手机号 pwd=新密码 */
	public static final String RESET_PASS = "user/resetpwd.php";

	/** 登录 参数: mb=手机号 pwd=登录密码 */
	public static final String LOGIN = "user/login_phone.php";

	/** 手势解锁登录 */
	public static final String LOGIN_GESTURE = "user/login_ucookie.php";

	/** 修改密码 参数: oldpwd=旧密码 newpwd=新密码 */
	public static final String CHANGE_PASS = "user/chgpwd.php";

	/** 修改密码 参数: mb=手机号 */
	public static final String AGAIN_SMS = "user/sendvc.php";

	/** 用户反馈 参数 fb=反馈内容 */
	public static final String USER_FEEDBACK = "user/feedback.php";

	/** 获取个人信息列表 */
	public static final String USER_SELF = "user/self.php";

	/** 更新版本信息等 */
	public static final String COMMON = "conf/autofinance.php";

	/** 用户信息接口 */
	public static final String SELF = "user/self.php";

	/** 保存用户信息接口 */
	public static final String SAVE_INFO = "user/saveinfo.php";

	/** 消息通知接口 */
	public static final String PUSH_LIST = "user/push_list.php";

	/** 用户登出接口 */
	public static final String LOGOUT = "user/logout_phone.php";

	/** 消息通知接口二次请求id=2 */
	public static final String PUSH_INFO = "user/push_info.php";

	/** 邀请码分享接口 */
	public static final String INVITE_SHARE = "/activ/invite_relation/share.php";

	/** 钱景企业QQ */
	public static final String ONLINE_SERVICE = "mqqwpa://im/chat?chat_type=crm&uin=800113900&version=1&src_type=web&web_src=http:://www.qianjing.com";

	/** 会员体系介绍页 */
	public static final String MEMBER_SYSTEM_INTRODUCE = "http://i.qianjing.com/help/member_info/index01.html";

	/**
	 * assemble
	 */

	/** 创建组合 1.3.4版起已废弃 */
	// public static final String CREATE_SCHEME = "financial/create_schema.php";

	/** 组合总资产 参数:sid */
	public static final String CAL_ASSETS = "financial_v3/calassets.php";

	/** 获取一个用户的组合列表 参数:type为1有持仓，2是无持仓 nm=返回个数 of=偏移量 不传参返回全部 */
	public static final String ASSEMBLE_LIST = "financial_v4/list_schema.php";

	/** 获取一个组合详情 参数:sid=方案id */
	public static final String ASSEMBLE_DETAIL = "financial_v4/schema_detail.php";

	/** 删除一个组合 参数:sid=方案id */
	public static final String REMOVE_ASSEMBLE = "financial_v2/remove_schema.php";

	/** 组合投资计算 参数: 不定 */
	public static final String CAL_ASSEMBLY = "financial_v4/cal_assembly.php";

	/** 组合手续费率计算 参数: 所有单只基金代码 */
	public static final String ASSEMBLE_BUY_FEE = "/financial_v3/get_fee.php";

	/** 组合申购 参数:sid=方案id sum=金额 pwd=密码 cd=银行卡 另加组合投资计算接口参数 */
	public static final String ASSEMBLE_BUY = "financial_v2/order_schema.php";

	/** 组合追加投资 参数:sid=方案id sum=金额 pwd=密码 cd=银行卡 另加组合投资计算接口参数 */
	public static final String ASSEMBLE_BUY_ADD = "financial_v2/additional_order.php";

	/** 等待下单银行返回接口 参数:sid=方案id opid=操作id */
	public static final String WAIT_ORDER = "financial_v2/wait_order.php";

	/** 更新一个组合 参数:sid=方案id */
	public static final String UPDATE_ASSEMBLE = "financial_v2/update_schema.php";

	/** 最小申购金额 参数:sid=组合id */
	public static final String MINSUB = "financial_v3/minsub.php";

	/** 最小赎回金额 参数:sid=组合id */
	public static final String MIN_REDEMP = "financial_v2/minredemp.php";

	/**  */
	public static final String REDEMP_VIEW = "/financial_v3/redemp_view.php";

	/** 组合赎回 参数:sid=组合id sum=金额 pwd=密码radio */
	public static final String REDEMP_SCHEMA = "financial_v2/redemp_schema.php";

	/** 组合预赎回 参数:sid=组合id sum=金额 radio */
	public static final String PREREDEMP_SCHEMA = "financial_v2/preredemp_schema.php";

	/** 组合预赎回 参数:sid=组合id nm=返回个数 of=偏移量 */
	public static final String LIST_OPERATE = "financial_v2/list_operate.php";

	/** 真实组合赎回接口 */
	public static final String ASSEMBLE_REDEEM = "/financial_v2/redemp_schema.php";

	/**  */
	public static final String ASSEMBLE_REDEEM_V3 = "/financial_v3/redemp_schema.php";

	/** 真实基金确认赎回接口 */
	public static final String ASSEMBLE_FUND_REDEEM_SURE = "/financial_v2/preredemp_schema.php";

	/** 持仓明细 参数:sid=组合id */
	public static final String LIST_ASSETS = "financial_v2/list_assets.php";

	/** 等待银行返回结果 参数：sid=组合id sopid=操作号 */
	public static final String BANK_BACK = "financial_v3/wait_order.php";

	/** 获取用户投资偏好 */
	public static final String GET_USER_HIBIT = "/financial_v2/get_user_hibit.php";

	/** 保存用户投资偏好 */
	public static final String SAVE_USER_HIBIT = "/financial_v2/save_user_hibit.php";

	/** 总资产接口 */
	public static final String ASSETS_TOTAL = "/fund_market/gather_assets.php";

	/** 组合交易记录 */
	public static final String TOTAL_ASSEMABLE_DETAILS = "/financial_v3/assembly_trade_list.php";

	/** 组合每日盈亏 */
	public static final String ASSEMBLE_EVERYDAY_PROFIT = "/financial_v3/assembly_everyday_profit.php";

	/** 快速购买列表 */
	public static final String QUICK_BUY_LIST = "/quick_buy/list_type.php";

	/** 快速购买组合详情 */
	public static final String QUICK_BUY_DETAIL = "/quick_buy_v2/view_data.php";

	/** 快速购买 */
	public static final String QUICK_BUY = "quick_buy/buy.php";

	/** 是否需要补购 */
	public static final String MEND_CHECK = "quick_buy/is_rebuy.php";

	/** 补购提交 */
	public static final String MEND_COMMIT = "quick_buy/rebuy.php";

	/**
	 * virtual assemble
	 */

	/** 虚拟组合创建接口 1.3.4版起已废弃 */
	// public static final String VIRTUAL_CREATE_ASSEMBLE = "/virtual_v2/create_schema.php";

	/** 虚拟总资产接口 */
	public static final String VIRTUAL_TOTAL_ASSETS = "/virtual_v2/calassets.php";

	/** 虚拟资产接口 */
	public static final String VIRTUAL_ASSETS = "/virtual_v2/list_schema.php";

	/** 虚拟删除组合接口 */
	public static final String VIRTUAL_DELETE_SCHEME = "/virtual_v2/remove_schema.php";

	/** 虚拟组合详情接口 */
	public static final String VIRTUAL_COMBINATION = "/virtual_v2/schema_detail.php";

	/** 虚拟修改组合接口 */
	public static final String VIRTUAL_MODIFY_SCHEME = "/virtual_v2/update_schema.php";

	/** 虚拟基金份额赎回接口 */
	public static final String VIRTUAL_FUND_REDEEM = "/virtual_v2/redemp_schema.php";

	/** 虚拟累计盈亏接口 */
	public static final String VIRTUAL_TOTAL_PAL = "/virtual_v2/list_operate.php";

	/** 虚拟持仓明细接口 */
	public static final String VIRTUAL_POSITION_DETAILS = "/virtual_v2/list_assets.php";

	/** 虚拟申购接口 */
	public static final String VIRTUAL_BUY = "/virtual_v2/order_schema.php";

	/** 虚拟追加投资接口 */
	public static final String VIRTUAL_ADD_BUY = "virtual_v2/additional_order.php";

	/** 是否使用虚拟账户接口 */
	public static final String YON_USE_VIRTUAL_ACCOUNT = "/virtual_v2/is_use_virtual.php";

	/** 是否首次使用虚拟账户 */
	public static final String FIRST_USE_VIRTUAL_ACCOUNT = "/virtual_v2/update_use_virtual.php";

	/** 虚拟基金确认赎回接口 */
	public static final String VIRTUAL_ASSEMBLE_FUND_REDEEM_SURE = "/virtual_v2/preredemp_schema.php";

	/**
	 * rebalance
	 */

	/** 再平衡详情接口 */
	public static final String REBALANCE_DETAIL = "/rebalance/prebalance_schema.php";

	/** 再平衡提交接口 */
	public static final String REBALANCE_SUBMIT = "/rebalance/balance_schema.php";

	/** 再平衡记录详情接口 */
	public static final String REBALANCE_HISTORY_DETAIL = "/rebalance/rebalance_schema_detail.php";

	/** 再平衡持仓对比接口 */
	public static final String REBALANCE_HOLDING_COMPARE = "/rebalance/rebalance_snapshot.php";

	/** 再平衡组合列表接口 */
	public static final String REBALANCE_ASSEMBLE_LIST = "/financial_v3/list_rebalance_schema.php";

	/** 关闭再平衡接口 */
	public static final String REBALANCE_CLOSE = "/financial_v3/close_rebalance.php";

	/** 再平衡拒绝接口 功能未开放 */
	public static final String REBALANCE_REFUSE = "/rebalance/refuse_balance.php";

	/**
	 * aip
	 */
	/** 定投详情 */
	public static final String ASSEMBLE_AIP_DETAIL = "/financial_v4/dt_detail.php";

	/** 定投修改预览 */
	public static final String ASSEMBLE_AIP_PRE_CHANGE = "/financial_v4/pre_change_day.php";

	/** 定投日期修改 */
	public static final String ASSEMBLE_AIP_CHANGE_RESULT = "/financial_v4/change_dt_day.php";

	/** 定投状态修改 */
	public static final String ASSEMBLE_AIP_CHANGE_STATE = "financial_v4/change_dt_state.php";

	/** 定投首次扣款日期 */
	public static final String ASSEMBLE_AIP_DATE_FIRST = "financial_v4/get_next_day.php";

	/** 定投申购 */
	public static final String ASSEMBLE_AIP_BUY = "financial_v4/order_schema.php";
	/**
	 * 定投扣款记录
	 */
	public static final String ASSEMBLE_DT_RECORD = "financial_v4/dt_record_detail.php";

	/**
	 * fund
	 */

	/** 获取基金列表 */
	public static final String FUND_LIST = "fund/list.php";

	/** 总资产情况 */
	public static final String ALL_PROPERTY = "fund/general.php";

	/** 持仓明细 */
	public static final String PROPERTY_DETAIL = "fund/assetses.php";

	/** 具体资金的持仓明细 参数：fd=基金代码 */
	public static final String PROPERTY_ASSES_DETAIL = "fund/fundasses.php";

	/** 基金申购 参数：fd=基金代码 sum=申购金额 cd=用于申购的银行卡号 pwd=密码 */
	public static final String SUBSCRIPT = "fund/subscript.php";

	/** 基金赎回 参数：fd=基金代码 sr=赎回基金份额 cd=用于申购的银行卡号 pwd=密码 */
	public static final String REDEMP = "fund/redemp.php";

	/** 交易明细 参数：fd=基金代码 */
	public static final String OPLOGS = "fund/oplogs.php";

	/** 基金信息 参数：fd=基金代码 */
	public static final String FUND_INFO = "fund/info.php";

	/** 基金撤单 参数：opid=操作数 pwd=密码 */
	public static final String WITH_DRAW = "fund/withdraw.php";

	/** 基金列表 */
	public static final String URL_FUND_LIST_ALL = "/fund/list.php";

	/** 基金详情 参数:fund_code=基金ID */
	public static final String URL_FUND_INFO = "fund_market/info.php";

	/** 我的基金总资产 */
	public static final String URL_FUND_MINE = "fund_market/total_assets.php ";

	/** 我的基金持仓明细 */
	public static final String URL_FUND_MINE_HOLD = "fund_market/fund_assets.php";

	/** 我的基金持仓详情 */
	public static final String URL_FUND_MINE_HOLD_DETAIL = "/fund_market/assets_details.php";

	/** 我的基金交易记录 */
	public static final String URL_FUND_MINE_HISTORY = "/fund_market/trade_logs.php";

	/** 基金列表 */
	public static final String NEW_FUND_LIST = "fund_market/list.php";

	/** 我的基金交易记录详情接口 */
	public static final String URL_FUND_HISTORY_DETAILS = "/fund_market/log_details.php";

	/** 基金申购详情 */
	public static final String FUND_BUY_DETAIL = "fund_market/subscript_details.php";

	/** 基金申购接口 */
	public static final String FUND_BUY = "fund_market_v2/subscript.php";

	/** 基金申购等待接口 */
	public static final String FUND_WAITING = "fund_market_v2/waitingbankback.php";

	/** 基金赎回接口 */
	public static final String FUND_REDEEM = "fund_market/redemp.php";

	/** 单只基金交易记录 */
	public static final String ALONE_FUND_DETAILS = "/fund_market_v2/fund_trade_details.php";

	/** 基金交易记录 */
	public static final String TOTAL_FUND_DETAILS = "/fund_market_v2/fund_trade_list.php";

	/** 活期宝交易记录 */
	public static final String TOTAL_WALLET_DETAILS = "/xianjinbao/get_trade_log_v2.php";

	/** 基金每日盈亏 */
	public static final String FUND_EVERYDAY_PROFIT = "/fund_market_v2/fund_everyday_profit.php";

	/** 基金全量搜索接口 */
	public static final String FUND_SEARCH = "/fund/search.php";

	/** 基金全量搜索数据最新更新时间接口 */
	public static final String FUND_LATESAT_UPDATE = "/fund/upsearch.php";

	/**
	 * card
	 */

	/** 获取绑定银行卡列表 */
	public static final String CARD_LIST = "card/list.php";

	/** 获取绑卡支持银行列表 */
	public static final String CARD_LIST_SUPPORT = "/conf/support_bank.php";

	/** 获取支行信息 */
	public static final String BRACH_LIST = "card/banklist.php";

	/** 绑定银行卡 参数 id=身份证号 nm=姓名 cd=卡号 bk=银行 sb=支行 mb=手机号 vc=验证码 */
	public static final String CARD_BOUND = "card/bound.php";

	/** 获取银行卡可解绑状态 */
	public static final String CARD_UNBOUND_STATE = "card/unboundcheck.php";

	/** 解绑银行卡 */
	public static final String CARD_UNBOUND = "card/unbound.php";

	/** 还原银行卡 */
	public static final String CARD_HTYCARD = "card/hycard.php";

	/** 绑卡Step1： B026 返回商户信息等 参数：fd=基金代码 */
	public static final String CHINAPAY_COMMIT = "user/chinapay_mobileopen.php";

	/** 绑卡Step2： B027 返回商户信息等 参数：card=银行卡 bankserial=银行编号 respCode=返回码 respMsg=返回信息 userInfo=用户信息 */
	public static final String CHINAPAY_VERIFY = "user/chinapay_mobilebackverify.php";

	/** 银行卡是否已解绑 参数：sid=组合id */
	public static final String IS_BANK_UNBOUND = "financial_v2/minsub.php";

	/** 快钱验证绑卡信息并发送短信接口 */
	public static final String QUICKBILL_CARD_VERIFY = "user/99bill_mobileopen.php";

	/** 快钱验证短信并绑卡接口 */
	public static final String QUICKBILL_SMS_VERIFY = "user/99bill_mobilebackverify.php";

	/** 快钱银行名称对应银行号接口 */
	public static final String QUICKBILL_CARD_MAP = "card/cardmap.php";

	/**
	 * wallet
	 */

	/** 活期宝总资产详情接口 */
	public static final String WALLET_ASSETS = "xianjinbao/get_assets.php";

	/** 活期宝充值 card 银行卡 sum 充值金额 password 交易密码 */
	public static final String WALLET_RECHARGE = "xianjinbao/recharge.php";

	/** 活期宝取现 card 银行卡 sum 取现金额 password 交易密码 type 取现类型 2快速取现，3普通取现 */
	public static final String WALLET_CASH = "xianjinbao/encashment.php";

	/** 充值后等待银行返回结果 */
	public static final String WALLET_WAITBANK = "xianjinbao/waitbankback.php";

	/** 活期宝七日年化收益率 */
	public static final String WALLET_YIELD_YEAR = "xianjinbao/everyday_yield.php";

	/** 活期宝当前持仓银行卡 */
	public static final String WALLET_CARD = "xianjinbao/get_assets_list.php";

	/** 活期宝交易记录 */
	public static final String WALLET_TRADE = "xianjinbao/get_trade_log.php";

	/** 活期宝历史每日收益 */
	public static final String WALLET_EVERYDAY_PROFIT = "xianjinbao/get_everyday_profit.php";

	/** 活期宝当日赎回次数 */
	public static final String WALLET_TODAY_REDEMP_COUNT = "xianjinbao/today_redemp_count.php";

	/** 活期宝充值时调取用户银行接口 和原先/card/list.php接口一致 另外加了single_limit单笔充值限额daily_limit每日充值限额 */
	public static final String WALLET_CARD_LIST = "xianjinbao/recharge_card_list.php";

	/** 获取金额限制接口 */
	public static final String WALLET_GET_LIMIT = "xianjinbao/get_limit.php";

	/** 获取充值收益时间 */
	public static final String WALLET_RECHARGE_TIME = "xianjinbao/recharge_time.php";

	/**
	 * p2p
	 */
	/** token生成 */
	public static final String P2P_CREATE_TOKEN = "p2p/tk.php ";

	/** p2p首页入口 */
	public static final String FIXED_INVEST_ENTER_DATA = "/p2p/jcm_index.php";

	/** p2p产品展示入口 */
	public static final String P2P_INVEST = "http://testwx.jiacaimao.com/Tdeal?tk=";
	/**
	 * p2p定存资产页
	 */
	public static final String TIME_DESPORITY_ASSET = "p2p/jcm_asset_detail.php";
	/**
	 * p2p定存收益记录
	 */
	public static final String TIME_DESPORITY_PROFIT_HISTORY = "p2p/jcm_profit_detail.php";

	/**
	 * p2p定存资产明细 int moblie int type
	 */
	public static final String TIME_DESPORITY_ASSET_DETAIL = "p2p/jcm_invest_list.php";

	/** p2p历史记录接口 */
	public static final String P2P_HISTORY_LIST = "p2p/jcm_trade_log.php";
	/** p2p历史记录详情接口 */
	public static final String P2P_HISTORY_DETAILS = "p2p/jcm_trade_detail.php";

	/**
	 * 保本保息
	 */
	/** 获取token **/
	public static final String P2P_TOKEN = "p2p/tk.php";
	/** 首页推荐 **/
	public static final String P2P_RECOMMAND = "p2p/jcm_recommend.php";
	/** 首页推荐产品详情 **/
	public static final String P2P_RECOMMAND_DETAIL = "p2p/jcm_breakeven_detail.php";

	/** p2p银行卡信息 **/
	public static final String P2P_RECOMMAND_CARD = "p2p/jcm_card_info.php";
	/** p2p每天免费取现次数 **/
	public static final String P2P_RECOMMAND_COUNT = "p2p/jcm_fee_info.php";
	/** p2p取现接口 **/
	public static final String P2P_RECOMMAND_REDEEM = "p2p/jcm_redemp.php";
	/** 推荐产品解释wap **/
	public static final String P2P_RECOMMAND_DETAIL_HELP = "http://i.qianjing.com/help/baoben_comb/qa4.html";
	/** 推荐产品购买url **/
	public static final String P2P_RECOMMAND_BUY = "http://testwx.jiacaimao.com/Tload/buy?bid=";
	/** 保本组合是否开放 **/
	public static final String P2P_STEADY_STATE = "p2p/jcm_status.php";

	/** 基金推荐详情 **/
	public static final String RECOMMAND_FUND_DETAIL = "financial_v4/baoben_detail.php";
	/**
	 * activity
	 */
	/** 获取活动信息 */
	public static final String ACTIVITY_STATE = "/activity/info.php";

	/** 获取用户的红包列表 */
	public static final String ACTIVITY_REDBAG_LIST = "/redbag/redbaglist.php";

	/** 购买单只基金使用红包 */
	public static final String ACTIVITY_REDBAG_BUY_FUND = "/redbag/buy_fund.php";

	/** 获取活动信息 */
	public static final String ACTIVITY_REDBAG_BUY_ASSEMBLE = "/redbag/buy_assembly.php";

	/** 申购后是否可参与抽奖 */
	public static final String ACTIVITY_LOTTEY_STATUS = "/activity/lottery_more_than.php";

	/**
	 * 每日盈亏列表
	 */
	public static final String ASSEMBLE_DAY_PROFIT = "/financial_v4/assembly_profit.php";
	/**
	 * 近一个月盈亏变化
	 */
	public static final String ASSETS_PROFIT_LIST = "/fund_market_v2/month_assets_list.php";
	/**
	 * 组合总资产
	 */
	public static final String ASSETS_TOTAL_DATA = "/fund_market_v2/all_assets.php";

	/**
	 * p2p相关
	 */
	/** 保本组合每日收益 */
	public static final String JCM_EVERYDAY_PROFIT = "p2p/jcm_everyday_profit.php";

	/** 保本组合申购 */
	public static final String STEADY_BUY = "financial_v4/baoben.php";

	/** 保本交易记录列表 */
	public static final String JCM_HISTORY_LIST = "p2p/jcm_breakeven_trade_log.php";

	/**
	 * p2p
	 */
	public static final String P2P_CARD_DETAIL = "p2p/jcm_card_info.php";

	/** 已投保本组合详情 */
	public static final String P2P_PORT_DETAIL = "p2p/jcm_my_breakeven_detail.php";

	/** 交易记录详情 */
	public static final String P2P_HISTORY_DETAIL = "p2p/jcm_trade_detail.php";

	/**p2p资产信息*/
	public static final String P2P_ASSETS_MSG = "/p2p/jcm_my_asset.php";

}
