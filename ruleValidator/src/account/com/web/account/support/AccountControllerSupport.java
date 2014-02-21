package com.web.account.support;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.web.account.uiview.UserUIView;
import com.web.pub.fw.exception.PubBaseException;

/**
 * 客户信息supprot
 * 
 * @since 6.1
 * @version 2012-4-20 下午02:57:12
 * @author 孟志昂
 */
@Service
public class AccountControllerSupport {

	/**
	 * 获取账户信息（面向于前端账户信息展示）
	 * 
	 * @param userView
	 *            用户view
	 * @return 账户view
	 * @throws EcpBaseException
	 */
	public UserUIView getAccount(UserUIView userView) throws PubBaseException {
		// 获取数据服务实例
		// IQryCustInfoService service =
		// ECPServiceFactory.getCustInfoQryService();
		// // 参数一： 客户id
		// final String customerId = userView.getCustomerid();
		// Assert.hasText(customerId, "登录用户没有关联客户");
		// // 参数二：集团id
		// String gourpId = EcpInvocationInfoProxy.getInstance().getPkGroup();
		// // 查询客户
		// CustomerPOJO customerPOJO = service.queryCustomer(customerId,
		// gourpId);
		// Assert.notNull(customerPOJO, "登录用户没有关联客户");
		return userView;
	}

	/**
	 * 更新密码
	 * 
	 * @param userId
	 *            用户id
	 * @param oldPassword
	 *            旧密码
	 * @param newPassword
	 *            新密码
	 * @return
	 * @throws EcpBaseException
	 */
	public Map<String, String> updatePassword(String userId,
			String oldPassword, String newPassword) throws PubBaseException {

		// // 获取数据服务实例
		// IUserMaintainService service = ECPServiceFactory
		// .getUserMaintainService();

		Map<String, String> message = new HashMap<String, String>();

		// try {
		// service.updatePassword(userId, oldPassword, newPassword);
		// message.put(IUserConstant.PWDUPD_IS_SUCCESS, "1");
		// message.put(IUserConstant.PWDUPD_MESSAGE,
		// IUserConstant.PWDUPD_SUCCESS_MSG);
		// } catch (EcpBaseException e) {
		// message.put(IUserConstant.PWDUPD_IS_SUCCESS, "0");
		// message.put(IUserConstant.PWDUPD_MESSAGE, e.getMessage());
		// }
		return message;
	}

	/**
	 * 忘记密码
	 * 
	 * @param userName
	 * @return
	 * @throws EcpBaseException
	 * @author:sunshine
	 * @date:2012-5-2 下午03:16:33
	 */
	public String forgetPwd(String userName) throws PubBaseException {
		// return ECPServiceFactory.getUserMaintainService().forgetPassword(
		// userName, null);
		return userName;
	}
}
