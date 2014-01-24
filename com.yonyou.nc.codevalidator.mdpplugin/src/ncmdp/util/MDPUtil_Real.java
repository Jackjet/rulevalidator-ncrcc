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
	 * 返回当前行业 name
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
	 * 返回后缀名 格式类似 "_131"
	 */
	public String getExtSuffix() {
			String _extSuffix = getExtSuffixSource();
			if (!MDPConstants.BASE_VERSIONTYPE.equalsIgnoreCase(getDevVersionType())) {
				_extSuffix = "_" + _extSuffix;
			}
		return _extSuffix;
	}

	/**
	 * 返回后缀名 格式类似 "131",如果为基础行业则返回""
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
	 * 获得当前开发维度
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
	 * 获得当前行业的名称
	 * @return
	 */
	public String getCurIndustry() {
//		// 当前行业
//		String curIndustry = DevelopPubServiceFactory.getService()
//				.getIndustryCode(getCurIndustryPriPK());
//		if (StringUtil.isEmptyWithTrim(curIndustry)) {
//				curIndustry = MDPConstants.BASE_INDUSTRY;
//			}
//		return curIndustry;
		return MDPConstants.BASE_INDUSTRY;
	}

	/**
	 * 获得当前的开发者所属的行业编码
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
	 * 获得开发者所属的行业代码，如06,32等
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
