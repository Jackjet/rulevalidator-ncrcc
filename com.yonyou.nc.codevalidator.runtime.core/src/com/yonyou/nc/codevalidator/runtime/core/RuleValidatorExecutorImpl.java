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
 * 规则验证执行的执行入口类
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
	 * 执行规则验证 执行的过程： 1. 全局规则验证执行； 2. 各个组件规则的执行；
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
				// mazhqa: 一般情况下执行规则验证都是以模块为单位进行执行的
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
			Logger.info(String.format("所有规则执行完成，共耗时: %s 毫秒", System.currentTimeMillis() - startTimeMillis));
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
				Logger.error("系统进行清理或进行统一数据导出时出现错误!", e);
			}
		}
		return result;
	}

	/**
	 * 根据执行单元递归地去执行规则验证
	 * <p>
	 * 先执行当前的业务组件，执行当前业务组件完成后，就开始递归执行子实体中的规则
	 * 
	 * @param runtimeContext
	 * @param businessComponent
	 *            - 执行单元
	 * @param lastRuleCheckConfiguration
	 *            - 上层级的规则检查配置上下文
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
		// 先执行当前业务组件
		ExecuteResult executorResult = executeConcreteBusinessComponentImpl(runtimeContext, businessComponent,
				lastRuleCheckConfiguration, ruleExportContext, validatorListener);
		result.addAll(executorResult.getExecuteResults());
		IRuleCheckConfiguration ruleCheckConfiguration = executorResult.getExecuteContext();

		// 然后根据是否是复合组件递归地去执行所有执行单元，并对其规则配置上下文进行赋值
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
	 * 对单独的业务组件进行规则验证，并不考虑其是否为集合类组件
	 * <P>
	 * 单独业务组件会将规则以及规则的上下文参数进行合并操作，以形成运行态对象执行
	 * 
	 * @param runtimeContext
	 *            - 执行上下文
	 * @param businessComponent
	 *            - 当前执行单元
	 * @param lastRuleConfigContexts
	 *            - 在上层规则执行时留下来下发的规则，这些规则有些可以在该组件下执行，有些则不行，需要继续下发
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
			Logger.error(String.format("解析执行单元： %s 配置文件出错, 不能继续进行!", businessComponent.getDisplayBusiCompName()), e1);
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
					ruleResult = new ErrorRuleExecuteResult(ruleIdentifier, String.format("规则标识符未在执行环境中发现:%s",
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
						Logger.info(String.format("任务: %s 执行完成，\n结果：%s", ruleIdentifier, ruleResult.toString()));
					} else {
						Logger.error(String.format("任务: %s 执行完成，\n结果: %s", ruleIdentifier,
								ruleResult != null ? ruleResult.toString() : "结果为空，请检查规则实现!"));
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
		Logger.info(String.format("业务组件:%s 的规则执行完成，共耗时: %s 毫秒", businessComponent.getDisplayBusiCompName(),
				System.currentTimeMillis() - startTimeMillis));
		try {
			ruleExportStrategy.batchExportResult(businessComponent, executeResultList, ruleExportContext);
		} catch (RuleBaseException e) {
			Logger.error(String.format("导出执行单元:%s 结果出错！", businessComponent), e);
		}
		return new ExecuteResult(executeResultList, ruleCheckConfiguration);
	}

	/**
	 * 初始化多语执行环境
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
			Logger.warn(String.format("初始化要求的环境(%s)不能在当前执行环境(%s)中满足，跳过初始化阶段", ExecutePeriod.DEPLOY.getName(),
					executePeriod.getName()));
		}
	}

	/**
	 * 在执行完成后，进行环境清理操作，包括清理临时表数据，线程数据，连接池等
	 * 
	 * @throws RuleBaseException
	 */
	private void cleanup() throws RuleBaseException {
		TempTableResourceCreatorUtils.cleanUp();
	}

	/**
	 * 用于执行结果进行包装的结果
	 * 
	 * @author mazhqa
	 * @since V2.7
	 */
	public static class ExecuteResult {

		/**
		 * 执行单元的执行结果
		 */
		private final List<IRuleExecuteResult> executeResults;
		/**
		 * 在执行单元执行后，将该执行单元下发的context进行处理，留给下属的执行单元使用
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
