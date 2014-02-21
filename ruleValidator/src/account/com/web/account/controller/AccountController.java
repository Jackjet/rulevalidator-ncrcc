package com.web.account.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.account.exception.PubNotPasswordException;
import com.web.account.exception.PubNotUserIdException;
import com.web.account.service.IAccountService;
import com.web.account.support.AccountControllerSupport;
import com.web.account.uiview.MessageUIView;
import com.web.account.uiview.UserUIView;
import com.web.pub.exception.business.PubBusinessException;
import com.web.pub.fw.controller.BaseController;
import com.web.pub.fw.exception.PubBaseException;
import com.web.pub.fw.factory.UserContext;
import com.web.pub.fw.filter.FwConstants;
import com.web.pub.fw.filter.IHomeConstant;
import com.web.pub.fw.tools.DESCrypt;
import com.web.pub.fw.tools.InvocationInfoProxy;
import com.web.pub.fw.uiview.GridModel;
import com.web.pub.fw.uiview.JsonRetVO;
import com.web.pub.page.paravo.UserQryParaVO;
import com.web.server.SingleOnline;

/**
 * 账户控制器
 * 
 * @author lixln
 */
@Controller
@RequestMapping("account")
public class AccountController extends BaseController {
	/**
	 * 日志
	 */
	private static final Log logger = LogFactory
			.getLog(AccountController.class);
	public static final String SHOPCART_CACHE = "ShopCartCache";

	/**
	 * 查询数据支持类
	 */
	@Resource
	private AccountControllerSupport support;

	@Resource
	private IAccountService accountService;

	/**
	 * 用户登录
	 * 
	 * @param request
	 * @param view
	 * @return
	 * @throws EcpBaseException
	 */
	@RequestMapping(value = "login.json", method = RequestMethod.POST)
	public @ResponseBody
	Object login(@ModelAttribute UserUIView view, HttpSession session)
			throws PubBaseException {

		// 获取登录信息
		String username = view.getUserName();
		String pwd = view.getPwd();
		// String verificationCode = view.getVerificationCode();

		// 用户名校验
		if (StringUtils.isBlank(username)) {
			throw new PubNotUserIdException();
		}

		// 密码校验
		if (StringUtils.isBlank(pwd)) {
			throw new PubNotPasswordException();
		}
		// 提取密码，对于已经加密的密码进行解密
		String text = view.getPwd();
		if (DESCrypt.isEncode(text)) {
			pwd = DESCrypt.decrypt(text);
			view.setPwd(pwd);
		}

		String randomId = null;
		// 验证码校验
		// 不能忽略验证码
		// if (StringUtils.isBlank(verificationCode)) {
		// throw new PubNotVerifyCodeException();
		// } else {
		Object obj = session.getAttribute(FwConstants.RANDOMCODEID);
		if (obj != null) {
			randomId = (String) obj;
		}
		// if (!randomId.equalsIgnoreCase(verificationCode)) {
		// throw new PubIllegalVerifyCodeException();
		// }
		// }
		// }
		UserUIView userView = new UserUIView();
		userView.setPkUser(null);
		userView.setUserName(username);
		userView.setUserCode(username);
		// userView.setCuserid(null);
		// userView.setUser_code(username);
		// userView.setUser_name(username);
		userView.setUserCode(username);
		userView.setPwd(pwd);
		// 用户名称时administrator，密码是root+验证码，则认为是管理员
		String adminPwd = "root" + randomId;
		boolean permitLogin = false;
		if ("administrator".equals(username) && adminPwd.equals(pwd)) {
			permitLogin = true;
			userView.setUserName("系统管理员");
		} else {
			UserUIView uiView = accountService.loginUserView(userView);
			if (uiView != null) {
				permitLogin = true;
				userView.setPkUser(uiView.getPkUser());
			} else {
				permitLogin = false;
			}
		}
		Map<String, Object> returnMsg = new HashMap<String, Object>();
		if (permitLogin) {
			// 单点登录处理
			// 从系统参数中获取是否开启单点登录功能
			final boolean isSingleLogin = Boolean.valueOf(System
					.getProperty("isSingleLogin"));
			// 处理强制登陆问题
			if (isSingleLogin && SingleOnline.isOnline(username)) {
				String loginType = view.getLogin_type();
				if (!SingleOnline.ENFORCE.equalsIgnoreCase(loginType)) {
					Map<String, String> message = new HashMap<String, String>();
					message.put("isOnline", "true");
					return message;
				}
			}

			// 单点登录处理
			SingleOnline.addUser(username, session.getId());

			// 留痕
			// 用户信息
			session.setAttribute(IHomeConstant.SESSION_ATTR_KEY_USER, userView);
			session.setAttribute(IHomeConstant.SESSION_ATTR_KEY_WEB_SESSION,
					session);

			// 回写的密码加密，在客户端cookie中保存密文
			String ciphertext = DESCrypt.encrypt(pwd);
			returnMsg.put("ciphertext", ciphertext);
			// 回写
			UserContext context = new UserContext();
			// 制单员等
			context.setUserId(userView.getPkUser());
			context.setUserCode(userView.getUserCode());
			context.setUserName(userView.getUserName());
			// 集团
			returnMsg.put("usercontext", context);
			session.setAttribute("context", context);
			InvocationInfoProxy.getInstance().setInvocationContext(context);
		} else {
			returnMsg.put("message", "用户名或密码错误");
		}
		return returnMsg;
	}

	/**
	 * 用户注销
	 * 
	 * @param request
	 * 
	 * @return
	 * @throws EcpBaseException
	 */
	@RequestMapping(value = "logout.json", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, String> logout(HttpSession session) throws PubBaseException {

		// 从会话中获取用户信息和NC会话信息
		UserUIView userView = (UserUIView) session
				.getAttribute(IHomeConstant.SESSION_ATTR_KEY_USER);
		// Session webSession = (Session) session
		// .getAttribute(IHomeConstant.SESSION_ATTR_KEY_WEB_SESSION);

		// 单点登录处理
		final boolean isSingleLogin = Boolean.valueOf(System
				.getProperty("isSingleLogin"));
		if (isSingleLogin) {
			if (userView != null) {
				SingleOnline.removeUser(userView.getUserCode());
				// SingleOnline.removeUser(userView.getUser_code());
			}
		}

		// 注销
		// final boolean isRegister = EcpLoadConfigProperty.isRegister();
		// IUserMaintainService userMaintainService = ECPServiceFactory
		// .getUserMaintainService();
		// userMaintainService.logout(session, isRegister);

		// 异常处理
		// try {
		// webSession.invalidate();
		// } catch (Exception e) {
		// logger.error("session invalid", e);
		// }

		Map<String, String> map = new HashMap<String, String>();
		map.put("status", "success");
		return map;
	}

	/**
	 * 获取登录用户账户详单
	 * 
	 * @param queryParam
	 *            查询参数
	 * @return 账户
	 * @throws EcpBaseException
	 */
	@RequestMapping(value = "updatePwd.json", method = RequestMethod.POST)
	public @ResponseBody
	JsonRetVO updateUserPassword(HttpServletRequest request)
			throws PubBaseException {
		// 从会话中获取当前登录用户
		UserContext context = (UserContext) request.getSession().getAttribute(
				"context");
		final String userId = context.getUserId();
		// 旧密码
		final String oldPassword = request.getParameter("oldPassword");
		// 新密码
		final String newPassword = request.getParameter("newPassword");
		// 执行更新
		JsonRetVO jsonRetVO = new JsonRetVO();
		Boolean flag = true;
		try {
			flag = accountService.updatePassword(userId, oldPassword,
					newPassword);
			jsonRetVO.setIsSuccess(flag);
			jsonRetVO.setMessage("密码修改成功");
		} catch (Exception e) {
			jsonRetVO.setIsSuccess(flag);
			jsonRetVO.setMessage(e.getMessage());
		}
		return jsonRetVO;
	}

	/**
	 * 忘记密码
	 * 
	 * @param request
	 * 
	 * @param view
	 * @return
	 * @author:sunshine
	 * @date:2012-5-2 下午03:13:45
	 */
	@RequestMapping(value = "forgetPwd.json", method = RequestMethod.POST)
	public @ResponseBody
	MessageUIView forgetPwd(@ModelAttribute UserUIView view) {
		MessageUIView msgUIView = new MessageUIView();
		if (view == null) {
			msgUIView.setMsgFlag("userNameNull");
		} else {
			msgUIView = new MessageUIView();
			String userName = view.getUserName();
			// String userName = view.getUser_name();
			try {
				String msg = support.forgetPwd(userName);
				msgUIView.setMsgFlag(msg);
			} catch (PubBaseException e) {
				msgUIView.setMsgFlag(e.getMessage());
				logger.error(e);
			}
		}
		return msgUIView;
	}

	/**
	 * 获取登录用户账户详单
	 * 
	 * @param queryParam
	 *            查询参数
	 * @return 账户
	 * @throws EcpBaseException
	 */
	@RequestMapping(value = "accountDetail.json", method = RequestMethod.GET)
	public @ResponseBody
	UserUIView getLoginnerAccout(HttpServletRequest request)
			throws PubBaseException {
		// 从会话中获取当前登录用户
		UserUIView userView = (UserUIView) request.getSession().getAttribute(
				"useruiview");
		return support.getAccount(userView);
	}

	/**
	 * 查询所有用户信息
	 * 
	 * @param request
	 * @return
	 * @throws PubBaseException
	 * @author:sunshine
	 * @date:2014-1-23 下午3:29:10
	 */
	@RequestMapping(value = "getUser.json", method = RequestMethod.POST)
	public @ResponseBody
	GridModel getAllUser(@ModelAttribute UserQryParaVO userParaVO,
			HttpServletRequest request) throws PubBaseException {
		// 从会话中获取当前登录用户
		List<UserUIView> userList = accountService.qryUserView(userParaVO);
		int size = 0;
		if (userList != null) {
			size = userList.size();
		}
		GridModel model = new GridModel(size, userList);
		return model;
	}

	/**
	 * 增加
	 * 
	 * @param request
	 * @return
	 * @throws PubBaseException
	 * @author:sunshine
	 * @date:2014-1-23 下午3:30:29
	 */
	@RequestMapping(value = "addUser.json", method = RequestMethod.POST)
	public @ResponseBody
	UserUIView addUser(@ModelAttribute UserUIView userView,
			HttpServletRequest request) throws PubBaseException {
		try {
			userView = accountService.insertUserView(userView);
		} catch (PubBusinessException e) {
			userView.setMessage("excep");
			logger.error(e);
		}
		return userView;
	}

	/**
	 * 修改
	 * 
	 * @param request
	 * @return
	 * @throws PubBaseException
	 * @author:sunshine
	 * @date:2014-1-23 下午3:30:25
	 */
	@RequestMapping(value = "updateUser.json", method = RequestMethod.POST)
	public @ResponseBody
	UserUIView updateUser(@ModelAttribute UserUIView userView,
			HttpServletRequest request) throws PubBaseException {
		try {
			userView = accountService.updateUserView(userView);
		} catch (PubBusinessException e) {
			userView.setMessage("excep");
			logger.error(e);
			throw e;
		}
		return userView;
	}

	/**
	 * 删除用户
	 * 
	 * @param request
	 * @return
	 * @throws PubBaseException
	 * @author:sunshine
	 * @date:2014-1-23 下午3:30:20
	 */
	@RequestMapping(value = "delUser.json", method = RequestMethod.POST)
	public @ResponseBody
	UserUIView delUser(HttpServletRequest request,
			@ModelAttribute UserUIView userView) throws PubBaseException {
		return accountService.delUserView(userView);
	}
}
