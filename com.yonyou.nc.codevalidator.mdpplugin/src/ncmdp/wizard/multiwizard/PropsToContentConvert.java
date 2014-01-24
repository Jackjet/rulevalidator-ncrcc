package ncmdp.wizard.multiwizard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ncmdp.editor.NCMDPEditor;
import ncmdp.tool.basic.StringUtil;
import ncmdp.util.MDPLogger;
import ncmdp.wizard.multiwizard.util.CommonUtil;
import ncmdp.wizard.multiwizard.util.IMultiElement;
import ncmdp.wizard.multiwizard.util.MultiUtils;
import ncmdp.wizard.multiwizard.util.UTFProperties;

public class PropsToContentConvert {

	private int curKeyValue = 0;

	private String preResID = null;

	private String curIndex;

	//默认资源文件
	UTFProperties defProp = null;

	//元素的显示名 - MultiContentVO 的映射
	private Map<String, MultiContentVO> name_content_map = new HashMap<String, MultiContentVO>();

	//默认资源文件 中的 value- key的映射
	private Map<String, String> value_key_propfileMap_def = new HashMap<String, String>();

	//	common资源文件 中的 value- key的映射
	private Map<String, String> value_key_propfileMap_commmon = new HashMap<String, String>();

	/**本次操作依赖的common模块*/
	List<String> depCommonModule = null;

	public PropsToContentConvert(String preResID, LangPathVO defLangVO, List<String> depCommonModule) {
		this.preResID = preResID.endsWith("-") ? preResID.substring(0, preResID.length() - 1) //$NON-NLS-1$
				: preResID;
		if (preResID.length() <= 8) {
			curIndex = "000000"; //$NON-NLS-1$
		} else if (preResID.length() == 9) {
			curIndex = "00000"; //$NON-NLS-1$
		} else if (preResID.length() == 10) {
			curIndex = "0000"; //$NON-NLS-1$
		}

		this.depCommonModule = depCommonModule;
		curKeyValue = 0;
		initProp(defLangVO);
	}

	/**
	 * 根据 原始文件内容 和 multiElements  解析出 MultiContent 
	 * @param multiEles
	 * @return
	 */
	public List<MultiContentVO> getContents(List<IMultiElement> multiEles) {
		if (multiEles == null || multiEles.size() == 0) { return null; }

		name_content_map.clear();
		for (IMultiElement ele : multiEles) {
			if (StringUtil.isEmptyWithTrim(ele.getDisplayName())) {
				continue;
			}
			MultiContentVO content = new MultiContentVO(ele.getDisplayName());
			name_content_map.put(ele.getDisplayName(), content);
		}

		for (Object key : defProp.keySet()) {//遍历多语资源文件中的key 
			String keyStr = (String) key;
			if (!StringUtil.isEmptyWithTrim(keyStr) && keyStr.trim().startsWith("2")) { //$NON-NLS-1$
				value_key_propfileMap_def.put(defProp.getProperty(keyStr), keyStr);
			}
		}

		//解析common以及ownResModue 目录下的 资源
		value_key_propfileMap_commmon = initCommonFile(depCommonModule);

		//赋值
		for (String name : name_content_map.keySet()) {
			MultiContentVO content = name_content_map.get(name);
			String resid = value_key_propfileMap_def.get(name);
			if (StringUtil.isEmptyWithTrim(resid)) {//指定文件内不存在
				resid = value_key_propfileMap_commmon.get(name);
				if (StringUtil.isEmptyWithTrim(resid)) {//common以及本模块资源中不存在
					//新生成id
					resid = creatKey(defProp);
					value_key_propfileMap_def.put(name, resid);
					defProp.put(resid, name);
				}
			}
			content.setResid(resid);
		}

		return new ArrayList<MultiContentVO>(name_content_map.values());
	}

	private Map<String, String> initCommonFile(List<String> depCommonModule2) {
		return CommonUtil.initCommonFile(depCommonModule2);
	}

	/**
	 * 更新bmf文件中的元素 resid
	 * @param elementList
	 */
	public void updateElements(List<IMultiElement> elementList) {
		for (IMultiElement ele : elementList) {
			String resid = ""; //$NON-NLS-1$
			if (value_key_propfileMap_def.containsKey(ele.getDisplayName())) {
				resid = value_key_propfileMap_def.get(ele.getDisplayName());
			} else {
				resid = value_key_propfileMap_commmon.get(ele.getDisplayName());
			}
			ele.setResid(resid);
		}
		// 设置状态为编辑状态
		NCMDPEditor.getActiveMDPEditor().setDirty(true);
	}

	private String creatKey(Map<String, String> propCH) {
		String curKey = creatCurKey();
		while (propCH.containsKey(curKey)) {
			curKeyValue++;
			curKey = creatCurKey();
		}
		return curKey;
	}

	private String creatCurKey() {
		if (curIndex.length() < Integer.toString(curKeyValue).length()) {
			throw new RuntimeException(Messages.PropsToContentConvert_6);
		}
		String keyEnd = curIndex.substring(0, curIndex.length()
				- Integer.toString(curKeyValue).length())
				+ curKeyValue;
		return preResID + "-" + keyEnd; //$NON-NLS-1$
	}

	private void initProp(LangPathVO defLangVO) {
		String filePath = defLangVO.getFilePath();
		defProp = new UTFProperties(defLangVO.getCharsetName());

		FileInputStream fisCH = null;
		File file = new File(filePath);
		if (!file.exists()) {
			MultiUtils.creatFileOrDirs(file.getAbsolutePath());
		}
		try {
			fisCH = new FileInputStream(filePath);
			defProp.load(fisCH);
		} catch (FileNotFoundException e) {
			MDPLogger.error(e.getMessage(), e);
		} catch (IOException e) {
			MDPLogger.error(e.getMessage(), e);
		} finally {
			if (fisCH != null) {
				try {
					fisCH.close();
				} catch (IOException e) {
					MDPLogger.error(e.getMessage(), e);
				}
			}
		}
	}

	public UTFProperties getDefProp() {
		return defProp;
	}
}
