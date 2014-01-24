package ncmdp.util;

import ncmdp.common.MDPConstants;
import ncmdp.tool.basic.StringUtil;

import org.eclipse.jface.dialogs.MessageDialog;

public class MDPUtil_Real {

	private static MDPUtil_Real mdpUtil = new MDPUtil_Real();

//	private String developerLay = null;
//	private String curIndustryName = null;
//	private String _extSuffix = null;
//	private String extSuffix = null;
//	private String curIndustry = null;
//	private String curIndustryPK = null;
//	private String devCode = null;
	public static MDPUtil_Real getInstance() {
		return mdpUtil;
	}

	/**
	 * ���ص�ǰ��ҵ name
	 * 
	 * @return
	 */
	public String getCurIndustryName() {
//		if(curIndustryName == null){
//			return DevelopPubServiceFactory.getService().getIndustryLabel(
//					getCurIndustryPriPK());
//		}
//		return curIndustryName;
		return null;
	}

	/**
	 * ���غ�׺�� ��ʽ���� "_131"
	 */
	public String getExtSuffix() {
			String _extSuffix = getExtSuffixSource();
			if (!MDPConstants.BASE_VERSIONTYPE.equalsIgnoreCase(getDevVersionType())) {
				_extSuffix = "_" + _extSuffix;
			}
		return _extSuffix;
	}

	/**
	 * ���غ�׺�� ��ʽ���� "131",���Ϊ������ҵ�򷵻�""
	 */
	private String getExtSuffixSource() {
//			IDevelopPubService service = DevelopPubServiceFactory.getService();
//			if (service != null) {
//				return service.getExtSuffix();
//			}else{
//				return null;
//			}
			return "";
	}

	/**
	 * ��õ�ǰ����ά��
	 * 
	 * @return
	 */
	public String getDevVersionType() {
//		try {
//			Developer dev = DevelopPubServiceFactory.getService().getDeveloper();
//			if (dev == null) {
//				return null;
//			}else{
//				String developerLay = dev.getAssetlayer();
//				if (StringUtil.isEmptyWithTrim(developerLay)) {
//					developerLay = MDPConstants.BASE_VERSIONTYPE;
//				}
//				return developerLay;
//			}
//		} catch (Exception e) {
//			MessageDialog.openError(null, "error",
//					"Error to get develop hiberarchy ,please check the MDE setting:"
//							+ e);
//			return MDPConstants.BASE_VERSIONTYPE;
//		}
		return MDPConstants.BASE_VERSIONTYPE;
	}
	
	/**
	 * ��õ�ǰ��ҵ������
	 * @return
	 */
	public String getCurIndustry() {
//		// ��ǰ��ҵ
//		String curIndustry = DevelopPubServiceFactory.getService()
//				.getIndustryCode(getCurIndustryPriPK());
//		if (StringUtil.isEmptyWithTrim(curIndustry)) {
//				curIndustry = MDPConstants.BASE_INDUSTRY;
//			}
//		return curIndustry;
		return MDPConstants.BASE_INDUSTRY;
	}

	/**
	 * ��õ�ǰ�Ŀ�������������ҵ����
	 * @return
	 */
	private String getCurIndustryPriPK() {
//			IDevelopPubService service = DevelopPubServiceFactory.getService();
//			if(service==null){
//				return null;
//			}else{
//				Developer dev = service.getDeveloper();
//				if (dev == null) {
//					return null;
//				}
//				return dev.getPk_industry();
//			}
		return null;
	}

	/**
	 * ��ÿ�������������ҵ���룬��06,32��
	 * 
	 * @return
	 */
	public String getDevIndustryCode() {
		try {
			String devCode = getExtSuffixSource();
			if (!StringUtil.isEmptyWithTrim(devCode) && devCode.length() > 2) {
				return devCode.substring(devCode.length() - 2, devCode.length());
			} else {
				return MDPConstants.BASE_INDUSTRY;
			}
		} catch (Exception e) {
			MDPLogger.error(e.getMessage(),e);
			MessageDialog.openError(null, "error",
					"Error to get developer code ,please check the MDE setting:"
							+ e);
			return "";
		}
	}
}
