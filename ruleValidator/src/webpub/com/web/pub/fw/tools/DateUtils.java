package com.web.pub.fw.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import com.web.pub.fw.exception.PubBaseException;

/**
 * 日期工具类
 * 
 * @author:sunshine
 * @date:2014-2-11 下午1:42:18
 */
public class DateUtils {

	public static final String PATTERN_19 = "yyyy-MM-dd HH:mm:ss";
	public static final String PATTERN_10 = "yyyy-MM-dd";
	public static final String PATTERN_TIME = "HH:mm:ss";

	/**
	 * 获取当前系统日期的下一周日期（用于订单预计收货日期初始值）
	 * 
	 * @return
	 */
	public static String getNextWeek() {
		Calendar c = Calendar.getInstance();
		c.roll(Calendar.DAY_OF_YEAR, 7);
		Date date = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	/**
	 * 获取当前系统日期
	 * 
	 * @return
	 */
	public static String getToday() {
		Calendar c = Calendar.getInstance();
		Date date = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	/**
	 * 日期比较
	 * 
	 * @param a
	 *            第一个日期
	 * @param b
	 *            第二个日期
	 * @param format
	 *            日期格式（例如：“yyyy-MM-dd”、“yyyy-MM-dd HH:mm:ss”）
	 * @return
	 * @throws EcpBaseException
	 */
	public static int compareTo(String a, String b, String format)
			throws PubBaseException {
		// 入参检验
		Assert.hasText(a, "第一个日期不能为空");
		Assert.hasText(b, "第二个日期不能为空");
		Assert.hasText(format, "日期格式不能为空");

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			Date c = sdf.parse(a);
			Date d = sdf.parse(b);
			return c.compareTo(d);
		} catch (ParseException e) {
			throw new PubBaseException("比较日期时发生错误", e);
		}
	}

	/**
	 * 获取当前日期
	 * 
	 * @return
	 */
	public static String getServerDate(String format) {
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(now);
	}

	/**
	 * string => date
	 * 
	 * @param dateStr
	 * @param patter
	 * @return
	 * @throws EcpBaseException
	 */
	public static Date format(String dateStr, String patter)
			throws PubBaseException {
		if (StringUtils.isNotBlank(dateStr)) {
			SimpleDateFormat sdf = new SimpleDateFormat(patter);
			try {
				return sdf.parse(dateStr);
			} catch (ParseException e) {
				throw new PubBaseException(e);
			}
		} else {
			return null;
		}
	}

	/**
	 * date => string
	 * 
	 * @param date
	 * @param patter
	 * @return
	 */
	public static String format(Date date, String patter) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(patter);
			return sdf.format(date);
		} else {
			return null;
		}
	}

	public static String format2(String date, String patter) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(patter);
			return sdf.format(date);
		} else {
			return null;
		}
	}
}
