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

@RuleDefinition(catalog = CatalogEnum.JAVACODE, description = "��һ�������У�һ����漰���EJB�����ĵ��ã����������һ��EJB�������÷����쳣��\r\n"
		+ "��ʹtry catch�����쳣����Ҳ�ᵼ����������Ļع�����������������try catch EJB�����쳣������������ύ��\r\n"
		+ "��ᵼ������ҵ���߼��������⡣��˽��������һ���������漰������EJB������NC��Ҳ�ɳ�Ϊ�ӿڷ�����ʱ��\r\n"
		+ "����try catch�Ե����쳣��������catch�����׳�һ���쳣������������������ع����Ƶ������ص����ε�BUG��", memo = "try catch�е�catch �����쳣����",
		coder = "luoxin3", repairLevel = RepairLevel.MUSTREPAIR, solution = "try catch�е�catch �����쳣����",
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
					ruleContext.addRuleViolation(ruleContext, this, node, "catch����EJB�쳣δ�׳�����Ҫ���������׳�", null);
				}
			}
			return super.visit(node, data);
		}

	}

}
