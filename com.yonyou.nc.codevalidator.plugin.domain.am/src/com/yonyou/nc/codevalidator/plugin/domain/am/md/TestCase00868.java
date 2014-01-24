package com.yonyou.nc.codevalidator.plugin.domain.am.md;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.yonyou.nc.codevalidator.resparser.AopResourceQuery;
import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.bpf.IBusiOperator;
import com.yonyou.nc.codevalidator.resparser.bpf.IMetaProcessFile;
import com.yonyou.nc.codevalidator.resparser.bpf.IRefOperation;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetadataResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ErrorRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.executeresult.SuccessRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.AopResource;
import com.yonyou.nc.codevalidator.resparser.resource.MetaResType;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.aop.AopAspectVO;
import com.yonyou.nc.codevalidator.sdk.aop.AopModuleVO;
import com.yonyou.nc.codevalidator.sdk.utils.StringUtils;

/**
 * 
 * @author zhangnane
 * 
 */
@RuleDefinition(catalog = CatalogEnum.METADATA, description = "检测日志注册正确", relatedIssueId = "868",
		subCatalog = SubCatalogEnum.MD_BASESETTING, coder = "zhangnane", executeLayer = ExecuteLayer.BUSICOMP,
		executePeriod = ExecutePeriod.CHECKOUT)
public class TestCase00868 extends AbstractMetadataResourceRuleDefinition {

	// 认为以此开头的class用来记录日志
	private static final String BUSILOGINTF = "nc.bs.aop.log";

	/**
	 * 获取AOP资源
	 * 
	 * @param ruleExecContext
	 * @return
	 * @throws RuleBaseException
	 */
	public List<AopResource> getAopResource(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		AopResourceQuery aopResourceQuery = new AopResourceQuery();
		aopResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
		List<AopResource> resources = ResourceManagerFacade.getResource(aopResourceQuery);
		return resources;
	}

	@Override
	protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		// 用于检测bpf文件和Aop文件中关于记录日志接口的不同
		Map<String, Set<String>> compareMap = new HashMap<String, Set<String>>();
		// 循环资源文件
		for (MetadataResource resource : resources) {
			IMetaProcessFile processFile = resource.getMetaProcessFile();
			List<IBusiOperator> busiOperators = processFile.getBusiOperators();
			// 循环业务操作
			for (IBusiOperator busiOperator : busiOperators) {
				if (busiOperator.isNeedLog()) {
					List<IRefOperation> refOperatrions = busiOperator.getRefOperations();
					// 循环业务操作内的"引用操作"
					for (IRefOperation refOperatrion : refOperatrions) {
						String interfaceNameBpf = refOperatrion.getRefOptInterface().getInterfaceName();
						if (compareMap.containsKey(interfaceNameBpf)) {
							compareMap.get(interfaceNameBpf).add(resource.getResourceFileName());
						} else {
							Set<String> compareSet = new HashSet<String>();
							compareSet.add(resource.getResourceFileName());
							compareMap.put(interfaceNameBpf, compareSet);
						}
					}
				}
			}
		}
		List<AopResource> aopResources = getAopResource(ruleExecContext);
		if (aopResources != null && aopResources.size() > 0) {
			for (AopResource aopResource : aopResources) {
				AopModuleVO aopmoduleVO = aopResource.getAopModuleVo();
				List<AopAspectVO> aopAspects = aopmoduleVO.getAopAspectVoList();
				// 循环aop中注册的接口
				for (AopAspectVO aopAspect : aopAspects) {
					// 如果以特定字符开始认为是记录日志
					if (aopAspect.getImplemenationClass().startsWith(BUSILOGINTF)) {
						String InterfaceNameAop = aopAspect.getComponentInterface();
						if (compareMap.get(InterfaceNameAop) != null) {
							compareMap.get(InterfaceNameAop).add(aopResource.getResourceFileName());
						} else {
							Set<String> compareSet = new HashSet<String>();
							compareSet.add(aopResource.getResourceFileName());
							compareMap.put(InterfaceNameAop, compareSet);
						}
					}
				}
			}
		}
		StringBuilder noteBuilder = dealCompareMap(compareMap);
		// 使用此方法是想进行特定的错误信息提示
		return StringUtils.isBlank(noteBuilder.toString()) ? new SuccessRuleExecuteResult(getIdentifier())
				: new ErrorRuleExecuteResult(getIdentifier(), noteBuilder.toString());
	}

	/**
	 * 处理结果供输出
	 * 
	 * @param compareMap
	 * @return
	 */
	private StringBuilder dealCompareMap(Map<String, Set<String>> compareMap) {
		Iterator<Entry<String, Set<String>>> compareMapItr = compareMap.entrySet().iterator();
		StringBuilder noteBuilder = new StringBuilder();
		while (compareMapItr.hasNext()) {
			Entry<String, Set<String>> comparetemp = compareMapItr.next();
			Set<String> compareSet = comparetemp.getValue();
			if (compareSet.size() == 1) {
				String singleResult = compareSet.iterator().next();
				if (singleResult.endsWith("aop")) {
					noteBuilder.append(String.format("\n接口：%s 的日志缺少bfp文件的记录日志，只在文件%s中注册了日志", comparetemp.getKey(),
							singleResult));
				} else if (singleResult.endsWith("bpf")) {
					noteBuilder.append(String.format("\n接口：%s 的日志缺少aop文件的注册，只在文件%s中记录了日志", comparetemp.getKey(),
							singleResult));
				}
			}
		}
		return noteBuilder;
	}

	/**
	 * 重写次方法用于获取bpf文件
	 */
	@Override
	protected MetaResType getMetaResType() {
		return MetaResType.BPF;
	}

}
