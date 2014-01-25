package com.yonyou.nc.codevalidator.plugin.domain.mm.md.bpf;

import java.lang.reflect.Method;
import java.util.List;

import com.yonyou.nc.codevalidator.resparser.bpf.IMetaProcessFile;
import com.yonyou.nc.codevalidator.resparser.bpf.IOpInterface;
import com.yonyou.nc.codevalidator.resparser.bpf.IOperation;
import com.yonyou.nc.codevalidator.resparser.bpf.IParameter;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetaProcessResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.md.IType;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.rule.ClassLoaderUtilsFactory;
import com.yonyou.nc.codevalidator.sdk.rule.IClassLoaderUtils;

/**
 * ҵ����־bpf�ļ��У��ӿڷ������ƺͲ������ͱ���ʹ����нӿڲ�����ͬ
 * @author qiaoyanga
 * @since 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.COMPILE, catalog = CatalogEnum.METADATA, subCatalog = SubCatalogEnum.MD_BASESETTING,
description = "ҵ����־bpf�ļ��У��ӿڷ������ƺͲ������ͱ���ʹ����нӿڲ�����ͬ", solution = "", coder = "qiaoyang",
specialParamDefine = "",relatedIssueId = "693")
public class TestCase00693 extends AbstractMetaProcessResourceRuleDefinition{

	@Override
	protected IRuleExecuteResult processResources(
			List<MetadataResource> resources,
			IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		if (resources != null && resources.size() > 0) {
			for (MetadataResource resource : resources) {
				StringBuilder noteBuilder = new StringBuilder();
				IMetaProcessFile metadataFile = resource.getMetaProcessFile();
				List<IOpInterface> interfaces = metadataFile.getOpInterfaces();
				if (interfaces != null && interfaces.size() > 0) {
					for (IOpInterface interf : interfaces) {
						// �õ�ʵ������
						String implInterf = interf.getImplementationClassName();
						// �õ�ʵ����
						IClassLoaderUtils loder =  ClassLoaderUtilsFactory.getClassLoaderUtils();
						Class<?> cls = loder.loadClass(ruleExecContext.getBusinessComponent().getProjectName(), implInterf);
						if (cls == null) {
							noteBuilder.append(interf.getDisplayName() + "��δ����ʵ����\n");
						}
						
						// ������ת��list
						List<IOperation> operations = interf.getOperations();
						if (operations == null || operations.size() == 0) {
							noteBuilder.append(interf.getDisplayName() + "��δ���巽��\n");
						} else {
							for (IOperation operation : operations) {
								String operationName = operation.getName();
								IType type = operation.getReturnType();
								List<IParameter> parameters = operation.getParameters();
								// �˴���ѯ�������Ƿ�����˷������ҷ���ֵ�Ƿ���type���͵�
								// �õ����з���
								Method[] mthods = cls.getMethods();
								boolean isImpl = false;
								for (Method mthod : mthods) {
									if(operationName.equals(mthod.getName()) && type.getClass().equals(mthod.getReturnType().getClass()) && parameters.size() == mthod.getParameterTypes().length) {
										isImpl = true;
										break;
									}
								}
								if (!isImpl) {
									noteBuilder.append(interf.getDisplayName() + "����" + operationName + "δ��ʵ����\n");
								}
							}
						}
					}
//				} else {
//					noteBuilder.append("δ����ӿ�\n");
				}
				if(noteBuilder.length() > 0) {
					result.addResultElement(resource.getResourcePath(), noteBuilder.toString());
				}
			}
		}
		return result;
	}

}
