package com.yonyou.nc.codevalidator.runtime;

import java.util.Date;

import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.RuleConstants;
import com.yonyou.nc.codevalidator.rule.RuntimeContext;
import com.yonyou.nc.codevalidator.rule.SystemRuntimeContext;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.sdk.utils.StringUtils;

/**
 * 服务端运行时上下文
 * 
 * @author mazhqa
 * @since V1.0
 */
public class ServerRuntimeContext implements RuntimeContext {

	private final String ncHome;
	private final String dataSourceName;
	private final BusinessComponent businessComponent;
	private BusinessComponent currentBusinessComponent;
	private String taskExecuteId;
	private SystemRuntimeContext systemRuntimeContext;
	
	private final String executePeriod;

	/**
	 * 工程名称
	 */
	private final String projectName;

	public ServerRuntimeContext(String ncHome, String dataSourceName, BusinessComponent businessComponent,
			String projectName, String executePeriod) {
		this.ncHome = ncHome;
		this.dataSourceName = dataSourceName;
		this.businessComponent = businessComponent;
		this.projectName = projectName;
		this.executePeriod = executePeriod;
	}

	@Override
	public boolean isRunInPlug() {
		return false;
	}

	@Override
	public String getNcHome() {
		return ncHome;
	}

	@Override
	public String getDataSource() {
		return dataSourceName;
	}

	@Override
	public BusinessComponent getBusinessComponents() {
		return businessComponent;
	}

	@Override
	public BusinessComponent getCurrentExecuteBusinessComponent() {
		return currentBusinessComponent;
	}

	@Override
	public void setCurrentExecuteBusinessComponent(BusinessComponent businessComponent) {
		this.currentBusinessComponent = businessComponent;
	}

	@Override
	public SystemRuntimeContext getSystemRuntimeContext() {
		if (systemRuntimeContext == null) {
			String currentServerFolder = System.getProperty(RuleConstants.CURRENT_SERVER_FOLDER) == null ? "" : System
					.getProperty(RuleConstants.CURRENT_SERVER_FOLDER);
			String globalConfigFilePath = currentServerFolder
					+ System.getProperty(RuleConstants.GLOBAL_CONFIG_FILEPATH);
			String globalExportFilePath = currentServerFolder
					+ System.getProperty(RuleConstants.GLOBAL_EXPORT_FILEPATH);
			String globalLogFilePath = currentServerFolder + System.getProperty(RuleConstants.GLOBAL_LOG_FILEPATH);
			String globalLogLevel = System.getProperty(RuleConstants.GLOBAL_LOG_LEVEL);
//			String executePeriodLevel = System.getProperty(RuleConstants.EXECUTE_LEVEL);
			ExecutePeriod executePeriodObject = StringUtils.isBlank(executePeriod) ? ExecutePeriod.DEPLOY
					: ExecutePeriod.getExecutePeriod(executePeriod);
			systemRuntimeContext = new SystemRuntimeContext(globalConfigFilePath, globalExportFilePath,
					globalLogFilePath, globalLogLevel, executePeriodObject);
		}
		return systemRuntimeContext;
	}

	@Override
	public String getTaskExecuteId() {
		if (taskExecuteId == null) {
			taskExecuteId = String.format("%1$tF_%1$tH-%1$tM-%1$tS", new Date());
		}
		return taskExecuteId;
	}

	@Override
	public String getProjectName() {
		return projectName;
	}

}
