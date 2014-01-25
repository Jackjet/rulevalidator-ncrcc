package ncmdp.project;

import java.io.File;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

/**
 * ±à¼­Æ÷µÄÊäÈë
 * @author wangxmn
 *
 */
public class RefMDFileEditorInput implements IEditorInput {
	private File file = null;
	public RefMDFileEditorInput(File file) {
		super();
		this.file = file;
	}

	public boolean exists() {
		return file != null && file.exists();
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return file == null ? null: file.getName();
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
		if(obj instanceof RefMDFileEditorInput){
			RefMDFileEditorInput other = (RefMDFileEditorInput)obj;
			if(file == null){
				if(other.getFile() == null){
					return true;
				}else{
					return false;
				}
			}else{
				return file.equals(other.getFile());
			}
		}else{
			return super.equals(obj);
		}
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

}
