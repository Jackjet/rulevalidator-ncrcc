package com.yonyou.nc.codevalidator.runtime.plugin.actions;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.osgi.framework.ServiceReference;

import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.ExecutorContextHelperFactory;
import com.yonyou.nc.codevalidator.rule.ICompositeExecuteUnit;
import com.yonyou.nc.codevalidator.rule.RuntimeContext;
import com.yonyou.nc.codevalidator.rule.SessionRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.rule.executor.IRuleValidatorExecutor;
import com.yonyou.nc.codevalidator.rule.executor.IValidatorListener;
import com.yonyou.nc.codevalidator.rule.executor.SessionExecResultManager;
import com.yonyou.nc.codevalidator.rule.executor.ValidatorEvent;
import com.yonyou.nc.codevalidator.runtime.plugin.PluginRuntimeContext;
import com.yonyou.nc.codevalidator.sdk.common.Activator;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

/**
 * 规则执行的eclipse任务Job，以异步的方式执行检查工作
 * 
 * @author mazhqa
 * @since V1.0
 */
public class RuleExecutorRuntimeJob extends Job {

	private RuntimeContext runtimeContext;

	public RuleExecutorRuntimeJob(PluginRuntimeContext pluginRuntimeContext) {
		super("规则验证任务");
		this.runtimeContext = pluginRuntimeContext;
	}

	private IRuleValidatorExecutor getRuleValidatorExecutor() {
		ServiceReference<IRuleValidatorExecutor> serviceReference = Activator.getBundleContext().getServiceReference(
				IRuleValidatorExecutor.class);
		return Activator.getBundleContext().getService(serviceReference);
	}

	@Override
	protected IStatus run(final IProgressMonitor monitor) {
		try {
			ExecutorContextHelperFactory.getExecutorContextHelper().startRuleExecutor(runtimeContext);
			Logger.info("开始执行验证...");
			int totalWork = getTotalWorkCount() + 1;
			monitor.beginTask("开始执行验证...", totalWork);
			IRuleValidatorExecutor executor = getRuleValidatorExecutor();
			if (executor == null) {
				Logger.error(String.format("执行规则的服务[类型:%s]未发现!", IRuleValidatorExecutor.class.getName()));
				return Status.CANCEL_STATUS;
			}
			IValidatorListener iValidatorListener = new IValidatorListener() {

				@Override
				public void notifyRuleEvent(BusinessComponent businessComponent, String ruleIdentifier,
						ValidatorEvent event) {
					monitor.setTaskName(String.format("执行单元：%s 规则：%s %s", businessComponent.getDisplayBusiCompName(),
							ruleIdentifier, event.getText()));
				}

				@Override
				public void notifyBusiCompEvent(BusinessComponent businessComponent, ValidatorEvent event) {
					monitor.setTaskName(String.format("执行单元：%s 所有规则  %s", businessComponent.getDisplayBusiCompName(),
							event.getText()));
					if (event == ValidatorEvent.END) {
						monitor.worked(1);
					}
				}

				@Override
				public boolean requireCancel() {
					return monitor.isCanceled();
				}

				@Override
				public void executeCancelOperation() {
					RuleExecutorRuntimeJob.this.cancel();
				}
			};
			SessionRuleExecuteResult sessionExecResult = executor.executeValidator(runtimeContext, iValidatorListener);
			SessionExecResultManager.getInstance().addResult(sessionExecResult);
			Logger.info("提示：执行完成!");
		} catch (Exception e) {
			Logger.error(String.format("执行出错:%s", e.getMessage()), e);
		} finally {
			monitor.done();
			try {
				ExecutorContextHelperFactory.getExecutorContextHelper().endRuleExecutor();
			} catch (RuleBaseException e) {
				Logger.error(e.getMessage(), e);
			}
		}
		return Status.OK_STATUS;
	}

	private int getTotalWorkCount() {
		BusinessComponent businessComponents = runtimeContext.getBusinessComponents();
		if (businessComponents instanceof ICompositeExecuteUnit) {
			ICompositeExecuteUnit moduleExecuteUnit = (ICompositeExecuteUnit) businessComponents;
			return moduleExecuteUnit.getSubBusinessComponentList().size() + 2;
		}
		// 默认为2，因为要有全局执行任务，和该执行任务
		return 2;
	}

}
