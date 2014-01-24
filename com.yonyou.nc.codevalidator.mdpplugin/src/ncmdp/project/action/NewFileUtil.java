package ncmdp.project.action;

import ncmdp.common.MDPConstants;
import ncmdp.model.JGraph;
import ncmdp.tool.basic.StringUtil;

public class NewFileUtil {
	/**
	 * ����Ƿ���������������������ش�����Ϣ
	 * 
	 * @param programCode
	 *            �����߱���
	 * @param industryCode
	 *            ��ҵ����
	 * @param graph
	 *            ��ǰ���
	 * @param programCode2
	 * 
	 * @return
	 */
	public static String checkNewIndFile(JGraph graph, String versiontype,
			String industryCode, String programCode) {
		StringBuffer errMsg = new StringBuffer();
		String oldVersionType = StringUtil.isEmptyWithTrim(graph
				.getVersionType()) ? MDPConstants.BASE_VERSIONTYPE : graph
				.getVersionType();// ֮ǰ��versiontype
		String curVersiontype = StringUtil.isEmptyWithTrim(versiontype) ? MDPConstants.BASE_VERSIONTYPE
				: versiontype;
		try {
			if (Integer.parseInt(curVersiontype) <= Integer
					.parseInt(oldVersionType)) {
				errMsg.append(Messages.NewFileUtil_0/* ��ǰ�Ŀ���ά�ȱ�������ļ�����ά�ȡ���ǰά�ȣ� */
						+ curVersiontype + Messages.NewFileUtil_1/* .�ļ�ά�ȣ� */
						+ oldVersionType);
			}
		} catch (Exception e) {
			errMsg.append(Messages.NewFileUtil_2/* ����ά�ȸ�ʽ�������������֣���ǰά�ȣ� */
					+ curVersiontype + Messages.NewFileUtil_3/* .�ļ�ά�ȣ� */
					+ oldVersionType);
		}
		return errMsg.toString();
	}
}
