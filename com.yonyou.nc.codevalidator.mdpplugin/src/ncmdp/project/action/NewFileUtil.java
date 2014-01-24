package ncmdp.project.action;

import ncmdp.common.MDPConstants;
import ncmdp.model.JGraph;
import ncmdp.tool.basic.StringUtil;

public class NewFileUtil {
	/**
	 * 检查是否符合增量开发条件，返回错误信息
	 * 
	 * @param programCode
	 *            开发者编码
	 * @param industryCode
	 *            行业编码
	 * @param graph
	 *            当前组件
	 * @param programCode2
	 * 
	 * @return
	 */
	public static String checkNewIndFile(JGraph graph, String versiontype,
			String industryCode, String programCode) {
		StringBuffer errMsg = new StringBuffer();
		String oldVersionType = StringUtil.isEmptyWithTrim(graph
				.getVersionType()) ? MDPConstants.BASE_VERSIONTYPE : graph
				.getVersionType();// 之前的versiontype
		String curVersiontype = StringUtil.isEmptyWithTrim(versiontype) ? MDPConstants.BASE_VERSIONTYPE
				: versiontype;
		try {
			if (Integer.parseInt(curVersiontype) <= Integer
					.parseInt(oldVersionType)) {
				errMsg.append(Messages.NewFileUtil_0/* 当前的开发维度必须高于文件所在维度。当前维度： */
						+ curVersiontype + Messages.NewFileUtil_1/* .文件维度： */
						+ oldVersionType);
			}
		} catch (Exception e) {
			errMsg.append(Messages.NewFileUtil_2/* 开发维度格式错误，请输入数字，当前维度： */
					+ curVersiontype + Messages.NewFileUtil_3/* .文件维度： */
					+ oldVersionType);
		}
		return errMsg.toString();
	}
}
