package com.yonyou.nc.codevalidator.export.api;

import java.io.File;
import java.util.Date;

import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.ExecutorContextHelperFactory;
import com.yonyou.nc.codevalidator.rule.GlobalExecuteUnit;
import com.yonyou.nc.codevalidator.rule.RulePersistenceConstants;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * ����Ĺ���־û����壬һ��Ĺ���־û��������Ӵ����м̳�
 * 
 * @author mazhqa
 * @since V2.4
 */
public abstract class AbstractPortionRulePersistence implements IPortionRulePersistence {

	@Override
	public final String getIdentifier() {
		return getClass().getName();
	}

	/**
	 * ����ҵ������õ����Ӧ�Ľ�����Ŀ¼�������ȫ��ִ�н�����������Ӧ��ȫ�������ļ��У�����Ǿ���ҵ������������ҵ�������rulecase�ļ�����
	 * 
	 * @param businessComponent
	 * @return
	 * @throws RuleBaseException - ��ȫ��ִ�н����δ�ҵ���Ӧ��ȫ�������ļ�
	 */
	public final String getRuleResultFilePath(BusinessComponent businessComponent) throws RuleBaseException {
		String resultFolder = null;
		if (businessComponent instanceof GlobalExecuteUnit) {
			String globalConfigFilePath = ExecutorContextHelperFactory.getExecutorContextHelper()
					.getCurrentRuntimeContext().getSystemRuntimeContext().getGlobalConfigFilePath();
			resultFolder = new File(globalConfigFilePath).getParentFile().getAbsolutePath();
		} else {
			resultFolder = businessComponent.getBusinessComponentPath();
		}

		String outputFilePath = String.format("%s/%s/%s/result_%4$tF_%4$tH-%4$tM-%4$tS.%5$s", resultFolder,
				RulePersistenceConstants.RULECASE_FOLDER_NAME, getResultFolderName(), new Date(), getFileExtension());
		return outputFilePath;
	}

	/**
	 * �õ��ļ�����չ����
	 * 
	 * @return
	 */
	protected abstract String getFileExtension();

	@Override
	public void resultFolderInitialize(File resultFolder) throws RuleBaseException {

	}
	
	@Override
	public boolean needExportFolder() {
		return true;
	}
	
}
