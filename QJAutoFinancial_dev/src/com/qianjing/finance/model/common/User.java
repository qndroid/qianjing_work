
package com.qianjing.finance.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**********************************************************
 * @文件名称：User.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年6月2日 下午5:13:07
 * @文件描述：用户信息Bean
 * @修改历史：2015年6月2日创建初始版本
 **********************************************************/
public class User extends BaseModel {

	private static final long serialVersionUID = 1L;

	@SerializedName("uid")
	private String uid;

	@SerializedName("nick")
	private String nick;

	@SerializedName("name")
	private String name;

	@SerializedName("identity")
	private String identity;

	@SerializedName("mobile")
	private String mobile;

	@SerializedName("passwd")
	private String passwd;

	@SerializedName("state")
	private String state;

	@SerializedName("opened")
	private int opened;

	@SerializedName("email")
	private String email;

	@SerializedName("emailVerified")
	private String emailVerified;

	@SerializedName("addr")
	private String addr;

	@SerializedName("hasMarried")
	private String hasMarried;

	@SerializedName("educate")
	private String educate;

	@SerializedName("work")
	private String work;

	@SerializedName("hasInvest")
	private String hasInvest;

	@SerializedName("is_use_virtual")
	private String is_use_virtual;

	@SerializedName("invite")
	private String invite;

	@SerializedName("parent")
	private String parent;

	@SerializedName("is_freeze")
	private String is_freeze;

	/** 普通会员是1，VIP会员是2 */
	@SerializedName("is_member")
	private int isMember;

	@SerializedName("sex")
	private int sex;

	/**
	 * 投资偏好
	 * age 年龄
	 * init 可投资金
	 * money 税后年收入
	 * preference 风险偏好
	 * risk 市场波动时的操作倾向
	 * */
	@SerializedName("risk")
	private String risk;

	@Expose(serialize = false)
	private String age;

	@Expose(serialize = false)
	private String money;

	@Expose(serialize = false)
	private String init;

	@Expose(serialize = false)
	private String preference;

	@Override
	public String toString() {
		return "UsertInfo [uid=" + uid + ", nick=" + nick + ", name=" + name + ", identity=" + identity + ", mobile=" + mobile + ", passwd=" + passwd
				+ ", state=" + state + ", opened=" + opened + ", email=" + email + ", emailVerified=" + emailVerified + ", addr=" + addr
				+ ", hasMarried=" + hasMarried + ", educate=" + educate + ", work=" + work + ", hasInvest=" + hasInvest + ", risk=" + risk
				+ ", is_use_virtual=" + is_use_virtual + ", invite=" + invite + ", parent=" + parent + ", is_freeze=" + is_freeze + "]";
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getOpened() {
		return opened;
	}

	public void setOpened(int opened) {
		this.opened = opened;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(String emailVerified) {
		this.emailVerified = emailVerified;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getHasMarried() {
		return hasMarried;
	}

	public void setHasMarried(String hasMarried) {
		this.hasMarried = hasMarried;
	}

	public String getEducate() {
		return educate;
	}

	public void setEducate(String educate) {
		this.educate = educate;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getHasInvest() {
		return hasInvest;
	}

	public void setHasInvest(String hasInvest) {
		this.hasInvest = hasInvest;
	}

	public String getRisk() {
		return risk;
	}

	public void setRisk(String risk) {
		this.risk = risk;
	}

	public String getIs_use_virtual() {
		return is_use_virtual;
	}

	public void setIs_use_virtual(String is_use_virtual) {
		this.is_use_virtual = is_use_virtual;
	}

	public String getInvite() {
		return invite;
	}

	public void setInvite(String invite) {
		this.invite = invite;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getIs_freeze() {
		return is_freeze;
	}

	public void setIs_freeze(String is_freeze) {
		this.is_freeze = is_freeze;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getInit() {
		return init;
	}

	public void setInit(String init) {
		this.init = init;
	}

	public String getPreference() {
		return preference;
	}

	public void setPreference(String preference) {
		this.preference = preference;
	}

	public int getIsMember() {
		return isMember;
	}

	public void setIsMember(int isMember) {
		this.isMember = isMember;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

}
