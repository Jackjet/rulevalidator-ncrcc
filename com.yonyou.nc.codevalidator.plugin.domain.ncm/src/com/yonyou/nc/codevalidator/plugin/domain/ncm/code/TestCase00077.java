package com.yonyou.nc.codevalidator.plugin.domain.ncm.code;

import japa.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.SourceCodeProcessor;
import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

import com.yonyou.nc.codevalidator.resparser.JavaResourceQuery;
import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.UpmResourceQuery;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassResource;
import com.yonyou.nc.codevalidator.resparser.resource.UpmResource;
import com.yonyou.nc.codevalidator.resparser.resource.utils.CommonParamProcessUtils;
import com.yonyou.nc.codevalidator.resparser.resource.utils.CommonRuleParams;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.PublicRuleDefinitionParam;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.rule.impl.AbstractRuleDefinition;
import com.yonyou.nc.codevalidator.sdk.upm.UpmComponentVO;
import com.yonyou.nc.codevalidator.sdk.upm.UpmModuleVO;

/**
 * 检测EJB实现类的成员变量
 * 
 * @author luoxin3
 * 
 */
@RuleDefinition(catalog = CatalogEnum.JAVACODE, description = "检查EJB实现类是否存在成员变量", memo = "NC的EJB实现类及由EJB实现类实例化的单例模式的类中，如果使用了非线程安全的控件，在多线程下会出并发错误。 ", solution = "删除EJB实现类中的成员变量", specialParamDefine = { "" }, coder = "luoxin3", subCatalog = SubCatalogEnum.JC_CODECRITERION, relatedIssueId = "77", executePeriod = ExecutePeriod.CHECKOUT)
@PublicRuleDefinitionParam(params = { CommonRuleParams.CODECONTAINEXTRAFOLDER })
public class TestCase00077 extends AbstractRuleDefinition {

	protected UpmResourceQuery getUpmResourceQuery(
			IRuleExecuteContext ruleExecContext) throws ResourceParserException {
		UpmResourceQuery resourceQuery = new UpmResourceQuery();
		resourceQuery.setBusinessComponent(ruleExecContext
				.getBusinessComponent());
		return resourceQuery;
	}

	public List<JavaClassResource> getFinalClassResource(
			IRuleExecuteContext ruleExecContext) throws RuleBaseException,
			ParseException, IOException {
		UpmResourceQuery upmResourceQuery = getUpmResourceQuery(ruleExecContext);
		List<UpmResource> upmResources = ResourceManagerFacade
				.getResource(upmResourceQuery);
		List<String> upmImplementationNames = new ArrayList<String>();
		for (UpmResource upmResource : upmResources) {
			// System.out.println(upmResource.getResourcePath()+ "     " +
			// upmResource.getResourceFileName());
			List<UpmComponentVO> upmComponentVOs = null;
			UpmModuleVO upmModuleVO = upmResource.getUpmModuleVo();
			upmComponentVOs = upmModuleVO.getPubComponentVoList();
			for (UpmComponentVO next : upmComponentVOs) {
				if (next.getInterfaceName() != null) {
					upmImplementationNames.add(next.getImplementationName());
				}
			}
		}
		JavaResourceQuery javaResourceQuery = new JavaResourceQuery();
		javaResourceQuery.setBusinessComponent(ruleExecContext
				.getBusinessComponent());
		// javaResourceQuery.setResPrivilege(JavaResPrivilege.PRIVATE);
		javaResourceQuery.setContainExtraWrapper(CommonParamProcessUtils
				.getCodeContainsExtraFolder(ruleExecContext));
		javaResourceQuery.setClassNameFilterList(upmImplementationNames);
		List<JavaClassResource> fianlClassResource = ResourceManagerFacade
				.getResource(javaResourceQuery);
		return fianlClassResource;
	}

	@Override
	public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		try {
			List<JavaClassResource> finalClassResources = getFinalClassResource(ruleExecContext);
			for (JavaClassResource resource : finalClassResources) {
				RuleContext ruleContext = SourceCodeProcessor.parseRule(
						resource.getResourcePath(), new BanFieldRule());
				List<RuleViolation> ruleViolationList = ruleContext
						.getRuleViolationList();
				String generateErrorDetail = SourceCodeProcessor
						.generateErrorDetail(ruleViolationList);
				if (generateErrorDetail != null
						&& generateErrorDetail.trim().length() > 0) {
					result.addResultElement(resource.getResourcePath(),
							generateErrorDetail);
				}
			}
		} catch (FileNotFoundException e) {
			throw new ResourceParserException(e);
		} catch (ParseException e) {
			throw new ResourceParserException(e);
		} catch (IOException e) {
			throw new ResourceParserException(e);
		}
		return result;
	}

	public static class BanFieldRule extends AbstractJavaRule {

		@Override
		public Object visit(ASTFieldDeclaration node, Object data) {
			if (!(node.isFinal() && node.isStatic())) {
				RuleContext ruleContext = (RuleContext) data;
				ruleContext.addRuleViolation(ruleContext, this, node,
						"该EJB实现类中含有成员变量", null);
			}
			return super.visit(node, data);
		}
	}
}
