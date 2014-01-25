package com.yonyou.nc.codevalidator.runtime.plugin.config;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

/**
 * 用于规则配置的过滤树结构，通过规则的某些条件过滤，可以更加方便地进行规则的选择等操作
 * @author mazhqa
 * @since V2.6
 */
public class RuleConfigFilterTreeComposite extends Composite {
	
	private TreeViewer treeViewer;

	public RuleConfigFilterTreeComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		treeViewer = new TreeViewer(this, SWT.BORDER);
		Tree tree = treeViewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		RuleConfigLabelTreeContentProvider  labelContentProvider = new RuleConfigLabelTreeContentProvider();
		treeViewer.setLabelProvider(labelContentProvider);
		treeViewer.setContentProvider(labelContentProvider);
		treeViewer.setInput(new RuleConfigTreeRoot());
		treeViewer.refresh();
	}

	public TreeViewer getConditionTreeViewer(){
		return treeViewer;
	}

}
