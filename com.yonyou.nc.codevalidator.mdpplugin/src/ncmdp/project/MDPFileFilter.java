package ncmdp.project;

import java.io.File;
import java.io.FileFilter;

import ncmdp.model.Constant;

public class MDPFileFilter implements FileFilter {

	public MDPFileFilter() {
	}
	
	public boolean accept(File f) {
		return f.isDirectory()
				|| f.getPath().toLowerCase().endsWith(Constant.MDFILE_SUFFIX)
				|| f.getPath().toLowerCase()
						.endsWith(Constant.MDFILE_BPF_SUFFIX);
	}

}
