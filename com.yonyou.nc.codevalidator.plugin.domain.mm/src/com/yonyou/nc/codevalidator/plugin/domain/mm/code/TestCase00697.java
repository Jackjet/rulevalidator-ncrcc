package com.yonyou.nc.codevalidator.plugin.domain.mm.code;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.SourceCodeProcessor;
import net.sourceforge.pmd.lang.java.ast.ASTAllocationExpression;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTImportDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTPackageDeclaration;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

import com.yonyou.nc.codevalidator.resparser.JavaResourceQuery;
import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.UpmResourceQuery;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractJavaQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassResource;
import com.yonyou.nc.codevalidator.resparser.resource.UpmResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.log.Logger;
import com.yonyou.nc.codevalidator.sdk.upm.UpmComponentVO;
import com.yonyou.nc.codevalidator.sdk.upm.UpmModuleVO;
import com.yonyou.nc.codevalidator.sdk.utils.StringUtils;

/**
 * 检测代码中是否存在new 接口实现类 代码存在
 * 
 * @author qiaoyanga
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.JAVACODE, description = "检测代码中是否存在new 接口实现类 代码存在【代码里不能直接引用UPM中的实现类】 ", relatedIssueId = "697", subCatalog = SubCatalogEnum.JC_CODECRITERION, coder = "qiaoyanga")
public class TestCase00697 extends AbstractJavaQueryRuleDefinition {

	@Override
	protected JavaResourceQuery getJavaResourceQuery(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		JavaResourceQuery javaResourceQuery = new JavaResourceQuery();
		javaResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
		return javaResourceQuery;
	}

	@Override
	protected IRuleExecuteResult processJavaRules(IRuleExecuteContext ruleExecContext, List<JavaClassResource> resources)
			throws RuleBaseException {
		List<String> upmImplClassList = new ArrayList<String>();
		UpmResourceQuery upmResourceQuery = new UpmResourceQuery();
		upmResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
		List<UpmResource> upmResources = ResourceManagerFacade.getResource(upmResourceQuery);
		for (UpmResource upmResource : upmResources) {
			UpmModuleVO upmModuleVo = upmResource.getUpmModuleVo();
			List<UpmComponentVO> pubComponentVoList = upmModuleVo.getPubComponentVoList();
			for (UpmComponentVO upmComponentVO : pubComponentVoList) {
				upmImplClassList.add(upmComponentVO.getImplementationName());
			}
		}

		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		try {
			for (JavaClassResource javaClassResource : resources) {
				RuleContext ruleContext = SourceCodeProcessor.parseRule(javaClassResource.getResourcePath(),
						new ScanNewUpmImplRule(upmImplClassList));
				List<RuleViolation> ruleViolationList = ruleContext.getRuleViolationList();
				String generateErrorDetail = SourceCodeProcessor.generateErrorDetail(ruleViolationList);
				if (StringUtils.isNotBlank(generateErrorDetail)) {
					result.addResultElement(javaClassResource.getResourcePath(), generateErrorDetail);
				}
			}
		} catch (FileNotFoundException e) {
			Logger.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 假定用户不会重写java.lang包中的类
	 * 
	 * @author qiaoyanga
	 */
	public static class ScanNewUpmImplRule extends AbstractJavaRule {

		private String currentPackageName;

		private List<String> upmImplClassList;

		private Map<String, String> importClassMap = new HashMap<String, String>();

		ScanNewUpmImplRule(List<String> upmImplClassList) {
			this.upmImplClassList = upmImplClassList;
		}

		@Override
		public Object visit(ASTPackageDeclaration node, Object data) {
			List<ASTName> astNames = node.findDescendantsOfType(ASTName.class);
			if (astNames.size() == 1) {
				ASTName astName = astNames.get(0);
				this.currentPackageName = astName.getImage();
			} else {
				RuleContext ruleContext = (RuleContext) data;
				ruleContext.addRuleViolation(ruleContext, this, node, "", null);
				return data;
			}
			return super.visit(node, data);
		}

		@Override
		public Object visit(ASTCompilationUnit node, Object data) {
			Object result = super.visit(node, data);
			return result;
		}

		@Override
		public Object visit(ASTImportDeclaration node, Object data) {
			List<ASTName> astNames = node.findDescendantsOfType(ASTName.class);
			if (!astNames.isEmpty()) {
				for (ASTName astName : astNames) {
					String importClassName = astName.getImage();
					this.importClassMap.put(importClassName.substring(importClassName.lastIndexOf(".") + 1),
							importClassName);
				}
			}
			return super.visit(node, data);
		}

		@Override
		public Object visit(ASTAllocationExpression node, Object data) {
			List<ASTClassOrInterfaceType> findDescendantsOfType = node
					.findChildrenOfType(ASTClassOrInterfaceType.class);
			if (!findDescendantsOfType.isEmpty()) {
				for (ASTClassOrInterfaceType astClassOrInterfaceType : findDescendantsOfType) {
					String classType = astClassOrInterfaceType.getImage();
					if (!classType.contains(".")) {
						classType = this.importClassMap.containsKey(classType) ? this.importClassMap.get(classType)
								: String.format("%s.%s", this.currentPackageName, classType);
					}
					if (this.upmImplClassList.contains(classType)) {
						RuleContext ruleContext = (RuleContext) data;
						ruleContext.addRuleViolation(ruleContext, this, node, "代码中是否存在new 接口实现类 :" + classType + "代码",
								null);
					}
				}
			}
			return super.visit(node, data);
		}

	}

}
