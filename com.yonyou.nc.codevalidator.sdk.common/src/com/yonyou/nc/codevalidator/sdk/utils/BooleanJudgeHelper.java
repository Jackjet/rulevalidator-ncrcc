package com.yonyou.nc.codevalidator.sdk.utils;

/**
 * ���ڲ���ֵ�жϵİ��������࣬��������������ֵ�жϴ���ɶ��Ա�������
 * <p>
 * ע�⣺���ܴ�����&&ʱ�׳��Ŀ�ָ�����⣬���磺
 * ��if(a!=null && a.property())������ֿ�ָ�룬�������еķ���andExpressions���׳���ָ��
 * @author mazhqa
 * @since V2.1
 */
public final class BooleanJudgeHelper {
	
	private BooleanJudgeHelper() {
		
	}
	
	/**
	 * �����е�������ʽȡ&&����
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
	 * �����е�������ʽȡ||����
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
