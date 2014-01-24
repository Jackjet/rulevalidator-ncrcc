package com.yonyou.nc.codevalidator.sdk.utils;

/**
 * 用于布尔值判断的帮助工具类，用于整理多个布尔值判断代码可读性变差的问题
 * <p>
 * 注意：不能处理在&&时抛出的空指针问题，例如：
 * 在if(a!=null && a.property())不会出现空指针，但此类中的方法andExpressions会抛出空指针
 * @author mazhqa
 * @since V2.1
 */
public final class BooleanJudgeHelper {
	
	private BooleanJudgeHelper() {
		
	}
	
	/**
	 * 对所有的输入表达式取&&操作
	 * @param expressions
	 * @return
	 */
	public static boolean andExpressions(boolean... expressions) {
		if(expressions == null) {
			return false;
		}
		for (boolean expression : expressions) {
			if(!expression) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 对所有的输入表达式取||操作
	 * @param expressions
	 * @return
	 */
	public static boolean orExpressions(boolean... expressions) {
		if(expressions == null) {
			return false;
		}
		for (boolean expression : expressions) {
			if(expression) {
				return true;
			}
		}
		return false;
	}

}
