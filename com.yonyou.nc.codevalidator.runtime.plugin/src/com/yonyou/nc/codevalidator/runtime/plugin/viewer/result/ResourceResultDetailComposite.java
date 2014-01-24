package com.yonyou.nc.codevalidator.runtime.plugin.viewer.result;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;

import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.impl.ResourceResultElement;
import com.yonyou.nc.codevalidator.runtime.plugin.Activator;

public class ResourceResultDetailComposite extends Composite implements
		IResultDetailComposite<ResourceRuleExecuteResult> {
	private Text detailText;
	private TreeViewer treeViewer;
	private Tree tree;

	public ResourceResultDetailComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gridData.widthHint = 200;
		composite.setLayoutData(gridData);

		treeViewer = new TreeViewer(composite, SWT.BORDER);
		tree = treeViewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Group group = new Group(this, SWT.V_SCROLL);
		group.setText("\u9519\u8BEF\u8BE6\u7EC6\u4FE1\u606F");
		group.setLayout(new GridLayout(1, false));
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		detailText = new Text(group, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		detailText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		// setWeights(new int[] {1, 2});
	}

	@Override
	public void loadRuleExecuteResult(final ResourceRuleExecuteResult executeResult) {
		final List<ResourceResultElement> resultElementList = executeResult.getResultElementList();

		LabelContentProvider labelContentProvider = new LabelContentProvider(resultElementList);
		treeViewer.setLabelProvider(labelContentProvider);
		treeViewer.setContentProvider(labelContentProvider);
		treeViewer.setInput(Arrays.asList(new Root(executeResult.getResult())));
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				detailText.setText("");
				ISelection selection = event.getSelection();
				if (selection instanceof TreeSelection) {
					TreeSelection treeSelection = (TreeSelection) selection;
					if (treeSelection.getFirstElement() instanceof ResourceResultElement) {
						ResourceResultElement resultElement = (ResourceResultElement) treeSelection.getFirstElement();
						detailText.setText(resultElement.getErrorDetail());
					} else if (treeSelection.getFirstElement() instanceof Root) {
						detailText.setText(executeResult.getNote());
					}
				}
			}
		});
		// treeViewer.setSelection(selection);
		treeViewer.getTree().select(treeViewer.getTree().getItem(0));
		detailText.setText(executeResult.getNote());
	}

	public static class Root {
		
		private String treeLabelText;
		
		public Root(String treeLabelText) {
			this.treeLabelText = treeLabelText;
		}
		
		public String getText() {
			return treeLabelText;
		}
	}

	public static class LabelContentProvider extends LabelProvider implements ITreeContentProvider {

		List<ResourceResultElement> resultElementList;

		public LabelContentProvider(List<ResourceResultElement> resultElementList) {
			this.resultElementList = resultElementList;
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}

		@Override
		public Image getImage(Object element) {
			return Activator.imageFromPlugin("/images/resource.gif");
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
			if (parentElement instanceof Root) {
				return resultElementList.toArray();
			}
			return null;
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			return element instanceof Root;
		}

		@Override
		public String getText(Object element) {
			if (element instanceof Root) {
				Root root = (Root) element;
				return root.getText();
			} else if (element instanceof ResourceResultElement) {
				ResourceResultElement resultElement = (ResourceResultElement) element;
				return resultElement.getResourcePath();
			}
			return super.getText(element);
		}

	}

}
