package com.yonyou.nc.codevalidator.runtime.plugin.viewer.result;

import java.util.List;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.runtime.plugin.Activator;

public class RuleExecResultTableLabelContentProvider implements IStructuredContentProvider, ITableLabelProvider {

//	private static final String RULE_STATUS = "����ִ��״̬";
//	private static final String RULE_REPAIR_LEVEL = "���صȼ�";
//	private static final String RULE_BUSICOMP = "ҵ�����";
//	private static final String RULE_TYPE = "��������";
//	private static final String RULE_DESC = "��������";
//	private static final String RULE_NOTE = "��ϸ��Ϣ";

//	private static final String SUCCESS_ICON = "/images/success.gif";
//	private static final String FAIL_ICON = "/images/fail.gif";
//	private static final String WARNING_ICON = "/images/warning.gif";
//	private static final String ERROR_ICON = "/images/mustfix.gif";

//	/**
//	 * ����������
//	 */
//	public static final String[] COLUMN_NAMES = new String[] { RULE_STATUS, RULE_REPAIR_LEVEL, RULE_BUSICOMP,
//			RULE_TYPE, RULE_DESC, RULE_NOTE };

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof Object[]) {
			return (Object[]) inputElement;
		} else if (inputElement instanceof List) {
			return ((List<?>) inputElement).toArray();
		}
		return new Object[0];
	}

	@Override
	public void dispose() {
	}

	@Override
	public void addListener(ILabelProviderListener listener) {

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {

	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		if (element instanceof IRuleExecuteResult) {
			RuleResultColumnVO ruleResultColumnVO = RuleResultConstants.RULE_RESULT_COLUMNS.get(columnIndex);
			IRuleExecuteResult executeResult = (IRuleExecuteResult) element;
			String iconPath = ruleResultColumnVO.getRuleResultVoProperty().getIconPath(executeResult);
			if (iconPath != null) {
				return Activator.imageFromPlugin(iconPath);
			}
		}
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element == null || !(element instanceof IRuleExecuteResult)) {
			return ""; //$NON-NLS-1$
		}
		IRuleExecuteResult executeResult = (IRuleExecuteResult) element;
		RuleResultColumnVO ruleResultColumnVO = RuleResultConstants.RULE_RESULT_COLUMNS.get(columnIndex);
		return ruleResultColumnVO.getRuleResultVoProperty().getPropertyValue(executeResult);
//		switch (columnIndex) {
//		case 0:
//			return "";
//		case 1:{
//			String ruleDefinitionIdentifier = executeResult.getRuleDefinitionIdentifier();
//			RuleDefinitionAnnotationVO ruleDefinitionVO = RuleDefinitionsReader.getInstance().getRuleDefinitionVO(
//					ruleDefinitionIdentifier);
//			return ruleDefinitionVO.getRepairLevel().getName();
//		}
//		case 2: {
//			BusinessComponent businessComponent = executeResult.getBusinessComponent();
//			return businessComponent.getDisplayBusiCompName();
//		}
//		case 3: {
//			String ruleDefinitionIdentifier = executeResult.getRuleDefinitionIdentifier();
//			RuleDefinitionAnnotationVO ruleDefinitionVO = RuleDefinitionsReader.getInstance().getRuleDefinitionVO(
//					ruleDefinitionIdentifier);
//			if (ruleDefinitionVO != null) {
//				return String.format("%s %s", ruleDefinitionVO.getCatalog() == null ? "" : ruleDefinitionVO
//						.getCatalog().getName(), ruleDefinitionVO.getSubCatalog() == null ? "" : ruleDefinitionVO
//						.getSubCatalog().getName());
//			}
//			return null;
//		}
//		case 4: {
//			String ruleDefinitionIdentifier = executeResult.getRuleDefinitionIdentifier();
//			RuleDefinitionAnnotationVO ruleDefinitionVO = RuleDefinitionsReader.getInstance().getRuleDefinitionVO(
//					ruleDefinitionIdentifier);
//			if (ruleDefinitionVO != null) {
//				return ruleDefinitionVO.getDescription();
//			}
//			return null;
//		}
//		case 5:
//			return executeResult.getNote();
//		default:
//			break;
//		}
//		return null;
	}

}
