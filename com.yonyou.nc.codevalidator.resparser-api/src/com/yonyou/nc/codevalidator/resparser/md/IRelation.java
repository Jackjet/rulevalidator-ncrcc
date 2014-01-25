package com.yonyou.nc.codevalidator.resparser.md;

/**
 * Ŀǰֻ����Relation�����������͵���ϵ��
 * ֮��ɿ��Ǽ�������Connection
 * @author zhangwch1
 *
 */
public interface IRelation {
	
	/**
	 * ��ȡ������Դ
	 * @return
	 */
	IEntity getTarget();
	
	/**
	 * ��ȡ������Ŀ��
	 * @return
	 */
	IEntity getSource();
	
	/**
	 * ��ȡ����Դ������
	 * @return
	 */
	IAttribute getSrcAttribute();
}
