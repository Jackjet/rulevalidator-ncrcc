package ncmdp.util;

import ncmdp.model.Constant;

/**
 * 获得一些有关行业开发配置的信息
 * @author wangxmn
 *
 */
public class MDPUtil {

	/**
	 * 返回实例
	 * @return
	 */
	private static MDPUtil_Real getInstance() {
		return MDPUtil_Real.getInstance();// 标准逻辑
	}

	/**
	 * 返回当前行业 name
	 * 
	 * @return
	 */
	public static String getCurIndustryName() {
		return getInstance().getCurIndustryName();
	}

	/**
	 * 返回后缀名 格式类似 "_131"
	 */
	public static String getExtSuffix() {
		return getInstance().getExtSuffix();
	}

	/**
	 * 获得当前开发维度
	 * 
	 * @return
	 */
	public static String getDevVersionType() {
		return getInstance().getDevVersionType();
	}
	
	/**
	 * 获得行业代码
	 * 
	 * @return
	 */
	public static String getDevCode() {
		return getInstance().getDevIndustryCode();
	}

	/**
	 * 判断是否为bpf文件
	 * @param filePath
	 * @return
	 */
	public static boolean isBpfInput(String filePath) {
		return filePath.endsWith(Constant.MDFILE_BPF_SUFFIX);
	}
}
