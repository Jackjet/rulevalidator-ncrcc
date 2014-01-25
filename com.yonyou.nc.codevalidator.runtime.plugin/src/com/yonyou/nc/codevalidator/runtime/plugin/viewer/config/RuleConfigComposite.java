package com.yonyou.nc.codevalidator.runtime.plugin.viewer.config;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;

import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.GlobalExecuteUnit;
import com.yonyou.nc.codevalidator.rule.ICompositeExecuteUnit;
import com.yonyou.nc.codevalidator.runtime.plugin.Activator;
import com.yonyou.nc.codevalidator.runtime.plugin.PluginProjectUtils;
import com.yonyou.nc.codevalidator.sdk.project.ProjectAnalyseUtils;

public class RuleConfigComposite extends Composite {

	private TreeViewer treeViewer;

	public RuleConfigComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));

		treeViewer = new TreeViewer(this, SWT.BORDER);
		Tree tree = treeViewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		TreeLabelContentProvider treeLabelContentProvider = new TreeLabelContentProvider();
		treeViewer.setLabelProvider(treeLabelContentProvider);
		treeViewer.setContentProvider(treeLabelContentProvider);
		List<IProject> mdeProjectsInCurrentWorkspace = PluginProjectUtils.getMdeProjectsInCurrentWorkspace();
		List<BusinessComponent> inputElements = new ArrayList<BusinessComponent>();
		for (IProject iProject : mdeProjectsInCurrentWorkspace) {
			inputElements.add(ProjectAnalyseUtils.getInnerBusinessComponent(iProject.getLocation().toString(),
					iProject.getName(), GlobalExecuteUnit.DEFAULT_GLOBAL_NAME));
		}
		treeViewer.setInput(inputElements);
	}

	TreeViewer getTreeViewer() {
		return treeViewer;
	}

	public static final class TreeLabelContentProvider extends LabelProvider implements ITreeContentProvider {

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}

		@SuppressWarnings("rawtypes")
		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof List) {
				return ((List) inputElement).toArray();
			}
			return null;
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof ICompositeExecuteUnit) {
				ICompositeExecuteUnit moduleExecuteUnit = (ICompositeExecuteUnit) parentElement;
				return moduleExecuteUnit.getSubBusinessComponentList().toArray();
			}
			return null;
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			if (element instanceof ICompositeExecuteUnit) {
				ICompositeExecuteUnit moduleExecuteUnit = (ICompositeExecuteUnit) element;
				return !moduleExecuteUnit.getSubBusinessComponentList().isEmpty();
			}
			return false;
		}

		@Override
		public String getText(Object element) {
			if (element instanceof IProject) {
				IProject project = (IProject) element;
				return project.getName();
			}
			if (element instanceof BusinessComponent) {
				BusinessComponent businessComponent = (BusinessComponent) element;
				return businessComponent.getDisplayBusiCompName();
				// return String.format("%s:%s", businessComponent.getModule(),
				// businessComponent.getBusinessComp());
			}
			return super.getText(element);
		}

		@Override
		public Image getImage(Object element) {
			// String iconPath = null;
			// if (element instanceof IProject) {
			// iconPath = "/images/jprj.gif";
			// } else if (element instanceof BusinessComponent) {
			// iconPath = "/images/busicomp.png";
			// }
			return Activator.imageFromPlugin("/images/busicomp.png");
			// if (iconPath != null) {
			// }
			// return super.getImage(element);
		}

	}

}
