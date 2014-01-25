package ncmdp.project;

import java.io.File;

import ncmdp.ui.tree.mdptree.MDPFileTreeItem;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.part.FileEditorInput;

public class MDFileEditInput implements IEditorInput {
	private File file = null;

	private IFile iFile = null;

	private String componentName = "";

	public MDFileEditInput(File file, IFile iFile, String componentName) {
		super();
		this.file = file;
		this.iFile = iFile;
		this.componentName = componentName;
	}

	public MDFileEditInput(MDPFileTreeItem selItem) {
		super();
		if (selItem != null) {
			this.file = selItem.getFile();
			this.iFile = selItem.getIFile();
			this.componentName = selItem.getComponentName();
		}
	}

	public boolean exists() {
		return file != null && file.exists();
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return file == null ? null : file.getName();
	}

	public IPersistableElement getPersistable() {
		return null;
	}

	public String getToolTipText() {
		return getName();
	}

	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class adapter) {
		return null;
	}

	public File getFile() {
		return file;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FileEditorInput) {
			FileEditorInput fei = (FileEditorInput) obj;
			if (file == null) {
				if (fei.getPath().toFile() == null) {
					return true;
				} else {
					return false;
				}
			} else {
				return file.equals(fei.getPath().toFile());
			}
		}
		if (obj instanceof MDFileEditInput) {
			MDFileEditInput other = (MDFileEditInput) obj;
			if (file == null) {
				if (other.getFile() == null) {
					return true;
				} else {
					return false;
				}
			} else {
				return file.equals(other.getFile());
			}
		} else {
			return super.equals(obj);
		}
	}
	

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	public IFile getIFile() {
		return iFile;
	}

	public void setIFile(IFile file) {
		iFile = file;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}
}
