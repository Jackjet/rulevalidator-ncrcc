package com.yonyou.nc.codevalidator.runtime.plugin;

import java.util.Date;

import com.yonyou.nc.codevalidator.resparser.datasource.IDataSourceService;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.RuntimeContext;
import com.yonyou.nc.codevalidator.rule.SystemRuntimeContext;

/**
 * 插件中使用的运行上下文
 * @author mazhqa
 * @since V1.0
 */
public class PluginRuntimeContext implements RuntimeContext {

	private String ncHome;
	/**
	 * 默认数据源为design
	 */
	private String dataSource = IDataSourceService.DEFAULT_DATASOURCE_NAME;
	/**
	 * MDE Project name.
	 */
	private String mdeProjectName;
	private final BusinessComponent topBusinessComponent;
	private BusinessComponent currentBusinessComponent;
	
	private String taskExecuteId;
	
	private final String projectName;
	
	/**
	 * 系统运行的上下文
	 */
	private final SystemRuntimeContext systemRuntimeContext;

	public PluginRuntimeContext(BusinessComponent topBusinessComponent, SystemRuntimeContext systemRuntimeContext, String projectName) {
		super();
		this.topBusinessComponent = topBusinessComponent;
		this.systemRuntimeContext = systemRuntimeContext;
		this.projectName = projectName;
	}

	public void setNcHome(String ncHome) {
		this.ncHome = ncHome;
	}

	public String getNcHome() {
		return ncHome;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getDataSource() {
		return dataSource;
	}

	public boolean isRunInPlug() {
		return true;
	}

	public BusinessComponent getBusinessComponents() {
		return topBusinessComponent;
	}

	public String getMdeProjectName() {
		return mdeProjectName;
	}

	public void setMdeProjectName(String mdeProjectName) {
		this.mdeProjectName = mdeProjectName;
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
		return systemRuntimeContext;
	}

	@Override
	public String getTaskExecuteId() {
		if(taskExecuteId == null) {
			taskExecuteId = String.format("%1$tF_%1$tH-%1$tM-%1$tS", new Date());
		}
		return taskExecuteId;
	}

	@Override
	public String getProjectName() {
		return projectName;
	}

}
