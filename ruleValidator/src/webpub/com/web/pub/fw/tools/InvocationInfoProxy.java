package com.web.pub.fw.tools;

import com.web.pub.fw.factory.UserContext;

/**
 * ECP系统环境变量
 * 
 * @author:sunshine
 * @date:2012-5-31 下午07:10:20
 */
public class InvocationInfoProxy {

	private static InvocationInfoProxy instance = new InvocationInfoProxy();

	private final ThreadLocal<UserContext> threadHolder = new ThreadLocal<UserContext>();

	/**
	 * 获取一个实例
	 */
	public static InvocationInfoProxy getInstance() {
		return instance;
	}

	public void setInvocationContext(UserContext context) {
		threadHolder.set(context);
	}

	public void clear() {
		threadHolder.remove();
	}

	private UserContext getUserContext() {
		if (threadHolder != null) {
			return threadHolder.get();
		}
		return null;
	}

	/**
	 * 获取用户主键
	 * 
	 * @return
	 * @author:sunshine
	 * @date:2012-5-31 下午07:18:37
	 */
	public String getPkUser() {
		if (getUserContext() != null) {
			return getUserContext().getUserId();
		}
		return null;
	}

	/**
	 * 获取用户名称
	 * 
	 * @return
	 * @date 2012-6-18 上午10:17:28
	 */
	public String getUserName() {
		if (getUserContext() != null) {
			return getUserContext().getUserName();
		}
		return null;
	}

	/**
	 * 获取用户code
	 * 
	 * @return
	 * @date 2012-6-18 上午10:17:28
	 */
	public String getUserCode() {
		if (getUserContext() != null) {
			return getUserContext().getUserCode();
		}
		return null;
	}
}
