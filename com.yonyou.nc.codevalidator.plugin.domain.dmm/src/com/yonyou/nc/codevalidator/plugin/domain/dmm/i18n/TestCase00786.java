package com.yonyou.nc.codevalidator.plugin.domain.dmm.i18n;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import com.yonyou.nc.codevalidator.resparser.JavaResourceQuery;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractJavaQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.RepairLevel;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

@RuleDefinition(catalog = CatalogEnum.LANG, coder = "guojunf", description = "代码中汉字不需要做多语的要标注（-notran-）", relatedIssueId = "786", subCatalog = SubCatalogEnum.LANG_CODE, repairLevel = RepairLevel.SUGGESTREPAIR)
public class TestCase00786 extends AbstractJavaQueryRuleDefinition {

	@Override
	protected JavaResourceQuery getJavaResourceQuery(
			IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		JavaResourceQuery query = new JavaResourceQuery();
		query.setBusinessComponent(ruleExecContext.getBusinessComponent());
		return query;
	}

	@Override
	protected IRuleExecuteResult processJavaRules(
			IRuleExecuteContext ruleExecContext,
			List<JavaClassResource> resources) throws RuleBaseException {

		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (JavaClassResource javaClassResource : resources) {
			this.parseFileLine(result, javaClassResource.getResourcePath());
		}
		return result;
	}

	private void parseFileLine(ResourceRuleExecuteResult result, String filePath)
			throws RuleBaseException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filePath));
			String lineInfo = "";
			int lineNo = 0;
			Pattern p1 = Pattern.compile("/[/|*].*[\u4e00-\u9fa5]+");
			Pattern p2 = Pattern.compile("[\u4e00-\u9fa5]");
			while ((lineInfo = br.readLine()) != null) {
				lineNo++;

				if (lineInfo.trim().startsWith("/*")
						|| lineInfo.trim().startsWith("*")
						|| lineInfo.trim().startsWith("//")
						|| lineInfo.trim().startsWith("*/")) {
					continue;
				}

				// 如果该行不包含汉字,则不做处理
				if (!this.isMatch(p2, lineInfo)) {
					continue;
				}

				if (this.isMatch(p1, lineInfo)) {
					continue;
				}

				// 包含汉字的情况
				if (!lineInfo.contains("-=notranslate=-")) {
					result.addResultElement(filePath + " 行号:" + lineNo,
							"代码中汉字不需要做多语的要标注(-notran-)");
				}

			}
		} catch (IOException e) {
			RuleBaseException ex = new RuleBaseException(e);
			ex.setStackTrace(e.getStackTrace());
			throw ex;
		} finally {
			IOUtils.closeQuietly(br);
		}

	}

	private boolean isMatch(Pattern pattern, String str) {
		Matcher matcher = pattern.matcher(str);
		if (matcher.find()) {
			return true;
		}
		return false;
	}

}
