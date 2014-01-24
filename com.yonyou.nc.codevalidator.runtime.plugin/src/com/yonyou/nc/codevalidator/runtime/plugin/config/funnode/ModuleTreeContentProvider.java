package com.yonyou.nc.codevalidator.runtime.plugin.config.funnode;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;

import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.function.utils.NCCommonQueryUtils;
import com.yonyou.nc.codevalidator.resparser.function.utils.NCModuleVO;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 模块组成的树，作为树结构的内容提供者
 * 
 * @author mazhqa
 * 
 */
public class ModuleTreeContentProvider extends LabelProvider implements ITreeContentProvider {

	public static final String ROOT_STR = "全部";
	public static final String ROOT_ID = "_root_";

	private List<NCModuleVO> moduleVoList = new ArrayList<NCModuleVO>();

	public ModuleTreeContentProvider() throws RuleBaseException {
		super();
		initializeData();
	}

	public String getText(Object element) {
		if (element instanceof NCModuleVO) {
			return ((NCModuleVO) element).getModulename();
		} else if (element instanceof String) {
			return ROOT_STR;
		}
		return null;
	}

	private void initializeData() throws RuleBaseException {
		try {
			moduleVoList = NCCommonQueryUtils.queryAllModuleVOs();
		} catch (ResourceParserException e) {
			throw new RuleBaseException(e);
		}
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	public void dispose() {
	}

	@SuppressWarnings("rawtypes")
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof List) {
			return ((List) inputElement).toArray();
		}
		String modulecode = ROOT_ID;
		List<NCModuleVO> filterModuleVoData = NCCommonQueryUtils.getAllChildrenOfModuleVO(moduleVoList, modulecode);
		return filterModuleVoData.toArray(new NCModuleVO[filterModuleVoData.size()]);
	}

	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof NCModuleVO) {
			NCModuleVO moduleVo = (NCModuleVO) parentElement;
			List<NCModuleVO> filterModuleVoData = NCCommonQueryUtils.getAllChildrenOfModuleVO(moduleVoList, moduleVo);
			return filterModuleVoData.toArray(new NCModuleVO[filterModuleVoData.size()]);
		} else if (parentElement instanceof String) {
			List<NCModuleVO> filterModuleVoData = NCCommonQueryUtils.getAllChildrenOfModuleVO(moduleVoList, ROOT_ID);
			return filterModuleVoData.toArray(new NCModuleVO[filterModuleVoData.size()]);
		}
		return null;
	}

	public Object getParent(Object element) {
		// if (element instanceof NCModuleVO) {
		// NCModuleVO child = (NCModuleVO) element;
		// if (child.getParentmodulecode().equals(ROOT_ID)) {
		// return ROOT_ID;
		// } else {
		// NCModuleVO parent = codemap.get(child.getParentmodulecode());
		// if (parent == null) {
		// for (NCModuleVO module : codemap.values()) {
		// if (module.getKey().endsWith(child.getParentmodulecode())) {
		// return module;
		// }
		// }
		// }
		//
		// return parent;
		// }
		// } else {
		// return null;
		// }
		return null;
	}

	public boolean hasChildren(Object element) {
		Object[] obj = getChildren(element);
		return obj == null ? false : obj.length > 0;
	}

}
