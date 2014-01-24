package com.yonyou.nc.codevalidator.runtime.plugin.config.funnode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.function.utils.NCCommonQueryUtils;
import com.yonyou.nc.codevalidator.resparser.function.utils.NCFunnodeVO;
import com.yonyou.nc.codevalidator.resparser.function.utils.NCModuleVO;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

public class FuncNodeTreeTableContentProvider extends LabelProvider implements ITableLabelProvider,
		ITreeContentProvider {

	private List<NCFunnodeVO> funcVoList;
	private List<NCModuleVO> moduleVoList = new ArrayList<NCModuleVO>();
	
	private final List<NCFunnodeVO> configedFuncVoList;
	
	private List<NCFunnodeVO> currentDisplayFuncVoList;

	public FuncNodeTreeTableContentProvider(List<NCFunnodeVO> configedFuncVoList) throws RuleBaseException {
		this.configedFuncVoList = configedFuncVoList;
		try {
			funcVoList = NCCommonQueryUtils.queryAllFunnodeVOs();
			moduleVoList = NCCommonQueryUtils.queryAllModuleVOs();
		} catch (ResourceParserException e) {
			throw new RuleBaseException(e);
		}
	}
	
	public List<NCFunnodeVO> getCurrentDisplayFuncVoList() {
		return Collections.unmodifiableList(currentDisplayFuncVoList);
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof NCModuleVO) {
			NCModuleVO moduleVo = (NCModuleVO) inputElement;
			List<NCModuleVO> allChildrenVos = NCCommonQueryUtils.getAllChildrenOfModuleVO(moduleVoList, moduleVo);
			allChildrenVos.add(moduleVo);
			currentDisplayFuncVoList = filterNcFuncVoList(allChildrenVos);
			final List<String> configedFuncNodeList = new ArrayList<String>();
			for (NCFunnodeVO funcNodeVo : configedFuncVoList) {
				configedFuncNodeList.add(funcNodeVo.getFuncode());
			}
			Collections.sort(currentDisplayFuncVoList, new Comparator<NCFunnodeVO>() {

				@Override
				public int compare(NCFunnodeVO o1, NCFunnodeVO o2) {
					String funcode1 = o1.getFuncode();
					String funcode2 = o2.getFuncode();
					if(configedFuncNodeList.contains(funcode1)) {
						return -1;
					}
					if(configedFuncNodeList.contains(funcode2)) {
						return 1;
					}
					return funcode1.compareTo(funcode2);
				}
			});
			return currentDisplayFuncVoList.toArray();
		} else if (inputElement instanceof String) {
			currentDisplayFuncVoList = new ArrayList<NCFunnodeVO>(funcVoList);
		}
		final List<String> configedFuncNodeList = new ArrayList<String>();
		for (NCFunnodeVO funcNodeVo : configedFuncVoList) {
			configedFuncNodeList.add(funcNodeVo.getFuncode());
		}
		Collections.sort(currentDisplayFuncVoList, new Comparator<NCFunnodeVO>() {

			@Override
			public int compare(NCFunnodeVO o1, NCFunnodeVO o2) {
				String funcode1 = o1.getFuncode();
				String funcode2 = o2.getFuncode();
				if(configedFuncNodeList.contains(funcode1)) {
					return -1;
				}
				if(configedFuncNodeList.contains(funcode2)) {
					return 1;
				}
				return funcode1.compareTo(funcode2);
			}
		});
		return currentDisplayFuncVoList.toArray();
	}

	private List<NCFunnodeVO> filterNcFuncVoList(List<NCModuleVO> moduleVoList) {
		List<NCFunnodeVO> result = new ArrayList<NCFunnodeVO>();
		List<String> moduleKeyList = new ArrayList<String>();
		List<String> moduleParentCodeList = new ArrayList<String>();
		for (NCModuleVO moduleVo : moduleVoList) {
			moduleKeyList.add(moduleVo.getKey());
			moduleParentCodeList.add(moduleVo.getModulecode());
		}
		for (NCFunnodeVO ncFunnodeVO : funcVoList) {
			if (moduleKeyList.contains(ncFunnodeVO.getParentcode())
					|| moduleParentCodeList.contains(ncFunnodeVO.getParentcode())) {
				result.add(ncFunnodeVO);
			}
		}
		return result;
	}

	public Object[] getChildren(Object parentElement) {
		return null;
	}

	public Object getParent(Object element) {
		return null;
	}

	public boolean hasChildren(Object element) {
		Object[] obj = getChildren(element);
		return obj == null ? false : obj.length > 0;
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		NCFunnodeVO funNodeVo = (NCFunnodeVO) element;
		String result = null;
		switch (columnIndex) {
		case 0:
			result = funNodeVo.getFuncode();
			break;
		case 1:
			result = funNodeVo.getFunname();
			break;
		default:
			break;
		}
		return result;
	}

}
