package com.qianjing.finance.constant;


/**
 * Title: ErrorCode
 * Description: 错误码
 * @author zhangqi
 * @date 2014-4-11
 */
public class ErrorConst {
	
	public static final int EC_OK = 0;
	
	public static final int EC_SYSERROR = -1;

	/** uid格式不正确 */
	public static final int EC_INVALID_UID = 101;  
	
	/** nick格式不正确 */
	public static final int EC_INVALID_NICK = 102;  
	
	/** 手机号格式不正确 */
	public static final int EC_INVALID_MOBILE = 103;
	
	/** 密码格式不正确  */
	public static final int EC_INVALID_PASSWD = 104;
	
	/** 验证码格式不正确  */
	public static final int EC_INVALID_VERICODE = 105;
	
	/** 姓名格式不正确 */
	public static final int EC_INVALID_NAME = 106;
	
	/** 身份证格式不正确 */
	public static final int EC_INVALID_IDENTITY = 107;
	
	/** 银行卡号格式不对 */
	public static final int EC_INVALID_CARD = 108; 
	
	/** 银行名称格式不对 */
	public static final int EC_INVALID_BANK = 109;
	
	/** 支行名称格式不对 */
	public static final int EC_INVALID_SUBBANK = 110;
	
	
	/** 手机号已经存在 */
	public static final int EC_MOBILE_EXISTS = 130;
	
	/** 手机号已经存在 */
	public static final int EC_MOBILE_NOEXISTS = 131;
	
	/** 密码不匹配 */
	public static final int EC_PASSWD_DISMATCH = 132;
	
	/** 无效的cookie（没有登录，tcookie是无效的） */
	public static final int EC_INVALID_COOKIE = 133;
	
	/** 错误的验证码 */
	public static final int EC_ERROR_VERICODE = 134;
	
	/** 用户的状态不是活跃状态（比如因为违规操作被屏蔽一段时间） */
	public static final int EC_USER_NOACTIV = 135;
	
	/** 帐户信息不匹配，比如第二次添加银行卡输入的身份信息不对 */
	public static final int EC_ACCOUNT_DISMATCH = 136;
	
	/** 信息验证失败，银行系统的检查结果 */
	public static final int EC_ACCOUNT_ERROR = 137;
	
	/** 银行卡已经存在 */
	public static final int EC_CARD_EXISTS = 138;
	
	/** 银行卡不存在 */
	public static final int EC_CARD_NOEXISTS = 139;
	
	/** 支行不存在 */
	public static final int EC_NOSUBBANK = 140;
	
	/** 需要pc验证 */
	public static final int EC_NEED_PCVERI = 141;
	
	/** 验证码发送失败 */
	public static final int EC_SMS_FAIL = 150;
	
	/** 验证码发送次数超过限制 */
	public static final int EC_SMS_UPBOUND = 151;
	
	/** 没有这支基金 */
	public static final int EC_NO_FUND = 201; 
	
	/** 基金申购失败 */
	public static final int EC_SUBSCRIPT_ERROR = 202;
	
	/** 没有该操作日志 */
	public static final int EC_NO_OPLOG = 203; 
	
	/** 没有足够资产 */
	public static final int EC_NOENOUGH_ASSETS = 204; 
	
	/** 错误的日志状态 */
	public static final int EC_ERROR_OPSTATE = 205;
	
	/** 没有足够的剩余基金份额，因为有份额正在赎回中 */
	public static final int EC_NOENOUGH_PREASSETS = 206;
	
	/** 银行返回结果失败 */
	public static final int EC_BANKBACK_FAIL = 211;
	
	/** 继续等待 */
	public static final int EC_BANKBACK_CONTINUE = 212;
	
	/** 不能撤单 */
	public static final int EC_CANT_WITHDRAW = 213;
	
	//根据错误码得到错误信息描述
	public static String getErrMsg(int errCode){
		String errMsg = "网络信号差，请重试";
		switch (errCode) {
		case EC_SYSERROR:
			errMsg = "系统错误";
			break;
		case EC_INVALID_UID:
			errMsg = "uid格式不正确";
			break;
		case EC_INVALID_NICK:
			errMsg = "nick格式不正确";
			break;
		case EC_INVALID_MOBILE:
			errMsg = "手机号格式不正确";
			break;
		case EC_INVALID_PASSWD:
			errMsg = "密码格式不正确";
			break;
		case EC_INVALID_VERICODE:
			errMsg = "验证码格式不正确";
			break;
		case EC_INVALID_NAME:
			errMsg = "姓名格式不正确";
			break;
		case EC_INVALID_IDENTITY:
			errMsg = "身份证格式不正确";
			break;
		case EC_INVALID_CARD:
			errMsg = "银行卡号格式不对";
			break;
		case EC_INVALID_BANK:
			errMsg = "银行名称格式不对";
			break;
		case EC_INVALID_SUBBANK:
			errMsg = "支行名称格式不对";
			break;
		case EC_MOBILE_EXISTS:
			errMsg = "手机号已经存在";
			break;
		case EC_MOBILE_NOEXISTS:
			errMsg = "该手机号未注册过";
			break;
		case EC_PASSWD_DISMATCH:
			errMsg = "账号或密码不正确，请重新输入";
			break;
		case EC_INVALID_COOKIE:
			errMsg = "太久没有登录，请重新登录";
			break;
		case EC_ERROR_VERICODE:
			errMsg = "错误的验证码";
			break;
		case EC_USER_NOACTIV:
			errMsg = "用户的状态不是活跃状态";
			break;
		case EC_ACCOUNT_DISMATCH:
			errMsg = "帐户信息不匹配";
			break;
		case EC_ACCOUNT_ERROR:
			errMsg = "信息验证失败，银行系统的检查结果";
			break;
		case EC_CARD_EXISTS:
			errMsg = "银行卡已经存在";
			break;
		case EC_CARD_NOEXISTS:
			errMsg = "银行卡不存在";
			break;
		case EC_NOSUBBANK:
			errMsg = "支行不存在";
			break;
		case EC_NEED_PCVERI:
			errMsg = "需要pc验证";
			break;
		case EC_SMS_FAIL:
			errMsg = "验证码发送失败";
			break;
		case EC_SMS_UPBOUND:
			errMsg = "验证码发送次数超过限制";
			break;
		case EC_NO_FUND:
			errMsg = "没有这支基金";
			break;
		case EC_SUBSCRIPT_ERROR:
			errMsg = "基金申购失败";
			break;
		case EC_NO_OPLOG:
			errMsg = "没有该操作日志";
			break;
		case EC_NOENOUGH_ASSETS:
			errMsg = "没有足够资产";
			break;
		case EC_ERROR_OPSTATE:
			errMsg = "错误的日志状态";
			break;
		case EC_NOENOUGH_PREASSETS:
			errMsg = "没有足够的剩余基金份额，因为有份额正在赎回中";
			break;
		case EC_BANKBACK_FAIL:
			errMsg = "银行返回结果失败";
			break;
		case EC_BANKBACK_CONTINUE:
			errMsg = "继续等待";
			break;
		case EC_CANT_WITHDRAW:
			errMsg = "不能撤单";
			break;
		default:
			break;
		}
		return errMsg;
	}
}
