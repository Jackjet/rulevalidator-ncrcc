package com.yonyou.nc.codevalidator.rule.vo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.yonyou.nc.codevalidator.rule.IRuleConfigContext;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;
import com.yonyou.nc.codevalidator.rule.impl.DefaultRuleConfigContext;

/**
 * 抽象的规则检查配置项
 * 
 * @author mazhqa
 * @since V2.6
 */
public abstract class AbstractRuleCheckConfiguration implements IRuleCheckConfiguration {

	protected final CommonParamConfiguration commonParamConfiguration = new CommonParamConfiguration();

	protected final Map<String, RuleItemConfigVO> id2RuleItemConfigVoMap = new HashMap<String, RuleItemConfigVO>();
	protected final List<RuleItemConfigVO> ruleItemConfigVoList = new ArrayList<RuleItemConfigVO>();

	@Override
	public CommonParamConfiguration getCommonParamConfiguration() {
		return commonParamConfiguration;
	}

	@Override
	public List<RuleItemConfigVO> getRuleItemConfigVoList() {
		return Collections.unmodifiableList(ruleItemConfigVoList);
	}

	/**
	 * 添加一条规则条目，同时同步通用参数
	 * 
	 * @param ruleItemConfigVo
	 */
	public void addRuleItemConfigVo(RuleItemConfigVO ruleItemConfigVo) {
		ruleItemConfigVoList.add(ruleItemConfigVo);
		id2RuleItemConfigVoMap.put(ruleItemConfigVo.getRuleDefinitionVO().getRuleDefinitionIdentifier(),
				ruleItemConfigVo);

		RuleDefinitionAnnotationVO ruleDefinitionVO = ruleItemConfigVo.getRuleDefinitionVO();
		CommonParamConfiguration commonParamConfiguration = ruleDefinitionVO.getCommonParamConfiguration();
		for (ParamConfiguration paramConfiguration : commonParamConfiguration.getParamConfigurationList()) {
			this.commonParamConfiguration.addParamConfig(paramConfiguration);
		}
	}

	/**
	 * 移除一条规则条目，通用参数进行调整
	 * 
	 * @param deleteRuleItemConfigVo
	 */
	public void removeRuleItemConfigVo(RuleItemConfigVO deleteRuleItemConfigVo) {
		ruleItemConfigVoList.remove(deleteRuleItemConfigVo);
		id2RuleItemConfigVoMap.remove(deleteRuleItemConfigVo.getRuleDefinitionVO().getRuleDefinitionIdentifier());

		RuleDefinitionAnnotationVO ruleDefinitionVO = deleteRuleItemConfigVo.getRuleDefinitionVO();
		CommonParamConfiguration deleteCommonParamConfiguration = ruleDefinitionVO.getCommonParamConfiguration();
		List<String> deleteParamConfigurationNameList = new ArrayList<String>(
				commonParamConfiguration.getParamConfigurationNameList());
		if (!deleteCommonParamConfiguration.getParamConfigurationList().isEmpty()) {
			for (RuleItemConfigVO ruleItemConfigVo : ruleItemConfigVoList) {
				List<String> existCommonParamList = ruleItemConfigVo.getRuleDefinitionVO()
						.getCommonParamConfiguration().getParamConfigurationNameList();
				if (existCommonParamList != null && existCommonParamList.size() > 0) {
					Iterator<String> commonParamListIterator = deleteParamConfigurationNameList.iterator();
					while (commonParamListIterator.hasNext()) {
						if (existCommonParamList.contains(commonParamListIterator.next())) {
							// 将要保留的参数从删除列表中删除
							commonParamListIterator.remove();
						}
					}
				}
			}
			// 最后删除不需要保留的参数
			for (String deleteCommonParam : deleteParamConfigurationNameList) {
				commonParamConfiguration.removeUnusedParam(deleteCommonParam);
			}
		}

	}

	@Override
	public List<IRuleConfigContext> toRuleConfigContexts() {
		CommonParamConfiguration commonParamConfiguration = this.getCommonParamConfiguration();
		List<RuleItemConfigVO> ruleItemConfigVoList = this.getRuleItemConfigVoList();
		List<IRuleConfigContext> result = new ArrayList<IRuleConfigContext>();
		for (RuleItemConfigVO ruleItemConfigVO : ruleItemConfigVoList) {
			DefaultRuleConfigContext ruleConfigContext = new DefaultRuleConfigContext();
			ruleConfigContext.setRuleDefinitionIdentifier(ruleItemConfigVO.getRuleDefinitionVO()
					.getRuleDefinitionIdentifier());
			ruleConfigContext.setRuleExecuteLevel(ruleItemConfigVO.getRuleExecuteLevel());
			PrivateParamConfiguration privateParamConfiguration = ruleItemConfigVO.getPrivateParamConfiguration();
			for (ParamConfiguration paramConfiguration : privateParamConfiguration.getParamConfigurationList()) {
				ruleConfigContext.setProperty(paramConfiguration.getParamName(), paramConfiguration.getParamValue());
			}
			for (ParamConfiguration paramConfiguration : commonParamConfiguration.getParamConfigurationList()) {
				ruleConfigContext.setGlobalProperty(paramConfiguration.getParamName(),
						paramConfiguration.getParamValue());
			}
			result.add(ruleConfigContext);
		}
		return result;
	}

	public void removeAll() {
		ruleItemConfigVoList.clear();
		id2RuleItemConfigVoMap.clear();
		commonParamConfiguration.removeAllParams();
	}

	@Override
	public int compareTo(IRuleCheckConfiguration o) {
		return this.getPriority() >= o.getPriority() ? 1 : -1;
	}

	/**
	 * 整合不同的规则检查配置项，进行合并；
	 * 
	 * @param ruleCheckConfiguration
	 */
	public void addRuleCheckConfiguration(IRuleCheckConfiguration ruleCheckConfiguration) {
//		if (ruleCheckConfiguration instanceof EmptyRuleCheckConfigration) {
//			return;
//		}
		CommonParamConfiguration otherCommonParamConfig = ruleCheckConfiguration.getCommonParamConfiguration();
		List<RuleItemConfigVO> otherRuleItemConfigVoList = ruleCheckConfiguration.getRuleItemConfigVoList();

		for (ParamConfiguration paramConfiguration : otherCommonParamConfig.getParamConfigurationList()) {
			this.commonParamConfiguration.addParamConfig(paramConfiguration);
		}

		for (RuleItemConfigVO ruleItemConfigVO : otherRuleItemConfigVoList) {
			addExtraRuleItemConfigVo(ruleItemConfigVO);
		}
	}
	
	private void addExtraRuleItemConfigVo(RuleItemConfigVO ruleItemConfigVO) {
		String ruleDefinitionIdentifier = ruleItemConfigVO.getRuleDefinitionVO().getRuleDefinitionIdentifier();
		if(!id2RuleItemConfigVoMap.containsKey(ruleDefinitionIdentifier)) {
			addRuleItemConfigVo(ruleItemConfigVO);
		}
	}


}
