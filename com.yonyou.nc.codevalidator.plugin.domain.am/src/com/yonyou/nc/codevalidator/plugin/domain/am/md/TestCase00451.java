package com.yonyou.nc.codevalidator.plugin.domain.am.md;

import java.lang.reflect.Method;
import java.util.List;

import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetadataResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.md.IEntity;
import com.yonyou.nc.codevalidator.resparser.md.IMetadataFile;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.ScopeEnum;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.rule.ClassLoaderUtilsFactory;
import com.yonyou.nc.codevalidator.sdk.rule.IClassLoaderUtils;

/**
 * �ʲ�����Ԫ��������VOʱ�����Զ����ɵ�getMetaData()ȥ��
 * 
 * @author ZG
 */
@RuleDefinition(catalog = CatalogEnum.METADATA, description = "�ʲ�����Ԫ��������VOʱ�����Զ����ɵ�getMetaData()ȥ��",
		relatedIssueId = "451", subCatalog = SubCatalogEnum.MD_FUNCCHECK, coder = "zhangguang", scope = ScopeEnum.AM,
		executeLayer = ExecuteLayer.BUSICOMP, executePeriod = ExecutePeriod.COMPILE)
public class TestCase00451 extends AbstractMetadataResourceRuleDefinition {

	public static final String AM_SUPERVO_CLASS = "nc.vo.am.common.AMSuperVO";

	@Override
	protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		IClassLoaderUtils classLoaderUtils = ClassLoaderUtilsFactory.getClassLoaderUtils();
		if (resources != null && resources.size() > 0) {
			for (MetadataResource resource : resources) {
				StringBuilder stringBuilder = new StringBuilder();
				IMetadataFile metadataFile = resource.getMetadataFile();
				// ��ʵ��
				if (metadataFile.containsMainEntity()) {
					// ����ʵ��
					List<IEntity> entitys = metadataFile.getAllEntities();
					for (IEntity entity : entitys) {
						String voName = entity.getFullClassName();
						Class<?> voClass = classLoaderUtils.loadClass(ruleExecContext.getBusinessComponent()
								.getProjectName(), voName);
						if (classLoaderUtils.isExtendsParentClass(ruleExecContext.getBusinessComponent()
								.getProjectName(), voName, AM_SUPERVO_CLASS)) {
							Method[] methods = voClass.getDeclaredMethods();
							for (Method method : methods) {
								// �õ������������࣬���бȽϵ��Ǹ÷�����amsupervo�ķ����ǻ��Ǹ����Ƿ���
								// �����������getMetaData()��������Ϊû��ȥ��������һ��ȥ����
								if (method.getName().equals("getMetaData")) {
									stringBuilder.append("ʵ��(" + entity.getDisplayName()
											+ ") ��Ĭ�ϵ�getMetaData()����û��ȥ��! \n");
								}
							}
						}
					}
					if (stringBuilder.toString().length() > 0) {
						result.addResultElement(resource.getResourcePath(), stringBuilder.toString());
					}
				}
			}
		}
		return result;
	}

}
