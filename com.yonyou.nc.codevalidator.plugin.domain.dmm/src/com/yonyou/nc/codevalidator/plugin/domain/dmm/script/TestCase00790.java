package com.yonyou.nc.codevalidator.plugin.domain.dmm.script;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractScriptQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.utils.FileEncodeUtils;

@RuleDefinition(catalog = CatalogEnum.PRESCRIPT, executePeriod = ExecutePeriod.COMPILE, executeLayer = ExecuteLayer.GLOBAL, coder = "wangjians", description = "脚本文件格式（UTF-8 NO BOM）检查规则 ", relatedIssueId = "790", subCatalog = SubCatalogEnum.PS_CONTENTCHECK)
public class TestCase00790 extends AbstractScriptQueryRuleDefinition {

	public static final String ENCODE = "UTF-8";

	@Override
	public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {

		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		String ncHomePath = ruleExecContext.getRuntimeContext().getNcHome();
		String ncScriptsPath = ncHomePath + "\\ncscript";
		@SuppressWarnings("unchecked")
		Collection<File> listFiles = FileUtils.listFiles(new File(ncScriptsPath), new String[] { "sql" }, true);
		if (listFiles == null || listFiles.isEmpty()) {
			result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(), "没有可查的数据库脚本文件");
		}
		String fileAttribute = null;
		for (File file : listFiles) {
			try {
				fileAttribute = FileEncodeUtils.getFileEncode(file.getCanonicalFile().getAbsolutePath());
				if (!fileAttribute.equals(ENCODE)) {
					result.addResultElement(file.getAbsolutePath(), "此脚本文件格式不是UTF-8");
				}
			} catch (IOException e) {
				throw new RuleBaseException(e);
			}
		}
		return result;
	}

}
