package com.yonyou.nc.codevalidator.export.api;

import java.io.File;
import java.util.Date;

import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.ExecutorContextHelperFactory;
import com.yonyou.nc.codevalidator.rule.GlobalExecuteUnit;
import com.yonyou.nc.codevalidator.rule.RulePersistenceConstants;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 抽象的规则持久化定义，一般的规则持久化操作都从此类中继承
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
	 * 根据业务组件得到其对应的结果输出目录，如果是全局执行结果，输出至对应的全局配置文件夹，如果是具体业务组件，输出至业务组件的rulecase文件夹下
	 * 
	 * @param businessComponent
	 * @return
	 * @throws RuleBaseException - 当全局执行结果并未找到对应的全局配置文件
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
	 * 得到文件的扩展名称
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
