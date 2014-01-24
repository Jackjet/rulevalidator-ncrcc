package com.yonyou.nc.codevalidator.runtime.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yonyou.nc.codevalidator.config.IRuleConfig;
import com.yonyou.nc.codevalidator.export.IRuleExportStrategy;
import com.yonyou.nc.codevalidator.export.api.IRecordExecutePersistence;
import com.yonyou.nc.codevalidator.export.api.RuleExportContext;
import com.yonyou.nc.codevalidator.resparser.executeresult.ErrorRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.executeresult.ExceptionRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.temp.TempTableResourceCreatorUtils;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.ExecutorContextHelperFactory;
import com.yonyou.nc.codevalidator.rule.ICompositeExecuteUnit;
import com.yonyou.nc.codevalidator.rule.IRuleConfigContext;
import com.yonyou.nc.codevalidator.rule.IRuleDefinition;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.RuleExecuteStatus;
import com.yonyou.nc.codevalidator.rule.RuntimeContext;
import com.yonyou.nc.codevalidator.rule.SessionRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.SystemRuntimeContext;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionsReader;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseRuntimeException;
import com.yonyou.nc.codevalidator.rule.executor.IRuleExecutorFactory;
import com.yonyou.nc.codevalidator.rule.executor.IRuleValidatorExecutor;
import com.yonyou.nc.codevalidator.rule.executor.IValidatorListener;
import com.yonyou.nc.codevalidator.rule.executor.ValidatorEvent;
import com.yonyou.nc.codevalidator.rule.impl.DefaultRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.vo.IRuleCheckConfiguration;
import com.yonyou.nc.codevalidator.rule.vo.RuleCheckConfigurationImpl;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

/**
 * ������ִ֤�е�ִ�������
 * 
 * @author mazhqa
 * @since V1.0
 */
public final class RuleValidatorExecutorImpl implements IRuleValidatorExecutor {

	private IRuleExportStrategy ruleExportStrategy;
	private IRuleExecutorFactory ruleExecutorFactory;
	private IRuleConfig ruleConfig;
	private IRecordExecutePersistence recordExecutePersistence;

	public RuleValidatorExecutorImpl() {

	}

	public void setRuleExportStrategy(IRuleExportStrategy ruleExportStrategy) {
		this.ruleExportStrategy = ruleExportStrategy;
	}

	public void removeRuleExportStrategy(IRuleExportStrategy ruleExportStrategy) {
		this.ruleExportStrategy = null;
	}

	public void setRuleExecutorFactory(IRuleExecutorFactory ruleExecutorFactory) {
		this.ruleExecutorFactory = ruleExecutorFactory;
	}

	public void removeRuleExecutorFactory(IRuleExecutorFactory ruleExecutorFactory) {
		this.ruleExecutorFactory = null;
	}

	public void setRuleConfig(IRuleConfig ruleConfig) {
		this.ruleConfig = ruleConfig;
	}

	public void removeRuleConfig(IRuleConfig ruleConfig) {
		this.ruleConfig = null;
	}

	public void setRecordExecutePersistence(IRecordExecutePersistence recordExecutePersistence) {
		this.recordExecutePersistence = recordExecutePersistence;
	}

	public void removeRecordExecutePersistence(IRecordExecutePersistence recordExecutePersistence) {
		this.recordExecutePersistence = null;
	}

	/**
	 * ִ�й�����֤ ִ�еĹ��̣� 1. ȫ�ֹ�����ִ֤�У� 2. ������������ִ�У�
	 * 
	 * @param runtimeContext
	 * @param validatorListener
	 * @return
	 */
	@Override
	public SessionRuleExecuteResult executeValidator(RuntimeContext runtimeContext, IValidatorListener validatorListener) {
		Logger.init();
		SessionRuleExecuteResult result = new SessionRuleExecuteResult();
		RuleExportContext ruleExportContext = new RuleExportContext();
		Date startTime = new Date();
		try {
			result.setStartTime(startTime);
			long startTimeMillis = System.currentTimeMillis();
			BusinessComponent businessComponent = runtimeContext.getBusinessComponents();
			result.setBusinessComponent(businessComponent);
			if (businessComponent instanceof ICompositeExecuteUnit) {
				// mazhqa: һ�������ִ�й�����֤������ģ��Ϊ��λ����ִ�е�
				ICompositeExecuteUnit compositeExecuteUnit = (ICompositeExecuteUnit) businessComponent;
				if (compositeExecuteUnit.getSubBusinessComponentList().size() == 1) {
					result.setBusinessComponent(compositeExecuteUnit.getSubBusinessComponentList().get(0));
				}
			}
			ruleExportContext.setBusinessComponent(businessComponent);
			ruleExportContext.setStartTime(startTime);
			if(recordExecutePersistence != null){
				String recordId = recordExecutePersistence.insertRecord(ruleExportContext);
				ruleExportContext.setRecordID(recordId);
			}
			List<IRuleExecuteResult> executeRulesResults = executeRulesRecursive(runtimeContext, businessComponent,
					new RuleCheckConfigurationImpl(), ruleExportContext, validatorListener);
			result.setRuleExecuteResults(executeRulesResults);
			Logger.info(String.format("���й���ִ����ɣ�����ʱ: %s ����", System.currentTimeMillis() - startTimeMillis));
		} catch (RuleBaseException e) {
			throw new RuleBaseRuntimeException(e);
		} finally {
			try {
				cleanup();
				Date endTime = new Date();
				result.setEndTime(endTime);
				ruleExportContext.setEndTime(endTime);
				ruleExportStrategy.totalResultExport(result);
				if(recordExecutePersistence != null){
					recordExecutePersistence.updateRecord(ruleExportContext);
				}
			} catch (RuleBaseException e) {
				Logger.error("ϵͳ������������ͳһ���ݵ���ʱ���ִ���!", e);
			}
		}
		return result;
	}

	/**
	 * ����ִ�е�Ԫ�ݹ��ȥִ�й�����֤
	 * <p>
	 * ��ִ�е�ǰ��ҵ�������ִ�е�ǰҵ�������ɺ󣬾Ϳ�ʼ�ݹ�ִ����ʵ���еĹ���
	 * 
	 * @param runtimeContext
	 * @param businessComponent
	 *            - ִ�е�Ԫ
	 * @param lastRuleCheckConfiguration
	 *            - �ϲ㼶�Ĺ���������������
	 * @param validatorListener
	 * @return
	 */
	private List<IRuleExecuteResult> executeRulesRecursive(RuntimeContext runtimeContext,
			BusinessComponent businessComponent, IRuleCheckConfiguration lastRuleCheckConfiguration,
			RuleExportContext ruleExportContext, IValidatorListener validatorListener) {
		List<IRuleExecuteResult> result = new ArrayList<IRuleExecuteResult>();
		if (validatorListener != null) {
			if (validatorListener.requireCancel()) {
				validatorListener.executeCancelOperation();
				return result;
			}
			validatorListener.notifyBusiCompEvent(businessComponent, ValidatorEvent.START);
		}
		// ��ִ�е�ǰҵ�����
		ExecuteResult executorResult = executeConcreteBusinessComponentImpl(runtimeContext, businessComponent,
				lastRuleCheckConfiguration, ruleExportContext, validatorListener);
		result.addAll(executorResult.getExecuteResults());
		IRuleCheckConfiguration ruleCheckConfiguration = executorResult.getExecuteContext();

		// Ȼ������Ƿ��Ǹ�������ݹ��ȥִ������ִ�е�Ԫ��������������������Ľ��и�ֵ
		if (businessComponent instanceof ICompositeExecuteUnit) {
			ICompositeExecuteUnit compositeExecuteUnit = (ICompositeExecuteUnit) businessComponent;
			List<BusinessComponent> subBusinessComponentList = compositeExecuteUnit.getSubBusinessComponentList();
			for (BusinessComponent subBusinessComponent : subBusinessComponentList) {
				result.addAll(executeRulesRecursive(runtimeContext, subBusinessComponent, ruleCheckConfiguration,
						ruleExportContext, validatorListener));
			}
		}
		if (validatorListener != null) {
			validatorListener.notifyBusiCompEvent(businessComponent, ValidatorEvent.END);
		}
		return result;
	}

	/**
	 * �Ե�����ҵ��������й�����֤�������������Ƿ�Ϊ���������
	 * <P>
	 * ����ҵ������Ὣ�����Լ�����������Ĳ������кϲ����������γ�����̬����ִ��
	 * 
	 * @param runtimeContext
	 *            - ִ��������
	 * @param businessComponent
	 *            - ��ǰִ�е�Ԫ
	 * @param lastRuleConfigContexts
	 *            - ���ϲ����ִ��ʱ�������·��Ĺ�����Щ������Щ�����ڸ������ִ�У���Щ���У���Ҫ�����·�
	 * @param validatorListener
	 * @return
	 */
	private ExecuteResult executeConcreteBusinessComponentImpl(RuntimeContext runtimeContext,
			BusinessComponent businessComponent, final IRuleCheckConfiguration lastRuleCheckConfiguration,
			RuleExportContext ruleExportContext, IValidatorListener validatorListener) {
		List<IRuleExecuteResult> executeResultList = new ArrayList<IRuleExecuteResult>();
		List<IRuleConfigContext> ruleConfigContextList = null;
		IRuleCheckConfiguration ruleCheckConfiguration = null;
		try {
			ruleCheckConfiguration = ruleConfig.parseRule(businessComponent);
			ruleCheckConfiguration.addRuleCheckConfiguration(lastRuleCheckConfiguration);
			ruleConfigContextList = ruleCheckConfiguration.toRuleConfigContexts();
			initRuntimeEnvironment(ruleConfigContextList);
		} catch (RuleBaseException e1) {
			Logger.error(String.format("����ִ�е�Ԫ�� %s �����ļ�����, ���ܼ�������!", businessComponent.getDisplayBusiCompName()), e1);
			return new ExecuteResult(executeResultList, lastRuleCheckConfiguration);
		}
		Map<String, IRuleDefinition> ruleDefMap = ruleExecutorFactory.getIdentifierToDefinitionMap();
		long startTimeMillis = System.currentTimeMillis();
		for (IRuleConfigContext ruleConfigContext : ruleConfigContextList) {
			String ruleIdentifier = ruleConfigContext.getRuleDefinitionIdentifier();
			if (validatorListener != null) {
				validatorListener.notifyRuleEvent(businessComponent, ruleIdentifier, ValidatorEvent.START);
			}
			DefaultRuleExecuteContext ruleExecuteContext = new DefaultRuleExecuteContext();
			ruleExecuteContext.setRuleConfigContext(ruleConfigContext);
			runtimeContext.setCurrentExecuteBusinessComponent(businessComponent);
			ruleExecuteContext.setRuntimeContext(runtimeContext);

			IRuleDefinition ruleDefinition = ruleDefMap.get(ruleIdentifier);
			IRuleExecuteResult ruleResult = null;
			boolean ignored = false;
			try {
				if (ruleDefinition == null) {
					ruleResult = new ErrorRuleExecuteResult(ruleIdentifier, String.format("�����ʶ��δ��ִ�л����з���:%s",
							ruleIdentifier));
				} else {
					RuleDefinitionAnnotationVO ruleDefinitionVO = RuleDefinitionsReader.getInstance()
							.getRuleDefinitionVO(ruleIdentifier);
					if (ruleDefinitionVO.getExecuteLayer().canExecuteInLayer(businessComponent.getExecuteLayer())) {
						ruleResult = ruleDefinition.actualExecute(ruleExecuteContext);
					} else {
						ignored = true;
						continue;
					}
				}
			} catch (Throwable e) {
				Logger.error(e.getMessage(), e);
				ruleResult = new ExceptionRuleExecuteResult(ruleIdentifier, e);
			} finally {
				if (!ignored) {
					if (ruleResult != null && ruleResult.getRuleExecuteStatus() == RuleExecuteStatus.SUCCESS) {
						Logger.info(String.format("����: %s ִ����ɣ�\n�����%s", ruleIdentifier, ruleResult.toString()));
					} else {
						Logger.error(String.format("����: %s ִ����ɣ�\n���: %s", ruleIdentifier,
								ruleResult != null ? ruleResult.toString() : "���Ϊ�գ��������ʵ��!"));
					}
					if (ruleResult != null) {
						ruleResult.setRuleDefinitionIdentifier(ruleIdentifier);
						ruleResult.setRuleExecuteContext(ruleExecuteContext);
						ruleResult.setBusinessComponent(businessComponent);
						executeResultList.add(ruleResult);
					}
					if (validatorListener != null) {
						validatorListener.notifyRuleEvent(businessComponent, ruleIdentifier, ValidatorEvent.END);
					}

				}
			}
		}
		Logger.info(String.format("ҵ�����:%s �Ĺ���ִ����ɣ�����ʱ: %s ����", businessComponent.getDisplayBusiCompName(),
				System.currentTimeMillis() - startTimeMillis));
		try {
			ruleExportStrategy.batchExportResult(businessComponent, executeResultList, ruleExportContext);
		} catch (RuleBaseException e) {
			Logger.error(String.format("����ִ�е�Ԫ:%s �������", businessComponent), e);
		}
		return new ExecuteResult(executeResultList, ruleCheckConfiguration);
	}

	/**
	 * ��ʼ������ִ�л���
	 * 
	 * @throws RuleBaseException
	 */
	private void initRuntimeEnvironment(List<IRuleConfigContext> ruleConfigContexts) throws RuleBaseException {
		SystemRuntimeContext systemRuntimeContext = ExecutorContextHelperFactory.getExecutorContextHelper()
				.getCurrentRuntimeContext().getSystemRuntimeContext();
		ExecutePeriod executePeriod = systemRuntimeContext.getExecutePeriod();
		if (ExecutePeriod.DEPLOY.canExecuteInEnv(executePeriod)) {
			Map<String, IRuleDefinition> ruleDefinitionMap = ruleExecutorFactory.getIdentifierToDefinitionMap();
			List<IRuleDefinition> ruleDefinitionList = new ArrayList<IRuleDefinition>();
			for (IRuleConfigContext ruleConfigContext : ruleConfigContexts) {
				String ruleDefinitionIdentifier = ruleConfigContext.getRuleDefinitionIdentifier();
				IRuleDefinition iRuleDefinition = ruleDefinitionMap.get(ruleDefinitionIdentifier);
				if (iRuleDefinition != null) {
					ruleDefinitionList.add(iRuleDefinition);
				}
			}
			TempTableResourceCreatorUtils.initEnvironment(ruleDefinitionList);
		} else {
			Logger.warn(String.format("��ʼ��Ҫ��Ļ���(%s)�����ڵ�ǰִ�л���(%s)�����㣬������ʼ���׶�", ExecutePeriod.DEPLOY.getName(),
					executePeriod.getName()));
		}
	}

	/**
	 * ��ִ����ɺ󣬽��л����������������������ʱ�����ݣ��߳����ݣ����ӳص�
	 * 
	 * @throws RuleBaseException
	 */
	private void cleanup() throws RuleBaseException {
		TempTableResourceCreatorUtils.cleanUp();
	}

	/**
	 * ����ִ�н�����а�װ�Ľ��
	 * 
	 * @author mazhqa
	 * @since V2.7
	 */
	public static class ExecuteResult {

		/**
		 * ִ�е�Ԫ��ִ�н��
		 */
		private final List<IRuleExecuteResult> executeResults;
		/**
		 * ��ִ�е�Ԫִ�к󣬽���ִ�е�Ԫ�·���context���д�������������ִ�е�Ԫʹ��
		 */
		private final IRuleCheckConfiguration ruleCheckConfiguration;

		public ExecuteResult(List<IRuleExecuteResult> executeResults, IRuleCheckConfiguration ruleCheckConfiguration) {
			super();
			this.executeResults = executeResults;
			this.ruleCheckConfiguration = ruleCheckConfiguration;
		}

		public List<IRuleExecuteResult> getExecuteResults() {
			return executeResults;
		}

		public IRuleCheckConfiguration getExecuteContext() {
			return ruleCheckConfiguration;
		}

	}

}
