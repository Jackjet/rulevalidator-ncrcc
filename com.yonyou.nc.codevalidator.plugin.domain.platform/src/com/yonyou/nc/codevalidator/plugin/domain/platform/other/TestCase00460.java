package com.yonyou.nc.codevalidator.plugin.domain.platform.other;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractUpmQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.UpmResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

@RuleDefinition(catalog = CatalogEnum.OTHERCONFIGFILE, executePeriod = ExecutePeriod.CHECKOUT, executeLayer = ExecuteLayer.BUSICOMP, subCatalog = SubCatalogEnum.OCF_UPM, description = "META-INF目录下面是否有module.xml文件", solution = "在相应的模块下加入module.xml文件", coder = "gaoyt", relatedIssueId = "460", memo = "META-INF目录下面是否有module.xml文件 ")
public class TestCase00460 extends AbstractUpmQueryRuleDefinition {

	private final static String MODULES = "modules";
	private final static String MODULE_XML = "module.xml";
	private final static String META_INF = "META-INF";

	@Override
	protected IRuleExecuteResult processUpmRules(
			IRuleExecuteContext ruleExecContext, List<UpmResource> resources)
			throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		String ncName = ruleExecContext.getRuntimeContext().getNcHome();
		StringBuilder path = new StringBuilder();
		String moduleName = ruleExecContext.getBusinessComponent().getModule();
		path.append(ncName).append(File.separator).append(MODULES)
				.append(File.separator).append(moduleName)
				.append(File.separator).append(META_INF);
		File file = new File(path.toString());
		String[] xmlFile = file.list(new FilenameFilter() {

			@Override
			public boolean accept(File file, String fileName) {
				if (MODULE_XML.equals(fileName)) {
					return true;
				}
				return false;
			}
		});
		if (null != xmlFile && 0 != xmlFile.length) {
			return result;
		}
		path.delete(0, path.length());
		path.append(moduleName).append("模块下没有").append(MODULE_XML);
		result.addResultElement(moduleName, path.toString());
		return result;
	}
}
