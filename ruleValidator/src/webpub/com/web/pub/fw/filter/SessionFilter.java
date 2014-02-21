package com.web.pub.fw.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.web.server.SingleOnline;

public class SessionFilter implements Filter {
	private static String[] suffixs = new String[] { ".tpl", ".css", ".js",
			".jpg", ".gif", ".html", ".png", ".cur", ".cab" };
	private static String[] urls = new String[] { "/account/login.json",
			"/account/logout.json", "/esfw/pub/randomid.json",
			"/account/forgetpwd.json", "/ecppub/params.json",
			"/esfw/management/uiporpertiesquery.json", "/src/pub/children.json" };

	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain fc)
			throws IOException, ServletException {

		// 绑定本次请求
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		// request.getContextPath();
		// 静态文件不过滤
		String context = request.getServletPath().toLowerCase();
		final boolean isSingleLogin = Boolean.valueOf(System
				.getProperty("isSingleLogin"));
		if (this.isStaticFile(context) || this.isNoProtected(context)) {
			fc.doFilter(request, response);
		} else if (request.getSession().getAttribute(
				IHomeConstant.SESSION_ATTR_KEY_USER) == null) {
			// 如果是ajax请求，则返回nologin
			if (request.getHeader("x-requested-with") != null
					&& request.getHeader("x-requested-with").equalsIgnoreCase(
							"XMLHttpRequest")) {
				PrintWriter printWriter = response.getWriter();
				printWriter.print("gotoLogin");
				printWriter.flush();
				printWriter.close();
			} else {
				response.sendRedirect(request.getContextPath() + "/login.html");
			}
			return;
		} else if (isSingleLogin) {
			if (!SingleOnline.isSessionValid(request.getSession().getId())) {
				if (request.getHeader("x-requested-with") != null
						&& request.getHeader("x-requested-with")
								.equalsIgnoreCase("XMLHttpRequest")) {

					String result = "kickout";
					PrintWriter printWriter = response.getWriter();
					printWriter.print(result);
					printWriter.flush();
					printWriter.close();
				} else {
					response.sendRedirect(request.getContextPath()
							+ "/login.html");
				}
			} else {
				fc.doFilter(request, response);
			}
		} else {
			fc.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {

	}

	private boolean isStaticFile(String context) {
		for (String suffix : suffixs) {
			if (context.endsWith(suffix)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是不需要安全认证的url
	 * 
	 * @param context
	 * @return
	 * @author:sunshine
	 * @date:2014-2-11 下午1:41:53
	 */
	private boolean isNoProtected(String context) {
		for (String url : urls) {
			if (url.equals(context)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void destroy() {

	}
}
