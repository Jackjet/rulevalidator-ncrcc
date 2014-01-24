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
 * ����ִ�е�eclipse����Job�����첽�ķ�ʽִ�м�鹤��
 * 
 * @author mazhqa
 * @since V1.0
 */
public class RuleExecutorRuntimeJob extends Job {

	private RuntimeContext runtimeContext;

	public RuleExecutorRuntimeJob(PluginRuntimeContext pluginRuntimeContext) {
		super("������֤����");
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
			Logger.info("��ʼִ����֤...");
			int totalWork = getTotalWorkCount() + 1;
			monitor.beginTask("��ʼִ����֤...", totalWork);
			IRuleValidatorExecutor executor = getRuleValidatorExecutor();
			if (executor == null) {
				Logger.error(String.format("ִ�й���ķ���[����:%s]δ����!", IRuleValidatorExecutor.class.getName()));
				return Status.CANCEL_STATUS;
			}
			IValidatorListener iValidatorListener = new IValidatorListener() {

				@Override
				public void notifyRuleEvent(BusinessComponent businessComponent, String ruleIdentifier,
						ValidatorEvent event) {
					monitor.setTaskName(String.format("ִ�е�Ԫ��%s ����%s %s", businessComponent.getDisplayBusiCompName(),
							ruleIdentifier, event.getText()));
				}

				@Override
				public void notifyBusiCompEvent(BusinessComponent businessComponent, ValidatorEvent event) {
					monitor.setTaskName(String.format("ִ�е�Ԫ��%s ���й���  %s", businessComponent.getDisplayBusiCompName(),
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
			Logger.info("��ʾ��ִ�����!");
		} catch (Exception e) {
			Logger.error(String.format("ִ�г���:%s", e.getMessage()), e);
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
		// Ĭ��Ϊ2����ΪҪ��ȫ��ִ�����񣬺͸�ִ������
		return 2;
	}

}
