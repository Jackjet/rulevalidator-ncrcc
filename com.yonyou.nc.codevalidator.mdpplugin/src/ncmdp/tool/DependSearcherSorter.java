package ncmdp.tool;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

public abstract class DependSearcherSorter {
	private boolean ignoreCircle = false;

	private static Stack<Object> stack = new Stack<Object>();

	List<Object> list = new ArrayList<Object>();

	HashMap<String, List<File>> hashmap = new HashMap<String, List<File>>();

	public DependSearcherSorter(boolean ignoreCircle) {
		super();
		this.ignoreCircle = ignoreCircle;
	}

	public DependSearcherSorter() {
		super();
	}

	public abstract String getIdentity(File obj);

	public abstract List<File> getDepend(File obj) throws Exception;

	public boolean ignoreCircle() {
		return ignoreCircle;
	}

	public List<File> getListDepends(File sourceFile) throws Exception {
		list.clear();
		filePathSet.clear();
		stack.clear();
		sortByDepend1(sourceFile);
		List<File> result = new ArrayList<File>();
		result.addAll(filePathSet);//收集所有的
		hashmap.clear();
		return result;
	}

	/**
	 * 重写收集算法 dingxm @2010-01-25 （60s->5s FT -_-!）
	 * 
	 * @param obj
	 */
	private HashSet<File> filePathSet = new HashSet<File>();

	private void sortByDepend1(File sourceFile)throws Exception {
		String id = getIdentity(sourceFile);//获得文件的绝对路径
		if (stack.contains(id)) {//表示已经在处理该文件了
			if (!ignoreCircle()) {//表示不支持循环调用，但是依然出现了循环调用，则报错
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < stack.size(); i++) {
					sb.append(stack.get(i)).append(",  ");
				}
				sb.append(id);
				throw new Exception("依赖的"+sourceFile.getName()+
						Messages.DependSearcherSorter_1 + sb.toString());
			} else {
				return;
			}
		}
		if (filePathSet.contains(sourceFile)) {//如果文件集中包括该文件
			return;
		} else {//如果文件集中不包括该文件
			filePathSet.add(sourceFile);
			stack.push(id);
			List<File> depends = getDepend(sourceFile);//获取该文件依赖的文件
			for (int i = 0; i < depends.size(); i++) {
				File dependObj = depends.get(i);
				String nchomePath = NCMDPTool.getNCHome();
				if (dependObj.getAbsoluteFile().getPath()
						.startsWith(nchomePath)) {
					continue;//如果为中间件中的元数据文件，则不予处理
				}
				sortByDepend1(dependObj);
			}
			stack.pop();
		}
	}
}
