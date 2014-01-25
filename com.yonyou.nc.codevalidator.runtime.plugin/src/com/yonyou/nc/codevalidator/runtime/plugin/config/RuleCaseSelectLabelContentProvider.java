package com.yonyou.nc.codevalidator.runtime.plugin.config;

import java.util.List;

import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;
import com.yonyou.nc.codevalidator.runtime.plugin.editor.IRuleConfigVOProperty;
import com.yonyou.nc.codevalidator.runtime.plugin.editor.RuleConfigConstants;

public class RuleCaseSelectLabelContentProvider extends LabelProvider implements ITableLabelProvider, ITableFontProvider, ITreeContentProvider{
	
//	private List<RuleDefinitionVO> displayedRuleVoList;
	
	public RuleCaseSelectLabelContentProvider() {
//		this.displayedRuleVoList = displayedRuleVoList;
	}

	@Override
	public Font getFont(Object element, int columnIndex) {
		return null;
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		RuleDefinitionAnnotationVO ruleDefinitionVo = (RuleDefinitionAnnotationVO) element;
		IRuleConfigVOProperty ruleConfigVOProperty = RuleConfigConstants.RULE_MULTI_CONFIG_COLUMNS.get(columnIndex).getRuleConfigVOProperty();
		return ruleConfigVOProperty.getPropertyValue(ruleDefinitionVo);
//		
//		String result = null;
//		switch (columnIndex) {
//		case 0:
//			String classname = ruleDefinitionVo.getRuleDefinitionName();
//			result = classname.substring(classname.lastIndexOf(".") + 1, classname.length());
//			break;
//		case 1:
//			result = ruleDefinitionVo.getCatalog().getName();
//			break;
//		case 2:
//			result = ruleDefinitionVo.getSubCatalog().getName();
//			break;
//		case 3:
//			result = ruleDefinitionVo.getDescription();
//			break;
//		case 4:
//			result = ruleDefinitionVo.getCoder();
//			break;
//		case 5:
//			result = ruleDefinitionVo.getMemo();
//			break;
//		default:
//			break;
//		}
//		return result;
	}
	
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	public void dispose() {
	}

	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof List) {
			@SuppressWarnings("unchecked")
			List<RuleDefinitionAnnotationVO> ruleVoList = (List<RuleDefinitionAnnotationVO>) inputElement;
			return ruleVoList.toArray();
		}
		return null;
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

}
