package com.yonyou.nc.codevalidator.export.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.yonyou.nc.codevalidator.export.api.AbstractPortionRulePersistence;
import com.yonyou.nc.codevalidator.export.api.RuleExportContext;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.utils.RuleXmlUtils;
import com.yonyou.nc.codevalidator.sdk.utils.XmlPersistenceConstants;

/**
 * xml实现的规则配置策略
 * 
 * @author mazhqa
 * @since V2.4
 */
public class XmlRulePersistenceStrategy extends AbstractPortionRulePersistence {

	@Override
	public String batchExportResult(BusinessComponent businessComponent, List<IRuleExecuteResult> ruleResultList,RuleExportContext context)
			throws RuleBaseException {
		String ruleResultFilePath = getRuleResultFilePath(businessComponent);
		File file = new File(ruleResultFilePath);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdir();
		}
		if (file.exists()) {
			file.delete();
		}
		resultFolderInitialize(file.getParentFile());
		FileOutputStream fos = null;
		try {
			boolean created = file.createNewFile();
			if(!created){
				throw new RuleBaseException(String.format("结果文件: %s 创建失败...", file.getAbsoluteFile()));
			}
			fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos, Charset.forName("GBK"));
			RuleXmlUtils.writeRuleExecuteResult(osw, ruleResultList);
			return ruleResultFilePath;
		} catch (IOException e) {
			throw new RuleBaseException(e);
		} finally {
			IOUtils.closeQuietly(fos);
		}
	}

	@Override
	public String getResultFolderName() {
		return "xml-result";
	}

	@Override
	public String getFileExtension() {
		return "xml";
	}

	@Override
	public void resultFolderInitialize(File resultFolder) throws RuleBaseException {
		if(!new File(resultFolder, XmlPersistenceConstants.RESULT_XSL_NAME).exists()) {
			copyFileToFolder(resultFolder, XmlPersistenceConstants.RESULT_XSL_NAME);
		}
	}

	private void copyFileToFolder(File folder, String fileName) throws RuleBaseException {
		File configStyleFile = new File(folder, fileName);
		if (!configStyleFile.exists()) {
			try {
				FileUtils.copyURLToFile(getClass().getResource(fileName), configStyleFile);
			} catch (IOException e) {
				throw new RuleBaseException(e);
			}
		}
	}

}
