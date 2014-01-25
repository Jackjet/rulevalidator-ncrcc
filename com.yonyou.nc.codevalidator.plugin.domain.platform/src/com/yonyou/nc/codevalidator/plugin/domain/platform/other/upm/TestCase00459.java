package com.yonyou.nc.codevalidator.plugin.domain.platform.other.upm;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.UpmResourceQuery;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractUpmQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.executeresult.SuccessRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.UpmResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.sdk.upm.UpmComponentVO;
import com.yonyou.nc.codevalidator.sdk.upm.UpmModuleVO;

/**
 * �Ա�upm�ļ���nchome�е�upm�ļ�
 * 
 */
@RuleDefinition(catalog = CatalogEnum.OTHERCONFIGFILE, executePeriod = ExecutePeriod.COMPILE,
		executeLayer = ExecuteLayer.BUSICOMP, description = "�Ա�upm�ļ���nchome�е�upm�ļ�", memo = "", solution = "",
		subCatalog = SubCatalogEnum.OCF_UPM, coder = "songhchb", relatedIssueId = "459")
public class TestCase00459 extends AbstractUpmQueryRuleDefinition {
	@Override
	protected UpmResourceQuery getUpmResourceQuery(IRuleExecuteContext ruleExecContext) throws ResourceParserException {
		UpmResourceQuery query = new UpmResourceQuery();
		query.setBusinessComponent(ruleExecContext.getBusinessComponent());
		return query;
	}

	@Override
	protected IRuleExecuteResult processUpmRules(IRuleExecuteContext ruleExecContext, List<UpmResource> resources)
			throws ResourceParserException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		List<UpmResource> ncHomeResources = this.getResourceList(ruleExecContext.getRuntimeContext().getNcHome()
				+ "\\modules\\" + ruleExecContext.getBusinessComponent().getModule() + "\\META-INF");

		// ��������ncHome�����е�sourceName-source Map
		Map<String, UpmResource> ncHomeResourcesNames = new HashMap<String, UpmResource>();
		if (ncHomeResources == null || ncHomeResources.size() == 0) {
			// noteBuilder.append("��Ӧ��nchome��û��upm�ļ�");
			return new SuccessRuleExecuteResult(getIdentifier());
		} else {
			for (UpmResource ncHomeResource : ncHomeResources) {
				ncHomeResourcesNames.put(ncHomeResource.getResourceFileName(), ncHomeResource);
			}
		}

		for (UpmResource resource : resources) {
			StringBuilder noteBuilder = new StringBuilder();
			if (!ncHomeResourcesNames.containsKey(resource.getResourceFileName())) {
				noteBuilder.append("nchome���Ҳ�����Ӧ��upm�ļ�");
			} else {
				try {
					UpmResource ncHomeResource = ncHomeResourcesNames.get(resource.getResourceFileName());
					UpmModuleVO upmModuleVO = resource.getUpmModuleVo();
					UpmModuleVO ncHomeVO = ncHomeResource.getUpmModuleVo();

					List<UpmComponentVO> pubComponentVoList = upmModuleVO.getPubComponentVoList();
					List<UpmComponentVO> ncHomeVoList = ncHomeVO.getPubComponentVoList();

					// ����ӿ�����-ʵ��������map
					Map<String, String> map = new HashMap<String, String>();
					if (ncHomeVoList != null) {
						for (UpmComponentVO ncHomeVo : ncHomeVoList) {
							String interfaceName = ncHomeVo.getInterfaceName();
							String implementationName = ncHomeVo.getImplementationName();
							if (interfaceName != null && implementationName != null && !"".equals(interfaceName)
									&& !"".equals(implementationName)) {
								map.put(interfaceName, implementationName);
							}
						}
					}

					if (pubComponentVoList != null) {
						for (UpmComponentVO upmComponentVO : pubComponentVoList) {
							String interfaceName = upmComponentVO.getInterfaceName();
							String implementationName = upmComponentVO.getImplementationName();
							if (map == null || map.size() == 0) {
								noteBuilder.append("ncHome�µ���Դ�ļ�û�нӿں�ʵ�����ע��");
							}
							if (!map.containsKey(interfaceName)) {
								noteBuilder.append("ncHome��δ����" + interfaceName + "�ӿ�");
							} else {
								String tempImplementationName = map.get(interfaceName);
								if (!implementationName.equals(tempImplementationName)) {
									noteBuilder.append(interfaceName + "�ӿ���nchome��"
											+ ncHomeResource.getResourceFileName() + "�ļ��ж�Ӧ��ʵ���಻ͬ");
								}
							}
						}
					}
				} catch (ResourceParserException e1) {
					noteBuilder.append(String.format("Upm�ļ������쳣��������Ϣ: %s\n", e1.getMessage()));
				} finally {
					if (noteBuilder.toString().trim().length() > 0) {
						result.addResultElement(resource.getResourcePath(), noteBuilder.toString());
					}
				}
			}

		}
		return result;
	}

	private List<UpmResource> getResourceList(String businessComponentPath) {

		List<UpmResource> result = new ArrayList<UpmResource>();

		File file = new File(businessComponentPath);
		@SuppressWarnings("rawtypes")
		Collection upmFiles = FileUtils.listFiles(file, new String[] { "upm" }, true);

		for (Object upmFileObject : upmFiles) {
			File upmFile = (File) upmFileObject;
			UpmResource upmResource = new UpmResource();
			upmResource.setResourcePath(upmFile.getAbsolutePath());
			result.add(upmResource);
		}
		return result;
	}

}
