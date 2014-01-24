package com.yonyou.nc.codevalidator.export.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.yonyou.nc.codevalidator.export.IRuleExportStrategy;
import com.yonyou.nc.codevalidator.export.api.IPortionRulePersistence;
import com.yonyou.nc.codevalidator.export.api.ITotalRuleExportStrategy;
import com.yonyou.nc.codevalidator.export.api.RuleExportContext;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.ExecutorContextHelperFactory;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.SessionRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.SystemRuntimeContext;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.log.Logger;
import com.yonyou.nc.codevalidator.sdk.utils.StringUtils;

/**
 * 规则导出策略的具体实现类
 * 
 * @author mazhqa
 * @since V2.6
 */
public class RuleExportStrategyImpl implements IRuleExportStrategy {

	private Map<String, IPortionRulePersistence> portionRulePersistenceMap = new HashMap<String, IPortionRulePersistence>();
	private Map<String, ITotalRuleExportStrategy> totalRulePersistenceMap = new HashMap<String, ITotalRuleExportStrategy>();

	public void addPortionRulePersistence(IPortionRulePersistence portionRulePersistence) {
		String identifier = portionRulePersistence.getIdentifier();
		if (portionRulePersistenceMap.containsKey(identifier)) {
			portionRulePersistenceMap.remove(identifier);
		}
		portionRulePersistenceMap.put(identifier, portionRulePersistence);
	}
	
	public void removePortionRulePersistence(IPortionRulePersistence portionRulePersistence) {
		String identifier = portionRulePersistence.getIdentifier();
		if(portionRulePersistenceMap.containsKey(identifier)) {
			portionRulePersistenceMap.remove(identifier);
		}
	}
	
	public void addTotalRulePersistence(ITotalRuleExportStrategy totalRulePersistence) {
		String identifier = totalRulePersistence.getIdentifier();
		if (totalRulePersistenceMap.containsKey(identifier)) {
			totalRulePersistenceMap.remove(identifier);
		}
		totalRulePersistenceMap.put(identifier, totalRulePersistence);
	}
	
	public void removeTotalRulePersistence(ITotalRuleExportStrategy totalRulePersistence) {
		String identifier = totalRulePersistence.getIdentifier();
		if(totalRulePersistenceMap.containsKey(identifier)) {
			totalRulePersistenceMap.remove(identifier);
		}
	}

	@Override
	public void batchExportResult(BusinessComponent businessComponent, List<IRuleExecuteResult> ruleExecuteResultList, RuleExportContext context)
			throws RuleBaseException {
		if (portionRulePersistenceMap.isEmpty()) {
			throw new RuleBaseException("没有配置任何导出策略，当前无法进行结果文件的导出工作...");
		}
		SystemRuntimeContext systemRuntimeContext = ExecutorContextHelperFactory.getExecutorContextHelper()
				.getCurrentRuntimeContext().getSystemRuntimeContext();
		String currentTime = ExecutorContextHelperFactory.getExecutorContextHelper().getCurrentRuntimeContext()
				.getTaskExecuteId();
		String globalExportFilePath = systemRuntimeContext.getGlobalExportFilePath();

		for (IPortionRulePersistence rulePersistence : portionRulePersistenceMap.values()) {
			String filePath = rulePersistence.batchExportResult(businessComponent, ruleExecuteResultList,context);
			String resultFolderName = rulePersistence.getResultFolderName();

			if (rulePersistence.needExportFolder() && StringUtils.isNotBlank(globalExportFilePath)) {
				File resultFolder = new File(String.format("%s/%s/%s", globalExportFilePath, currentTime,
						resultFolderName));
				try {
					FileUtils.forceMkdir(resultFolder);
					String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
					File resultFile = new File(resultFolder, String.format("%s_%s",
							businessComponent.getDisplayBusiCompName(), fileName));
					boolean createNewFile = resultFile.createNewFile();
					if (createNewFile) {
						FileUtils.copyFile(new File(filePath), resultFile);
					}
				} catch (IOException e) {
					Logger.error(String.format("在整合文件，进行复制操作时失败...详情：%s", resultFolder.getAbsolutePath()), e);
					continue;
				}
			}
		}
	}

	@Override
	public void resultFolderInitialize(File resultFolder) throws RuleBaseException {
		for (IPortionRulePersistence portionRulePersistence : portionRulePersistenceMap.values()) {
			portionRulePersistence.resultFolderInitialize(resultFolder);
		}
	}

	@Override
	public void totalResultExport(SessionRuleExecuteResult sessionRuleExecuteResult) throws RuleBaseException {
		Logger.info("本次执行结果保存开始...");
		if(totalRulePersistenceMap.isEmpty()) {
			Logger.info("未发现执行结果全局导出策略，跳过此阶段...");
		} else {
			for (ITotalRuleExportStrategy totalRuleExportStrategy : totalRulePersistenceMap.values()) {
				totalRuleExportStrategy.totalResultExport(sessionRuleExecuteResult);
			}
		}
	}

}
