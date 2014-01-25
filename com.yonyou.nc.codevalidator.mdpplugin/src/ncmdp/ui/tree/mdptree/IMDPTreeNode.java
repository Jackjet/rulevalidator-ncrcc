package ncmdp.ui.tree.mdptree;

import java.io.File;

import org.eclipse.core.resources.IResource;

public interface IMDPTreeNode {
	public File getFile();
	public void deleteNode();
	public IResource getIResource();
	public String getModuleName();
	public void updateState();
}
