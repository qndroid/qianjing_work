package com.qianjing.finance.model.common;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @description 消息通知列表实体
 * @author fangyan
 * @date 2015年8月12日
 */
public class InformList extends BaseModel
{

	private static final long serialVersionUID = 1L;

	/** 消息通知列表键 */
	@SerializedName("data")
	public InformListKey keyList;

	public static class InformListKey
	{

		/** 消息通知列表 */
		@SerializedName("sysmsgs")
		public List<Information> listInform;

		public static class Information
		{
			/*
			 * "audience": "all", "content": "近期操作提示：震荡中等待时机，底部区间可分布建仓", "contentid": "25550", "contenturl":
			 * "http://url.cn/ccMItA", "id": "25550", "is_read": "1", "isok": "0", "log_time": 1439292404, "msg_id":
			 * "0", "msg_type": "2", "platform": "all", "sendno": "0", "title": "8月11日收市点评"
			 */

			@SerializedName("audience")
			public String audience;

			@SerializedName("content")
			public String content;

			@SerializedName("contentid")
			public String contentId;

			@SerializedName("contenturl")
			public String contentUrl;

			@SerializedName("id")
			public String id;

			@SerializedName("is_read")
			public String isRead;

			@SerializedName("isok")
			public String isOk;

			@SerializedName("log_time")
			public String logTime;

			@SerializedName("msg_id")
			public String msgId;

			@SerializedName("msg_type")
			public String msgType;

			@SerializedName("platform")
			public String platform;

			@SerializedName("sendno")
			public String sendno;

			@SerializedName("title")
			public String title;

		}
	}
}
