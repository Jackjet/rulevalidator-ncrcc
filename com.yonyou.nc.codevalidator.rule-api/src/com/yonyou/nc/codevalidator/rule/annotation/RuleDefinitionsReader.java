package com.yonyou.nc.codevalidator.rule.annotation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.framework.ServiceReference;

import com.yonyou.nc.codevalidator.rule.Activator;
import com.yonyou.nc.codevalidator.rule.IRuleDefinition;
import com.yonyou.nc.codevalidator.rule.executor.IRuleExecutorFactory;
import com.yonyou.nc.codevalidator.rule.vo.CommonParamConfiguration;
import com.yonyou.nc.codevalidator.rule.vo.ParamConfiguration;
import com.yonyou.nc.codevalidator.rule.vo.PrivateParamConfiguration;

/**
 * �������ȡ��
 * 
 * @author luoweid
 * @modify mazhqa - ��OSGi������ʹ�����ֵ���������������һЩ��̬������ʵ�ǱȽ�Σ�յģ���Ϊ���Ҫ���Ǹ����ʵ����ʼ��ʱ�����⣬
 *         ��������ʼ����ʱ��Ƚ��磬���������в�û�м������еķ�����ͻ���ɷ���Ķ�ʧ
 */
public final class RuleDefinitionsReader {

	private List<RuleDefinitionAnnotationVO> ruleDefinitionList = new ArrayList<RuleDefinitionAnnotationVO>();
	private Map<String, RuleDefinitionAnnotationVO> ruleIdToDefMap = new HashMap<String, RuleDefinitionAnnotationVO>();
	private Map<String, CommonParamConfiguration> classToCommonParamMap = new ConcurrentHashMap<String, CommonParamConfiguration>();

	private static RuleDefinitionsReader instance = null;

	public static RuleDefinitionsReader getInstance() {
		if (instance == null) {
			synchronized (RuleDefinitionsReader.class) {
				if (null == instance) {
					instance = new RuleDefinitionsReader();
				}
			}
		}
		return instance;
	}

	private IRuleExecutorFactory getRuleExecutorFactory() {
		ServiceReference<IRuleExecutorFactory> serviceReference = Activator.getBundleContext().getServiceReference(
				IRuleExecutorFactory.class);
		return Activator.getBundleContext().getService(serviceReference);
	}

	private RuleDefinitionsReader() {
		IRuleExecutorFactory ruleExecutorFactory = getRuleExecutorFactory();
		Map<String, IRuleDefinition> ruleDefMap = ruleExecutorFactory.getIdentifierToDefinitionMap();
		for (Iterator<IRuleDefinition> iterator = ruleDefMap.values().iterator(); iterator.hasNext();) {
			IRuleDefinition ruleConfigDefinition = iterator.next();
			Class<? extends IRuleDefinition> ruleDefinitionClass = ruleConfigDefinition.getClass();
			if (ruleDefinitionClass.isAnnotationPresent(RuleDefinition.class)) {
				RuleDefinitionAnnotationVO ruleDefinitionVo = loadRuleDefinitionVo(ruleDefinitionClass);
				ruleDefinitionList.add(ruleDefinitionVo);
				ruleIdToDefMap.put(ruleDefinitionClass.getName(), ruleDefinitionVo);
			}
		}
	}

	private RuleDefinitionAnnotationVO loadRuleDefinitionVo(Class<? extends IRuleDefinition> ruleDefinitionClass) {
		RuleDefinitionAnnotationVO ruleDefinitionVO = new RuleDefinitionAnnotationVO(ruleDefinitionClass.getName());
		RuleDefinition ruleDefinitionAnnotation = ruleDefinitionClass.getAnnotation(RuleDefinition.class);
		ruleDefinitionVO.setCatalog(ruleDefinitionAnnotation.catalog());
		ruleDefinitionVO.setSubCatalog(ruleDefinitionAnnotation.subCatalog());
		ruleDefinitionVO.setCheckRole(ruleDefinitionAnnotation.checkRole());
		ruleDefinitionVO.setCoder(ruleDefinitionAnnotation.coder());
		ruleDefinitionVO.setDescription(String.format("[%s] %s", ruleDefinitionAnnotation.coder(),
				ruleDefinitionAnnotation.description()));
		ruleDefinitionVO.setMemo(ruleDefinitionAnnotation.memo());
		ruleDefinitionVO.setScope(ruleDefinitionAnnotation.scope());
		ruleDefinitionVO.setPrivateParamConfiguration(getPrivateParamConfiguration(ruleDefinitionAnnotation
				.specialParamDefine()));
		ruleDefinitionVO.setSolution(ruleDefinitionAnnotation.solution());
		ruleDefinitionVO.setRepairLevel(ruleDefinitionAnnotation.repairLevel());
		ruleDefinitionVO.setRelatedIssueId(ruleDefinitionAnnotation.relatedIssueId());
		ruleDefinitionVO.setExecuteLayer(ruleDefinitionAnnotation.executeLayer());
		ruleDefinitionVO.setExecutePeriod(ruleDefinitionAnnotation.executePeriod());
		ruleDefinitionVO.setCommonParamConfiguration(getCommonParamConfiguration(ruleDefinitionClass));
		return ruleDefinitionVO;
	}

	private CommonParamConfiguration getCommonParamConfiguration(Class<? extends IRuleDefinition> ruleDefinitionClass) {
		if (classToCommonParamMap.get(ruleDefinitionClass) != null) {
			return classToCommonParamMap.get(ruleDefinitionClass);
		}
		CommonParamConfiguration commonParamConfiguration = new CommonParamConfiguration();
		Class<?> itemClass = ruleDefinitionClass;
		// �ݲ�֧�ֲ���������֮��ĸ��ǣ���Ҫ������
		while (itemClass != null && itemClass != Object.class) {
			if (itemClass.isAnnotationPresent(PublicRuleDefinitionParam.class)) {
				PublicRuleDefinitionParam pubParamAnnotation = itemClass.getAnnotation(PublicRuleDefinitionParam.class);
				List<ParamConfiguration> processParamConfigurations = processParamConfigurations(pubParamAnnotation
						.params());
				for (ParamConfiguration paramConfiguration : processParamConfigurations) {
					commonParamConfiguration.addParamConfig(paramConfiguration);
				}
			}
			itemClass = itemClass.getSuperclass();
		}
		classToCommonParamMap.put(itemClass.getName(), commonParamConfiguration);
		return commonParamConfiguration;
	}

	private List<ParamConfiguration> processParamConfigurations(String[] params) {
		List<ParamConfiguration> result = new ArrayList<ParamConfiguration>();
		if (params != null && params.length > 0) {
			for (String param : params) {
				if (param != null && param.trim().length() > 0) {
					result.add(ParamConfiguration.parseConfig(param));
				}
			}
		}
		return result;
	}

	private PrivateParamConfiguration getPrivateParamConfiguration(String[] specialParamDefine) {
		List<ParamConfiguration> processParamConfigurations = processParamConfigurations(specialParamDefine);
		PrivateParamConfiguration privateParamConfiguration = new PrivateParamConfiguration();
		for (ParamConfiguration paramConfiguration : processParamConfigurations) {
			privateParamConfiguration.addParamName(paramConfiguration);
		}
		return privateParamConfiguration;
	}

	/**
	 * �õ���ǰ���������й����Map
	 * 
	 * @return
	 */
	public Map<String, RuleDefinitionAnnotationVO> getAllRuleDefinitionMap() {
		return Collections.unmodifiableMap(ruleIdToDefMap);
	}

	public List<RuleDefinitionAnnotationVO> getAllDefinitionVos() {
		List<RuleDefinitionAnnotationVO> ruleDefinitionVos = new ArrayList<RuleDefinitionAnnotationVO>(
				ruleIdToDefMap.values());
		Collections.sort(ruleDefinitionVos);
		return Collections.unmodifiableList(ruleDefinitionVos);
	}

	/**
	 * ��ȡָ������Ķ�����Ϣ
	 * 
	 * @param clz
	 * @return
	 */
	public RuleDefinitionAnnotationVO getRuleDefinitionVO(Class<IRuleDefinition> clz) {
		return getRuleDefinitionVO(clz.getName());
	}

	/**
	 * ��ȡָ������Ķ�����Ϣ
	 * 
	 * @param clzName
	 *            ���������
	 * @return
	 */
	public RuleDefinitionAnnotationVO getRuleDefinitionVO(String clzName) {
		return ruleIdToDefMap.get(clzName);
	}

	/**
	 * ��ȡ���е�ȫ�ֹ�������
	 * 
	 * @return
	 */
	public String[] getPublicParam() {
		Set<String> result = new HashSet<String>();
		for (CommonParamConfiguration commonParams : classToCommonParamMap.values()) {
			for (ParamConfiguration paramConfiguration : commonParams.getParamConfigurationList()) {
				result.add(paramConfiguration.getParamName());
			}
		}
		return result.toArray(new String[0]);
	}

	/**
	 * ��ȡָ�������Ӧ�Ĺ�������
	 * 
	 * @param clz
	 * @return
	 */
	public String[] getPublicParamByRule(Class<IRuleDefinition>... clzs) {
		List<String> ruleDefinitionClassNameList = new ArrayList<String>();
		if (clzs != null) {
			for (Class<?> clz : clzs) {
				ruleDefinitionClassNameList.add(clz.getName());
			}
		}
		return getPublicParamByRule(ruleDefinitionClassNameList.toArray(new String[0]));
	}

	/**
	 * ��ȡָ�������Ӧ�Ĺ�������
	 * 
	 * @param clz
	 * @return
	 */
	public String[] getPublicParamByRule(String... clzNames) {
		if (clzNames == null || clzNames.length == 0) {
			return new String[0];
		}
		Set<String> result = new HashSet<String>();
		for (String clzName : clzNames) {
			CommonParamConfiguration commonParamConfiguration = classToCommonParamMap.get(clzName);
			for (ParamConfiguration paramConfiguration : commonParamConfiguration.getParamConfigurationList()) {
				result.add(paramConfiguration.getParamName());
			}
		}
		return result.toArray(new String[0]);
	}

}
