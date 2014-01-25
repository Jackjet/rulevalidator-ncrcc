package com.yonyou.nc.codevalidator.runtime.plugin.viewer.result;

import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionsReader;

/**
 * 规则执行结果VO属性
 * 
 * @author mazhqa
 * @since V2.8
 */
public interface IRuleResultVOProperty {

	public static final IRuleResultVOProperty RULE_EXECUTE_STATUS = new AbstractRuleResultVOProperty() {

		@Override
		public String getPropertyValue(IRuleExecuteResult ruleExecuteResult) {
			return "";
		}

		public String getIconPath(IRuleExecuteResult ruleExecuteResult) {
			return ruleExecuteResult.getRuleExecuteStatus().getIconName();
		}
	};

	public static final IRuleResultVOProperty RULE_EXECUTE_LEVEL = new AbstractRuleResultVOProperty() {

		@Override
		public String getPropertyValue(IRuleExecuteResult ruleExecuteResult) {
			return ruleExecuteResult.getRuleExecuteContext().getRuleConfigContext().getRuleExecuteLevel()
					.getDisplayName();
		}

		public String getIconPath(IRuleExecuteResult ruleExecuteResult) {
			return ruleExecuteResult.getRuleExecuteContext().getRuleConfigContext().getRuleExecuteLevel().getIconPath();
		};
	};

	public static final IRuleResultVOProperty RULE_IDENTIFIER = new AbstractRuleResultVOProperty() {

		@Override
		public String getPropertyValue(IRuleExecuteResult ruleExecuteResult) {
			String identifier = ruleExecuteResult.getRuleDefinitionIdentifier();
			return identifier.substring(identifier.lastIndexOf(".") + 1);
		}

	};

	public static final IRuleResultVOProperty EXECUTE_UNIT = new AbstractRuleResultVOProperty() {

		@Override
		public String getPropertyValue(IRuleExecuteResult ruleExecuteResult) {
			return ruleExecuteResult.getBusinessComponent().getDisplayBusiCompName();
		}

		public String getIconPath(IRuleExecuteResult ruleExecuteResult) {
			return "/images/busicomp.png";
		};
	};

	public static final IRuleResultVOProperty RULE_CATALOG = new AbstractRuleResultVOProperty() {

		@Override
		public String getPropertyValue(IRuleExecuteResult ruleExecuteResult) {
			String ruleDefinitionIdentifier = ruleExecuteResult.getRuleDefinitionIdentifier();
			RuleDefinitionAnnotationVO ruleDefinitionVO = RuleDefinitionsReader.getInstance().getRuleDefinitionVO(
					ruleDefinitionIdentifier);
			if (ruleDefinitionVO != null) {
				return String.format("%s %s", ruleDefinitionVO.getCatalog() == null ? "" : ruleDefinitionVO
						.getCatalog().getName(), ruleDefinitionVO.getSubCatalog() == null ? "" : ruleDefinitionVO
						.getSubCatalog().getName());
			}
			return "UNKNOWN";
		}

		public String getIconPath(IRuleExecuteResult ruleExecuteResult) {
			return "/images/ruletype.gif";
		};
	};

	public static final IRuleResultVOProperty RESULT_NOTE = new AbstractRuleResultVOProperty() {

		@Override
		public String getPropertyValue(IRuleExecuteResult ruleExecuteResult) {
			return ruleExecuteResult.getNote();
		}

	};

	abstract class AbstractRuleResultVOProperty implements IRuleResultVOProperty {
		@Override
		public String getIconPath(IRuleExecuteResult ruleExecuteResult) {
			return null;
		}
	}

	/**
	 * 根据规则规则执行结果得到具体的属性值
	 * 
	 * @param ruleExecuteResult
	 * @return
	 */
	String getPropertyValue(IRuleExecuteResult ruleExecuteResult);

	/**
	 * 对应图标的地址
	 * 
	 * @param ruleExecuteResult
	 * @return
	 */
	String getIconPath(IRuleExecuteResult ruleExecuteResult);

}
