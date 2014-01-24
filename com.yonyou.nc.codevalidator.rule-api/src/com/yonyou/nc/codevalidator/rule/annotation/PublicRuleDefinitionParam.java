package com.yonyou.nc.codevalidator.rule.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ���򹫹���������
 *
 * @author luoweid
 * @since V1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PublicRuleDefinitionParam {

	/**
	 * ���õĹ���������Ϣ�����ڴ����ö�����������飩
	 * <p>
	 * �����ò����ı༭���ͣ��ö��ŷָ��������δ���ã�Ĭ��Ϊ�ַ����ı��༭��
	 * <p>
	 * ʾ�����ɽ����ò��� ���ƣ���ͬʱ���ò������ƺͲ����༭������
	 * <li>paramName</li>
	 * <li>paramName=editorType</li>
	 * @return
	 */
	String[] params();
	
}
