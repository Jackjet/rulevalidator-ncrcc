package ncmdp.cache;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.eclipse.core.resources.IProject;

import ncmdp.common.MDPConstants;
import ncmdp.project.BuildFileTreeUtil;
import ncmdp.tool.NCMDPTool;
import ncmdp.tool.basic.StringUtil;
import ncmdp.util.MDPUtil;
import ncmdp.util.ProjectUtil;

/**
 * 考虑行业的 实体id@Industry-> filePath map
 * @author dingxm
 */
public class EntityIDIndustryMap implements Serializable {

	private static final long serialVersionUID = 42L;

	private static final String tag = "@";

	HashMap<String, String> realMap = new HashMap<String, String>();

	public String get(String entityID) {
		String industry = MDPUtil.getDevCode();
		if(industry==null){
			return null;
		}
		return get(entityID, industry);
	}

	/**
	 * 获得entityID
	 * @param entityID
	 * @param industry
	 * @return
	 */
	public String get(String entityID, String industry) {
		String filePath = null;
		if (!MDPConstants.BASE_INDUSTRY.equalsIgnoreCase(industry)) {
			filePath = realMap.get(entityID + tag + industry);
		}
		if (StringUtil.isEmptyWithTrim(filePath)) {
			filePath = realMap.get(entityID + tag + MDPConstants.BASE_INDUSTRY);
		}
		if (StringUtil.isEmptyWithTrim(filePath)||!(new File(filePath).exists())) {
			//遍历查找
			IProject[] projects = NCMDPTool.getOpenedJavaProjects();
			int procount = projects == null ? 0 : projects.length;
			XPath xpath = XPathFactory.newInstance().newXPath();
			for (int i = 0; i < procount; i++) {
				ProjectUtil.analysisProject(projects[i], xpath, this);
				if (!MDPConstants.BASE_INDUSTRY.equalsIgnoreCase(industry)) {
					filePath = realMap.get(entityID + tag + industry);
				}
				if (StringUtil.isEmptyWithTrim(filePath)) {
					filePath = realMap.get(entityID + tag + MDPConstants.BASE_INDUSTRY);
				}
				if(filePath !=null){
					break;
				}
			}
			File[] refDir = BuildFileTreeUtil.getRefMDFiles();
			int count = refDir.length;
			if (count > 0) {
				for (int i = 0; i < count; i++) {
					File f = refDir[i];
					ProjectUtil.putInMapByPath(f, xpath, this);
					if (!MDPConstants.BASE_INDUSTRY.equalsIgnoreCase(industry)) {
						filePath = realMap.get(entityID + tag + industry);
					}
					if (StringUtil.isEmptyWithTrim(filePath)) {
						filePath = realMap.get(entityID + tag + MDPConstants.BASE_INDUSTRY);
					}
					if(filePath!=null){
						break;
					}
				}
			}
		}
		return filePath;
	}

	public void put(String entityID, String industry, String filePath) {
		if (StringUtil.isEmptyWithTrim(industry)) {
			industry = MDPConstants.BASE_INDUSTRY;
		}
		realMap.put(entityID + tag + MDPConstants.BASE_INDUSTRY, filePath);
	}

	public void clear() {
		realMap.clear();
	}
}
