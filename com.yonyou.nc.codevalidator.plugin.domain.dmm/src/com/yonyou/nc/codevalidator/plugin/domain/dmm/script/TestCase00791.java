package com.yonyou.nc.codevalidator.plugin.domain.dmm.script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

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

@RuleDefinition(catalog = CatalogEnum.PRESCRIPT, coder = "wangjians", description = "�����ű��ָ�����go�������� ",
		relatedIssueId = "791", subCatalog = SubCatalogEnum.PS_CONTENTCHECK, executePeriod = ExecutePeriod.COMPILE,
		executeLayer = ExecuteLayer.MODULE)
public class TestCase00791 extends AbstractScriptQueryRuleDefinition {

	public static final String GO = "go";

	@Override
	public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		String ncHomePath = ruleExecContext.getRuntimeContext().getNcHome();
		String module = ruleExecContext.getBusinessComponent().getModule();
		String ncScriptsPath = ncHomePath + "\\ncscript" + "\\" + module;
		@SuppressWarnings("unchecked")
		Collection<File> listFiles = FileUtils.listFiles(new File(ncScriptsPath), new String[] { "sql" }, true);
		if (listFiles == null || listFiles.isEmpty()) {
			result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(), "û�пɲ�����ݿ�ű��ļ�");
		}
		String lineStr = null;
		BufferedReader br = null;
		for (File file : listFiles) {
			try {
				br = new BufferedReader(new FileReader(file));
				lineStr = this.readSql(br);
				// �ж��ǲ���Ԥ�ýű�������ǽ���ű�������
				if (lineStr.startsWith("create")) {
					continue;
				}
				int index = 0; // 0��sql��1��go
				while (lineStr != null) {
					if (!lineStr.equals(GO)) {
						if (index == 1) {
							result.addResultElement(file.getAbsolutePath(), "�˽ű��ļ�û��ͨ�������ű��ָ�����go��������");
						}
						index++;
					}
					if (lineStr.equals(GO)) {
						index = 0;
					}
					lineStr = this.readSql(br);
				}
			} catch (IOException e) {
				throw new RuleBaseException(e);
			} finally {
				IOUtils.closeQuietly(br);
			}
		}
		return result;
	}

	/**
	 * ��Ԥ�ýű��ļ�������
	 * 
	 * @param br
	 * @return
	 * @throws IOException
	 */
	public String readSql(BufferedReader br) throws IOException {
		String sql = null;
		sql = br.readLine();
		// �ж˿���
		while (true) {
			if (sql == null || sql.length() < 1) {
				sql = br.readLine();
			}
			if (sql != null && sql.length() > 0) {
				break;
			}
		}
		// ��������insert��������go
		if (sql != null) {
			while (true) {
				if (sql.equals(GO)) {
					return sql;
				}
				if (!sql.endsWith(")")) {
					sql = sql + br.readLine();
				}
				if (sql.endsWith(")")) {
					break;
				}
			}
		}

		return sql;
	}

}
