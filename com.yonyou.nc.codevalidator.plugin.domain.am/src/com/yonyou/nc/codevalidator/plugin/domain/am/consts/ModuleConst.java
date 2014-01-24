package com.yonyou.nc.codevalidator.plugin.domain.am.consts;


import java.util.HashMap;
import java.util.Map;

/**
 * 资产管理引用模块常量类。
 */
public class ModuleConst {

	/** 收付报：AP模块 */
	public static final String AP = "AP";
	
	/** 收付报：AR模块 */
	public static final String AR = "AR";
	
	/** 会计平台：FIP模块*/
	public static final String FIP = "FIP";
	
	/** 资产管理：FA模块 */
	public static final String FA = "FA";
	
	/** 资产管理：资产公共 */
	public static final String AMPUB = "AMPUB";
	
	/** 资产管理：AIM模块 */
	public static final String AIM = "AIM";
	
	/** 资产管理：AUM模块 */
	public static final String AUM = "AUM";
	
	/** 资产管理：ALM模块 */
	public static final String ALM = "ALM";
	/** 资产管理：ALO模块 */
	public static final String ALO = "ALO";
	/** 资产管理：ALI模块 */
	public static final String ALI = "ALI";

	
	/** 维修维护：EOM模块 */
	public static final String EOM = "EOM";
	
	/** 维修维护：EMM模块 */
	public static final String EMM = "EMM";
	
	/** 维修维护：EWM模块 */
	public static final String EWM = "EWM";
	
	/**周转材：易耗品管理模块 */
	public static final String RUM = "RUM";
	
	/**周转材：周转材租出模块 */
	public static final String ROM = "ROM";
	
	/**周转材：周转材租入模块 */
	public static final String RLM = "RLM";
	
	
	/** 供应链：IC模块 */
	public static final String IC = "IC";
	
	/** 供应链：PU模块 */
	public static final String PU = "PO";
	
	/** 预算：TB模块 */
	public static final String TB = "TB";
	
	/** 总账：GL模块 */
	public static final String GL = "GL";
	
	/**管理会计：责任会计 */
	public static final String RESA = "RESA";
	
	
	
	/** 管理会计：RESA模块  */
	public static final String RESA_FUNCCODE = "3820";
	
	/** 收付报：AP模块 */
	public static final String AP_FUNCCODE = "2008";
	
	/** 会计平台：FIP模块*/
	public static final String FIP_FUNCCODE = "1017";
	
	/** 收付报：AR模块 */
	public static final String AR_FUNCCODE = "2006";
	
	/** 资产管理：FA模块 */
	public static final String FA_FUNCCODE = "2012";
	
	/** 资产管理 */
	public static final String AM_FUNCCODE = "45";
	
	/** 资产管理：资产公共 */
	public static final String AMPUB_FUNCCODE = "4501";
	
	/** 资产管理：AIM模块 */
	public static final String AIM_FUNCCODE = "4510";
	
	/** 资产管理：AUM模块 */
	public static final String AUM_FUNCCODE = "4520";
	
	/** 资产管理：ALM模块 */
	public static final String ALM_FUNCCODE = "4530";
	
	/** 资产管理：ALO模块 */
	public static final String ALO_FUNCCODE = "4530";
	
	/** 资产管理：ALI模块 */
	public static final String ALI_FUNCCODE = "4532";
	
	/** 维修维护：EOM模块 */
	public static final String EOM_FUNCCODE = "4540";
	
	/** 维修维护：EMM模块 */
	public static final String EMM_FUNCCODE = "4550";
	
	/** 维修维护：EWM模块 */
	public static final String EWM_FUNCCODE = "4560";
	
	/**周转材：易耗品管理模块 */
	public static final String RUM_FUNCCODE= "4580";
	
	/**周转材：周转材租出模块 */
	public static final String ROM_FUNCCODE = "4583";
	
	/**周转材：周转材租入模块 */
	public static final String RLM_FUNCCODE = "4585";
	
	
	/** 供应链：IC模块 */
	public static final String IC_FUNCCODE = "4008";
	
	/** 供应链：SCMPUB模块 */
	public static final String SCMPUB_FUNCCODE = "4001";
	
	/** 供应链：PU模块 */
	public static final String PU_FUNCCODE = "4004";
	
	/** 预算：TB模块 */
	public static final String TB_FUNCCODE = "1812";
	
	/** 项目管理：项目综合管理模块 */
	public static final String PIM_FUNCCODE = "4810";
	

	private static Map<String, String> funCodeToModuleCodeMap = null;
	
	private static Map<String, String> moduleCodeTofunCodeMap = null;
	
	static {
		funCodeToModuleCodeMap = new HashMap<String, String>();
		moduleCodeTofunCodeMap = new HashMap<String, String>();
		funCodeToModuleCodeMap.put(AMPUB_FUNCCODE, AMPUB);
		
		funCodeToModuleCodeMap.put(AIM_FUNCCODE, AIM);
		funCodeToModuleCodeMap.put(AUM_FUNCCODE, AUM);
		funCodeToModuleCodeMap.put(ALM_FUNCCODE, ALM);
		funCodeToModuleCodeMap.put(ALO_FUNCCODE, ALO);
		funCodeToModuleCodeMap.put(ALI_FUNCCODE, ALI);
		
		funCodeToModuleCodeMap.put(FA_FUNCCODE, FA);
		
		funCodeToModuleCodeMap.put(EOM_FUNCCODE, EOM);
		funCodeToModuleCodeMap.put(EMM_FUNCCODE, EMM);
		funCodeToModuleCodeMap.put(EWM_FUNCCODE, EWM);
		
		funCodeToModuleCodeMap.put(RUM_FUNCCODE, RUM);
		funCodeToModuleCodeMap.put(ROM_FUNCCODE, ROM);
		funCodeToModuleCodeMap.put(RLM_FUNCCODE, RLM);
		
		moduleCodeTofunCodeMap.put(AMPUB,AMPUB_FUNCCODE);
		
		moduleCodeTofunCodeMap.put(ALM, ALM_FUNCCODE);
		moduleCodeTofunCodeMap.put(AIM, AIM_FUNCCODE);
		moduleCodeTofunCodeMap.put(AUM, AUM_FUNCCODE);
		moduleCodeTofunCodeMap.put(ALO, ALO_FUNCCODE);
		moduleCodeTofunCodeMap.put(ALI, ALI_FUNCCODE);
		
		
		moduleCodeTofunCodeMap.put(FA,FA_FUNCCODE);
		
		moduleCodeTofunCodeMap.put(EOM,EOM_FUNCCODE);
		moduleCodeTofunCodeMap.put(EMM,EMM_FUNCCODE);
		moduleCodeTofunCodeMap.put(EWM,EWM_FUNCCODE);
		
		moduleCodeTofunCodeMap.put(RUM,RUM_FUNCCODE);
		moduleCodeTofunCodeMap.put(ROM,ROM_FUNCCODE);
		moduleCodeTofunCodeMap.put(RLM,RLM_FUNCCODE);
		
	}
	
	public static String getModuleCodeByFunCode(String funCode) {
		return funCodeToModuleCodeMap.get(funCode);
	}
	
	public static String getFuncCodeByModuleCode(String moduleCode) {
		return moduleCodeTofunCodeMap.get(moduleCode);
	}
}
