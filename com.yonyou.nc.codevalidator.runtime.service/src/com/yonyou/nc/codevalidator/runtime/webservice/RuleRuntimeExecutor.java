package com.yonyou.nc.codevalidator.runtime.webservice;

import org.osgi.framework.ServiceReference;

import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.ExecutorContextHelperFactory;
import com.yonyou.nc.codevalidator.rule.SessionRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseRuntimeException;
import com.yonyou.nc.codevalidator.rule.executor.IRuleValidatorExecutor;
import com.yonyou.nc.codevalidator.rule.executor.SessionExecResultManager;
import com.yonyou.nc.codevalidator.runtime.IRuleRuntimeExecutor;
import com.yonyou.nc.codevalidator.runtime.ServerRuntimeContext;
import com.yonyou.nc.codevalidator.sdk.log.Logger;
import com.yonyou.nc.codevalidator.sdk.project.ProjectAnalyseUtils;

/**
 * Server端执行规则检查主类， 在本bundle中会将此服务发布成web service
 * 
 * @author mazhqa
 * @since V2.0
 */
public class RuleRuntimeExecutor implements IRuleRuntimeExecutor {

	// @Override
	// public void execute(String ncHome, String codePath, String
	// dataSourceName) {
	// BusinessComponent businessComponent =
	// ProjectAnalyseUtils.getBusinessComponents(srcFolder);
	// ServerRuntimeContext runtimeContext = new ServerRuntimeContext(ncHome,
	// dataSourceName, businessComponent,
	// codePath);
	// execute(runtimeContext);
	// }

	@Override
	public void execute(String ncHome, String codePath, String dataSourceName, String executePerid, String productCode) {
		BusinessComponent businessComponent = ProjectAnalyseUtils.getBusinessComponents(codePath, productCode);
		ServerRuntimeContext runtimeContext = new ServerRuntimeContext(ncHome, dataSourceName, businessComponent,
				codePath, executePerid);
		execute(runtimeContext);
	}

	public SessionRuleExecuteResult execute(ServerRuntimeContext runtimeContext) {
		Logger.info(String.format("开始执行验证..., 当前执行验证的阶段是:%s ", runtimeContext.getSystemRuntimeContext()
				.getExecutePeriod().getName()));
		try {
			ExecutorContextHelperFactory.getExecutorContextHelper().startRuleExecutor(runtimeContext);
			ServiceReference<IRuleValidatorExecutor> serviceReference = Activator.getContext().getServiceReference(
					IRuleValidatorExecutor.class);
			IRuleValidatorExecutor ruleValidatorExecutor = Activator.getContext().getService(serviceReference);
			SessionRuleExecuteResult sessionExecResult = ruleValidatorExecutor.executeValidator(runtimeContext, null);
			SessionExecResultManager.getInstance().addResult(sessionExecResult);
			Logger.info("提示：执行完成!");
			return sessionExecResult;
		} catch (RuleBaseException e) {
			Logger.error(e.getMessage(), e);
			throw new RuleBaseRuntimeException(e);
		} finally {
			try {
				ExecutorContextHelperFactory.getExecutorContextHelper().endRuleExecutor();
			} catch (RuleBaseException e) {
				Logger.error(e.getMessage(), e);
			}
		}
	}

}
