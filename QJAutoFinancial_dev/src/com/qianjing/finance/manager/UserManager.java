package com.qianjing.finance.manager;

import com.qianjing.finance.model.common.User;

/**
 * @description 单例管理用户信息
 * @author majinxin
 * @date 2015年8月19日
 */
public class UserManager {

	private static UserManager userManager = null;
	private User user = null;

	public static UserManager getInstance() {

		if (userManager == null) {

			synchronized (UserManager.class) {

				if (userManager == null) {

					userManager = new UserManager();
				}
				return userManager;
			}
		} else {

			return userManager;
		}
	}

	/**
	 * 初始化用户信息
	 * 
	 * @param user
	 */
	public void setUser(User user) {

		this.user = user;
	}

	/**
	 * 判断用户是否登陆
	 * 
	 * @return
	 */
	public User getUser() {

		return this.user;
	}

	/**
	 * 清空用户信息
	 */
	public void removeUser() {

		this.user = null;
	}
}
