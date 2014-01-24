package ncmdp.util;

import ncmdp.model.Constant;

/**
 * ���һЩ�й���ҵ�������õ���Ϣ
 * @author wangxmn
 *
 */
public class MDPUtil {

	/**
	 * ����ʵ��
	 * @return
	 */
	private static MDPUtil_Real getInstance() {
		return MDPUtil_Real.getInstance();// ��׼�߼�
	}

	/**
	 * ���ص�ǰ��ҵ name
	 * 
	 * @return
	 */
	public static String getCurIndustryName() {
		return getInstance().getCurIndustryName();
	}

	/**
	 * ���غ�׺�� ��ʽ���� "_131"
	 */
	public static String getExtSuffix() {
		return getInstance().getExtSuffix();
	}

	/**
	 * ��õ�ǰ����ά��
	 * 
	 * @return
	 */
	public static String getDevVersionType() {
		return getInstance().getDevVersionType();
	}
	
	/**
	 * �����ҵ����
	 * 
	 * @return
	 */
	public static String getDevCode() {
		return getInstance().getDevIndustryCode();
	}

	/**
	 * �ж��Ƿ�Ϊbpf�ļ�
	 * @param filePath
	 * @return
	 */
	public static boolean isBpfInput(String filePath) {
		return filePath.endsWith(Constant.MDFILE_BPF_SUFFIX);
	}
}
