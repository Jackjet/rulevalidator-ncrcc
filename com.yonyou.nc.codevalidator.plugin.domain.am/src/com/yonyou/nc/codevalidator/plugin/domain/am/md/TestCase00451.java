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
 * 资产方面元数据生成VO时，把自动生成的getMetaData()去掉
 * 
 * @author ZG
 */
@RuleDefinition(catalog = CatalogEnum.METADATA, description = "资产方面元数据生成VO时，把自动生成的getMetaData()去掉",
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
				// 主实体
				if (metadataFile.containsMainEntity()) {
					// 所有实体
					List<IEntity> entitys = metadataFile.getAllEntities();
					for (IEntity entity : entitys) {
						String voName = entity.getFullClassName();
						Class<?> voClass = classLoaderUtils.loadClass(ruleExecContext.getBusinessComponent()
								.getProjectName(), voName);
						if (classLoaderUtils.isExtendsParentClass(ruleExecContext.getBusinessComponent()
								.getProjectName(), voName, AM_SUPERVO_CLASS)) {
							Method[] methods = voClass.getDeclaredMethods();
							for (Method method : methods) {
								// 得到方法所属的类，进行比较的是该方法是amsupervo的方法是还是该类是方法
								// 如果本类中有getMetaData()方法则认为没有去掉，否则一经去掉了
								if (method.getName().equals("getMetaData")) {
									stringBuilder.append("实体(" + entity.getDisplayName()
											+ ") 中默认的getMetaData()方法没有去掉! \n");
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
