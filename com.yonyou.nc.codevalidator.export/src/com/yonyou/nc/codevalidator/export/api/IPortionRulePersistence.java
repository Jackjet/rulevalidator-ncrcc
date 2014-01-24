package com.yonyou.nc.codevalidator.export.api;

import java.io.File;
import java.util.List;

import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.IIdentifier;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * ������ʽ���й�������ĳ־û��ӿڣ���ÿ�������ִ�е�Ԫ�ͽ���������;
 * <P>
 * �˽ӿڲ���ֱ��ʹ�ã������ڸ�������ʵ��
 * 
 * @author mazhqa
 * @since V2.7
 */
public interface IPortionRulePersistence extends IIdentifier {

	/**
	 * ��������ִ�н�����ɣ����б������������ĸ�ʽ����
	 * 
	 * @param businessComponent
	 *            - ��ǰִ�е�Ԫ
	 * @param ruleResultList
	 *            - ִ�е�Ԫ�ϵĹ������б�
	 * @return ����ļ�Ŀ¼��Ӧ���ļ��� - �����IRuleFolderPersistenceʵ�ֵĻ���������ǣ�����null������DAO)
	 * @throws RuleBaseException
	 */
	String batchExportResult(BusinessComponent businessComponent,
			List<IRuleExecuteResult> ruleResultList, RuleExportContext context)
			throws RuleBaseException;

	/**
	 * ����ļ��г�ʼ������
	 * 
	 * @param resultFolder
	 * @throws RuleBaseException
	 */
	void resultFolderInitialize(File resultFolder) throws RuleBaseException;

	/**
	 * �洢���������ļ�������
	 * 
	 * @return
	 */
	String getResultFolderName();

	/**
	 * �Ƿ���Ҫ������ļ���
	 * @return
	 */
	boolean needExportFolder();
}
