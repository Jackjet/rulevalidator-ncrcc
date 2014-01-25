package com.yonyou.nc.codevalidator.runtime.plugin.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.dialogs.FilteredItemsSelectionDialog;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionsReader;
import com.yonyou.nc.codevalidator.runtime.plugin.Activator;
import com.yonyou.nc.codevalidator.runtime.plugin.common.RuleDefinitionVOComposite;

/**
 * 过滤规则定义的选择对话框
 * 
 * @author mazhqa
 * 
 */
public class FilteredRuleDefinitionSelectionDialog extends FilteredItemsSelectionDialog {

	private String dialogSettingsKey = "com.yonyou.nc.codevalidator.resources.ui.dialogs.FilteredRuleDefinitionSelectionDialog";

	protected List<IFilteredItem> resources = new ArrayList<IFilteredItem>();

	private IFilteredItemPersistenceFactory persistenceFactory = null;

	private RuleDefinitionVOComposite ruleDefinitionVOComposite;

	public FilteredRuleDefinitionSelectionDialog(Shell shell) {
		super(shell);
		persistenceFactory = new RuleDefinitionVOItemFactory();
		initResources();
		setSelectionHistory(new ResourceSelectionHistory());
		setListLabelProvider(new ResourceListItemLabelProvider());
		setDetailsLabelProvider(new ResourceItemDetailsLabelProvider());
		setInitialPattern("**");
		setTitle("规则选择框");
	}

	private void initResources() {
		Collection<RuleDefinitionAnnotationVO> ruleDefinitionVos = RuleDefinitionsReader.getInstance().getAllRuleDefinitionMap()
				.values();
		for (RuleDefinitionAnnotationVO ruleDefinitionVO : ruleDefinitionVos) {
			resources.add(new RuleDefinitionVOItem(ruleDefinitionVO));
		}
		if (persistenceFactory != null) {
			persistenceFactory.addFilteredItem(resources);
		}
	}

	// public void setResources(List<IFilteredItem> resources) {
	// this.resources = resources;
	// if (persistenceFactory != null) {
	// persistenceFactory.addFilteredItem(resources);
	// }
	// }

	@Override
	protected Control createExtendedContentArea(Composite parent) {
		// RuleExecResultDetailComposite result = new
		// RuleExecResultDetailComposite(parent, SWT.NONE);
		// return result;
		ruleDefinitionVOComposite = new RuleDefinitionVOComposite(parent, SWT.NONE);
		ruleDefinitionVOComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		return ruleDefinitionVOComposite;
	}

	@Override
	protected IDialogSettings getDialogSettings() {
		// IDialogSettings settings =
		// Activator.getDefault().getDialogSettings().getSection(dialogSettingsKey);
		// if (settings == null) {
		// settings =
		// Activator.getDefault().getDialogSettings().addNewSection(dialogSettingsKey);
		// }
		// return settings;
		return new DialogSettings(dialogSettingsKey);
	}

	@Override
	protected IStatus validateItem(Object item) {
		return Status.OK_STATUS;
	}

	@Override
	protected ItemsFilter createFilter() {
		return new RuleDefinitionVOItemFilter();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Comparator getItemsComparator() {
		return new Comparator() {
			public int compare(Object arg0, Object arg1) {
				if (arg0 instanceof IFilteredItem && arg1 instanceof IFilteredItem) {
					return ((IFilteredItem) arg0).compareTo((IFilteredItem) arg1);
				} else if (arg0 != null && arg1 != null) {
					return arg0.toString().compareTo(arg1.toString());
				}
				return 0;
			}
		};
	}

	@Override
	protected void fillContentProvider(AbstractContentProvider contentProvider, ItemsFilter itemsFilter,
			IProgressMonitor progressMonitor) throws CoreException {
		resources.clear();
		Collection<RuleDefinitionAnnotationVO> ruleDefinitionVos = RuleDefinitionsReader.getInstance().getAllRuleDefinitionMap()
				.values();
		for (RuleDefinitionAnnotationVO ruleDefinitionVO : ruleDefinitionVos) {
			resources.add(new RuleDefinitionVOItem(ruleDefinitionVO));
		}
		progressMonitor.beginTask("开始执行所有资源扫描...", resources.size());
		for (IFilteredItem resource : resources) {
			progressMonitor.setTaskName(String.format("加入资源:%s", resource.getItemText()));
			contentProvider.add(resource, itemsFilter);
			progressMonitor.worked(1);
		}

		progressMonitor.done();
	}

	@Override
	public String getElementName(Object item) {
		if (item instanceof IFilteredItem) {
			IFilteredItem filteredItem = (IFilteredItem) item;
			return filteredItem.getItemText();
		}
		return null;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(850, 600);
	}

	private class ResourceListItemLabelProvider extends LabelProvider implements ILabelProviderListener {
		// Need to keep our own list of listeners
		private ListenerList listeners = new ListenerList();
		WorkbenchLabelProvider provider = new WorkbenchLabelProvider();

		/**
		 * Creates a new instance of the class
		 */
		public ResourceListItemLabelProvider() {
			super();
			provider.addListener(this);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
		 */
		public Image getImage(Object element) {
			return getImageFromObject(element);
		}

		protected Image getImageFromObject(Object obj) {
			if (obj instanceof IFilteredItem) {
				IFilteredItem iFilteredItem = (IFilteredItem) obj;
				if (iFilteredItem.getImagePath() != null) {
					// return Activator.getImage(iFilteredItem.getBundleID(),
					// iFilteredItem.getImagePath());
					return Activator.imageFromPlugin(iFilteredItem.getImagePath());
				}
			}
			return provider.getImage(obj);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
		 */
		public String getText(Object element) {
			if (element instanceof IFilteredItem) {
				return ((IFilteredItem) element).getItemText();
			} else {
				return super.getText(element);
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.LabelProvider#dispose()
		 */
		public void dispose() {
			provider.removeListener(this);
			provider.dispose();
			super.dispose();
		}

		/*
		 * @see
		 * org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse
		 * .jface.viewers.ILabelProviderListener)
		 */
		public void addListener(ILabelProviderListener listener) {
			listeners.add(listener);
		}

		/*
		 * @see
		 * org.eclipse.jface.viewers.LabelProvider#removeListener(org.eclipse
		 * .jface.viewers.ILabelProviderListener)
		 */
		public void removeListener(ILabelProviderListener listener) {
			listeners.remove(listener);
		}

		/*
		 * @see
		 * org.eclipse.jface.viewers.ILabelProviderListener#labelProviderChanged
		 * (org.eclipse.jface.viewers.LabelProviderChangedEvent)
		 */
		public void labelProviderChanged(LabelProviderChangedEvent event) {
			Object[] l = listeners.getListeners();
			for (Object object : l) {
				((ILabelProviderListener) object).labelProviderChanged(event);
			}
		}
		
	}

	/**
	 * A label provider for details of ResourceItem objects.
	 */
	private class ResourceItemDetailsLabelProvider extends ResourceListItemLabelProvider {
		
		@Override
		public void dispose() {
			super.dispose();
		}
		/*
		 * @see
		 * org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
		 */
		public Image getImage(Object element) {
			// return provider.getImage(element);
			return getImageFromObject(element);
		}

		protected Image getImageFromObject(Object obj) {
			if (obj instanceof IFilteredItem) {
				IFilteredItem iFilteredItem = (IFilteredItem) obj;
				if (iFilteredItem.getDetailImagePath() != null) {
					return Activator.imageFromPlugin(iFilteredItem.getDetailImagePath());
				}
			}
			return provider.getImage(obj);
		}

		/**
		 * 搜索框底部父路径详细信息
		 */
		public String getText(Object element) {
			if (element instanceof IFilteredItem) {
				if (element instanceof RuleDefinitionVOItem) {
					RuleDefinitionVOItem ruleDefinitionVoItem = (RuleDefinitionVOItem) element;
					RuleDefinitionAnnotationVO ruleDefinitionVo = ruleDefinitionVoItem.getRuleDefinitionVO();
					ruleDefinitionVOComposite.loadRuleDefinitionVo(ruleDefinitionVo);
				}
				return ((IFilteredItem) element).getItemDetailText();
			} else {
				return super.getText(element);
			}
		}

		/*
		 * @see
		 * org.eclipse.jface.viewers.ILabelProviderListener#labelProviderChanged
		 * (org.eclipse.jface.viewers.LabelProviderChangedEvent)
		 */
		public void labelProviderChanged(LabelProviderChangedEvent event) {
			Object[] l = super.listeners.getListeners();
			for (int i = 0; i < super.listeners.size(); i++) {
				((ILabelProviderListener) l[i]).labelProviderChanged(event);
			}
		}
	}

	private class ResourceSelectionHistory extends SelectionHistory {
		/*
		 */
		protected Object restoreItemFromMemento(IMemento element) {
			if (persistenceFactory != null) {
				return persistenceFactory.createElement(element);
			}
			return element.getString("resource"); //$NON-NLS-1$
		}

		/*
		 */
		protected void storeItemToMemento(Object item, IMemento element) {
			if (item instanceof IFilteredItem) {
				IFilteredItem iFilteredItem = (IFilteredItem) item;
				if (persistenceFactory != null) {
					persistenceFactory.saveState(iFilteredItem, element);
				} else {
					element.putString("resource", iFilteredItem.getItemText()); //$NON-NLS-1$
				}
			} else {
				element.putString("resource", item.toString()); //$NON-NLS-1$
			}
		}
	}

	private class RuleDefinitionVOItemFilter extends ItemsFilter {

		public RuleDefinitionVOItemFilter() {
			super();
		}

		@Override
		public boolean matchItem(Object item) {
			if (item instanceof IFilteredItem) {
				return matches(((IFilteredItem) item).getItemText());
			} else {
				return matches(item.toString());
			}
		}

		@Override
		public boolean isConsistentItem(Object item) {
			return true;
		}
	}

}
