package com.yonyou.nc.codevalidator.runtime.plugin.editor;

import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;
import com.yonyou.nc.codevalidator.rule.vo.RuleItemConfigVO;

/**
 * 用于规则配置属性获取的接口
 * 
 * @author mazhqa
 * @since V2.4
 */
public interface IRuleConfigVOProperty {

	public static final IRuleConfigVOProperty EXECUTE_LEVEL = new IRuleConfigVOProperty() {

		@Override
		public String getPropertyValue(RuleItemConfigVO ruleItemConfigVO) {
			return ruleItemConfigVO.getRuleExecuteLevel().getDisplayName();
		}

		@Override
		public String getPropertyValue(RuleDefinitionAnnotationVO ruleDefinitionVO) {
			throw new UnsupportedOperationException();
		}

	};

	public static final IRuleConfigVOProperty SIMPLE_CLASSNAME = new AbstractRuleConfigVoProperty() {

		@Override
		public String getPropertyValue(RuleDefinitionAnnotationVO ruleDefinitonVO) {
			return ruleDefinitonVO.getSimpleIdentifier();
		}
	};

	public static final IRuleConfigVOProperty CATALOG_NAME = new AbstractRuleConfigVoProperty() {

		@Override
		public String getPropertyValue(RuleDefinitionAnnotationVO ruleDefinitonVO) {
			return ruleDefinitonVO.getCatalog().getName();
		}
	};

	public static final IRuleConfigVOProperty EXECUTE_PERIOD_NAME = new AbstractRuleConfigVoProperty() {

		@Override
		public String getPropertyValue(RuleDefinitionAnnotationVO ruleDefinitonVO) {
			return ruleDefinitonVO.getExecutePeriod().getName();
		}
	};

	public static final IRuleConfigVOProperty EXECUTE_LAYER_NAME = new AbstractRuleConfigVoProperty() {

		@Override
		public String getPropertyValue(RuleDefinitionAnnotationVO ruleDefinitonVO) {
			return ruleDefinitonVO.getExecuteLayer().getName();
		}
	};

	public static final IRuleConfigVOProperty SUB_CATALOG_NAME = new AbstractRuleConfigVoProperty() {

		@Override
		public String getPropertyValue(RuleDefinitionAnnotationVO ruleDefinitonVO) {
			return ruleDefinitonVO.getSubCatalog().getName();
		}
	};

	public static final IRuleConfigVOProperty DESCRIPTION = new AbstractRuleConfigVoProperty() {

		@Override
		public String getPropertyValue(RuleDefinitionAnnotationVO ruleDefinitonVO) {
			return ruleDefinitonVO.getDescription();
		}
	};

	public static final IRuleConfigVOProperty SPECIAL_PARAMETERS = new AbstractRuleConfigVoProperty() {

		@Override
		public String getPropertyValue(RuleItemConfigVO ruleItemConfigVO) {
			return ruleItemConfigVO.getPrivateParamConfiguration().toString();
		}

		@Override
		public String getPropertyValue(RuleDefinitionAnnotationVO ruleDefinitonVO) {
			throw new UnsupportedOperationException();
		}
	};

	public static final IRuleConfigVOProperty CODER = new AbstractRuleConfigVoProperty() {

		@Override
		public String getPropertyValue(RuleDefinitionAnnotationVO ruleDefinitonVO) {
			return ruleDefinitonVO.getCoder();
		}
	};

	public static final IRuleConfigVOProperty MEMO = new AbstractRuleConfigVoProperty() {

		@Override
		public String getPropertyValue(RuleDefinitionAnnotationVO ruleDefinitonVO) {
			return ruleDefinitonVO.getMemo();
		}
	};

	public static final IRuleConfigVOProperty REPAIR_LEVEL = new AbstractRuleConfigVoProperty() {

		@Override
		public String getPropertyValue(RuleDefinitionAnnotationVO ruleDefinitionVO) {
			return ruleDefinitionVO.getRepairLevel().getName();
		}
	};

	public abstract static class AbstractRuleConfigVoProperty implements IRuleConfigVOProperty {
		public String getPropertyValue(RuleItemConfigVO ruleItemConfigVO) {
			RuleDefinitionAnnotationVO ruleDefinitionVO = ruleItemConfigVO.getRuleDefinitionVO();
			return getPropertyValue(ruleDefinitionVO);
		}
	}

	/**
	 * 根据规则配置VO得到具体的属性值
	 * 
	 * @param ruleItemConfigVO
	 * @return
	 */
	String getPropertyValue(RuleItemConfigVO ruleItemConfigVO);

	/**
	 * 根据规则配置VO得到具体的属性值
	 * 
	 * @param ruleItemConfigVO
	 * @return
	 */
	String getPropertyValue(RuleDefinitionAnnotationVO ruleDefinitionVO);

}
