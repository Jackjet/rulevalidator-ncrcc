package ncmdp.util;

import java.io.File;

import ncmdp.model.Attribute;
import ncmdp.model.Cell;
import ncmdp.model.JGraph;
import ncmdp.model.ValueObject;
import ncmdp.tool.NCMDPTool;

public class MDPCommonUtil {
	public static String prefix = "Features_";
	public static String suffix = ".xml";
	public static String path =  "/ierp/metadata/Features/";
	
	// �Ƿ�ԭʼ�ļ�������
	public static boolean attrCanModify(JGraph graph, Cell cell, Attribute atr) {
		boolean noModify = graph.isIndustryIncrease() && cell.isSource()
				&& atr.isSource();
		return !noModify;
	}
	/**
	 * ��������ļ�,��feature_uap.xml
	 * @param moduleName
	 * @return
	 */
	public static File getFeatureFile(String moduleName){
		String ncHome = NCMDPTool.getNCHome();
		String filename = prefix + moduleName + suffix;
		return new File(ncHome, path + filename);
	}
	
	public static File getFeatureFile(ValueObject model){
		String name = model.getGraph().getOwnModule();
		return getFeatureFile(name);
	}
}
