package com.yonyou.nc.codevalidator.plugin.domain.ncm.code;

import japa.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.SourceCodeProcessor;
import net.sourceforge.pmd.lang.java.ast.ASTBlock;
import net.sourceforge.pmd.lang.java.ast.ASTCatchStatement;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTThrowStatement;
import net.sourceforge.pmd.lang.java.ast.ASTTryStatement;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

import com.yonyou.nc.codevalidator.resparser.JavaResourceQuery;
import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.UpmResourceQuery;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractJavaQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassResource;
import com.yonyou.nc.codevalidator.resparser.resource.UpmResource;
import com.yonyou.nc.codevalidator.resparser.resource.utils.CommonRuleParams;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.PublicRuleDefinitionParam;
import com.yonyou.nc.codevalidator.rule.annotation.RepairLevel;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.code.JavaResPrivilege;
import com.yonyou.nc.codevalidator.sdk.upm.UpmComponentVO;
import com.yonyou.nc.codevalidator.sdk.upm.UpmModuleVO;

@RuleDefinition(catalog = CatalogEnum.JAVACODE, description = "在一个事务中，一般会涉及多个EJB方法的调用，如果当其中一个EJB方法调用发生异常，\r\n"
		+ "则即使try catch掉此异常，则也会导致整个事务的回滚，所以如果错误理解try catch EJB调用异常后事务会正常提交，\r\n"
		+ "则会导致整个业务逻辑处理问题。因此建议如果在一个事务中涉及到其它EJB方法（NC中也可成为接口方法）时，\r\n"
		+ "不能try catch吃掉此异常，必须在catch中再抛出一个异常，避免因错误理解事务回滚机制导致隐藏的隐蔽的BUG。", memo = "try catch中的catch 必须异常处理",
		coder = "luoxin3", repairLevel = RepairLevel.MUSTREPAIR, solution = "try catch中的catch 必须异常处理",
		specialParamDefine = {}, subCatalog = SubCatalogEnum.JC_CONCURRENISSUES, relatedIssueId = "80",
		executePeriod = ExecutePeriod.CHECKOUT)
@PublicRuleDefinitionParam(params = { CommonRuleParams.CODECONTAINEXTRAFOLDER })
public class TestCase00080 extends AbstractJavaQueryRuleDefinition {

	protected UpmResourceQuery getUpmResourceQuery(IRuleExecuteContext ruleExecContext) throws ResourceParserException {
		UpmResourceQuery resourceQuery = new UpmResourceQuery();
		resourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
		return resourceQuery;
	}

	protected JavaResourceQuery getJavaResourceQuery(IRuleExecuteContext ruleExecContext)
			throws ResourceParserException {
		JavaResourceQuery javaResourceQuery = new JavaResourceQuery();
		javaResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
		javaResourceQuery.setResPrivilege(JavaResPrivilege.PUBLIC);
		return javaResourceQuery;
	}

	public List<String> getFinalClassResource(IRuleExecuteContext ruleExecContext) throws RuleBaseException,
			ParseException, IOException {
		UpmResourceQuery upmResourceQuery = getUpmResourceQuery(ruleExecContext);
		List<UpmResource> upmResources = ResourceManagerFacade.getResource(upmResourceQuery);
		List<String> upmInterfaceNames = new ArrayList<String>();
		for (UpmResource upmResource : upmResources) {
			List<UpmComponentVO> upmComponentVOs = null;
			UpmModuleVO upmModuleVO = upmResource.getUpmModuleVo();
			upmComponentVOs = upmModuleVO.getPubComponentVoList();
			for (UpmComponentVO next : upmComponentVOs) {
				if (next.getInterfaceName() != null && "cmt".equals(next.getTx())) {
					String interfaceName = next.getInterfaceName();
					String implementationName = next.getImplementationName();
					upmInterfaceNames.add(interfaceName.substring(interfaceName.lastIndexOf(".") + 1));
					upmInterfaceNames.add(implementationName.substring(implementationName.lastIndexOf(".") + 1));
				}
			}
		}
		return upmInterfaceNames;
	}

	@Override
	protected IRuleExecuteResult processJavaRules(IRuleExecuteContext ruleExecContext, List<JavaClassResource> resources)
			throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		try {
			List<String> interfaceName = getFinalClassResource(ruleExecContext);
			for (JavaClassResource resource : resources) {
				RuleContext ruleContext = SourceCodeProcessor.parseRule(resource.getResourcePath(),
						new DealEJBException(interfaceName));
				List<RuleViolation> ruleViolationList = ruleContext.getRuleViolationList();
				String errorDetail = SourceCodeProcessor.generateErrorDetail(ruleViolationList);
				if (errorDetail != null && errorDetail.trim().length() > 0) {
					result.addResultElement(resource.getResourcePath(), errorDetail);
				}
			}
			return result;
		} catch (IOException e) {
			throw new ResourceParserException(e);
		} catch (ParseException e) {
			throw new ResourceParserException(e);
		}
	}

	public static class DealEJBException extends AbstractJavaRule {
		private boolean hasSameName = false;
		private int catchStatus = 0;

		private List<String> interfaceNames = new ArrayList<String>();

		public DealEJBException(List<String> interfaceNames) {
			this.interfaceNames = interfaceNames;
		}

		@Override
		public Object visit(ASTTryStatement node, Object data) {
			List<ASTName> astNames = node.findDescendantsOfType(ASTName.class);
			for (ASTName astName : astNames) {
				for (String interfaceName : interfaceNames) {
					String name = astName.getImage();
					if (name.contains(interfaceName) && !name.contains("_RequiresNew")) {
						hasSameName = true;
						Object result = super.visit(node, data);
						hasSameName = false;
						return result;
					}
				}
			}
			Object result = super.visit(node, data);
			return result;
		}

		@Override
		public Object visit(ASTCatchStatement node, Object data) {
			catchStatus++;
			Object result = super.visit(node, data);
			catchStatus--;
			return result;
		}

		@Override
		public Object visit(ASTBlock node, Object data) {
			if (catchStatus > 0 && hasSameName) {
				List<ASTThrowStatement> throwStatements = node.findDescendantsOfType(ASTThrowStatement.class);
				if (throwStatements.isEmpty()) {
					RuleContext ruleContext = (RuleContext) data;
					ruleContext.addRuleViolation(ruleContext, this, node, "catch块中EJB异常未抛出，需要进行重新抛出", null);
				}
			}
			return super.visit(node, data);
		}

	}

}
