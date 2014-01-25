package com.yonyou.nc.codevalidator.runtime.plugin.editor;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;

import com.yonyou.nc.codevalidator.rule.vo.ParamConfiguration;
import com.yonyou.nc.codevalidator.runtime.plugin.Activator;
import com.yonyou.nc.codevalidator.runtime.plugin.celleditor.ICellEditorDescriptor;

/**
 * 可配置的表格编辑支持，每行根据不同的参数配置编辑器，可以设置不同的celleditor
 * @author mazhqa
 * @since V2.3
 */
public class ConfigurableEditingSupport extends EditingSupport {

	private TableViewer tableViewer;
	private IRuleEditorListener ruleEditorListener;
	
	public ConfigurableEditingSupport(TableViewer tableViewer, IRuleEditorListener ruleEditorListener) {
		super(tableViewer);
		this.tableViewer = tableViewer;
		this.ruleEditorListener = ruleEditorListener;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		if (element instanceof ParamConfiguration) {
			ParamConfiguration paramConfiguration = (ParamConfiguration) element;
			String editorType = paramConfiguration.getEditorType();
			try {
				@SuppressWarnings("unchecked")
				Class<ICellEditorDescriptor> editorTypeClass = (Class<ICellEditorDescriptor>) Platform.getBundle(Activator.PLUGIN_ID).loadClass(editorType);
				ICellEditorDescriptor cellEditorDescriptor = editorTypeClass.newInstance();
				return cellEditorDescriptor.getCellEditor(tableViewer);
			} catch (ClassNotFoundException e) {
				MessageDialog.openError(tableViewer.getTable().getShell(), "错误!", e.getMessage());
			} catch (InstantiationException e) {
				MessageDialog.openError(tableViewer.getTable().getShell(), "错误!", e.getMessage());
			} catch (IllegalAccessException e) {
				MessageDialog.openError(tableViewer.getTable().getShell(), "错误!", e.getMessage());
			}
		}
		return null;
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		if (element instanceof ParamConfiguration) {
			ParamConfiguration paramConfiguration = (ParamConfiguration) element;
			return paramConfiguration.getParamValue() == null ? "" : paramConfiguration.getParamValue();
		}
		return null;
	}

	@Override
	protected void setValue(Object element, Object value) {
		if (element instanceof ParamConfiguration) {
			ParamConfiguration paramConfiguration = (ParamConfiguration) element;
			paramConfiguration.setParamValue(value == null ? null : String.valueOf(value));
			tableViewer.refresh();
			if(ruleEditorListener != null){
				ruleEditorListener.ruleEditorChanged();
			}
		}
	}

}
