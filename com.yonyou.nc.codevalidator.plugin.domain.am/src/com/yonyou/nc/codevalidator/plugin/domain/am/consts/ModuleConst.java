package com.yonyou.nc.codevalidator.plugin.domain.am.consts;


import java.util.HashMap;
import java.util.Map;

/**
 * �ʲ���������ģ�鳣���ࡣ
 */
public class ModuleConst {

	/** �ո�����APģ�� */
	public static final String AP = "AP";
	
	/** �ո�����ARģ�� */
	public static final String AR = "AR";
	
	/** ���ƽ̨��FIPģ��*/
	public static final String FIP = "FIP";
	
	/** �ʲ�����FAģ�� */
	public static final String FA = "FA";
	
	/** �ʲ������ʲ����� */
	public static final String AMPUB = "AMPUB";
	
	/** �ʲ�����AIMģ�� */
	public static final String AIM = "AIM";
	
	/** �ʲ�����AUMģ�� */
	public static final String AUM = "AUM";
	
	/** �ʲ�����ALMģ�� */
	public static final String ALM = "ALM";
	/** �ʲ�����ALOģ�� */
	public static final String ALO = "ALO";
	/** �ʲ�����ALIģ�� */
	public static final String ALI = "ALI";

	
	/** ά��ά����EOMģ�� */
	public static final String EOM = "EOM";
	
	/** ά��ά����EMMģ�� */
	public static final String EMM = "EMM";
	
	/** ά��ά����EWMģ�� */
	public static final String EWM = "EWM";
	
	/**��ת�ģ��׺�Ʒ����ģ�� */
	public static final String RUM = "RUM";
	
	/**��ת�ģ���ת�����ģ�� */
	public static final String ROM = "ROM";
	
	/**��ת�ģ���ת������ģ�� */
	public static final String RLM = "RLM";
	
	
	/** ��Ӧ����ICģ�� */
	public static final String IC = "IC";
	
	/** ��Ӧ����PUģ�� */
	public static final String PU = "PO";
	
	/** Ԥ�㣺TBģ�� */
	public static final String TB = "TB";
	
	/** ���ˣ�GLģ�� */
	public static final String GL = "GL";
	
	/**�����ƣ����λ�� */
	public static final String RESA = "RESA";
	
	
	
	/** �����ƣ�RESAģ��  */
	public static final String RESA_FUNCCODE = "3820";
	
	/** �ո�����APģ�� */
	public static final String AP_FUNCCODE = "2008";
	
	/** ���ƽ̨��FIPģ��*/
	public static final String FIP_FUNCCODE = "1017";
	
	/** �ո�����ARģ�� */
	public static final String AR_FUNCCODE = "2006";
	
	/** �ʲ�����FAģ�� */
	public static final String FA_FUNCCODE = "2012";
	
	/** �ʲ����� */
	public static final String AM_FUNCCODE = "45";
	
	/** �ʲ������ʲ����� */
	public static final String AMPUB_FUNCCODE = "4501";
	
	/** �ʲ�����AIMģ�� */
	public static final String AIM_FUNCCODE = "4510";
	
	/** �ʲ�����AUMģ�� */
	public static final String AUM_FUNCCODE = "4520";
	
	/** �ʲ�����ALMģ�� */
	public static final String ALM_FUNCCODE = "4530";
	
	/** �ʲ�����ALOģ�� */
	public static final String ALO_FUNCCODE = "4530";
	
	/** �ʲ�����ALIģ�� */
	public static final String ALI_FUNCCODE = "4532";
	
	/** ά��ά����EOMģ�� */
	public static final String EOM_FUNCCODE = "4540";
	
	/** ά��ά����EMMģ�� */
	public static final String EMM_FUNCCODE = "4550";
	
	/** ά��ά����EWMģ�� */
	public static final String EWM_FUNCCODE = "4560";
	
	/**��ת�ģ��׺�Ʒ����ģ�� */
	public static final String RUM_FUNCCODE= "4580";
	
	/**��ת�ģ���ת�����ģ�� */
	public static final String ROM_FUNCCODE = "4583";
	
	/**��ת�ģ���ת������ģ�� */
	public static final String RLM_FUNCCODE = "4585";
	
	
	/** ��Ӧ����ICģ�� */
	public static final String IC_FUNCCODE = "4008";
	
	/** ��Ӧ����SCMPUBģ�� */
	public static final String SCMPUB_FUNCCODE = "4001";
	
	/** ��Ӧ����PUģ�� */
	public static final String PU_FUNCCODE = "4004";
	
	/** Ԥ�㣺TBģ�� */
	public static final String TB_FUNCCODE = "1812";
	
	/** ��Ŀ������Ŀ�ۺϹ���ģ�� */
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
