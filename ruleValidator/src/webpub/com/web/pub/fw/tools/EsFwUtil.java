package com.web.pub.fw.tools;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 框架工具类
 * 
 * @author zhenghb
 * @date 2012-3-26 下午01:28:44
 */
@RequestMapping("file")
public class EsFwUtil {

	/**
	 * 日志
	 */
	private static final Log logger = LogFactory.getLog(EsFwUtil.class);
	private static final String FIEL_SEPARATOR = File.separator;

	// 生成随机数时的数据源
	private static char[] codeSequence = { 'a', 'b', 'c', 'd', 'e', 'f', 'g',
			'h', 'i', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 't', 'u', 'v', 'w',
			'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	/**
	 * ajax返回值
	 * 
	 * @param response
	 * @param content
	 */
	public static void render(HttpServletResponse response, String content) {
		try {
			response.setContentType("text/html;charset=UTF-8 ");
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.getWriter().write(content);
			response.getWriter().flush();
		} catch (IOException e) {
			logger.error(e);
		}
	}

	/**
	 * 判断字符串是否为null或者空字符串，如果是那么也返回空字符串
	 * 
	 * @param string
	 * @return
	 */
	public static String ifNull(Object string) {
		String value = "";
		if (string != null) {
			value = string.toString();
		}
		return value;
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(String value) {
		return value == null || value.trim().equals("");
	}

	/**
	 * 判断List是否为空
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isListEmpty(List<?> value) {
		return value == null || value.isEmpty() || value.size() == 0;
	}

	/**
	 * 获取当前的时间信息。（包括日期和时间）
	 */
	public static Date getCurrentDate() {
		Calendar calendar = Calendar.getInstance();
		return calendar.getTime();
	}

	/**
	 * 将字符串转成DATE
	 * 
	 * @param datestr
	 *            日期字符串
	 * @param pattern
	 *            格式
	 * @return
	 */
	public static Date parseDate(String datestr, String pattern) {
		DateFormat formatter = new SimpleDateFormat(pattern);
		try {
			return formatter.parse(datestr);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 生成指定长度的随机数，在生成验证码时使用
	 * 
	 * @param length
	 * @return
	 * @author:sunshine
	 * @date:2012-4-12 上午10:02:17
	 */
	public static String genNumPassword(int length) {
		// 创建一个随机数生成器类
		Random random = new Random();
		// randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
		StringBuffer randomCode = new StringBuffer();
		for (int i = 0; i < length; i++) {
			// 得到随机产生的验证码数字。
			String strRand = String.valueOf(codeSequence[random.nextInt(32)]);
			randomCode.append(strRand);
		}
		return randomCode.toString();
	}

	/**
	 * 获取sql文件路径
	 * 
	 * @return
	 * @author:sunshine
	 * @date:2014-1-20 下午4:08:17
	 */
	public static String getInitSqlFilePath(String fileName) {
		StringBuffer initSqlFilePath = new StringBuffer();
		String rootPath = System.getProperty("webapp.root");
		initSqlFilePath.append(rootPath);
		initSqlFilePath.append(FIEL_SEPARATOR).append("WEB-INF");
		initSqlFilePath.append(FIEL_SEPARATOR).append("config");
		initSqlFilePath.append(FIEL_SEPARATOR).append(fileName);
		return initSqlFilePath.toString();
	}
}
