package com.yonyou.nc.codevalidator.rule.annotation;

/**
 * ����Ӧ�÷�Χ�� ALL("��������"), EB("��������"), EP("���Ӳɹ�"), SCM("��Ӧ��"), FI("����"), AM("�ʲ�"),
 * TM("�ʽ�"), MM("��������"), PM("��������"), HR("HR"), IM("��ҵ����")
 * 
 * @author luoweid
 * 
 */
public enum ScopeEnum {

	ALL("��������"), EB("��������"), EP("���Ӳɹ�"), SCM("��Ӧ��"), FI("����"), AM("�ʲ�"), TM("�ʽ�"), MM("��������"), PM("��������"), HR("HR"), IM(
			"��ҵ����");

	private String name;

	private ScopeEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}

}
